package com.bstek.urule.console.database.manager.project;

import java.util.List;

public interface ProjectQuery {
   ProjectQuery userId(String var1);

   ProjectQuery name(String var1);

   ProjectQuery nameLike(String var1);

   ProjectQuery type(String var1);

   ProjectQuery groupId(String var1);

   ProjectQuery orderbyCreateDate(String var1);

   ProjectQuery orderbyName(String var1);

   List list();

   List listIds();
}
