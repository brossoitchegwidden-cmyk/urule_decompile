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

   public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("width");
      String parameter2 = req.getParameter("height");
      int number = 200;
      int number2 = 30;
      if (StringUtils.isNotBlank(parameter)) {
         number = Integer.valueOf(parameter);
      }

      if (StringUtils.isNotBlank(parameter2)) {
         number2 = Integer.valueOf(parameter2);
      }

      resp.setContentType("image/jpeg");
      ServletOutputStream outputStream = resp.getOutputStream();
      InputStream inputStream = CaptchaBuilder.ins.build(req, number, number2);
      IOUtils.copy(inputStream, outputStream);
      outputStream.close();
   }

   public String url() {
      return "/captcha";
   }
}
