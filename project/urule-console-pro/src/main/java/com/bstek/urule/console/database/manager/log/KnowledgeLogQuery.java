package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.Page;
import java.util.Date;
import java.util.List;

public interface KnowledgeLogQuery {
   KnowledgeLogQuery projectId(Long projectId);

   KnowledgeLogQuery groupId(String groupId);

   KnowledgeLogQuery user(String user);

   KnowledgeLogQuery ip(String ip);

   KnowledgeLogQuery packetId(Long packetId);

   KnowledgeLogQuery packetNameLike(String packetName);

   KnowledgeLogQuery orderTime();

   KnowledgeLogQuery dateBegin(Date date);

   KnowledgeLogQuery dateEnd(Date date);

   Page paging(int pageIndex, int pageSize);

   List list();

   /**获取日志详情*/
   KnowledgeLog details(Long id);
}
