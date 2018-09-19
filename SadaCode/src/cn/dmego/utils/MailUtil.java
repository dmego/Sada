package cn.dmego.utils;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailSSLSocketFactory;

import cn.dmego.domain.MailInfo;

/**
 * @Name: MailUtil
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月20日
 * 
 */
public class MailUtil {
	private static String host = "smtp.qq.com";
	private static String from = "2833924665@qq.com";
	private static String password = "zirrgqzbcrtzdcge";

	public static String sendMail(MailInfo mailInfo) throws MessagingException, GeneralSecurityException {
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		final Properties pro = new Properties();
		pro.setProperty("mail.smtp.host", host);
		pro.setProperty("mail.smtp.auth", "true");
		pro.setProperty("mail.smtp.user", from);
		pro.setProperty("mail.smtp.pass", password);
		pro.put("mail.smtp.ssl.enable", "true");
		pro.put("mail.smtp.ssl.socketFactory", sf);

		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session session = Session.getInstance(pro, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(pro.getProperty("mail.smtp.user"), pro.getProperty("mail.smtp.pass"));
			}
		});

		session.setDebug(true);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setSubject(mailInfo.getSubject());
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getTo()));
		message.setSentDate(new Date());

		if ("html".equals(mailInfo.getType())) {
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			message.setContent(mainPart);
			Transport.send(message);

		} else {
			message.setText(mailInfo.getContent());
			Transport.send(message);
		}

		return "success";

	}
	public static void main(String[] args) {
		MailInfo m = new MailInfo();
		m.setTo("freeliuxn@163.com");
		m.setSubject("哈哈");
		m.setType("text");
		m.setContent("123456");
		try {
			sendMail(m);
		} catch (MessagingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
}
