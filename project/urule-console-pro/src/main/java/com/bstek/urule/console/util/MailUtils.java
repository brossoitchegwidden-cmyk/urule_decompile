package com.bstek.urule.console.util;

import com.bstek.urule.console.config.Configure;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailUtils {
   public static void sendHtmlMail(MailInfo info) throws Exception {
      info.setHost(Configure.getConfigure().getProperty("urule.mail.smtp.host"));
      info.setFormName(Configure.getConfigure().getProperty("urule.mail.smtp.user"));
      info.setFormPassword(Configure.getConfigure().getProperty("urule.mail.smtp.pass"));
      info.setAuth(Configure.getConfigure().getBoolean("urule.mail.smtp.auth", true));
      Message message = resolveMessage(info);
      MimeMultipart mimeMultipart = new MimeMultipart();
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      ((BodyPart)mimeBodyPart).setContent(info.getContent(), "text/html; charset=utf-8");
      ((Multipart)mimeMultipart).addBodyPart(mimeBodyPart);
      message.setContent(mimeMultipart);
      Transport.send(message);
   }

   private static Message resolveMessage(MailInfo mailInfo) throws Exception {
      final Properties properties = System.getProperties();
      properties.setProperty("mail.smtp.host", mailInfo.getHost());
      properties.setProperty("mail.smtp.auth", Boolean.toString(mailInfo.isAuth()));
      properties.setProperty("mail.smtp.user", mailInfo.getFormName());
      properties.setProperty("mail.smtp.pass", mailInfo.getFormPassword());
      Session session = null;
      if (mailInfo.isAuth()) {
         session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(properties.getProperty("mail.smtp.user"), properties.getProperty("mail.smtp.pass"));
            }
         });
      } else {
         session = Session.getInstance(properties);
      }

      MimeMessage mimeMessage = new MimeMessage(session);
      ((Message)mimeMessage).setSubject(MimeUtility.encodeText(mailInfo.getSubject(), "UTF-8", "B"));
      ((Message)mimeMessage).setFrom(new InternetAddress(properties.getProperty("mail.smtp.user"), Configure.getConfigure().getProperty("urule.application.title", "URULE")));
      ((Message)mimeMessage).setRecipient(RecipientType.TO, new InternetAddress(mailInfo.getToAddress()));
      ((Message)mimeMessage).setSentDate(new Date());
      return mimeMessage;
   }
}
