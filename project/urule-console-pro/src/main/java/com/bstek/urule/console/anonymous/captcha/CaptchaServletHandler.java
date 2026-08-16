package com.bstek.urule.console.anonymous.captcha;

import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.util.StringUtils;
import java.io.InputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

public class CaptchaServletHandler extends AnonymousServletHandler {
   public static final String URL = "/captcha";

   public void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("width");
      String var4 = var1.getParameter("height");
      int var5 = 200;
      int var6 = 30;
      if (StringUtils.isNotBlank(var3)) {
         var5 = Integer.valueOf(var3);
      }

      if (StringUtils.isNotBlank(var4)) {
         var6 = Integer.valueOf(var4);
      }

      var2.setContentType("image/jpeg");
      ServletOutputStream var7 = var2.getOutputStream();
      InputStream var8 = CaptchaBuilder.ins.build(var1, var5, var6);
      IOUtils.copy(var8, var7);
      var7.close();
   }

   public String url() {
      return "/captcha";
   }
}
