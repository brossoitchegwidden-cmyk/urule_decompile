package com.bstek.urule.console.editor.packet.scenario;

import java.util.List;

public class ScenarioData {
   private String a;
   private String b;
   private List c;
   private List d;

   public String getScenarioId() {
      return this.a;
   }

   public void setScenarioId(String var1) {
      this.a = var1;
   }

   public String getScenarioDesc() {
      return this.b;
   }

   public void setScenarioDesc(String var1) {
      this.b = var1;
   }

   public List getInput() {
      return this.c;
   }

   public void setInput(List var1) {
      this.c = var1;
   }

   public List getOutput() {
      return this.d;
   }

   public void setOutput(List var1) {
      this.d = var1;
   }
}
