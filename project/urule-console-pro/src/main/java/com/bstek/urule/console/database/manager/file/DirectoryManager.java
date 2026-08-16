package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.console.database.model.RuleFile;
import java.util.List;

public interface DirectoryManager {
   long ROOT_FILE_ID = 0L;
   DirectoryManager ins = new DirectoryManagerImpl();

   void add(RuleFile var1);

   RuleFile get(long var1);

   void remove(long var1);

   void updateDeleteFlag(long var1, boolean var3, String var4);

   void changeName(long var1, String var3, String var4);

   boolean checkExist(long var1, long var3, String var5, String var6);

   List list(long var1, long var3);

   List list(long var1, long var3, String var5);

   void changeParent(long var1, long var3);

   void changeGeneral(long var1);

   void changeType(long var1, String var3);

   void deleteByProjectId(long var1);

   long countByType(long var1, String var3);

   boolean hasTypeFolder(long var1);
}
