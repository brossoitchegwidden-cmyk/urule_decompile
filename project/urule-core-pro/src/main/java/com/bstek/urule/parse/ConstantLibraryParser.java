package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.constant.Constant;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.constant.ConstantLibrary;
import java.util.ArrayList;
import org.dom4j.Element;

public class ConstantLibraryParser implements Parser<ConstantLibrary> {
   public static final String BEAN_ID = "urule.constantLibraryParser";

   public ConstantLibrary parse(Element element) {
      ArrayList items = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("category")) {
               items.add(this.resolveConstantCategory(element2));
            }
         }
      }

      ConstantLibrary constantLibrary = new ConstantLibrary();
      constantLibrary.setCategories(items);
      return constantLibrary;
   }

   private ConstantCategory resolveConstantCategory(Element element) {
      ConstantCategory constantCategory = new ConstantCategory();
      constantCategory.setName(element.attributeValue("name"));
      constantCategory.setUuid(element.attributeValue("uuid"));
      constantCategory.setLabel(element.attributeValue("label"));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("constant")) {
               constantCategory.addConstant(this.resolveConstant(element2));
            }
         }
      }

      return constantCategory;
   }

   private Constant resolveConstant(Element element) {
      Constant constant = new Constant();
      constant.setUuid(element.attributeValue("uuid"));
      constant.setName(element.attributeValue("name"));
      constant.setLabel(element.attributeValue("label"));
      constant.setType(Datatype.valueOf(element.attributeValue("type")));
      return constant;
   }

   @Override
   public boolean support(String name) {
      return name.equals("constant-library");
   }
}
