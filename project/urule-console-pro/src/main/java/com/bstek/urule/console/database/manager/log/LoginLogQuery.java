package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.Page;
import java.util.Date;

public interface LoginLogQuery {
   LoginLogQuery userId(String var1);

   LoginLogQuery userIdLike(String var1);

   LoginLogQuery ip(String var1);

   LoginLogQuery username(String var1);

   LoginLogQuery loginDateBegin(Date var1);

   LoginLogQuery loginDateEnd(Date var1);

   Page paging(int var1, int var2);
}
