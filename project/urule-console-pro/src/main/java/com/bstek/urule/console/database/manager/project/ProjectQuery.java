package com.bstek.urule.console.database.manager.project;

import java.util.List;

public interface ProjectQuery {
   ProjectQuery userId(String userId);

   ProjectQuery name(String name);

   ProjectQuery nameLike(String name);

   ProjectQuery type(String type);

   ProjectQuery groupId(String groupId);

   ProjectQuery orderbyCreateDate(String asc);

   ProjectQuery orderbyName(String asc);

   List list();

   List listIds();
}
