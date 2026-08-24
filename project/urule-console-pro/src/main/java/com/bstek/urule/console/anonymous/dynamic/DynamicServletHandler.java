package com.bstek.urule.console.anonymous.dynamic;

import com.bstek.urule.Utils;
import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.anonymous.ValidateUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketCacheImpl;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.manager.jar.DynamicJarManager;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

public class DynamicServletHandler extends AnonymousServletHandler {
   public static final String URL = "/dynamic";
   private RemoteDynamicJarsBuilder remoteDynamicJarsBuilder = ServiceUtils.getRemoteDynamicJarsBuilder();
   private DynamicSpringConfigLoader dynamicSpringConfigLoader = ServiceUtils.getDynamicSpringConfigLoader();

   public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (ValidateUtils.validateUserPwd(req, this.remoteDynamicJarsBuilder.getUser(), this.remoteDynamicJarsBuilder.getPwd())) {
         String text = req.getContextPath() + "/urule" + "/dynamic";
         String requestURI = req.getRequestURI();
         String substring = requestURI.substring(text.length());
         int number = substring.lastIndexOf("/");
         String substring2 = substring.substring(number + 1, substring.length());
         if (StringUtils.isNotBlank(substring2)) {
            this.invokeHandlerMethod(substring2, req, resp);
         } else {
            if (this.evaluateCondition(req)) {
               return;
            }

            String dynamicJarsStoreDirectPath = this.dynamicSpringConfigLoader.buildDynamicJarsStoreDirectPath();
            int jarFiles = DynamicJarManager.ins.createJarFiles(dynamicJarsStoreDirectPath);
            if (jarFiles == 0) {
               return;
            }

            this.dynamicSpringConfigLoader.loadDynamicJars(dynamicJarsStoreDirectPath);
         }

      }
   }

   public void recacheAllPackets(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (this.evaluateCondition(req)) {
         this.writeResponse(resp, "ok");
      } else {
         ((PacketCacheImpl)PacketCache.ins).doRecacheAllPackets();
         IDGenerator.getInstance().clean();
         this.writeResponse(resp, "ok");
      }
   }

   public void syncPacket(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (this.evaluateCondition(req)) {
         this.writeResponse(resp, "ok");
      } else {
         long longValue = Long.valueOf(req.getParameter("id"));
         ((PacketCacheImpl)PacketCache.ins).doReloadPacket(longValue);
         this.writeResponse(resp, "ok");
      }
   }

   public void syncPacketForRemoveProject(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (this.evaluateCondition(req)) {
         this.writeResponse(resp, "ok");
      } else {
         long longValue = Long.valueOf(req.getParameter("projectId"));
         ((PacketCacheImpl)PacketCache.ins).doRemoveProjectPackets(longValue);
         this.writeResponse(resp, "ok");
      }
   }

   public void checkLatestJarsDir(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String dynamicJarsIdDigest2 = "n";
      String dynamicJarsIdDigest = this.dynamicSpringConfigLoader.getDynamicJarsIdDigest();
      if (dynamicJarsIdDigest != null) {
         dynamicJarsIdDigest2 = dynamicJarsIdDigest;
      }

      HashMap valuesByKey = new HashMap();
      String parameter = req.getParameter("digest");
      valuesByKey.put("digest", dynamicJarsIdDigest2);
      String dynamicJarsStoreDirectPath = this.dynamicSpringConfigLoader.getDynamicJarsStoreDirectPath();
      if (StringUtils.isBlank(dynamicJarsStoreDirectPath)) {
         valuesByKey.put("match", true);
      } else if (parameter == null) {
         valuesByKey.put("match", false);
      } else {
         valuesByKey.put("match", parameter.equals(dynamicJarsIdDigest2));
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   public void loadDynamicJars(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String dynamicJarsStoreDirectPath = this.dynamicSpringConfigLoader.getDynamicJarsStoreDirectPath();
      if (dynamicJarsStoreDirectPath == null) {
         throw new RuleException("Current jars dir not exist.");
      } else {
         byte[] bytes = this.dynamicSpringConfigLoader.zipDynamicJars();
         ServletOutputStream outputStream = resp.getOutputStream();
         DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
         dataOutputStream.write(bytes);
         dataOutputStream.flush();
         dataOutputStream.close();
         IOUtils.closeQuietly(outputStream);
      }
   }

   private void writeResponse(HttpServletResponse httpServletResponse, String text) throws ServletException, IOException {
      httpServletResponse.setContentType("text/plain");
      PrintWriter writer = httpServletResponse.getWriter();
      writer.write(text);
      writer.flush();
      writer.close();
   }

   private boolean evaluateCondition(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
      String parameter = httpServletRequest.getParameter("systemId");
      if (parameter != null) {
         parameter = URLDecoder.decode(parameter, "utf-8");
         if (parameter.contentEquals(Utils.SystemId)) {
            return true;
         }
      }

      return false;
   }

   public String url() {
      return "/dynamic";
   }
}
