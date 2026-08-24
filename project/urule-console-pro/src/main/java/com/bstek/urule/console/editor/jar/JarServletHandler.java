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
   private DynamicSpringConfigLoader dynamicSpringConfigLoader = ServiceUtils.getDynamicSpringConfigLoader();
   private JarCacheAdapter hostJarCacheAdapter = null;

   public JarCacheAdapter getJarCacheAdapter() {
      if (this.hostJarCacheAdapter == null) {
         try {
            this.hostJarCacheAdapter = (JarCacheAdapter)Utils.getApplicationContext().getBean(JarCacheAdapter.BEAN_ID);
         } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
            this.hostJarCacheAdapter = new HostJarCacheAdapter();
         }
      }

      return this.hostJarCacheAdapter;
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void deploy(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String dynamicJarsStoreDirectPath = this.dynamicSpringConfigLoader.buildDynamicJarsStoreDirectPath();
      int jarFiles = DynamicJarManager.ins.createJarFiles(dynamicJarsStoreDirectPath);
      HashMap valuesByKey = new HashMap();
      String parameter = req.getParameter("groupId");
      if (jarFiles > 0) {
         this.dynamicSpringConfigLoader.loadDynamicJars(dynamicJarsStoreDirectPath);
         List dynamicJars = this.getJarCacheAdapter().loadDynamicJars(parameter, UrlType.cluster);
         List list = UrlService.ins.load(UrlType.client, parameter).getList();
         valuesByKey.put("clients", list);
         valuesByKey.put("result", dynamicJars);
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void sendToClients(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      List dynamicJars = this.getJarCacheAdapter().loadDynamicJars(parameter, UrlType.client);
      this.writeObjectToJson(resp, dynamicJars);
   }

   public void load(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      this.writeObjectToJson(resp, DynamicJarManager.ins.newQuery().groupId(parameter).list());
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      DynamicJar dynamicJar = new DynamicJar();
      dynamicJar.setGroupId(req.getParameter("groupId"));
      dynamicJar.setDesc(req.getParameter("desc"));
      dynamicJar.setCreateUser(SecurityUtils.getLoginUsername(req));
      DynamicJarManager.ins.add(dynamicJar);
      this.writeObjectToJson(resp, dynamicJar);
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      DynamicJar dynamicJar = new DynamicJar();
      dynamicJar.setDesc(req.getParameter("desc"));
      dynamicJar.setId(Long.valueOf(req.getParameter("id")));
      dynamicJar.setUpdateUser(SecurityUtils.getLoginUsername(req));
      DynamicJarManager.ins.update(dynamicJar);
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void download(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      byte[] jar = DynamicJarManager.ins.loadJar(longValue);
      DynamicJar dynamicJar = DynamicJarManager.ins.load(longValue);
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jar);
      FileUtils.downloadFile(dynamicJar.getName(), byteArrayInputStream, resp);
      IOUtils.closeQuietly(byteArrayInputStream);
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void upload(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      String loginUsername = SecurityUtils.getLoginUsername(req);
      UploadFile uploadFile = FileUtils.uploadFile(req);
      if (!uploadFile.getName().toLowerCase().endsWith(".jar")) {
         throw new RuleException("请上传后缀名为.jar的文件");
      } else {
         InputStream inputStream = uploadFile.getInputStream();
         byte[] bytes = IOUtils.toByteArray(inputStream);
         IOUtils.closeQuietly(inputStream);
         DynamicJarManager.ins.updateJar(longValue, uploadFile.getName(), loginUsername, bytes);
         this.writeObjectToJson(resp, uploadFile.getName());
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "dynamicJar",
      code = "manager"
   )
   public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      DynamicJarManager.ins.delete(longValue);
   }

   public String url() {
      return "/jar";
   }
}
