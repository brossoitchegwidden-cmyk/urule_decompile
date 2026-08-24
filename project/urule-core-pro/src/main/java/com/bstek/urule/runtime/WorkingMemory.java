package com.bstek.urule.runtime;

import com.bstek.urule.runtime.log.LogManager;
import com.bstek.urule.runtime.rete.Context;
import java.util.List;
import java.util.Map;

public interface WorkingMemory {
   /**插入一个业务数据对象，对应到规则当中就是一个变量对象*/
   boolean insert(Object fact);

   /**更新一个在当前WorkingMemory中已存在的业务对象，如果对象存在，那么WorkingMemory会重新评估这个对象*/
   boolean update(Object fact);

   /**获取当前WorkingMemory中的某个参数值*/
   Object getParameter(String key);

   Map<String, Object> getParameters();

   Map<String, Object> getAllFactsMap();

   List<Object> getFactList();

   /**根据knowledgePackageWrapper的id返回对应的KnowledgeSession对象*/
   KnowledgeSession getKnowledgeSession(String id);

   /**将KnowledgeSession对象放入缓存以备下次调用时使用*/
   void putKnowledgeSession(String id, KnowledgeSession session);

   /**向当前Session中放入变量*/
   void setSessionValue(String key, Object value);

   /**取出当前Session中对应的变量*/
   Object getSessionValue(String key);

   Map<String, Object> getSessionValueMap();

   /**激活某个设置了互斥组属性的具体的规则*/
   void activeRule(String activationGroupName, String ruleName);

   /**激活指定名称的执行组*/
   void activePendedGroup(String groupName);

   /**激活指定名称的执行组并立即执行执行组规则对应的动作部分*/
   void activePendedGroupAndExecute(String groupName);

   /**返回当前上下文对象*/
   Context getContext();

   LogManager getLogManager();

   FactManager getFactManager();
}
