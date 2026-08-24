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
   public static Map build(ResultWrapper result) throws Exception {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String text = "基于场景的批量测试报告-" + simpleDateFormat.format(new Date());
      String text2 = buildReportContent(result);
      HashMap buildResult = new HashMap();
      buildResult.put("title", text);
      buildResult.put("content", text2);
      return buildResult;
   }

   private static String buildReportContent(ResultWrapper resultWrapper) {
      StringBuilder stringBuilder = new StringBuilder();
      if (resultWrapper == null) {
         stringBuilder.append("<h1>方案测试报告不存在，或已过期，请重新测试具体方案!</h1>");
         return stringBuilder.toString();
      } else {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
         stringBuilder.append("<h5 style=\"margin:5px;\">报告生成时间：" + simpleDateFormat.format(new Date()) + "</h5>");
         stringBuilder.append("<h5 style=\"margin:5px;\">准备数据(解析方案中的Excel文件等)耗时：" + resultWrapper.getPrepareTime() + "ms，运行规则耗时：" + resultWrapper.getTotalTime() + "ms</h5>");
         StringBuilder stringBuilder2 = new StringBuilder();
         stringBuilder2.append("<table style='margin:5px;border-collapse: collapse;border:solid 1px #cacaca;font-size:12px;width:100%' border='1'>");
         int number = 0;
         int number2 = 0;

         for(TestResult testResult : (Iterable<TestResult>)(Iterable<?>)(resultWrapper.getResultList())) {
            int number3 = 0;
            int number4 = 0;

            for(ValueCompare valueCompare : (Iterable<ValueCompare>)(Iterable<?>)(testResult.getValueCompares())) {
               if (valueCompare.isMatched()) {
                  ++number3;
               } else {
                  ++number4;
               }
            }

            if (number4 > 0) {
               stringBuilder2.append("<tr name='fail_row'>");
            } else {
               stringBuilder2.append("<tr name='success_row'>");
            }

            stringBuilder2.append("<td>");
            stringBuilder2.append("<div style=\"margin:5px\">场景" + testResult.getScenarioId() + "，" + testResult.getScenarioDesc() + "</div>");
            stringBuilder2.append("<div style=\"margin:5px\">耗时：" + testResult.getConsumeTime() + "ms，预期结果匹配数量为：" + number3 + "条，不匹配为：" + number4 + "条，");
            if (number4 > 0) {
               stringBuilder2.append("<span style=\"color:red\"><label>失败&nbsp;X</label></span>");
               ++number2;
            } else {
               stringBuilder2.append("<span style=\"color:green\">成功&radic;</span>");
               ++number;
            }

            stringBuilder2.append("<span  name='detail' style='margin-left:10px;cursor:pointer;color:blue' c-data='" + testResult.getScenarioId() + "'>明细</span>");
            stringBuilder2.append("</div>");
            String text = buildScenarioDetails(resultWrapper, testResult);
            stringBuilder2.append(text);
            stringBuilder2.append("</td>");
            stringBuilder2.append("</tr>");
         }

         BigDecimal decimalValue = Utils.toBigDecimal(number).divide(Utils.toBigDecimal(number2 + number), 2, 4).multiply(Utils.toBigDecimal(100));
         stringBuilder.append("<h5 style=\"margin:5px;\">成功：" + number + "条，失败：" + number2 + "条；成功率：" + decimalValue.toPlainString() + "%；<span style='cursor:pointer;text-decoration:underline' id='see_success'>只看成功的</span>，<span style='cursor:pointer;text-decoration:underline' id='see_fail'>只看失败的</span>，<span style='cursor:pointer;text-decoration:underline' id='see_all'>查看全部</span></h5>");
         stringBuilder2.append("</td>");
         stringBuilder2.append("</tr>");
         stringBuilder2.append("</table>");
         stringBuilder.append(stringBuilder2);
         return stringBuilder.toString();
      }
   }

   private static String buildScenarioDetails(ResultWrapper resultWrapper, TestResult testResult) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("<div id='detailContent-" + testResult.getScenarioId() + "' style='margin:5px;background:#f7f7f7;display:none'>");
      stringBuilder.append("<div style='margin-top:20px'><h3>详细内容</h3></div>");
      stringBuilder.append("<fieldset style='border:solid 1px #ddd;border-radius:5px;margin:5px'>");
      stringBuilder.append("<legend>");
      stringBuilder.append("输入数据");
      stringBuilder.append("</legend>");
      stringBuilder.append("<pre style='color:#9c27b0;white-space:pre-wrap'>");
      stringBuilder.append(testResult.getInputData());
      stringBuilder.append("</pre>");
      stringBuilder.append("</fieldset>");
      stringBuilder.append("<fieldset style='border:solid 1px #ddd;border-radius:5px;margin:5px'>");
      stringBuilder.append("<legend>");
      stringBuilder.append("预期输出数据");
      stringBuilder.append("</legend>");
      stringBuilder.append("<pre style='color:#2196F3;white-space:pre-wrap'>");
      stringBuilder.append(testResult.getOutputData());
      stringBuilder.append("</pre>");
      stringBuilder.append("</fieldset>");
      int number = 1;
      stringBuilder.append("<fieldset style='border:solid 1px #ddd;border-radius:5px;margin:5px'>");
      stringBuilder.append("<legend>");
      stringBuilder.append("预期结果匹配情况");
      stringBuilder.append("</legend>");

      for(ValueCompare valueCompare : (Iterable<ValueCompare>)(Iterable<?>)(testResult.getValueCompares())) {
         stringBuilder.append("<div style=\"margin-top:5px\">");
         stringBuilder.append(number + ".");
         stringBuilder.append("对象\"" + valueCompare.getCategory() + "\"的");
         stringBuilder.append("\"" + valueCompare.getFieldName() + "\"" + valueCompare.getOp().toString() + "预期值");
         if (valueCompare.getExpectedData() == null) {
            stringBuilder.append("null");
         } else {
            stringBuilder.append("\"" + valueCompare.getExpectedData() + "\"");
         }

         stringBuilder.append(",实际值为");
         if (valueCompare.getData() == null) {
            stringBuilder.append("null");
         } else {
            stringBuilder.append("\"" + valueCompare.getData() + "\"");
         }

         stringBuilder.append(",");
         if (valueCompare.isMatched()) {
            stringBuilder.append("<span style=\"color:green\">成功&nbsp;&radic;</span>");
         } else {
            stringBuilder.append("<span style=\"color:red\"><label>失败&nbsp;X</label></span>");
         }

         stringBuilder.append("</div>");
         ++number;
      }

      stringBuilder.append("</fieldset>");
      stringBuilder.append("<div style=\"margin-top:5px\"><span style='color:#545454'>触发的规则数量：</span>" + testResult.getMatchedRuleList().size() + "<div>");
      stringBuilder.append("<div style=\"margin-top:5px\"><span style='color:#545454'>未触发的规则数量：</span>" + testResult.getNotMatchedRuleList().size() + "<div>");
      stringBuilder.append("<div style='margin-top:5px'><span style=\"color:#545454;\">经过的规则流节点数量：</span>" + testResult.getFlowNodeList().size() + "<div>");
      stringBuilder.append("<div style=\"margin-top:5px\"><span style='color:#545454'>触发的规则列表：</span>");
      stringBuilder.append("<span style='margin:5px;color:#9c27b0'>");
      boolean flag = false;

      for(MatchedRuleLog matchedRuleLog : (Iterable<MatchedRuleLog>)(Iterable<?>)(testResult.getMatchedRuleList())) {
         if (flag) {
            stringBuilder.append("、");
         }

         stringBuilder.append(matchedRuleLog.getRuleName() + "<span style='color:#795548'>(" + valueOrNone(matchedRuleLog.getRuleFile()) + ")</span>");
         flag = true;
      }

      stringBuilder.append("</span>");
      stringBuilder.append("<div>");
      stringBuilder.append("<div style=\"margin-top:5px\"><span style='color:#545454'>未触发的规则列表：</span>");
      stringBuilder.append("<span style='margin:5px;color:#9c27b0'>");

      for(RuleData ruleData : (Iterable<RuleData>)(Iterable<?>)(testResult.getNotMatchedRuleList())) {
         if (flag) {
            stringBuilder.append("、");
         }

         stringBuilder.append(ruleData.getName() + "<span style='color:#795548'>(" + valueOrNone(ruleData.getFile()) + ")</span>");
         flag = true;
      }

      stringBuilder.append("</span>");
      stringBuilder.append("<div>");
      stringBuilder.append("<div style='margin-top:5px'><span style=\"color:#545454;\">经过的规则流节点列表：</span>");
      stringBuilder.append("<span style='margin:5px;color:#9c27b0'>");

      for(FlowNodeLog flowNodeLog : (Iterable<FlowNodeLog>)(Iterable<?>)(testResult.getFlowNodeList())) {
         if (flag) {
            stringBuilder.append("、");
         }

         stringBuilder.append(flowNodeLog.getNodeName() + "<span style='color:#795548'>(" + valueOrNone(flowNodeLog.getFile()) + ")</span>");
         flag = true;
      }

      stringBuilder.append("</span>");
      stringBuilder.append("<div>");
      stringBuilder.append("<div style=\"color:#545454;margin-top:5px\">运行日志：</div>");
      List logs = testResult.getLogs();
      if (logs != null && logs.size() != 0) {
         stringBuilder.append("<div style=\"font-size:11px;border:dotted 1px #a5a5a5;margin:5px;padding:5px;border-radius:5px\">");
         appendLogHtml(stringBuilder, logs);
         stringBuilder.append("</div>");
      } else {
         stringBuilder.append("<div style='color:#df3600'>当前未开启日志输出功能</div>");
      }

      stringBuilder.append("</div>");
      return stringBuilder.toString();
   }

   private static String valueOrNone(String text) {
      return text == null ? "无" : text;
   }

   private static void appendLogHtml(StringBuilder stringBuilder, List items) {
      for(Log log : (Iterable<Log>)(Iterable<?>)(items)) {
         if (log instanceof UnitLog) {
            stringBuilder.append("<div style=\"margin:8px;border:dashed 1px #cccccc\">");
            UnitLog unitLog = (UnitLog)log;
            List logs = unitLog.getLogs();
            appendLogHtml(stringBuilder, logs);
            stringBuilder.append("</div>");
         } else if (log instanceof DataLog) {
            DataLog dataLog = (DataLog)log;
            String htmlMsg = dataLog.getHtmlMsg();
            stringBuilder.append(htmlMsg);
         }
      }

   }
}
