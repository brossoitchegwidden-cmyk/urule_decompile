package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.Page;
import java.util.Date;
import java.util.List;

public interface KnowledgeLogQuery {
   KnowledgeLogQuery projectId(Long var1);

   KnowledgeLogQuery groupId(String var1);

   KnowledgeLogQuery user(String var1);

   KnowledgeLogQuery ip(String var1);

   KnowledgeLogQuery packetId(Long var1);

   KnowledgeLogQuery packetNameLike(String var1);

   KnowledgeLogQuery orderTime();

   KnowledgeLogQuery dateBegin(Date var1);

   KnowledgeLogQuery dateEnd(Date var1);

   Page paging(int var1, int var2);

   List list();

   KnowledgeLog details(Long var1);
}
