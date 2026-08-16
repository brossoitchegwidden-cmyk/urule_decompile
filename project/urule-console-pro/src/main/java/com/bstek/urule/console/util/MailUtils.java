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
   public static void sendHtmlMail(MailInfo var0) throws Exception {
      var0.setHost(Configure.getConfigure().getProperty("urule.mail.smtp.host"));
      var0.setFormName(Configure.getConfigure().getProperty("urule.mail.smtp.user"));
      var0.setFormPassword(Configure.getConfigure().getProperty("urule.mail.smtp.pass"));
      var0.setAuth(Configure.getConfigure().getBoolean("urule.mail.smtp.auth", true));
      Message var1 = a(var0);
      MimeMultipart var2 = new MimeMultipart();
      MimeBodyPart var3 = new MimeBodyPart();
      ((BodyPart)var3).setContent(var0.getContent(), "text/html; charset=utf-8");
      ((Multipart)var2).addBodyPart(var3);
      var1.setContent(var2);
      Transport.send(var1);
   }

   private static Message a(MailInfo var0) throws Exception {
      final Properties var1 = System.getProperties();
      var1.setProperty("mail.smtp.host", var0.getHost());
      var1.setProperty("mail.smtp.auth", Boolean.toString(var0.isAuth()));
      var1.setProperty("mail.smtp.user", var0.getFormName());
      var1.setProperty("mail.smtp.pass", var0.getFormPassword());
      Session var2 = null;
      if (var0.isAuth()) {
         var2 = Session.getInstance(var1, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(var1.getProperty("mail.smtp.user"), var1.getProperty("mail.smtp.pass"));
            }
         });
      } else {
         var2 = Session.getInstance(var1);
      }

      MimeMessage var3 = new MimeMessage(var2);
      ((Message)var3).setSubject(MimeUtility.encodeText(var0.getSubject(), "UTF-8", "B"));
      ((Message)var3).setFrom(new InternetAddress(var1.getProperty("mail.smtp.user"), Configure.getConfigure().getProperty("urule.application.title", "URULE")));
      ((Message)var3).setRecipient(RecipientType.TO, new InternetAddress(var0.getToAddress()));
      ((Message)var3).setSentDate(new Date());
      return var3;
   }
}
