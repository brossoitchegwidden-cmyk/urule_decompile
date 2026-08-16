package com.bstek.urule.console.database.manager.packet.deploy.file;

import com.bstek.urule.console.database.model.PacketDeployFile;
import java.util.List;

public interface PacketDeployFileManager {
   PacketDeployFileManager ins = new PacketDeployFileManagerImpl();

   PacketDeployFile load(long var1);

   List loadFiles(long var1);

   List loadFilesWithContent(long var1);

   void add(PacketDeployFile var1);

   void deleteByDeployId(long var1);

   void deleteByProjectId(long var1);

   PacketDeployFileQuery newQuery();
}
