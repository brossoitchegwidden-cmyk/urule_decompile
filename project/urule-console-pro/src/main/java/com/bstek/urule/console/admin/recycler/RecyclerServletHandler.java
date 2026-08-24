package com.bstek.urule.console.admin.recycler;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.FileQuery;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RecyclerServletHandler extends ApiServletHandler {
   private static final String HH_MM_SS = "-HH-mm-ss";

   /**撤销删除*/
   public void undo(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("id");
      String loginUsername = SecurityUtils.getLoginUsername(req);
      Long longValue = Long.parseLong(parameter);
      RuleFile ruleFile = FileManager.ins.get(longValue);
      if (loginUsername.equals(ruleFile.getUpdateUser())) {
         boolean flag = FileManager.ins.checkExist(ruleFile.getProjectId(), ruleFile.getParentId(), ruleFile.getType(), ruleFile.getName());
         if (flag) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("-HH-mm-ss");
            FileManager.ins.rename(longValue, loginUsername, ruleFile.getName() + simpleDateFormat.format(new Date(System.currentTimeMillis())));
         }

         FileManager.ins.updateDeleteFlag(longValue, false, loginUsername);
      }

   }

   /**彻底删除文件*/
   @Transactional
   public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("id");
      String loginUsername = SecurityUtils.getLoginUsername(req);
      Long longValue = Long.parseLong(parameter);
      RuleFile ruleFile = FileManager.ins.get(longValue);
      if (loginUsername.equals(ruleFile.getUpdateUser())) {
         FileManager.ins.remove(longValue);
      }

   }

   /**查询所有被删除的文件对象*/
   public void list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("name");
      FileQuery fileQuery = FileManager.ins.newQuery();
      fileQuery.deleted(true);
      fileQuery.updateUser(SecurityUtils.getLoginUsername(req));
      fileQuery.desc("UPDATE_DATE_");
      if (StringUtils.isNotBlank(parameter)) {
         fileQuery.nameLike(parameter);
      }

      this.writeObjectToJson(resp, fileQuery.list(ContextHolder.getProjectId()));
   }

   public String url() {
      return "/recycler";
   }
}
