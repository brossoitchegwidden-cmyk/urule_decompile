package com.bstek.urule.runtime.log;

public class ScoreCardLog extends DataLog {
   private String name;
   private String path;

   public ScoreCardLog(String name, String path) {
      this.name = name;
      this.path = path;
      if (path.endsWith(".scc")) {
         this.name = this.isEnglishLanguage() ? "ComplexScorecard" : "复杂评分卡";
         String text = this.isEnglishLanguage() ? "execute [%s]:%s" : "执行[%s]:%s";
         this.msg = String.format(text, this.name, path);
      } else {
         String text2 = this.isEnglishLanguage() ? "execute scorecard [%s]:%s" : "执行评分卡[%s]:%s";
         this.msg = String.format(text2, this.name, path);
      }
   }

   public String getName() {
      return this.name;
   }

   public String getPath() {
      return this.path;
   }
}
