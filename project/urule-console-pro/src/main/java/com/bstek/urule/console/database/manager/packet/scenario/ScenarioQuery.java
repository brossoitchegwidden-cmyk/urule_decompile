package com.bstek.urule.console.database.manager.packet.scenario;

import java.util.List;

public interface ScenarioQuery {
   ScenarioQuery id(long var1);

   ScenarioQuery packetId(long var1);

   ScenarioQuery projectId(long var1);

   ScenarioQuery nameLike(String var1);

   ScenarioQuery descLike(String var1);

   List list();
}
