package com.bstek.urule.console.database.manager.jar;

import com.bstek.urule.console.database.model.DynamicJar;

public interface DynamicJarManager {
   DynamicJarManager ins = new DynamicJarManagerImpl();

   byte[] loadJar(long var1);

   DynamicJar load(long var1);

   void add(DynamicJar var1);

   void update(DynamicJar var1);

   void delete(long var1);

   int createJarFiles(String var1);

   void updateJar(long var1, String var3, String var4, byte[] var5);

   DynamicJarQuery newQuery();
}
