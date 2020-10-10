package org.javaboy.mailserver.receiver;


import com.rabbitmq.client.Channel;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;


@Component
public class MyMailReceiver {
    public static final Logger logger = LoggerFactory.getLogger(MyMailReceiver.class);

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MailProperties mailProperties;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    StringRedisTemplate redisTemplate;

    //邮件服务模块
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message  message, Channel channel) throws IOException {
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //生成消息的唯一标识符
        String msgId = (String) headers.get("spring_returned_message_correlation");
        if (redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)) {
            //redis 中包含该 key，说明该消息已经被消费过
            logger.info(msgId + ":该条消息已经被消费过了🤭");
            channel.basicAck(tag, false);//basicAck：确认消息已消费方法
            return;
        }
        //收到消息，发送邮件
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            helper.setTo(employee.getEmail());
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("入职欢迎");
            helper.setSentDate(new Date());
            //定义邮件模板
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJobLevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            //将邮件模板添加到 templateEngine
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            //邮件发送
            javaMailSender.send(msg);
            //消息发送成功处理
            redisTemplate.opsForHash().put("mail_log",msgId,"littleSun");
            channel.basicAck(tag, false);
            logger.info(msgId+":此次邮件已成功发送😄");
        } catch (MessagingException e) {
            //basicNack：消息失败处理方法, true：该消息不丢弃,等待下一次消费
            channel.basicNack(tag,false,true);
            e.printStackTrace();
            logger.error("此次邮件发送失败了😔    ：" + e.getMessage());
        }
    }
}
