package com.bstek.urule.console.editor.url;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.database.manager.url.UrlManager;
import com.bstek.urule.console.database.model.UrlConfig;
import com.bstek.urule.console.database.model.UrlType;
import com.bstek.urule.console.database.service.url.UrlData;
import com.bstek.urule.console.database.service.url.UrlService;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UrlServletHandler extends ApiServletHandler {
   public void load(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      UrlType var3 = UrlType.valueOf(var1.getParameter("type"));
      String var4 = var1.getParameter("groupId");
      UrlData var5 = UrlService.ins.load(var3, var4);
      this.a(var2, var5);
   }

   @URuleAuthorization(
      authType = "group",
      model = "clientUrls",
      code = "manager"
   )
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      UrlType var3 = UrlType.valueOf(var1.getParameter("type"));
      UrlConfig var4 = new UrlConfig();
      var4.setType(var3);
      var4.setName(var1.getParameter("name"));
      var4.setGroupId(var1.getParameter("groupId"));
      String var5 = var1.getParameter("url");
      if (StringUtils.isNotBlank(var5) && var5.endsWith("/")) {
         throw new RuleException("URL不能以'/'结尾");
      } else {
         var4.setUrl(var5);
         var4.setCreateUser(SecurityUtils.getLoginUsername(var1));
         UrlManager.ins.add(var4);
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "clientUrls",
      code = "manager"
   )
   public void update(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      UrlConfig var3 = new UrlConfig();
      var3.setName(var1.getParameter("name"));
      String var4 = var1.getParameter("url");
      if (StringUtils.isNotBlank(var4) && var4.endsWith("/")) {
         throw new RuleException("URL不能以'/'结尾");
      } else {
         var3.setUrl(var4);
         var3.setId(Long.valueOf(var1.getParameter("id")));
         var3.setUpdateUser(SecurityUtils.getLoginUsername(var1));
         UrlManager.ins.update(var3);
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "clientUrls",
      code = "manager"
   )
   public void delete(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      UrlManager.ins.delete(var3);
   }

   public String url() {
      return "/url";
   }
}
