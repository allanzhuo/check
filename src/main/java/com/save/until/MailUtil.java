package com.save.until;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.mail.util.MailSSLSocketFactory;
/**
 * 邮件发送
 * @author zhangzhuo
 *
 */
public class MailUtil  {
    final static Logger logger = LoggerFactory.getLogger(MailUtil.class);
    private static MailUtil instance = new MailUtil();  
    private MailUtil (){}  
    public static MailUtil getInstance() {  
        return instance;  
    }  

    public void sendMail(Set<String> mail) {
        String from = "604552732@qq.com";// 发件人电子邮箱
        String host = "smtp.qq.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)

        Properties properties =new Properties();

        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证

        try {
            //QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("604552732@qq.com", "fgxqlnhazbumbfaa"); // 发件人邮箱账号、授权码
                }
            });

            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("23962957@qq.com"));
            message.setSubject("赛飞可视化巡检故障通知");
            StringBuffer sb = new StringBuffer();
            for (String string : mail) {
                sb.append("<div>"+string+"</div><br/><hr/>");
            }
            String content = sb.toString();
            message.setContent(content, "text/html;charset=UTF-8");
            Transport.send(message);
            logger.info("故障邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}