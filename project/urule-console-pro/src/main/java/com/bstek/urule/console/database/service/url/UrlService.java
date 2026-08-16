package com.bstek.urule.console.database.service.url;

import com.bstek.urule.console.database.model.UrlType;

public interface UrlService {
   UrlService ins = new UrlServiceImpl();

   UrlData load(UrlType var1, String var2);
}
