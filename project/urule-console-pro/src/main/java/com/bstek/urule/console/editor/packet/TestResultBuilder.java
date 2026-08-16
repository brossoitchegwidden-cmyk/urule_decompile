package com.bstek.urule.console.editor.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.console.editor.packet.scenario.ResultWrapper;
import com.bstek.urule.console.editor.packet.scenario.TestResult;
import com.bstek.urule.console.editor.packet.scenario.ValueCompare;
import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.runtime.log.DataLog;
import com.bstek.urule.runtime.log.FlowNodeLog;
import com.bstek.urule.runtime.log.Log;
import com.bstek.urule.runtime.log.MatchedRuleLog;
import com.bstek.urule.runtime.log.UnitLog;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestResultBuilder {
   public static Map build(ResultWrapper var0) throws Exception {
      SimpleDateFormat var1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String var2 = "基于场景的批量测试报告-" + var1.format(new Date());
      String var3 = a(var0);
      HashMap var4 = new HashMap();
      var4.put("title", var2);
      var4.put("content", var3);
      return var4;
   }

   private static String a(ResultWrapper var0) {
      StringBuilder var1 = new StringBuilder();
      if (var0 == null) {
         var1.append("<h1>方案测试报告不存在，或已过期，请重新测试具体方案!</h1>");
         return var1.toString();
      } else {
         SimpleDateFormat var2 = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
         var1.append("<h5 style=\"margin:5px;\">报告生成时间：" + var2.format(new Date()) + "</h5>");
         var1.append("<h5 style=\"margin:5px;\">准备数据(解析方案中的Excel文件等)耗时：" + var0.getPrepareTime() + "ms，运行规则耗时：" + var0.getTotalTime() + "ms</h5>");
         StringBuilder var3 = new StringBuilder();
         var3.append("<table style='margin:5px;border-collapse: collapse;border:solid 1px #cacaca;font-size:12px;width:100%' border='1'>");
         int var4 = 0;
         int var5 = 0;

         for(TestResult var7 : (Iterable<TestResult>)(Iterable<?>)(var0.getResultList())) {
            int var8 = 0;
            int var9 = 0;

            for(ValueCompare var11 : (Iterable<ValueCompare>)(Iterable<?>)(var7.getValueCompares())) {
               if (var11.isMatched()) {
                  ++var8;
               } else {
                  ++var9;
               }
            }

            if (var9 > 0) {
               var3.append("<tr name='fail_row'>");
            } else {
               var3.append("<tr name='success_row'>");
            }

            var3.append("<td>");
            var3.append("<div style=\"margin:5px\">场景" + var7.getScenarioId() + "，" + var7.getScenarioDesc() + "</div>");
            var3.append("<div style=\"margin:5px\">耗时：" + var7.getConsumeTime() + "ms，预期结果匹配数量为：" + var8 + "条，不匹配为：" + var9 + "条，");
            if (var9 > 0) {
               var3.append("<span style=\"color:red\"><label>失败&nbsp;X</label></span>");
               ++var5;
            } else {
               var3.append("<span style=\"color:green\">成功&radic;</span>");
               ++var4;
            }

            var3.append("<span  name='detail' style='margin-left:10px;cursor:pointer;color:blue' c-data='" + var7.getScenarioId() + "'>明细</span>");
            var3.append("</div>");
            String var13 = a(var0, var7);
            var3.append(var13);
            var3.append("</td>");
            var3.append("</tr>");
         }

         BigDecimal var12 = Utils.toBigDecimal(var4).divide(Utils.toBigDecimal(var5 + var4), 2, 4).multiply(Utils.toBigDecimal(100));
         var1.append("<h5 style=\"margin:5px;\">成功：" + var4 + "条，失败：" + var5 + "条；成功率：" + var12.toPlainString() + "%；<span style='cursor:pointer;text-decoration:underline' id='see_success'>只看成功的</span>，<span style='cursor:pointer;text-decoration:underline' id='see_fail'>只看失败的</span>，<span style='cursor:pointer;text-decoration:underline' id='see_all'>查看全部</span></h5>");
         var3.append("</td>");
         var3.append("</tr>");
         var3.append("</table>");
         var1.append(var3);
         return var1.toString();
      }
   }

   private static String a(ResultWrapper var0, TestResult var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("<div id='detailContent-" + var1.getScenarioId() + "' style='margin:5px;background:#f7f7f7;display:none'>");
      var2.append("<div style='margin-top:20px'><h3>详细内容</h3></div>");
      var2.append("<fieldset style='border:solid 1px #ddd;border-radius:5px;margin:5px'>");
      var2.append("<legend>");
      var2.append("输入数据");
      var2.append("</legend>");
      var2.append("<pre style='color:#9c27b0;white-space:pre-wrap'>");
      var2.append(var1.getInputData());
      var2.append("</pre>");
      var2.append("</fieldset>");
      var2.append("<fieldset style='border:solid 1px #ddd;border-radius:5px;margin:5px'>");
      var2.append("<legend>");
      var2.append("预期输出数据");
      var2.append("</legend>");
      var2.append("<pre style='color:#2196F3;white-space:pre-wrap'>");
      var2.append(var1.getOutputData());
      var2.append("</pre>");
      var2.append("</fieldset>");
      int var3 = 1;
      var2.append("<fieldset style='border:solid 1px #ddd;border-radius:5px;margin:5px'>");
      var2.append("<legend>");
      var2.append("预期结果匹配情况");
      var2.append("</legend>");

      for(ValueCompare var5 : (Iterable<ValueCompare>)(Iterable<?>)(var1.getValueCompares())) {
         var2.append("<div style=\"margin-top:5px\">");
         var2.append(var3 + ".");
         var2.append("对象\"" + var5.getCategory() + "\"的");
         var2.append("\"" + var5.getFieldName() + "\"" + var5.getOp().toString() + "预期值");
         if (var5.getExpectedData() == null) {
            var2.append("null");
         } else {
            var2.append("\"" + var5.getExpectedData() + "\"");
         }

         var2.append(",实际值为");
         if (var5.getData() == null) {
            var2.append("null");
         } else {
            var2.append("\"" + var5.getData() + "\"");
         }

         var2.append(",");
         if (var5.isMatched()) {
            var2.append("<span style=\"color:green\">成功&nbsp;&radic;</span>");
         } else {
            var2.append("<span style=\"color:red\"><label>失败&nbsp;X</label></span>");
         }

         var2.append("</div>");
         ++var3;
      }

      var2.append("</fieldset>");
      var2.append("<div style=\"margin-top:5px\"><span style='color:#545454'>触发的规则数量：</span>" + var1.getMatchedRuleList().size() + "<div>");
      var2.append("<div style=\"margin-top:5px\"><span style='color:#545454'>未触发的规则数量：</span>" + var1.getNotMatchedRuleList().size() + "<div>");
      var2.append("<div style='margin-top:5px'><span style=\"color:#545454;\">经过的规则流节点数量：</span>" + var1.getFlowNodeList().size() + "<div>");
      var2.append("<div style=\"margin-top:5px\"><span style='color:#545454'>触发的规则列表：</span>");
      var2.append("<span style='margin:5px;color:#9c27b0'>");
      boolean var7 = false;

      for(MatchedRuleLog var6 : (Iterable<MatchedRuleLog>)(Iterable<?>)(var1.getMatchedRuleList())) {
         if (var7) {
            var2.append("、");
         }

         var2.append(var6.getRuleName() + "<span style='color:#795548'>(" + a(var6.getRuleFile()) + ")</span>");
         var7 = true;
      }

      var2.append("</span>");
      var2.append("<div>");
      var2.append("<div style=\"margin-top:5px\"><span style='color:#545454'>未触发的规则列表：</span>");
      var2.append("<span style='margin:5px;color:#9c27b0'>");

      for(RuleData var12 : (Iterable<RuleData>)(Iterable<?>)(var1.getNotMatchedRuleList())) {
         if (var7) {
            var2.append("、");
         }

         var2.append(var12.getName() + "<span style='color:#795548'>(" + a(var12.getFile()) + ")</span>");
         var7 = true;
      }

      var2.append("</span>");
      var2.append("<div>");
      var2.append("<div style='margin-top:5px'><span style=\"color:#545454;\">经过的规则流节点列表：</span>");
      var2.append("<span style='margin:5px;color:#9c27b0'>");

      for(FlowNodeLog var13 : (Iterable<FlowNodeLog>)(Iterable<?>)(var1.getFlowNodeList())) {
         if (var7) {
            var2.append("、");
         }

         var2.append(var13.getNodeName() + "<span style='color:#795548'>(" + a(var13.getFile()) + ")</span>");
         var7 = true;
      }

      var2.append("</span>");
      var2.append("<div>");
      var2.append("<div style=\"color:#545454;margin-top:5px\">运行日志：</div>");
      List var11 = var1.getLogs();
      if (var11 != null && var11.size() != 0) {
         var2.append("<div style=\"font-size:11px;border:dotted 1px #a5a5a5;margin:5px;padding:5px;border-radius:5px\">");
         a(var2, var11);
         var2.append("</div>");
      } else {
         var2.append("<div style='color:#df3600'>当前未开启日志输出功能</div>");
      }

      var2.append("</div>");
      return var2.toString();
   }

   private static String a(String var0) {
      return var0 == null ? "无" : var0;
   }

   private static void a(StringBuilder var0, List var1) {
      for(Log var3 : (Iterable<Log>)(Iterable<?>)(var1)) {
         if (var3 instanceof UnitLog) {
            var0.append("<div style=\"margin:8px;border:dashed 1px #cccccc\">");
            UnitLog var4 = (UnitLog)var3;
            List var5 = var4.getLogs();
            a(var0, var5);
            var0.append("</div>");
         } else if (var3 instanceof DataLog) {
            DataLog var6 = (DataLog)var3;
            String var7 = var6.getHtmlMsg();
            var0.append(var7);
         }
      }

   }
}
