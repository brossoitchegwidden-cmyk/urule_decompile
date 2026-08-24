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
   public void load(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      UrlType urlType = UrlType.valueOf(req.getParameter("type"));
      String parameter = req.getParameter("groupId");
      UrlData urlData = UrlService.ins.load(urlType, parameter);
      this.writeObjectToJson(resp, urlData);
   }

   @URuleAuthorization(
      authType = "group",
      model = "clientUrls",
      code = "manager"
   )
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      UrlType urlType = UrlType.valueOf(req.getParameter("type"));
      UrlConfig urlConfig = new UrlConfig();
      urlConfig.setType(urlType);
      urlConfig.setName(req.getParameter("name"));
      urlConfig.setGroupId(req.getParameter("groupId"));
      String parameter = req.getParameter("url");
      if (StringUtils.isNotBlank(parameter) && parameter.endsWith("/")) {
         throw new RuleException("URL不能以'/'结尾");
      } else {
         urlConfig.setUrl(parameter);
         urlConfig.setCreateUser(SecurityUtils.getLoginUsername(req));
         UrlManager.ins.add(urlConfig);
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "clientUrls",
      code = "manager"
   )
   public void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      UrlConfig urlConfig = new UrlConfig();
      urlConfig.setName(req.getParameter("name"));
      String parameter = req.getParameter("url");
      if (StringUtils.isNotBlank(parameter) && parameter.endsWith("/")) {
         throw new RuleException("URL不能以'/'结尾");
      } else {
         urlConfig.setUrl(parameter);
         urlConfig.setId(Long.valueOf(req.getParameter("id")));
         urlConfig.setUpdateUser(SecurityUtils.getLoginUsername(req));
         UrlManager.ins.update(urlConfig);
      }
   }

   @URuleAuthorization(
      authType = "group",
      model = "clientUrls",
      code = "manager"
   )
   public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      UrlManager.ins.delete(longValue);
   }

   public String url() {
      return "/url";
   }
}
