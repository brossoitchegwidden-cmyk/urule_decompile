package com.bstek.urule.console.database.manager.url;

import com.bstek.urule.console.database.model.UrlType;
import java.util.List;

public interface UrlQuery {
   UrlQuery nameLike(String var1);

   UrlQuery urlLike(String var1);

   UrlQuery type(UrlType var1);

   UrlQuery groupId(String var1);

   UrlQuery id(long var1);

   List list();
}
