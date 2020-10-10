package org.javaboy.vhr.service;


import  org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.MailConstants;
import org.javaboy.vhr.model.MailSendLog;
import org.javaboy.vhr.model.RespPageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class EmployeeService {
    //日志打印
    public final static Logger logger= LoggerFactory.getLogger(Employee.class);
    //用于计算工作的年份的
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    //算出姐精确两位小数
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    MailSendLogService mailSendLogService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //分页操作
    public  RespPageBean getEmployeeByPage(Integer page, Integer size,Employee employee,Date[] beginDateScope) {
        if(page != null && size!= null){
            page=(page-1)*size;
        }
        List<Employee> data = employeeMapper.getEmployeeByPage(page,size, employee,beginDateScope);
        long total= employeeMapper.getTotal(employee,beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addEmp(Employee employee) {
        //计算合同日期的问题
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        double month = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12 + (Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract)));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(month/12)));
        int result = employeeMapper.insertSelective(employee);
        //员工添加成功入职后,发送邮件通知
        if (result == 1) {
            Employee emp = employeeMapper.getEmployeeById(employee.getId());
            //生成消息的唯一id
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setCreateTime(new Date());
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailSendLog.setEmpId(emp.getId());
            //消息发送失败,重新开始发送的时间
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT));
            mailSendLogService.insert(mailSendLog);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(msgId));
        }
        return result;
    }

    public Integer maxWorkID() {
        return employeeMapper.maxWorkID();
    }

    public Integer deleteEmpById(Integer id) {
        return employeeMapper.deleteByPrimaryKey(id);
    }

    public Integer updateEmp(Employee employee) {
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public Integer addEmps(List<Employee> list) {
        return employeeMapper.addEmps(list);
    }

    public RespPageBean getEmployeeByPageWithSalary(Integer page, Integer size) {
        if(page !=  null && size != null){
            page= (page-1) * size;
        }
        List<Employee> list=  employeeMapper.getEmployeeByPageWithSalary(page, size);
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(list);
        respPageBean.setTotal(employeeMapper.getTotal(null,null));
        return respPageBean;

    }

    public Integer updateEmployeeSalaryById(Integer eid, Integer sid) {
        return employeeMapper.updateEmployeeSalaryById(eid,sid);
    }

    public Employee getEmployeeById(Integer empId) {
        return employeeMapper.getEmployeeById(empId);
    }
}
