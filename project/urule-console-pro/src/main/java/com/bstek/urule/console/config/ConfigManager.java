package com.bstek.urule.console.config;

import com.bstek.urule.console.config.exception.ConfigLoadException;
import com.bstek.urule.console.config.exception.SetupException;
import com.bstek.urule.console.config.setup.SetupInfo;

public interface ConfigManager {
   void load() throws ConfigLoadException;

   void init(SetupInfo var1) throws SetupException;

   String getProperty(String var1);
}
