package com.bstek.urule.console.database.service.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.database.manager.file.DirectoryManager;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.version.VersionFileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.service.reference.ReferenceService;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.exception.CopyException;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.ReferenceDeleteException;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class FileServiceImpl implements FileService {
   public List tree(Long var1, RuleFileType var2) {
      return this.tree(var1, 0L, var2.name());
   }

   public void removeDir(RuleFile var1) {
      this.removeDir(var1, true);
   }

   public void removeDir(RuleFile var1, boolean var2) {
      for(RuleFile var5 : (Iterable<RuleFile>)(Iterable<?>)(this.tree(var1.getProjectId(), var1.getId(), var1.getType()))) {
         if (var5.isDirectory()) {
            this.a(var5);
         } else {
            if (!var2) {
               List var6 = ReferenceService.ins.uuid(var5.getProjectId(), var5.getId(), (String)null);
               if (var6.size() > 0) {
                  throw new ReferenceDeleteException(var6.size());
               }
            }

            VersionFileManager.ins.deleteByFileId(var5.getId());
            FileManager.ins.remove(var5.getId());
         }
      }

      DirectoryManager.ins.remove(var1.getId());
   }

   private void a(RuleFile var1) {
      for(RuleFile var3 : (Iterable<RuleFile>)(Iterable<?>)(var1.getChildren())) {
         if (var3.isDirectory()) {
            this.a(var3);
         } else {
            VersionFileManager.ins.deleteByFileId(var3.getId());
            FileManager.ins.remove(var3.getId());
         }
      }

      DirectoryManager.ins.remove(var1.getId());
   }

   public List tree(Long var1, Long var2) {
      Connection var3 = JdbcUtils.getConnection();

      List var14;
      try {
         List var4 = DirectoryManager.ins.list(var1, var2);

         for(RuleFile var6 : (Iterable<RuleFile>)(Iterable<?>)(var4)) {
            List var7 = this.tree(var1, var6.getId());
            var6.setChildren(var7);
         }

         List var13 = FileManager.ins.list(var1, var2);
         var13.addAll(var4);
         var14 = var13;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var14;
   }

   public List tree(Long var1, Long var2, String var3) {
      Connection var4 = JdbcUtils.getConnection();

      List var16;
      try {
         List var5 = DirectoryManager.ins.list(var1, var2, var3);

         for(RuleFile var7 : (Iterable<RuleFile>)(Iterable<?>)(var5)) {
            List var8 = this.tree(var1, var7.getId(), var3);
            var7.setChildren(var8);
         }

         Object var14 = null;
         List var15;
         if (ResourceType.General.name().equals(var3)) {
            var15 = FileManager.ins.list(var1, var2);
         } else {
            var15 = FileManager.ins.list(var1, var2, var3);
         }

         var15.addAll(var5);
         var16 = var15;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var16;
   }

   public void updateFileDeleteFlag(Long var1, boolean var2, String var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         RuleFile var5 = FileManager.ins.get(var1);
         if (var5 != null) {
            FileManager.ins.updateDeleteFlag(var1, var2, var3);
            if (!var2 && var5.getParentId() > 0L) {
               DirectoryManager.ins.updateDeleteFlag(var5.getParentId(), false, var3);
            }
         }
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public List menus(Long var1) {
      ArrayList var2 = new ArrayList();
      RuleFile var3 = new RuleFile();
      var3.setName("规则");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.General.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      var3 = new RuleFile();
      var3.setName("库文件");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.Library.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      var3 = new RuleFile();
      var3.setName("决策集");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.RuleSet.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      var3 = new RuleFile();
      var3.setName("决策表");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.DecisionTable.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      var3 = new RuleFile();
      var3.setName("决策树");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.DecisionTree.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      var3 = new RuleFile();
      var3.setName("评分卡");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.Scorecard.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      var3 = new RuleFile();
      var3.setName("决策流");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.Flow.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      var3 = new RuleFile();
      var3.setName("动作模版");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.ActionTemplate.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      var3 = new RuleFile();
      var3.setName("条件模版");
      var3.setDirectory(true);
      var3.setId(0L);
      var3.setProjectId(var1);
      var3.setType(ResourceType.ConditionTemplate.name());
      var3.setVirtual(true);
      var3.setChildren(new ArrayList());
      var2.add(var3);
      return var2;
   }

   public RuleFile copyFile(long var1, long var3, long var5, String var7, String var8) {
      RuleFile var9 = this.a(var1, var3, var5, var7, var8);
      FileCopyUtils.replaceAllContent();
      return var9;
   }

   private RuleFile a(long var1, long var3, long var5, String var7, String var8) {
      RuleFile var9 = FileManager.ins.get(Long.valueOf(var5));
      if (var9 == null) {
         throw new CopyException(String.format("被复制的文件ID【%s】不合法，没有对应的文件！", var5));
      } else if (var9.isDeleted()) {
         throw new CopyException(String.format("回收站文件【%s】无法被复制！", var5));
      } else {
         String var10 = StringUtils.isBlank(var7) ? var9.getName() : var7.trim();
         boolean var11 = FileManager.ins.checkExist(var9.getProjectId(), var3, var9.getType(), var10);
         if (var11) {
            throw new CopyException(String.format("文件名称【%s】重复，无法复制!", var10));
         } else {
            FileCopyUtils.copyFile(var1, var3, var9, var10, var8);
            SystemLogUtils.addRuleFileOperationLog(var9.getType(), "add", var9.getId(), String.format("Create a new %s type file %s[%s]", var9.getType(), var9.getName(), var9.getId()));
            return var9;
         }
      }
   }

   public List copyFiles(long var1, long var3, List var5, String var6) {
      ArrayList var7 = new ArrayList();

      for(Long var9 : (Iterable<Long>)(Iterable<?>)(var5)) {
         RuleFile var10 = DirectoryManager.ins.get(var9);
         if (var10 == null) {
            var10 = FileManager.ins.get(var9);
         }

         if (var10 == null) {
            throw new CopyException(String.format("被复制的文件ID【%s】不合法，没有对应的文件或目录！", var9));
         }

         RuleFile var11 = null;
         if (var10.isDirectory()) {
            var11 = this.b(var1, var3, var9, var10.getName(), var6);
         } else {
            var11 = this.copyFile(var1, var3, var9, var10.getName(), var6);
         }

         var7.add(var11);
      }

      FileCopyUtils.replaceAllContent();
      return var7;
   }

   public RuleFile copyDir(long var1, long var3, long var5, String var7, String var8) {
      RuleFile var9 = this.b(var1, var3, var5, var7, var8);
      FileCopyUtils.replaceAllContent();
      return var9;
   }

   private RuleFile b(long var1, long var3, long var5, String var7, String var8) {
      RuleFile var9 = DirectoryManager.ins.get(var5);
      if (var9 == null) {
         throw new CopyException(String.format("被复制的目录ID【%s】不合法，没有对应的目录！", var5));
      } else if (var9.isDeleted()) {
         throw new CopyException(String.format("回收站目录【%s】无法被复制！", var5));
      } else {
         RuleFile var10 = DirectoryManager.ins.get(var3);
         if (var3 > 0L) {
            if (var10 == null) {
               throw new CopyException(String.format("复制目标目录ID【%s】不合法，没有对应的目录,无法复制!", var5));
            }

            if (!var10.getType().equalsIgnoreCase(var9.getType())) {
               throw new CopyException(String.format("复制目标目录【%s】文件类型和当前被复制的目录【%s】文件类型不一致,无法复制!", var10.getType(), var9.getType()));
            }

            if (var10.isDeleted()) {
               throw new CopyException(String.format("复制目标目录【%s】为回收站目录，无法被复制！", var5));
            }
         }

         String var11 = StringUtils.isBlank(var7) ? var9.getName() : var7.trim();
         boolean var12 = DirectoryManager.ins.checkExist(var9.getProjectId(), var3, var9.getType(), var11);
         if (var12) {
            throw new CopyException(String.format("目录名称【%s】重复，无法复制!", var11));
         } else {
            FileCopyUtils.copyDir(var1, var3, var9, var11, var8);
            return var9;
         }
      }
   }
}
