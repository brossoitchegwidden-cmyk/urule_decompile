package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.rule.Parameter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class FunctionLeftPart implements LeftPart {
   @JsonIgnore
   private String id;
   private String name;
   private List<Parameter> parameters;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<Parameter> getParameters() {
      return this.parameters;
   }

   public void setParameters(List<Parameter> parameters) {
      this.parameters = parameters;
   }

   @Override
   public String getId() {
      if (this.id == null) {
         String text = LocaleHolder.isEnglish() ? "Function" : "函数";
         if (this.parameters != null) {
            String text2 = "";
            int number = 0;

            for (Parameter parameter : this.parameters) {
               if (number > 0) {
                  text2 = text2 + ",";
               }

               text2 = text2 + parameter.getId();
               number++;
            }

            this.id = "[" + text + "]." + this.name + "(" + text2 + ")";
         } else {
            this.id = "[" + text + "]." + this.name;
         }
      }

      return this.id;
   }
}
