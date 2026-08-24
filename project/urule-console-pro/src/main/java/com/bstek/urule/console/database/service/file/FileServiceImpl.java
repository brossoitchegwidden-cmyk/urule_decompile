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
   public List tree(Long projectId, RuleFileType type) {
      return this.tree(projectId, 0L, type.name());
   }
   public void removeDir(RuleFile ruleFile) {
      this.removeDir(ruleFile, true);
   }
   public void removeDir(RuleFile ruleFile, boolean force) {
      for(RuleFile ruleFile2 : (Iterable<RuleFile>)(Iterable<?>)(this.tree(ruleFile.getProjectId(), ruleFile.getId(), ruleFile.getType()))) {
         if (ruleFile2.isDirectory()) {
            this.executeServiceOperation(ruleFile2);
         } else {
            if (!force) {
               List items = ReferenceService.ins.uuid(ruleFile2.getProjectId(), ruleFile2.getId(), (String)null);
               if (items.size() > 0) {
                  throw new ReferenceDeleteException(items.size());
               }
            }

            VersionFileManager.ins.deleteByFileId(ruleFile2.getId());
            FileManager.ins.remove(ruleFile2.getId());
         }
      }

      DirectoryManager.ins.remove(ruleFile.getId());
   }

   private void executeServiceOperation(RuleFile ruleFile) {
      for(RuleFile ruleFile2 : (Iterable<RuleFile>)(Iterable<?>)(ruleFile.getChildren())) {
         if (ruleFile2.isDirectory()) {
            this.executeServiceOperation(ruleFile2);
         } else {
            VersionFileManager.ins.deleteByFileId(ruleFile2.getId());
            FileManager.ins.remove(ruleFile2.getId());
         }
      }

      DirectoryManager.ins.remove(ruleFile.getId());
   }
   public List tree(Long projectId, Long parentId) {
      Connection connection = JdbcUtils.getConnection();

      List treeResult;
      try {
         List items = DirectoryManager.ins.list(projectId, parentId);

         for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
            List items2 = this.tree(projectId, ruleFile.getId());
            ruleFile.setChildren(items2);
         }

         List items3 = FileManager.ins.list(projectId, parentId);
         items3.addAll(items);
         treeResult = items3;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return treeResult;
   }
   public List tree(Long projectId, Long parentId, String type) {
      Connection connection = JdbcUtils.getConnection();

      List treeResult;
      try {
         List items = DirectoryManager.ins.list(projectId, parentId, type);

         for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
            List items2 = this.tree(projectId, ruleFile.getId(), type);
            ruleFile.setChildren(items2);
         }

         Object objectValue = null;
         List items3;
         if (ResourceType.General.name().equals(type)) {
            items3 = FileManager.ins.list(projectId, parentId);
         } else {
            items3 = FileManager.ins.list(projectId, parentId, type);
         }

         items3.addAll(items);
         treeResult = items3;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return treeResult;
   }

   public void updateFileDeleteFlag(Long id, boolean deleted, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         RuleFile ruleFile = FileManager.ins.get(id);
         if (ruleFile != null) {
            FileManager.ins.updateDeleteFlag(id, deleted, account);
            if (!deleted && ruleFile.getParentId() > 0L) {
               DirectoryManager.ins.updateDeleteFlag(ruleFile.getParentId(), false, account);
            }
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public List menus(Long projectId) {
      ArrayList menusResult = new ArrayList();
      RuleFile ruleFile = new RuleFile();
      ruleFile.setName("规则");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.General.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      ruleFile = new RuleFile();
      ruleFile.setName("库文件");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.Library.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      ruleFile = new RuleFile();
      ruleFile.setName("决策集");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.RuleSet.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      ruleFile = new RuleFile();
      ruleFile.setName("决策表");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.DecisionTable.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      ruleFile = new RuleFile();
      ruleFile.setName("决策树");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.DecisionTree.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      ruleFile = new RuleFile();
      ruleFile.setName("评分卡");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.Scorecard.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      ruleFile = new RuleFile();
      ruleFile.setName("决策流");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.Flow.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      ruleFile = new RuleFile();
      ruleFile.setName("动作模版");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.ActionTemplate.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      ruleFile = new RuleFile();
      ruleFile.setName("条件模版");
      ruleFile.setDirectory(true);
      ruleFile.setId(0L);
      ruleFile.setProjectId(projectId);
      ruleFile.setType(ResourceType.ConditionTemplate.name());
      ruleFile.setVirtual(true);
      ruleFile.setChildren(new ArrayList());
      menusResult.add(ruleFile);
      return menusResult;
   }
   public RuleFile copyFile(long projectId, long parentId, long id, String name, String account) {
      RuleFile ruleFile = this.executeServiceOperation(projectId, parentId, id, name, account);
      FileCopyUtils.replaceAllContent();
      return ruleFile;
   }

   private RuleFile executeServiceOperation(long longValue, long longValue2, long longValue3, String text, String text2) {
      RuleFile ruleFile = FileManager.ins.get(Long.valueOf(longValue3));
      if (ruleFile == null) {
         throw new CopyException(String.format("被复制的文件ID【%s】不合法，没有对应的文件！", longValue3));
      } else if (ruleFile.isDeleted()) {
         throw new CopyException(String.format("回收站文件【%s】无法被复制！", longValue3));
      } else {
         String text3 = StringUtils.isBlank(text) ? ruleFile.getName() : text.trim();
         boolean flag = FileManager.ins.checkExist(ruleFile.getProjectId(), longValue2, ruleFile.getType(), text3);
         if (flag) {
            throw new CopyException(String.format("文件名称【%s】重复，无法复制!", text3));
         } else {
            FileCopyUtils.copyFile(longValue, longValue2, ruleFile, text3, text2);
            SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "add", ruleFile.getId(), String.format("Create a new %s type file %s[%s]", ruleFile.getType(), ruleFile.getName(), ruleFile.getId()));
            return ruleFile;
         }
      }
   }
   public List copyFiles(long projectId, long parentId, List idList, String account) {
      ArrayList copyFilesResult = new ArrayList();

      for(Long longValue : (Iterable<Long>)(Iterable<?>)(idList)) {
         RuleFile ruleFile = DirectoryManager.ins.get(longValue);
         if (ruleFile == null) {
            ruleFile = FileManager.ins.get(longValue);
         }

         if (ruleFile == null) {
            throw new CopyException(String.format("被复制的文件ID【%s】不合法，没有对应的文件或目录！", longValue));
         }

         RuleFile ruleFile2 = null;
         if (ruleFile.isDirectory()) {
            ruleFile2 = this.resolveRuleFile(projectId, parentId, longValue, ruleFile.getName(), account);
         } else {
            ruleFile2 = this.copyFile(projectId, parentId, longValue, ruleFile.getName(), account);
         }

         copyFilesResult.add(ruleFile2);
      }

      FileCopyUtils.replaceAllContent();
      return copyFilesResult;
   }
   public RuleFile copyDir(long projectId, long parentId, long id, String name, String account) {
      RuleFile ruleFile = this.resolveRuleFile(projectId, parentId, id, name, account);
      FileCopyUtils.replaceAllContent();
      return ruleFile;
   }

   private RuleFile resolveRuleFile(long longValue, long longValue2, long longValue3, String text, String text2) {
      RuleFile ruleFile = DirectoryManager.ins.get(longValue3);
      if (ruleFile == null) {
         throw new CopyException(String.format("被复制的目录ID【%s】不合法，没有对应的目录！", longValue3));
      } else if (ruleFile.isDeleted()) {
         throw new CopyException(String.format("回收站目录【%s】无法被复制！", longValue3));
      } else {
         RuleFile ruleFile2 = DirectoryManager.ins.get(longValue2);
         if (longValue2 > 0L) {
            if (ruleFile2 == null) {
               throw new CopyException(String.format("复制目标目录ID【%s】不合法，没有对应的目录,无法复制!", longValue3));
            }

            if (!ruleFile2.getType().equalsIgnoreCase(ruleFile.getType())) {
               throw new CopyException(String.format("复制目标目录【%s】文件类型和当前被复制的目录【%s】文件类型不一致,无法复制!", ruleFile2.getType(), ruleFile.getType()));
            }

            if (ruleFile2.isDeleted()) {
               throw new CopyException(String.format("复制目标目录【%s】为回收站目录，无法被复制！", longValue3));
            }
         }

         String text3 = StringUtils.isBlank(text) ? ruleFile.getName() : text.trim();
         boolean flag = DirectoryManager.ins.checkExist(ruleFile.getProjectId(), longValue2, ruleFile.getType(), text3);
         if (flag) {
            throw new CopyException(String.format("目录名称【%s】重复，无法复制!", text3));
         } else {
            FileCopyUtils.copyDir(longValue, longValue2, ruleFile, text3, text2);
            return ruleFile;
         }
      }
   }
}
