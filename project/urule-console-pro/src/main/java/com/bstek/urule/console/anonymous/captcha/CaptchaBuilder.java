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
   private static final String a = "CAPTCHA_KEY";
   public static final CaptchaBuilder ins = new CaptchaBuilder();

   private CaptchaBuilder() {
   }

   public InputStream build(HttpServletRequest var1, int var2, int var3) {
      BufferedImage var4 = new BufferedImage(var2, var3, 1);
      Graphics var5 = var4.getGraphics();
      var5.setColor(this.a(210, 240));
      var5.fillRect(0, 0, var2, var3);
      int var7 = (new Random()).nextInt(20);
      int var8 = (new Random()).nextInt(20);
      String var9 = "";
      int var10 = 0;
      int var11 = (new Random()).nextInt(3);
      if (var11 == 0) {
         if (var7 > var8) {
            var10 = var7 - var8;
            var9 = var7 + " - " + var8 + " = ? ";
         } else {
            var10 = var7 + var8;
            var9 = var7 + " + " + var8 + " = ? ";
         }
      } else if (var11 == 1) {
         if (var7 > var8) {
            var10 = var7 - var8;
            var9 = var7 + " - ? = " + var8;
         } else {
            var10 = var7 + var8;
            var9 = "? - " + var7 + " = " + var8;
         }
      } else if (var7 > var8) {
         var10 = var7 - var8;
         var9 = var7 + " - ? = " + var8;
      } else {
         var10 = var7 + var8;
         var9 = "? - " + var7 + " = " + var8;
      }

      Font var12 = new Font("Times New Roman", 3, var3);
      var5.setFont(var12);
      var5.setColor(this.a(120, 200));
      FontMetrics var13 = var5.getFontMetrics();
      int var14 = var13.stringWidth(var9);
      int var15 = (var2 - var14) / 2;
      int var16 = var3 - (var3 - var3) / 2 - 3;
      var5.drawString(var9, var15, var16);
      var5.dispose();
      var1.getSession().setAttribute("CAPTCHA_KEY", var10);
      ByteArrayOutputStream var17 = new ByteArrayOutputStream();

      try {
         ImageOutputStream var18 = ImageIO.createImageOutputStream(var17);
         ImageIO.write(var4, "JPEG", var18);
         var18.close();
         ByteArrayInputStream var19 = new ByteArrayInputStream(var17.toByteArray());
         var17.close();
         return var19;
      } catch (IOException var20) {
         throw new RuntimeException(var20);
      }
   }

   public String getCaptchResult(HttpServletRequest var1) {
      Object var2 = var1.getSession().getAttribute("CAPTCHA_KEY");
      return var2 == null ? null : var2.toString();
   }

   public void cleanCaptch(HttpServletRequest var1) {
      var1.getSession().removeAttribute("CAPTCHA_KEY");
   }

   private Color a(int var1, int var2) {
      Random var3 = new Random();
      int var4 = var1 + var3.nextInt(var2 - var1);
      int var5 = var1 + var3.nextInt(var2 - var1);
      int var6 = var1 + var3.nextInt(var2 - var1);
      return new Color(var4, var5, var6);
   }
}
