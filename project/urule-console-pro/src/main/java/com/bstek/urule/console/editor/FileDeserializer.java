package com.bstek.urule.console.editor;

import com.bstek.urule.Utils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.XXESAXReader;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.parse.deserializer.ActionLibraryDeserializer;
import com.bstek.urule.parse.deserializer.ActionTemplateDeserializer;
import com.bstek.urule.parse.deserializer.ComplexScorecardDeserializer;
import com.bstek.urule.parse.deserializer.ConditionTemplateDeserializer;
import com.bstek.urule.parse.deserializer.ConstantLibraryDeserializer;
import com.bstek.urule.parse.deserializer.CrosstableDeserializer;
import com.bstek.urule.parse.deserializer.DecisionTableDeserializer;
import com.bstek.urule.parse.deserializer.DecisionTreeDeserializer;
import com.bstek.urule.parse.deserializer.Deserializer;
import com.bstek.urule.parse.deserializer.FlowDeserializer;
import com.bstek.urule.parse.deserializer.ParameterLibraryDeserializer;
import com.bstek.urule.parse.deserializer.RuleSetDeserializer;
import com.bstek.urule.parse.deserializer.ScorecardDeserializer;
import com.bstek.urule.parse.deserializer.VariableLibraryDeserializer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;

public class FileDeserializer {
   private List deserializers;

   private FileDeserializer() {
      this.deserializers = new ArrayList();
      ApplicationContext applicationContext = Utils.getApplicationContext();
      ActionLibraryDeserializer actionLibraryDeserializer = (ActionLibraryDeserializer)applicationContext.getBean("urule.actionLibraryDeserializer");
      VariableLibraryDeserializer variableLibraryDeserializer = (VariableLibraryDeserializer)applicationContext.getBean("urule.variableLibraryDeserializer");
      ParameterLibraryDeserializer parameterLibraryDeserializer = (ParameterLibraryDeserializer)applicationContext.getBean("urule.parameterLibraryDeserializer");
      ConstantLibraryDeserializer constantLibraryDeserializer = (ConstantLibraryDeserializer)applicationContext.getBean("urule.constantLibraryDeserializer");
      RuleSetDeserializer ruleSetDeserializer = (RuleSetDeserializer)applicationContext.getBean("urule.ruleSetDeserializer");
      DecisionTableDeserializer decisionTableDeserializer = (DecisionTableDeserializer)applicationContext.getBean("urule.decisionTableDeserializer");
      DecisionTreeDeserializer decisionTreeDeserializer = (DecisionTreeDeserializer)applicationContext.getBean("urule.decisionTreeDeserializer");
      ScorecardDeserializer scorecardDeserializer = (ScorecardDeserializer)applicationContext.getBean("urule.scorecardDeserializer");
      ComplexScorecardDeserializer complexScorecardDeserializer = (ComplexScorecardDeserializer)applicationContext.getBean("urule.complexScorecardDeserializer");
      CrosstableDeserializer crosstableDeserializer = (CrosstableDeserializer)applicationContext.getBean("urule.crosstableDeserializer");
      ConditionTemplateDeserializer conditionTemplateDeserializer = (ConditionTemplateDeserializer)applicationContext.getBean("urule.conditionTemplateDeserializer");
      ActionTemplateDeserializer actionTemplateDeserializer = (ActionTemplateDeserializer)applicationContext.getBean("urule.actionTemplateDeserializer");
      FlowDeserializer flowDeserializer = (FlowDeserializer)applicationContext.getBean("urule.flowDeserializer");
      this.deserializers.add(actionLibraryDeserializer);
      this.deserializers.add(variableLibraryDeserializer);
      this.deserializers.add(constantLibraryDeserializer);
      this.deserializers.add(ruleSetDeserializer);
      this.deserializers.add(decisionTableDeserializer);
      this.deserializers.add(decisionTreeDeserializer);
      this.deserializers.add(parameterLibraryDeserializer);
      this.deserializers.add(scorecardDeserializer);
      this.deserializers.add(complexScorecardDeserializer);
      this.deserializers.add(crosstableDeserializer);
      this.deserializers.add(conditionTemplateDeserializer);
      this.deserializers.add(actionTemplateDeserializer);
      this.deserializers.add(flowDeserializer);
   }

   public static final FileDeserializer getInstance() {
      return FileDeserializer.Instance.INSTANCE;
   }

   public Object deserialize(Element element) {
      try {
         return this.getDeserializer(element).deserialize(element);
      } catch (Exception exception) {
         if (StringUtils.isBlank(exception.getMessage())) {
            throw new DeserializeException(exception.getClass().getName());
         } else {
            throw new DeserializeException(exception.getMessage());
         }
      }
   }

   public Deserializer getDeserializer(Element element) {
      for(Deserializer deserializer : (Iterable<Deserializer>)(Iterable<?>)(this.deserializers)) {
         if (deserializer.support(element)) {
            return deserializer;
         }
      }

      throw new RuleException("Unknow element :" + element.asXML());
   }

   public Element parseXml(String content) throws Exception {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes("utf-8"));
      XXESAXReader xXESAXReader = new XXESAXReader();

      try {
         Document document = ((SAXReader)xXESAXReader).read(byteArrayInputStream);
         Element rootElement = document.getRootElement();
         ((InputStream)byteArrayInputStream).close();
         return rootElement;
      } catch (DocumentException documentException) {
         throw new DeserializeException(documentException);
      }
   }

   private static class Instance {
      private static final FileDeserializer INSTANCE = new FileDeserializer();
   }
}
