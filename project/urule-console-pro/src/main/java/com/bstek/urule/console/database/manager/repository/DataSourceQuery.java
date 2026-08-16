package com.bstek.urule.console.database.manager.repository;

import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface DataSourceQuery {
   DataSourceQuery id(Long var1);

   DataSourceQuery nameLike(String var1);

   DataSourceQuery createUserLike(String var1);

   DataSourceQuery type(String var1);

   DataSourceQuery groupId(String var1);

   List list();

   void page(Page var1);
}
