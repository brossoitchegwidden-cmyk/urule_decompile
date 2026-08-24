package com.bstek.urule.console.database.manager.url;

import com.bstek.urule.console.database.model.UrlType;
import java.util.List;

public interface UrlQuery {
   UrlQuery nameLike(String name);

   UrlQuery urlLike(String url);

   UrlQuery type(UrlType type);

   UrlQuery groupId(String groupId);

   UrlQuery id(long id);

   List list();
}
