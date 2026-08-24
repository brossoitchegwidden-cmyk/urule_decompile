package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;
import java.util.UUID;
import org.dom4j.Element;

public class ActionLibraryParser implements Parser<ActionLibrary> {
   public ActionLibrary parse(Element element) {
      ActionLibrary actionLibrary = new ActionLibrary();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("spring-bean")) {
               actionLibrary.addSpringBean(this.resolveSpringBean(element2));
            }
         }
      }

      return actionLibrary;
   }

   private SpringBean resolveSpringBean(Element element) {
      SpringBean springBean = new SpringBean();
      springBean.setId(element.attributeValue("id"));
      springBean.setUuid(element.attributeValue("uuid"));
      springBean.setName(element.attributeValue("name"));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            Method method = new Method();
            method.setMethodName(element2.attributeValue("method-name"));
            method.setUuid(element2.attributeValue("uuid"));
            method.setName(element2.attributeValue("name"));

            for (Object objectValue2 : element2.elements()) {
               if (objectValue2 != null && objectValue2 instanceof Element) {
                  Element element3 = (Element)objectValue2;
                  if (element3.getName().equals("parameter")) {
                     Parameter parameter = new Parameter();
                     parameter.setUuid(UUID.randomUUID().toString());
                     parameter.setName(element3.attributeValue("name"));
                     parameter.setType(Datatype.valueOf(element3.attributeValue("type")));
                     method.addParameter(parameter);
                  }
               }
            }

            springBean.addMethod(method);
         }
      }

      return springBean;
   }

   @Override
   public boolean support(String name) {
      return name.equals("action-library");
   }
}
