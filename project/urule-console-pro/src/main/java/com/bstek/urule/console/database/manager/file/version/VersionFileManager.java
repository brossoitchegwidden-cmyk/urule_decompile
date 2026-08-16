package com.bstek.urule.console.database.manager.file.version;

import com.bstek.urule.console.database.model.VersionFile;
import java.util.List;

public interface VersionFileManager {
   VersionFileManager ins = new VersionFileManagerImpl();

   VersionFile loadFile(long var1);

   String loadFileContent(long var1);

   List loadFiles(long var1);

   VersionFile loadFile(long var1, String var3);

   void saveFile(VersionFile var1);

   void deleteByProjectId(long var1);

   void deleteByFileId(long var1);

   VersionFileQuery newQuery();
}
