package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.console.database.model.RuleFile;
import java.util.List;

public interface FileManager {
   long ROOT_FILE_ID = 0L;
   FileManager ins = new FileManagerImpl();

   RuleFile get(long var1);

   void add(RuleFile var1);

   void update(RuleFile var1);

   void remove(long var1);

   void rename(long var1, String var3, String var4);

   void updateDeleteFlag(long var1, boolean var3, String var4);

   boolean checkExist(long var1, long var3, String var5, String var6);

   void changeParent(long var1, long var3);

   List list(long var1, long var3);

   List list(long var1, long var3, String var5);

   String loadContent(long var1);

   void updateContent(long var1, String var3, String var4);

   void lock(long var1, String var3);

   void unlock(long var1, String var3, String var4);

   void deleteByProjectId(long var1);

   FileQuery newQuery();

   FileCountQuery newCountQuery();
}
