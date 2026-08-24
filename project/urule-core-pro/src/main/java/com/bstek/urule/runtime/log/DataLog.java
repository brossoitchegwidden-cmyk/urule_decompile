package com.bstek.urule.runtime.log;

import com.bstek.urule.LocaleHolder;

public abstract class DataLog implements Log {
   protected String msg;

   public String getMsg() {
      return this.msg;
   }

   public String getHtmlMsg() {
      String text = "#000";
      if (this instanceof CriteriaLog) {
         text = "#6495ED";
      } else if (this instanceof MetLog) {
         text = "#009688";
      } else if (this instanceof ConsoleOutputLog) {
         text = "#000";
      } else if (this instanceof ExecuteBeanMethodLog) {
         text = "#8A2BE2";
      } else if (this instanceof ExecuteFunctionLog) {
         text = "#008B8B";
      } else if (this instanceof FlowNodeLog) {
         text = "#9932CC";
      } else if (this instanceof ValueAssignLog) {
         text = "#FF7F50";
      } else if (this instanceof ScoreCardBean) {
         text = "#40E0D0";
      } else if (this instanceof ExcecuteScoreCardLog) {
         text = "#40E0D0";
      } else if (this instanceof ScoreCardSumLog) {
         text = "#40E0D0";
      } else if (this instanceof MatchedRuleLog) {
         text = "#d48746";
      } else if (this instanceof IFErrorLog) {
         text = "#FF0606";
      } else if (this instanceof AddRuleToExecuteQueueLog) {
         AddRuleToExecuteQueueLog addRuleToExecuteQueueLog = (AddRuleToExecuteQueueLog)this;
         if (addRuleToExecuteQueueLog.isAdd()) {
            text = "#13d8c6";
         } else {
            text = "#FF5722";
         }
      }

      return "<div style=\"color:" + text + ";margin-top:2px\">" + this.msg + "</div>";
   }

   protected boolean isEnglishLanguage() {
      return LocaleHolder.isEnglish();
   }
}
