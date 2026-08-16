package com.bstek.urule.console.anonymous.setup;

import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.config.HomeLocator;
import com.bstek.urule.console.config.bootstrap.BootstrapManager;
import com.bstek.urule.console.config.exception.ConfigLoadException;
import com.bstek.urule.console.config.setup.SetupInfo;
import com.bstek.urule.console.config.setup.SetupManager;
import com.bstek.urule.console.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

public class SetupServletHandler extends AnonymousServletHandler {
   public static final String URL = "/setup";

   public String url() {
      return "/setup";
   }

   public void init(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = HomeLocator.getHomePath();
      if (StringUtils.isBlank(var3)) {
         throw new ConfigLoadException("当前系统环境下未发现URULE_HOME的配置:urule-init.properties,无法生成配置文件，请确认!");
      } else {
         HashMap var4 = new HashMap();

         try {
            if (!BootstrapManager.get().isBootstrapped()) {
               ObjectMapper var5 = JsonMapper.builder().build();
               String var6 = var1.getParameter("setupInfo");
               SetupInfo var7 = (SetupInfo)var5.readValue(var6, SetupInfo.class);
               SetupManager.setup(var7);
               var4.put("success", true);
            } else {
               var4.put("success", false);
               var4.put("message", "Initialization has been completed and cannot be repeated.");
            }
         } catch (Exception var8) {
            var8.printStackTrace();
            var4.put("success", false);
            var4.put("message", var8.getMessage());
         }

         this.a(var2, var4);
      }
   }

   public void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = this.a(var1);
      if (var3.indexOf("setup/init") > -1) {
         this.init(var1, var2);
      } else {
         VelocityContext var4 = new VelocityContext();
         var4.put("chunkName", "setup");
         var4.put("contextPath", var1.getContextPath());
         var2.setContentType("text/html");
         var2.setCharacterEncoding("utf-8");
         String var5 = "template.html";
         Template var6 = this.c.getTemplate("asserts/urule/html/" + var5, "utf-8");
         PrintWriter var7 = var2.getWriter();
         var6.merge(var4, var7);
         var7.close();
      }

   }
}
