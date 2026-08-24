package com.bstek.urule.console.editor.packet.scenario;

import java.util.List;

public class ScenarioData {
   private String scenarioId;
   private String scenarioDesc;
   private List input;
   private List output;

   public String getScenarioId() {
      return this.scenarioId;
   }

   public void setScenarioId(String scenarioId) {
      this.scenarioId = scenarioId;
   }

   public String getScenarioDesc() {
      return this.scenarioDesc;
   }

   public void setScenarioDesc(String scenarioDesc) {
      this.scenarioDesc = scenarioDesc;
   }

   public List getInput() {
      return this.input;
   }

   public void setInput(List input) {
      this.input = input;
   }

   public List getOutput() {
      return this.output;
   }

   public void setOutput(List output) {
      this.output = output;
   }
}
