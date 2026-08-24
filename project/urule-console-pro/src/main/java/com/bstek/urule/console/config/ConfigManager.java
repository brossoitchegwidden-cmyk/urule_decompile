package com.bstek.urule.console.config;

import com.bstek.urule.console.config.exception.ConfigLoadException;
import com.bstek.urule.console.config.exception.SetupException;
import com.bstek.urule.console.config.setup.SetupInfo;

public interface ConfigManager {
   /**加载配置信息*/
   void load() throws ConfigLoadException;

   /**初始化配置文件信息，并保存配置文件*/
   void init(SetupInfo setupInfo) throws SetupException;

   /**获取属性配置值*/
   String getProperty(String key);
}
