package com.bstek.urule.console.database.manager.repository;

import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface DataSourceQuery {
   DataSourceQuery id(Long id);

   DataSourceQuery nameLike(String nameLike);

   DataSourceQuery createUserLike(String createUser);

   DataSourceQuery type(String type);

   DataSourceQuery groupId(String groupId);

   List list();

   void page(Page page);
}
