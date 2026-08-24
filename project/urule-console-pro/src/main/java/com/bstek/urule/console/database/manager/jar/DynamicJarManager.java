package com.bstek.urule.console.database.manager.jar;

import com.bstek.urule.console.database.model.DynamicJar;

public interface DynamicJarManager {
   DynamicJarManager ins = new DynamicJarManagerImpl();

   byte[] loadJar(long id);

   DynamicJar load(long id);

   void add(DynamicJar jar);

   void update(DynamicJar jar);

   void delete(long id);

   int createJarFiles(String path);

   void updateJar(long id, String fileName, String updateUser, byte[] bytes);

   DynamicJarQuery newQuery();
}
