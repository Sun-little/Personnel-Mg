package org.javaboy.mailserver;

import org.javaboy.vhr.model.MailConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.amqp.core.Queue;

@SpringBootApplication
public class MailserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailserverApplication.class, args);
    }

    //创建邮件消息队列
    @Bean
    Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

}
