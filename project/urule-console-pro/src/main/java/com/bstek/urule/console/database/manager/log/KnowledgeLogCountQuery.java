package com.bstek.urule.console.database.manager.log;

import java.util.Date;
import java.util.List;

public interface KnowledgeLogCountQuery {
   KnowledgeLogCountQuery projectId(Long var1);

   KnowledgeLogCountQuery groupId(String var1);

   KnowledgeLogCountQuery user(String var1);

   KnowledgeLogCountQuery packageId(Long var1);

   KnowledgeLogCountQuery packageName(String var1);

   KnowledgeLogCountQuery dateBegin(Date var1);

   KnowledgeLogCountQuery dateEnd(Date var1);

   List listExec();

   List listTime();
}
