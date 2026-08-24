package com.bstek.urule;

import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.exception.RuleAssertException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.WorkingMemory;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.json.JsonMapper.Builder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Utils implements ApplicationContextAware {
   private static boolean debug = true;
   private static boolean spaceToZero;
   private static boolean subPropertyNotExistToNull = true;
   public static final String SystemId = UUID.randomUUID().toString();
   private static ApplicationContext applicationContext;
   private static BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
   public static final String VAR_PREFIX = "${";
   private static Map<String, FunctionDescriptor> functionDescriptorMap = new HashMap<>();
   private static Map<String, FunctionDescriptor> functionDescriptorLabelMap = new HashMap<>();

   public static ApplicationContext getApplicationContext() {
      return applicationContext;
   }

   public static String decodeURL(String str) {
      if (StringUtils.isBlank(str)) {
         return str;
      }

      try {
         str = URLDecoder.decode(str, "utf-8");
         return URLDecoder.decode(str, "utf-8");
      } catch (Exception exception) {
         return str;
      }
   }

   public static String getClassName(Object fact) {
      String className = null;
      if (fact instanceof GeneralEntity) {
         GeneralEntity generalEntity = (GeneralEntity)fact;
         className = generalEntity.getTargetClass();
      } else {
         className = fact.getClass().getName();
      }

      return className;
   }

   public static String decodeContent(String content) {
      if (StringUtils.isBlank(content)) {
         return content;
      }

      try {
         return URLDecoder.decode(content, "utf-8");
      } catch (Exception exception) {
         return content;
      }
   }

   public static Throwable buildCause(Throwable throwable, StringBuilder sb) {
      if (throwable instanceof RuleAssertException) {
         RuleException ruleException = (RuleException)throwable;
         String tipMsg = ruleException.getTipMsg();
         if (tipMsg != null) {
            sb.append(tipMsg);
         }
      }

      return throwable.getCause() != null ? buildCause(throwable.getCause(), sb) : throwable;
   }

   public static String encodeURL(String str) {
      if (StringUtils.isBlank(str)) {
         return str;
      }

      try {
         return URLEncoder.encode(str, "utf-8");
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
         throw new RuleException(unsupportedEncodingException);
      }
   }

   public static String toUTF8(String text) {
      try {
         if (text == null) {
            return null;
         }

         byte[] bytes = text.getBytes("iso8859-1");
         boolean flag = text.equals(new String(bytes, "iso8859-1"));
         if (flag) {
            text = new String(bytes, "utf-8");
         }

         flag = text.equals(new String(text.getBytes("iso8859-1"), "iso8859-1"));
         if (flag) {
            text = new String(bytes, "utf-8");
         }

         return text;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public static Object getObjectProperty(Object object, String property) {
      try {
         return PropertyUtils.getProperty(object, property);
      } catch (NoSuchMethodException noSuchMethodException) {
         if (subPropertyNotExistToNull) {
            return null;
         } else {
            throw new RuleException(noSuchMethodException);
         }
      } catch (NestedNullException nestedNullException) {
         if (subPropertyNotExistToNull) {
            return null;
         } else {
            throw new RuleException(nestedNullException);
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public static void setObjectProperty(Object object, String property, Object value) {
      try {
         Utils.beanUtilsBean.setProperty(object, property, value);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public static void assignVariableObjectDefaultValue(GeneralEntity ge, WorkingMemory workingMemory) {
      String targetClass = ge.getTargetClass();
      KnowledgeSession knowledgeSession = (KnowledgeSession)workingMemory;
      List knowledgePackageList = knowledgeSession.getKnowledgePackageList();
      VariableCategory variableCategory = null;

      for (KnowledgePackage knowledgePackage : (Iterable<KnowledgePackage>)(Iterable<?>)(knowledgePackageList)) {
         variableCategory = knowledgePackage.getVariableCategoryWithDefaultValue(targetClass);
         if (variableCategory != null) {
            break;
         }
      }

      if (variableCategory != null) {
         for (Variable variable : variableCategory.getVariables()) {
            String defaultValue = variable.getDefaultValue();
            if (defaultValue != null) {
               String name = variable.getName();
               Object objectProperty = getObjectProperty(ge, name);
               if (objectProperty == null) {
                  objectProperty = variable.getType().convert(defaultValue);
                  setObjectProperty(ge, name, objectProperty);
               }
            }
         }
      }
   }

   public static Datatype getDatatype(Object obj) {
      Datatype datatype = null;
      if (obj == null) {
         datatype = Datatype.Object;
      } else if (obj instanceof Integer) {
         datatype = Datatype.Integer;
      } else if (obj instanceof Long) {
         datatype = Datatype.Long;
      } else if (obj instanceof Double) {
         datatype = Datatype.Double;
      } else if (obj instanceof Float) {
         datatype = Datatype.Float;
      } else if (obj instanceof BigDecimal) {
         datatype = Datatype.BigDecimal;
      } else if (obj instanceof Boolean) {
         datatype = Datatype.Boolean;
      } else if (obj instanceof Date) {
         datatype = Datatype.Date;
      } else if (obj instanceof List) {
         datatype = Datatype.List;
      } else if (obj instanceof Set) {
         datatype = Datatype.Set;
      } else if (obj instanceof Enum) {
         datatype = Datatype.Enum;
      } else if (obj instanceof Map) {
         datatype = Datatype.Map;
      } else if (obj instanceof String) {
         datatype = Datatype.String;
      } else if (obj instanceof Character) {
         datatype = Datatype.Char;
      } else {
         datatype = Datatype.Object;
      }

      return datatype;
   }

   public static BigDecimal toBigDecimal(Object val) {
      try {
         if (val instanceof BigDecimal) {
            return (BigDecimal)val;
         }

         if (val == null) {
            throw new IllegalArgumentException("Null can not to BigDecimal.");
         }

         if (val instanceof String) {
            String trimmedText = (String)val;
            if (spaceToZero && "".equals(trimmedText.trim())) {
               return BigDecimal.valueOf(0L);
            }

            trimmedText = trimmedText.trim();
            return new BigDecimal(trimmedText);
         }

         if (val instanceof Number) {
            return new BigDecimal(val.toString());
         }

         if (val instanceof Character) {
            char character = (Character)val;
            return new BigDecimal(character);
         }
      } catch (Exception exception) {
         if (val != null && "".equals(val.toString().trim())) {
            throw new NumberFormatException("Can not convert 空格 to number.");
         }

         throw new NumberFormatException("Can not convert " + val + " to number.");
      }

      throw new IllegalArgumentException(val.getClass().getName() + " can not to BigDecimal.");
   }

   public static byte[] compress(String content) {
      if (content == null) {
         return null;
      }

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      GZIPOutputStream gZIPOutputStream = null;

      try {
         gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
         gZIPOutputStream.write(content.getBytes("UTF-8"));
         IOUtils.closeQuietly(gZIPOutputStream);
         return byteArrayOutputStream.toByteArray();
      } catch (IOException iOException) {
         throw new RuleException(iOException);
      }
   }

   public static String uncompress(byte[] content) {
      if (content.length < 1) {
         return null;
      }

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
      GZIPInputStream gZIPInputStream = null;

      try {
         gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
         byte[] bytes = IOUtils.toByteArray(gZIPInputStream);
         return new String(bytes, "UTF-8");
      } catch (IOException iOException) {
         throw new RuleException(iOException);
      } finally {
         IOUtils.closeQuietly(byteArrayOutputStream);
         IOUtils.closeQuietly(byteArrayInputStream);
         IOUtils.closeQuietly(gZIPInputStream);
      }
   }

   public static Rule buildElseRule(Rule rule) {
      if (rule.getElseRule() != null) {
         return rule.getElseRule();
      } else {
         Other other = rule.getOther();
         if (other != null && other.getActions().size() != 0) {
            Rule elseRule = new Rule();
            elseRule.setFile(rule.getFile());
            elseRule.setName(rule.getName() + " - else");
            elseRule.setMutexGroup(rule.getMutexGroup());
            elseRule.setPendedGroup(rule.getPendedGroup());
            elseRule.setAutoFocus(rule.getAutoFocus());
            elseRule.setEffectiveDate(rule.getEffectiveDate());
            elseRule.setExpiresDate(rule.getExpiresDate());
            elseRule.setEnabled(rule.getEnabled());
            elseRule.setDebug(rule.getDebug());
            elseRule.setSalience(rule.getSalience());
            Rhs rhs = new Rhs();
            elseRule.setRhs(rhs);
            rhs.setActions(other.getActions());
            rule.setElseRule(elseRule);
            return elseRule;
         } else {
            return null;
         }
      }
   }

   public static String knowledgePackageToString(KnowledgePackage knowledgePackage) {
      Builder builder = JsonMapper.builder();
      builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      ObjectMapper objectMapper = builder.build();
      objectMapper.setSerializationInclusion(Include.NON_NULL);
      objectMapper.setDateFormat(new SimpleDateFormat(Configure.getDateFormat()));
      KnowledgePackageWrapper knowledgePackageWrapper = new KnowledgePackageWrapper(knowledgePackage);

      try {
         return objectMapper.writeValueAsString(knowledgePackageWrapper);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public static String knowledgePackageLibToString(KnowledgePackage knowledgePackage) {
      KnowledgePackageImpl knowledgePackageImpl = (KnowledgePackageImpl)knowledgePackage;
      List variableCategories = knowledgePackageImpl.getVariableCategories();
      Builder builder = JsonMapper.builder();
      builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      ObjectMapper objectMapper = builder.build();
      objectMapper.setSerializationInclusion(Include.NON_NULL);
      objectMapper.setDateFormat(new SimpleDateFormat(Configure.getDateFormat()));

      try {
         return objectMapper.writeValueAsString(variableCategories);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public static List<VariableCategory> stringToKnowledgePackageLib(String content) {
      try {
         ObjectMapper objectMapper = JsonMapper.builder().build();
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
         objectMapper.getDeserializationConfig().with(simpleDateFormat);
         objectMapper.setDateFormat(simpleDateFormat);
         return (List<VariableCategory>)objectMapper.readValue(content, new VariableCategoryListTypeReference());
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public static KnowledgePackage stringToKnowledgePackage(String content) {
      return stringToKnowledgePackageWrapper(content).getKnowledgePackage();
   }

   public static KnowledgePackageWrapper stringToKnowledgePackageWrapper(String content) {
      try {
         ObjectMapper objectMapper = JsonMapper.builder().build();
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
         objectMapper.getDeserializationConfig().with(simpleDateFormat);
         objectMapper.setDateFormat(simpleDateFormat);
         KnowledgePackageWrapper knowledgePackageWrapper = (KnowledgePackageWrapper)objectMapper.readValue(content, KnowledgePackageWrapper.class);
         knowledgePackageWrapper.buildDeserialize();
         KnowledgePackage knowledgePackage = knowledgePackageWrapper.getKnowledgePackage();
         Map flowMap = knowledgePackage.getFlowMap();
         if (flowMap != null && flowMap.size() > 0) {
            for (FlowDefinition flowDefinition : (Iterable<FlowDefinition>)(Iterable<?>)(flowMap.values())) {
               flowDefinition.buildConnectionToNode();
            }
         }

         return knowledgePackageWrapper;
      } catch (Exception exception) {
         throw new DeserializeException(exception);
      }
   }

   public static FunctionDescriptor findFunctionDescriptor(String functionName) {
      if (!functionDescriptorMap.containsKey(functionName)) {
         throw new RuleException("Function[" + functionName + "] not exist.");
      } else {
         return functionDescriptorMap.get(functionName);
      }
   }

   public static Map<String, FunctionDescriptor> getFunctionDescriptorLabelMap() {
      return functionDescriptorLabelMap;
   }

   public static Map<String, FunctionDescriptor> getFunctionDescriptorMap() {
      return functionDescriptorMap;
   }

   public void setDebug(boolean debug) {
      Utils.debug = debug;
   }

   public static void setSpaceToZero(boolean spaceToZero) {
      Utils.spaceToZero = spaceToZero;
   }

   public static void setSubPropertyNotExistToNull(boolean subPropertyNotExistToNull) {
      Utils.subPropertyNotExistToNull = subPropertyNotExistToNull;
   }

   public static boolean isDebug() {
      return debug;
   }

   public static boolean isSpaceToZero() {
      return spaceToZero;
   }

   public static void resetApplicationContext(ApplicationContext applicationContext) {
      Utils.applicationContext = applicationContext;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      functionDescriptorMap.clear();
      functionDescriptorLabelMap.clear();

      for (FunctionDescriptor functionDescriptor : applicationContext.getBeansOfType(FunctionDescriptor.class).values()) {
         if (!functionDescriptor.isDisabled()) {
            if (functionDescriptorMap.containsKey(functionDescriptor.getName())) {
               throw new RuntimeException("Duplicate function [" + functionDescriptor.getName() + "]");
            }

            functionDescriptorMap.put(functionDescriptor.getName(), functionDescriptor);
            functionDescriptorLabelMap.put(functionDescriptor.getLabel(), functionDescriptor);
         }
      }

      Utils.applicationContext = applicationContext;
   }

   static {
      ConvertUtilsBean convertUtils = Utils.beanUtilsBean.getConvertUtils();
      convertUtils.register(new DateConverter(null), Date.class);
      convertUtils.register(new LongConverter(null), Long.class);
      convertUtils.register(new ShortConverter(null), Short.class);
      convertUtils.register(new IntegerConverter(null), Integer.class);
      convertUtils.register(new DoubleConverter(null), Double.class);
      convertUtils.register(new FloatConverter(null), Float.class);
      convertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
      convertUtils.register(new BigIntegerConverter(null), BigInteger.class);
      convertUtils.register(new ByteConverter(null), Byte.class);
   }
}
