package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.parse.ActionLibraryParser;
import org.dom4j.Element;

public class ActionLibraryDeserializer implements Deserializer<ActionLibrary> {
   public static final String BEAN_ID = "urule.actionLibraryDeserializer";
   private ActionLibraryParser actionLibraryParser;

   public ActionLibrary deserialize(Element root) {
      return this.actionLibraryParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.actionLibraryParser.support(root.getName());
   }

   public void setActionLibraryParser(ActionLibraryParser actionLibraryParser) {
      this.actionLibraryParser = actionLibraryParser;
   }
}
