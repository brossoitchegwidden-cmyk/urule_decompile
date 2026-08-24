package com.bstek.urule.console.database.manager.packet.scenario;

import java.util.List;

public interface ScenarioQuery {
   ScenarioQuery id(long id);

   ScenarioQuery packetId(long packetId);

   ScenarioQuery projectId(long projectId);

   ScenarioQuery nameLike(String name);

   ScenarioQuery descLike(String desc);

   List list();
}
