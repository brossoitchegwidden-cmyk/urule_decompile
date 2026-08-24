package com.bstek.urule.console.database.service.resource;

import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceProvider;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.version.VersionFileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.model.VersionFile;

public class FileResourceProvider implements ResourceProvider {
   public Resource provide(long fileId, String version) {
      if (version != null) {
         VersionFileManager versionFileManager = VersionFileManager.ins;
         VersionFile file = versionFileManager.loadFile(fileId, version);
         String fileContent = versionFileManager.loadFileContent(file.getId());
         return new Resource(fileId, fileContent, file.getPath(), file.getVersion());
      } else {
         FileManager fileManager = FileManager.ins;
         RuleFile ruleFile = fileManager.get(fileId);
         String content = fileManager.loadContent(fileId);
         return new Resource(fileId, content, ruleFile.getPath(), (String)null);
      }
   }
}
