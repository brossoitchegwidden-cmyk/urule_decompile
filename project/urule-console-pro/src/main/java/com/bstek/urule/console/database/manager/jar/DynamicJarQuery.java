package com.bstek.urule.console.database.manager.jar;

import java.util.List;

public interface DynamicJarQuery {
   DynamicJarQuery id(long id);

   DynamicJarQuery nameLike(String name);

   DynamicJarQuery groupId(String groupId);

   DynamicJarQuery descLike(String desc);

   List list();
}
