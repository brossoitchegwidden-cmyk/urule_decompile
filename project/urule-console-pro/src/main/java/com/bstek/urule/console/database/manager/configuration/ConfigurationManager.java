package com.bstek.urule.console.database.manager.configuration;

import com.bstek.urule.console.config.Configuration;
import com.bstek.urule.console.database.model.Page;

public interface ConfigurationManager {
   ConfigurationManager ins = new ConfigurationManagerImpl();

   /**获取配置项列表*/
   void getConfigurations(Configuration condition, Page page);

   /**查询配置项*/
   Configuration getConfiguration(String key);

   /**删除配置项*/
   void deleteConfiguration(long id);

   /**更新配置项*/
   void updateConfiguration(Configuration configuration);

   /**新增配置项*/
   void insertConfiguration(Configuration configuration);
}
