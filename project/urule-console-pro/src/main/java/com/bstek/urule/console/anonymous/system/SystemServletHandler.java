package com.bstek.urule.console.anonymous.system;

import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.runtime.DynamicSpringConfigLoaderImpl;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SystemServletHandler extends AnonymousServletHandler {
   public void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      var2.setCharacterEncoding("UTF-8");
      var2.setContentType("text/html;charset=UTF-8");
      PrintWriter var3 = var2.getWriter();
      StringBuilder var4 = new StringBuilder();
      var4.append("Product Version：urule-pro-" + DynamicSpringConfigLoaderImpl.getProductVersion());
      var4.append("<br><br>");
      if (StringUtils.isNotBlank(DynamicSpringConfigLoaderImpl.getAuthInfo())) {
         String var5 = DynamicSpringConfigLoaderImpl.getLimitDate();
         var4.append("License ：Authorized to " + DynamicSpringConfigLoaderImpl.getAuthInfo() + "（授权给【" + DynamicSpringConfigLoaderImpl.getAuthInfo() + "】使用），Limited : " + var5 + "");
      } else {
         var4.append("License ：You are using a trial version,please purchase the commercial license.（当前为试用版，请购买商业授权）");
      }

      var4.append("<br><br>");
      var4.append("Key:" + DynamicSpringConfigLoaderImpl.getLicenseKey());
      var4.append("<br><br>");
      var3.write(var4.toString());
      var3.flush();
      var3.close();
   }

   public String url() {
      return "/system";
   }
}
