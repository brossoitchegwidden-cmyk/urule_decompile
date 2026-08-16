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
   private RemoteDynamicJarsBuilder a = ServiceUtils.getRemoteDynamicJarsBuilder();
   private DynamicSpringConfigLoader e = ServiceUtils.getDynamicSpringConfigLoader();

   public void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (ValidateUtils.validateUserPwd(var1, this.a.getUser(), this.a.getPwd())) {
         String var3 = var1.getContextPath() + "/urule" + "/dynamic";
         String var4 = var1.getRequestURI();
         String var5 = var4.substring(var3.length());
         int var6 = var5.lastIndexOf("/");
         String var7 = var5.substring(var6 + 1, var5.length());
         if (StringUtils.isNotBlank(var7)) {
            this.c(var7, var1, var2);
         } else {
            if (this.c(var1)) {
               return;
            }

            String var8 = this.e.buildDynamicJarsStoreDirectPath();
            int var9 = DynamicJarManager.ins.createJarFiles(var8);
            if (var9 == 0) {
               return;
            }

            this.e.loadDynamicJars(var8);
         }

      }
   }

   public void recacheAllPackets(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (this.c(var1)) {
         this.a(var2, "ok");
      } else {
         ((PacketCacheImpl)PacketCache.ins).doRecacheAllPackets();
         IDGenerator.getInstance().clean();
         this.a(var2, "ok");
      }
   }

   public void syncPacket(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (this.c(var1)) {
         this.a(var2, "ok");
      } else {
         long var3 = Long.valueOf(var1.getParameter("id"));
         ((PacketCacheImpl)PacketCache.ins).doReloadPacket(var3);
         this.a(var2, "ok");
      }
   }

   public void syncPacketForRemoveProject(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (this.c(var1)) {
         this.a(var2, "ok");
      } else {
         long var3 = Long.valueOf(var1.getParameter("projectId"));
         ((PacketCacheImpl)PacketCache.ins).doRemoveProjectPackets(var3);
         this.a(var2, "ok");
      }
   }

   public void checkLatestJarsDir(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = "n";
      String var4 = this.e.getDynamicJarsIdDigest();
      if (var4 != null) {
         var3 = var4;
      }

      HashMap var5 = new HashMap();
      String var6 = var1.getParameter("digest");
      var5.put("digest", var3);
      String var7 = this.e.getDynamicJarsStoreDirectPath();
      if (StringUtils.isBlank(var7)) {
         var5.put("match", true);
      } else if (var6 == null) {
         var5.put("match", false);
      } else {
         var5.put("match", var6.equals(var3));
      }

      this.a(var2, var5);
   }

   public void loadDynamicJars(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = this.e.getDynamicJarsStoreDirectPath();
      if (var3 == null) {
         throw new RuleException("Current jars dir not exist.");
      } else {
         byte[] var4 = this.e.zipDynamicJars();
         ServletOutputStream var5 = var2.getOutputStream();
         DataOutputStream var6 = new DataOutputStream(var5);
         var6.write(var4);
         var6.flush();
         var6.close();
         IOUtils.closeQuietly(var5);
      }
   }

   private void a(HttpServletResponse var1, String var2) throws ServletException, IOException {
      var1.setContentType("text/plain");
      PrintWriter var3 = var1.getWriter();
      var3.write(var2);
      var3.flush();
      var3.close();
   }

   private boolean c(HttpServletRequest var1) throws UnsupportedEncodingException {
      String var2 = var1.getParameter("systemId");
      if (var2 != null) {
         var2 = URLDecoder.decode(var2, "utf-8");
         if (var2.contentEquals(Utils.SystemId)) {
            return true;
         }
      }

      return false;
   }

   public String url() {
      return "/dynamic";
   }
}
