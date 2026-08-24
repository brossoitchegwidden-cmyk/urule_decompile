package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.console.database.model.RuleFile;
import java.util.List;

public interface DirectoryManager {
   long ROOT_FILE_ID = 0L;
   DirectoryManager ins = new DirectoryManagerImpl();

   /**新增目录*/
   void add(RuleFile ruleFile);

   /**获取目录对象*/
   RuleFile get(long id);

   /**目录删除*/
   void remove(long id);

   /**设置目录删除标记*/
   void updateDeleteFlag(long id, boolean deleted, String account);

   /**更改目录名称*/
   void changeName(long id, String newName, String account);

   /**检查是否重名*/
   boolean checkExist(long projectId, long parentId, String type, String name);

   /**加载目录列表*/
   List list(long projectId, long parentId);

   /**加载目录列表*/
   List list(long projectId, long parentId, String type);

   /**目录移动*/
   void changeParent(long id, long newParentId);

   /**目录类型转换*/
   void changeGeneral(long projectId);

   /**目录类型转换*/
   void changeType(long id, String type);

   /**删除指定项目下的所有目录*/
   void deleteByProjectId(long projectId);

   long countByType(long projectId, String type);

   boolean hasTypeFolder(long projectId);
}
