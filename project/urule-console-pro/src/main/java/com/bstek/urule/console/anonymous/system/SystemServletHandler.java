package com.bstek.urule.console.anonymous.system;

import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.runtime.DynamicSpringConfigLoaderImpl;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SystemServletHandler extends AnonymousServletHandler {
   public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      resp.setCharacterEncoding("UTF-8");
      resp.setContentType("text/html;charset=UTF-8");
      PrintWriter writer = resp.getWriter();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Product Version：urule-pro-" + DynamicSpringConfigLoaderImpl.getProductVersion());
      stringBuilder.append("<br><br>");
      if (StringUtils.isNotBlank(DynamicSpringConfigLoaderImpl.getAuthInfo())) {
         String limitDate = DynamicSpringConfigLoaderImpl.getLimitDate();
         stringBuilder.append("License ：Authorized to " + DynamicSpringConfigLoaderImpl.getAuthInfo() + "（授权给【" + DynamicSpringConfigLoaderImpl.getAuthInfo() + "】使用），Limited : " + limitDate + "");
      } else {
         stringBuilder.append("License ：You are using a trial version,please purchase the commercial license.（当前为试用版，请购买商业授权）");
      }

      stringBuilder.append("<br><br>");
      stringBuilder.append("Key:" + DynamicSpringConfigLoaderImpl.getLicenseKey());
      stringBuilder.append("<br><br>");
      writer.write(stringBuilder.toString());
      writer.flush();
      writer.close();
   }

   public String url() {
      return "/system";
   }
}
