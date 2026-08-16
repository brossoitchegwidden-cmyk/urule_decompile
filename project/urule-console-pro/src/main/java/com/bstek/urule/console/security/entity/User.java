package com.bstek.urule.console.security.entity;

import java.util.List;

public interface User {
   String getName();

   String getDesc();

   List getGroups();

   void setGroups(List var1);
}
