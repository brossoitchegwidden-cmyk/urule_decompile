package com.bstek.urule.console.database.manager.packet.deploy.file;

import com.bstek.urule.console.database.model.PacketDeployFile;
import java.util.List;

public interface PacketDeployFileManager {
   PacketDeployFileManager ins = new PacketDeployFileManagerImpl();

   PacketDeployFile load(long id);

   List loadFiles(long packetDeployId);

   List loadFilesWithContent(long packetDeployId);

   void add(PacketDeployFile file);

   void deleteByDeployId(long deployId);

   void deleteByProjectId(long projectId);

   PacketDeployFileQuery newQuery();
}
