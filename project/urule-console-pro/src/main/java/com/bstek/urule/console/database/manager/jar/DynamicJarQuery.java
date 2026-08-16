package com.bstek.urule.console.database.manager.jar;

import java.util.List;

public interface DynamicJarQuery {
   DynamicJarQuery id(long var1);

   DynamicJarQuery nameLike(String var1);

   DynamicJarQuery groupId(String var1);

   DynamicJarQuery descLike(String var1);

   List list();
}
