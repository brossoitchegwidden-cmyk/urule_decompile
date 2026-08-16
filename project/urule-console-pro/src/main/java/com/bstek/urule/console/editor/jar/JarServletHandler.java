package com.bstek.urule.console.editor.jar;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.database.manager.jar.DynamicJarManager;
import com.bstek.urule.console.database.model.DynamicJar;
import com.bstek.urule.console.database.model.UrlType;
import com.bstek.urule.console.database.service.url.UrlService;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.console.util.UploadFile;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class JarServletHandler extends ApiServletHandler {
   private DynamicSpringConfigLoader e = ServiceUtils.getDynamicSpringConfigLoader();
   private JarCacheAdapter f = null;

   public JarCacheAdapter getJarCacheAdapter() {
      if (this.f == null) {
         try {
            this.f = (JarCacheAdapter)Utils.getApplicationContext().getBean(JarCacheAdapter.BEAN_ID);
         } catch (NoSuchBeanDefinitionException var2) {
            this.f = new HostJarCacheAdapter();
         }
      }

      return this.f;
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void deploy(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = this.e.buildDynamicJarsStoreDirectPath();
      int var4 = DynamicJarManager.ins.createJarFiles(var3);
      HashMap var5 = new HashMap();
      String var6 = var1.getParameter("groupId");
      if (var4 > 0) {
         this.e.loadDynamicJars(var3);
         List var7 = this.getJarCacheAdapter().loadDynamicJars(var6, UrlType.cluster);
         List var8 = UrlService.ins.load(UrlType.client, var6).getList();
         var5.put("clients", var8);
         var5.put("result", var7);
      }

      this.a(var2, var5);
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void sendToClients(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      List var4 = this.getJarCacheAdapter().loadDynamicJars(var3, UrlType.client);
      this.a(var2, var4);
   }

   public void load(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      this.a(var2, DynamicJarManager.ins.newQuery().groupId(var3).list());
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      DynamicJar var3 = new DynamicJar();
      var3.setGroupId(var1.getParameter("groupId"));
      var3.setDesc(var1.getParameter("desc"));
      var3.setCreateUser(SecurityUtils.getLoginUsername(var1));
      DynamicJarManager.ins.add(var3);
      this.a(var2, var3);
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void update(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      DynamicJar var3 = new DynamicJar();
      var3.setDesc(var1.getParameter("desc"));
      var3.setId(Long.valueOf(var1.getParameter("id")));
      var3.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      DynamicJarManager.ins.update(var3);
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void download(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      byte[] var5 = DynamicJarManager.ins.loadJar(var3);
      DynamicJar var6 = DynamicJarManager.ins.load(var3);
      ByteArrayInputStream var7 = new ByteArrayInputStream(var5);
      FileUtils.downloadFile(var6.getName(), var7, var2);
      IOUtils.closeQuietly(var7);
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void upload(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      String var5 = SecurityUtils.getLoginUsername(var1);
      UploadFile var6 = FileUtils.uploadFile(var1);
      if (!var6.getName().toLowerCase().endsWith(".jar")) {
         throw new RuleException("请上传后缀名为.jar的文件");
      } else {
         InputStream var7 = var6.getInputStream();
         byte[] var8 = IOUtils.toByteArray(var7);
         IOUtils.closeQuietly(var7);
         DynamicJarManager.ins.updateJar(var3, var6.getName(), var5, var8);
         this.a(var2, var6.getName());
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void delete(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      DynamicJarManager.ins.delete(var3);
   }

   public String url() {
      return "/jar";
   }
}
