package com.bstek.urule.console.database.manager.url;

import com.bstek.urule.console.database.model.UrlConfig;

public interface UrlManager {
   UrlManager ins = new UrlManagerImpl();

   void add(UrlConfig var1);

   void delete(long var1);

   void update(UrlConfig var1);

   UrlConfig load(long var1);

   UrlQuery newQuery();
}
