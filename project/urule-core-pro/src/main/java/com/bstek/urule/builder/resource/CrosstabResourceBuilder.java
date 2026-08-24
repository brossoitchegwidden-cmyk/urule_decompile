package com.bstek.urule.builder.resource;

import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.parse.deserializer.CrosstableDeserializer;
import org.dom4j.Element;

public class CrosstabResourceBuilder implements ResourceBuilder<CrosstabDefinition> {
   private CrosstableDeserializer crosstableDeserializer;

   public CrosstabDefinition build(Element root, String file) {
      return this.crosstableDeserializer.deserialize(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.CrossDecisionTable;
   }

   @Override
   public boolean support(Element root) {
      return this.crosstableDeserializer.support(root);
   }

   public void setCrosstableDeserializer(CrosstableDeserializer crosstableDeserializer) {
      this.crosstableDeserializer = crosstableDeserializer;
   }
}
