package com.bstek.urule.console.database.manager.file.version;

import com.bstek.urule.console.database.model.VersionFile;
import java.util.List;

public interface VersionFileManager {
   VersionFileManager ins = new VersionFileManagerImpl();

   VersionFile loadFile(long id);

   String loadFileContent(long id);

   List loadFiles(long fileId);

   VersionFile loadFile(long fileId, String version);

   void saveFile(VersionFile file);

   void deleteByProjectId(long projectId);

   void deleteByFileId(long fileId);

   VersionFileQuery newQuery();
}
