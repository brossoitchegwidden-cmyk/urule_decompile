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

   public void setName(String var1) {
      this.name = var1;
   }

   public List<Parameter> getParameters() {
      return this.parameters;
   }

   public void setParameters(List<Parameter> var1) {
      this.parameters = var1;
   }

   @Override
   public String getId() {
      if (this.id == null) {
         String var1 = LocaleHolder.isEnglish() ? "Function" : "函数";
         if (this.parameters != null) {
            String var2 = "";
            int var3 = 0;

            for (Parameter var5 : this.parameters) {
               if (var3 > 0) {
                  var2 = var2 + ",";
               }

               var2 = var2 + var5.getId();
               var3++;
            }

            this.id = "[" + var1 + "]." + this.name + "(" + var2 + ")";
         } else {
            this.id = "[" + var1 + "]." + this.name;
         }
      }

      return this.id;
   }
}
