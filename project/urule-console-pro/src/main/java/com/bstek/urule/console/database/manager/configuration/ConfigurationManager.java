package com.bstek.urule.console.database.manager.configuration;

import com.bstek.urule.console.config.Configuration;
import com.bstek.urule.console.database.model.Page;

public interface ConfigurationManager {
   ConfigurationManager ins = new ConfigurationManagerImpl();

   void getConfigurations(Configuration var1, Page var2);

   Configuration getConfiguration(String var1);

   void deleteConfiguration(long var1);

   void updateConfiguration(Configuration var1);

   void insertConfiguration(Configuration var1);
}
