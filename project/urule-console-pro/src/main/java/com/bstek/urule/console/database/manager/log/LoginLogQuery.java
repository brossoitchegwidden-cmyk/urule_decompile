package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.Page;
import java.util.Date;

public interface LoginLogQuery {
   LoginLogQuery userId(String userId);

   LoginLogQuery userIdLike(String userId);

   LoginLogQuery ip(String ip);

   LoginLogQuery username(String username);

   LoginLogQuery loginDateBegin(Date date);

   LoginLogQuery loginDateEnd(Date date);

   Page paging(int pageIndex, int pageSize);
}
