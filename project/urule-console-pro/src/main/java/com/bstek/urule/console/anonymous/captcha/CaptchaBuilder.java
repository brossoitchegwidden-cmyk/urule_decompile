package com.bstek.urule.console.anonymous.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;

public class CaptchaBuilder {
   private static final String CAPTCHA_KEY = "CAPTCHA_KEY";
   public static final CaptchaBuilder ins = new CaptchaBuilder();

   private CaptchaBuilder() {
   }

   public InputStream build(HttpServletRequest req, int width, int height) {
      BufferedImage bufferedImage = new BufferedImage(width, height, 1);
      Graphics graphics = bufferedImage.getGraphics();
      graphics.setColor(this.resolveColor(210, 240));
      graphics.fillRect(0, 0, width, height);
      int number = (new Random()).nextInt(20);
      int number2 = (new Random()).nextInt(20);
      String text = "";
      int number3 = 0;
      int number4 = (new Random()).nextInt(3);
      if (number4 == 0) {
         if (number > number2) {
            number3 = number - number2;
            text = number + " - " + number2 + " = ? ";
         } else {
            number3 = number + number2;
            text = number + " + " + number2 + " = ? ";
         }
      } else if (number4 == 1) {
         if (number > number2) {
            number3 = number - number2;
            text = number + " - ? = " + number2;
         } else {
            number3 = number + number2;
            text = "? - " + number + " = " + number2;
         }
      } else if (number > number2) {
         number3 = number - number2;
         text = number + " - ? = " + number2;
      } else {
         number3 = number + number2;
         text = "? - " + number + " = " + number2;
      }

      Font font = new Font("Times New Roman", 3, height);
      graphics.setFont(font);
      graphics.setColor(this.resolveColor(120, 200));
      FontMetrics fontMetrics = graphics.getFontMetrics();
      int number5 = fontMetrics.stringWidth(text);
      int number6 = (width - number5) / 2;
      int number7 = height - (height - height) / 2 - 3;
      graphics.drawString(text, number6, number7);
      graphics.dispose();
      req.getSession().setAttribute("CAPTCHA_KEY", number3);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      try {
         ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
         ImageIO.write(bufferedImage, "JPEG", imageOutputStream);
         imageOutputStream.close();
         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
         byteArrayOutputStream.close();
         return byteArrayInputStream;
      } catch (IOException iOException) {
         throw new RuntimeException(iOException);
      }
   }

   public String getCaptchResult(HttpServletRequest req) {
      Object attribute = req.getSession().getAttribute("CAPTCHA_KEY");
      return attribute == null ? null : attribute.toString();
   }

   public void cleanCaptch(HttpServletRequest req) {
      req.getSession().removeAttribute("CAPTCHA_KEY");
   }

   private Color resolveColor(int number, int number2) {
      Random random = new Random();
      int number3 = number + random.nextInt(number2 - number);
      int number4 = number + random.nextInt(number2 - number);
      int number5 = number + random.nextInt(number2 - number);
      return new Color(number3, number4, number5);
   }
}
