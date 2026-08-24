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

   public void init(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String homePath = HomeLocator.getHomePath();
      if (StringUtils.isBlank(homePath)) {
         throw new ConfigLoadException("当前系统环境下未发现URULE_HOME的配置:urule-init.properties,无法生成配置文件，请确认!");
      } else {
         HashMap valuesByKey = new HashMap();

         try {
            if (!BootstrapManager.get().isBootstrapped()) {
               ObjectMapper objectMapper = JsonMapper.builder().build();
               String parameter = req.getParameter("setupInfo");
               SetupInfo setupInfo = (SetupInfo)objectMapper.readValue(parameter, SetupInfo.class);
               SetupManager.setup(setupInfo);
               valuesByKey.put("success", true);
            } else {
               valuesByKey.put("success", false);
               valuesByKey.put("message", "Initialization has been completed and cannot be repeated.");
            }
         } catch (Exception exception) {
            java.util.logging.Logger.getLogger(SetupServletHandler.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
            valuesByKey.put("success", false);
            valuesByKey.put("message", exception.getMessage());
         }

         this.writeObjectToJson(resp, valuesByKey);
      }
   }

   public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String text = this.resolveRequestedMethod(req);
      if (text.indexOf("setup/init") > -1) {
         this.init(req, resp);
      } else {
         VelocityContext velocityContext = new VelocityContext();
         velocityContext.put("chunkName", "setup");
         velocityContext.put("contextPath", req.getContextPath());
         resp.setContentType("text/html");
         resp.setCharacterEncoding("utf-8");
         String text2 = "template.html";
         Template template = this.velocityEngine.getTemplate("asserts/urule/html/" + text2, "utf-8");
         PrintWriter writer = resp.getWriter();
         template.merge(velocityContext, writer);
         writer.close();
      }

   }
}
