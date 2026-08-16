package com.bstek.urule.console.database.manager.packet.scenario;

import com.bstek.urule.console.database.model.Scenario;

public interface ScenarioManager {
   ScenarioManager ins = new ScenarioManagerImpl();

   Scenario load(long var1);

   ScenarioQuery newQuery();

   void uploadExcel(long var1, String var3, byte[] var4);

   byte[] loadExcelFile(long var1);

   Scenario add(Scenario var1);

   void update(Scenario var1);

   void delete(long var1);

   void deleteByPacketId(long var1);

   void deleteByProjectId(long var1);
}
