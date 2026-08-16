package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.Page;
import java.util.Date;
import java.util.List;

public interface OperationLogQuery {
   OperationLogQuery userId(String var1);

   OperationLogQuery userIdLike(String var1);

   OperationLogQuery groupId(String var1);

   OperationLogQuery username(String var1);

   OperationLogQuery projectId(Long var1);

   OperationLogQuery category(String var1);

   OperationLogQuery categoryLike(String var1);

   OperationLogQuery categoryIn(List var1);

   OperationLogQuery actionIn(List var1);

   OperationLogQuery dateBegin(Date var1);

   OperationLogQuery dateEnd(Date var1);

   Page paging(int var1, int var2);

   List list();
}
