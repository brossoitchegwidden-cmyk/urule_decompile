package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.console.database.model.RuleFile;
import java.util.List;

public interface FileManager {
   long ROOT_FILE_ID = 0L;
   FileManager ins = new FileManagerImpl();

   /**获取规则文件*/
   RuleFile get(long id);

   /**新增规则文件*/
   void add(RuleFile file);

   /**更新规则文件(文件同步使用)*/
   void update(RuleFile file);

   /**删除规则文件*/
   void remove(long id);

   /**重命名规则文件*/
   void rename(long id, String account, String newName);

   /**设置文件删除标记*/
   void updateDeleteFlag(long id, boolean deleted, String account);

   /**检测规则文件是否存在*/
   boolean checkExist(long projectId, long parentId, String type, String name);

   /**移动规则文件*/
   void changeParent(long id, long newDirId);

   /**查询指定父节点指定项目下的文件列表*/
   List list(long projectId, long parentId);

   /**查询指定父节点指定项目指定类型下的文件列表*/
   List list(long projectId, long parentId, String type);

   /**加载规则文件xml信息*/
   String loadContent(long id);

   /**更新规则文件xml配置*/
   void updateContent(long id, String account, String content);

   /**锁定文件*/
   void lock(long id, String userId);

   /**解锁文件*/
   void unlock(long id, String version, String account);

   /**删除指定项目下的所有规则文件*/
   void deleteByProjectId(long projectId);

   FileQuery newQuery();

   FileCountQuery newCountQuery();
}
