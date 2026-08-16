package com.bstek.urule.console.database.service.resource;

import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceProvider;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.version.VersionFileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.model.VersionFile;

public class FileResourceProvider implements ResourceProvider {
   public Resource provide(long var1, String var3) {
      if (var3 != null) {
         VersionFileManager var7 = VersionFileManager.ins;
         VersionFile var8 = var7.loadFile(var1, var3);
         String var9 = var7.loadFileContent(var8.getId());
         return new Resource(var1, var9, var8.getPath(), var8.getVersion());
      } else {
         FileManager var4 = FileManager.ins;
         RuleFile var5 = var4.get(var1);
         String var6 = var4.loadContent(var1);
         return new Resource(var1, var6, var5.getPath(), (String)null);
      }
   }
}
