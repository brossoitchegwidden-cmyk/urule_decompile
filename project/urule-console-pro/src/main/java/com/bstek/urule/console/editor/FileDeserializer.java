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
   private List a;

   private FileDeserializer() {
      this.a = new ArrayList();
      ApplicationContext var1 = Utils.getApplicationContext();
      ActionLibraryDeserializer var2 = (ActionLibraryDeserializer)var1.getBean("urule.actionLibraryDeserializer");
      VariableLibraryDeserializer var3 = (VariableLibraryDeserializer)var1.getBean("urule.variableLibraryDeserializer");
      ParameterLibraryDeserializer var4 = (ParameterLibraryDeserializer)var1.getBean("urule.parameterLibraryDeserializer");
      ConstantLibraryDeserializer var5 = (ConstantLibraryDeserializer)var1.getBean("urule.constantLibraryDeserializer");
      RuleSetDeserializer var6 = (RuleSetDeserializer)var1.getBean("urule.ruleSetDeserializer");
      DecisionTableDeserializer var7 = (DecisionTableDeserializer)var1.getBean("urule.decisionTableDeserializer");
      DecisionTreeDeserializer var8 = (DecisionTreeDeserializer)var1.getBean("urule.decisionTreeDeserializer");
      ScorecardDeserializer var9 = (ScorecardDeserializer)var1.getBean("urule.scorecardDeserializer");
      ComplexScorecardDeserializer var10 = (ComplexScorecardDeserializer)var1.getBean("urule.complexScorecardDeserializer");
      CrosstableDeserializer var11 = (CrosstableDeserializer)var1.getBean("urule.crosstableDeserializer");
      ConditionTemplateDeserializer var12 = (ConditionTemplateDeserializer)var1.getBean("urule.conditionTemplateDeserializer");
      ActionTemplateDeserializer var13 = (ActionTemplateDeserializer)var1.getBean("urule.actionTemplateDeserializer");
      FlowDeserializer var14 = (FlowDeserializer)var1.getBean("urule.flowDeserializer");
      this.a.add(var2);
      this.a.add(var3);
      this.a.add(var5);
      this.a.add(var6);
      this.a.add(var7);
      this.a.add(var8);
      this.a.add(var4);
      this.a.add(var9);
      this.a.add(var10);
      this.a.add(var11);
      this.a.add(var12);
      this.a.add(var13);
      this.a.add(var14);
   }

   public static final FileDeserializer getInstance() {
      return FileDeserializer.Instance.a;
   }

   public Object deserialize(Element var1) {
      try {
         return this.getDeserializer(var1).deserialize(var1);
      } catch (Exception var3) {
         if (StringUtils.isBlank(var3.getMessage())) {
            throw new DeserializeException(var3.getClass().getName());
         } else {
            throw new DeserializeException(var3.getMessage());
         }
      }
   }

   public Deserializer getDeserializer(Element var1) {
      for(Deserializer var3 : (Iterable<Deserializer>)(Iterable<?>)(this.a)) {
         if (var3.support(var1)) {
            return var3;
         }
      }

      throw new RuleException("Unknow element :" + var1.asXML());
   }

   public Element parseXml(String var1) throws Exception {
      ByteArrayInputStream var2 = new ByteArrayInputStream(var1.getBytes("utf-8"));
      XXESAXReader var3 = new XXESAXReader();

      try {
         Document var4 = ((SAXReader)var3).read(var2);
         Element var5 = var4.getRootElement();
         ((InputStream)var2).close();
         return var5;
      } catch (DocumentException var6) {
         throw new DeserializeException(var6);
      }
   }

   private static class Instance {
      private static FileDeserializer a = new FileDeserializer();
   }
}
