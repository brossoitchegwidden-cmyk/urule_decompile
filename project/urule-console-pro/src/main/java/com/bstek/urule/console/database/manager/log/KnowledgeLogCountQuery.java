package com.bstek.urule.console.database.manager.log;

import java.util.Date;
import java.util.List;

public interface KnowledgeLogCountQuery {
   KnowledgeLogCountQuery projectId(Long projectId);

   KnowledgeLogCountQuery groupId(String groupId);

   KnowledgeLogCountQuery user(String user);

   KnowledgeLogCountQuery packageId(Long packageId);

   KnowledgeLogCountQuery packageName(String packageName);

   KnowledgeLogCountQuery dateBegin(Date date);

   KnowledgeLogCountQuery dateEnd(Date date);

   List listExec();

   List listTime();
}
