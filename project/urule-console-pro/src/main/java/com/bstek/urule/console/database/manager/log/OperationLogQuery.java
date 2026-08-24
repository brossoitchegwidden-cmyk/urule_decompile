package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.Page;
import java.util.Date;
import java.util.List;

public interface OperationLogQuery {
   OperationLogQuery userId(String userId);

   OperationLogQuery userIdLike(String userId);

   OperationLogQuery groupId(String groupId);

   OperationLogQuery username(String username);

   OperationLogQuery projectId(Long projectId);

   OperationLogQuery category(String category);

   OperationLogQuery categoryLike(String category);

   OperationLogQuery categoryIn(List categorys);

   OperationLogQuery actionIn(List actions);

   OperationLogQuery dateBegin(Date date);

   OperationLogQuery dateEnd(Date date);

   Page paging(int pageIndex, int pageSize);

   List list();
}
