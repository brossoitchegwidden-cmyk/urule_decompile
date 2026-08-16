package com.bstek.urule.runtime;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DynamicSpringConfigLoader {
   String BEAN_ID = "urule.dynamicSpringConfigLoader";

   void loadDynamicJars(String var1) throws Exception;

   byte[] zipDynamicJars() throws IOException, FileNotFoundException;

   String buildDynamicJarsStoreDirectPath();

   String getDynamicJarsStoreDirectPath();

   String getDynamicJarsIdDigest();

   void resetDynamicJarsIdDigest(String var1);
}
