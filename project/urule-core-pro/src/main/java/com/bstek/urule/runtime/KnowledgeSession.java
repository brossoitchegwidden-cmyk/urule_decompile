package com.bstek.urule.runtime;

import com.bstek.urule.runtime.agenda.AgendaFilter;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.rete.ReteInstance;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface KnowledgeSession extends WorkingMemory {
   /**执行当前WorkMemory中所有满足条件的规则*/
   RuleExecutionResponse fireRules();

   /**对当前WorkMemory中所有满足条件的规则进行过滤执行*/
   RuleExecutionResponse fireRules(AgendaFilter filter);

   /**对当前WorkMemory中所有满足条件的规则进行过滤执行,并向WorkingMemory中设置一个Map的参数对象*/
   RuleExecutionResponse fireRules(Map<String, Object> parameters, AgendaFilter filter);

   /**对当前WorkMemory中所有满足条件的规则进行执行，并定义执行的最大数目，超出后就不再执行*/
   RuleExecutionResponse fireRules(int max);

   /**对当前WorkMemory中所有满足条件的规则进行执行，并定义执行的最大数目，超出后就不再执行， 并向WorkingMemory中设置一个Map的参数对象*/
   RuleExecutionResponse fireRules(Map<String, Object> parameters, int max);

   /**对当前WorkMemory中所有满足条件的规则进行过滤执行，并定义执行数目的最大值*/
   RuleExecutionResponse fireRules(AgendaFilter filter, int max);

   /**对当前WorkMemory中所有满足条件的规则进行过滤执行，并定义执行数目的最大值， 并向WorkingMemory中设置一个Map的参数对象*/
   RuleExecutionResponse fireRules(Map<String, Object> parameters, AgendaFilter filter, int max);

   /**对当前WorkMemory中所有满足条件的规则进行执行,并向WorkingMemory中设置一个Map的参数对象*/
   RuleExecutionResponse fireRules(Map<String, Object> parameters);

   /**根据规则流ID，执行目标规则流*/
   FlowExecutionResponse startProcess(String processId);

   /**根据规则流ID，执行目标规则流,并向WorkingMemory中设置一个Map的参数对象*/
   FlowExecutionResponse startProcess(String processId, Map<String, Object> parameters);

   /**执行将日志信息写入到日志文件操作，要看到日志文件我们需要设置urule.debugToFile属性值为true， 同时定义输出文件目录属性urule.defaultHtmlFileDebugPath，这样在urule.debug属性为true情况下就会向这个目录下写入日志文件, 需要的时候，可以通过实现com.bstek.urule.debug.DebugWriter接口定义自己的日志输出文件，这样就可以将日志输出到任何地方*/
   void writeLogFile() throws IOException;

   List<KnowledgePackage> getKnowledgePackageList();

   List<ReteInstance> getReteInstanceList();

   Map<String, KnowledgeSession> getKnowledgeSessionMap();

   KnowledgeSession getParentSession();

   void initFromParentSession(KnowledgeSession parentSession);
}
