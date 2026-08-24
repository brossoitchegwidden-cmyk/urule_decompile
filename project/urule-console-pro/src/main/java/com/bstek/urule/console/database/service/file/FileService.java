package com.bstek.urule.console.database.service.file;

import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.type.RuleFileType;
import java.util.List;

public interface FileService {
   FileService ins = new FileServiceImpl();
   String COPY_FILE_KEY = "urule_file_copy";

   /**获取文件目录树顶层节点*/
   List menus(Long projectId);

   /**获取文件树*/
   List tree(Long projectId, Long parentId);

   /**获取指定类型的文件树*/
   List tree(Long projectId, RuleFileType type);

   void updateFileDeleteFlag(Long id, boolean deleted, String account);

   /**获取指定类型的文件树*/
   List tree(Long projectId, Long parentId, String type);

   /**删除指定目录*/
   void removeDir(RuleFile ruleFile);

   /**删除指定目录*/
   void removeDir(RuleFile ruleFile, boolean force);

   /**复制文件*/
   RuleFile copyFile(long projectId, long parentId, long id, String name, String account);

   /**复制多个文件*/
   List copyFiles(long projectId, long parentId, List idList, String account);

   /**复制目录*/
   RuleFile copyDir(long projectId, long parentId, long id, String name, String account);
}
