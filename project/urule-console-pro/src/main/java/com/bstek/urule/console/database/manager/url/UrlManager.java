package com.bstek.urule.console.database.manager.url;

import com.bstek.urule.console.database.model.UrlConfig;

public interface UrlManager {
   UrlManager ins = new UrlManagerImpl();

   void add(UrlConfig url);

   void delete(long id);

   void update(UrlConfig url);

   UrlConfig load(long id);

   UrlQuery newQuery();
}
