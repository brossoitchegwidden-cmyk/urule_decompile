package com.bstek.urule.console.database.service.file;

import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.type.RuleFileType;
import java.util.List;

public interface FileService {
   FileService ins = new FileServiceImpl();
   String COPY_FILE_KEY = "urule_file_copy";

   List menus(Long var1);

   List tree(Long var1, Long var2);

   List tree(Long var1, RuleFileType var2);

   void updateFileDeleteFlag(Long var1, boolean var2, String var3);

   List tree(Long var1, Long var2, String var3);

   void removeDir(RuleFile var1);

   void removeDir(RuleFile var1, boolean var2);

   RuleFile copyFile(long var1, long var3, long var5, String var7, String var8);

   List copyFiles(long var1, long var3, List var5, String var6);

   RuleFile copyDir(long var1, long var3, long var5, String var7, String var8);
}
