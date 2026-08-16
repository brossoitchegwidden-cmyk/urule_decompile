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
   private static final String e = "-HH-mm-ss";

   public void undo(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("id");
      String var4 = SecurityUtils.getLoginUsername(var1);
      Long var5 = Long.parseLong(var3);
      RuleFile var6 = FileManager.ins.get(var5);
      if (var4.equals(var6.getUpdateUser())) {
         boolean var7 = FileManager.ins.checkExist(var6.getProjectId(), var6.getParentId(), var6.getType(), var6.getName());
         if (var7) {
            Calendar var8 = Calendar.getInstance();
            var8.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat var9 = new SimpleDateFormat("-HH-mm-ss");
            FileManager.ins.rename(var5, var4, var6.getName() + var9.format(new Date(System.currentTimeMillis())));
         }

         FileManager.ins.updateDeleteFlag(var5, false, var4);
      }

   }

   @Transactional
   public void delete(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("id");
      String var4 = SecurityUtils.getLoginUsername(var1);
      Long var5 = Long.parseLong(var3);
      RuleFile var6 = FileManager.ins.get(var5);
      if (var4.equals(var6.getUpdateUser())) {
         FileManager.ins.remove(var5);
      }

   }

   public void list(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("name");
      FileQuery var4 = FileManager.ins.newQuery();
      var4.deleted(true);
      var4.updateUser(SecurityUtils.getLoginUsername(var1));
      var4.desc("UPDATE_DATE_");
      if (StringUtils.isNotBlank(var3)) {
         var4.nameLike(var3);
      }

      this.a(var2, var4.list(ContextHolder.getProjectId()));
   }

   public String url() {
      return "/recycler";
   }
}
