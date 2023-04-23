package net.javaguide.springboot.utills;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import aj.org.objectweb.asm.Type;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtils {

	
	@Autowired
	private JavaMailSender emailSender;
	
	public void sendSimpleMessage(String to,String subject,String text,List<String> list) {
		
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("persisttechbusiness@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		if (list !=null && list.size()>0) {
			message.setCc(getccArray(list));
		}
		
		emailSender.send(message);
	
			
			
		
		
	}
	
	private String[] getccArray(List<String> ccList) {
		String[] cc=new String[ccList.size()];
		for (int i = 0; i < ccList.size(); i++) {
			cc[i]=ccList.get(i);
			
		}
		return cc;
	}
	
	public void forgotMail(String to,String subject,String password) throws MessagingException {
		MimeMessage message=emailSender.createMimeMessage();
		MimeMessageHelper helper= new MimeMessageHelper(message , true);
		helper.setFrom("persisttechbusiness@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<p><b>Your Login details for persist tech system System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
		message.setContent(htmlMsg,"text/html");
		emailSender.send(message);


		
	}
}
