package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;

public class CriteriaLog extends DataLog {
   public CriteriaLog(Criteria criteria, EvaluateResponse response) {
      String id = criteria.getId();
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append(String.format(this.isEnglishLanguage() ? "^^Condition: %s" : "^^条件：%s", id));
      String text = response.getResult() ? (this.isEnglishLanguage() ? "match" : "满足") : (this.isEnglishLanguage() ? "mismatch" : "不满足");
      stringBuffer.append("➤" + text);
      String text2 = this.isEnglishLanguage() ? "left value： %s" : "左值： %s";
      String text3 = response.getLeftResult() == null ? "null" : response.getLeftResult().toString();
      stringBuffer.append(", " + String.format(text2, text3));
      if (response.getOp() != null) {
         stringBuffer.append("【" + response.getOp().toString() + "】");
      }

      String text4 = this.isEnglishLanguage() ? "right value: %s" : "右值： %s";
      String text5 = response.getRightResult() == null ? "null" : response.getRightResult().toString();
      stringBuffer.append(" " + String.format(text4, text5));
      this.msg = stringBuffer.toString();
   }
}
