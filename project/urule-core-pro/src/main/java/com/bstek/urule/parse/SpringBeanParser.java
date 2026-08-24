package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;
import org.dom4j.Element;

public class SpringBeanParser implements Parser<SpringBean> {
   public SpringBean parse(Element element) {
      SpringBean springBean = new SpringBean();
      springBean.setId(element.attributeValue("id"));
      springBean.setUuid(element.attributeValue("uuid"));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("method")) {
               Method method = this.resolveMethod(element2);
               springBean.addMethod(method);
            }
         }
      }

      return springBean;
   }

   private Method resolveMethod(Element element) {
      Method method = new Method();
      method.setName(element.attributeValue("name"));
      method.setUuid(element.attributeValue("uuid"));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("parameter")) {
               Parameter parameter = new Parameter();
               parameter.setType(Datatype.valueOf(element2.attributeValue("type")));
               method.addParameter(parameter);
            }
         }
      }

      return method;
   }

   @Override
   public boolean support(String name) {
      return name.equals("spring-bean");
   }
}
