package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.parse.crosstab.CrosstabParser;
import org.dom4j.Element;

public class CrosstableDeserializer implements Deserializer<CrosstabDefinition> {
   public static final String BEAN_ID = "urule.crosstableDeserializer";
   private CrosstabParser crosstabParser;

   public CrosstabDefinition deserialize(Element root) {
      return this.crosstabParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return "crosstab".equals(root.getName());
   }

   public void setCrosstabParser(CrosstabParser crosstabParser) {
      this.crosstabParser = crosstabParser;
   }
}
