package com.bstek.urule.console.database.manager.packet.scenario;

import com.bstek.urule.console.database.model.Scenario;

public interface ScenarioManager {
   ScenarioManager ins = new ScenarioManagerImpl();

   Scenario load(long id);

   ScenarioQuery newQuery();

   void uploadExcel(long id, String username, byte[] bytes);

   byte[] loadExcelFile(long id);

   Scenario add(Scenario scenario);

   void update(Scenario scenario);

   void delete(long id);

   void deleteByPacketId(long packetId);

   void deleteByProjectId(long projectId);
}
