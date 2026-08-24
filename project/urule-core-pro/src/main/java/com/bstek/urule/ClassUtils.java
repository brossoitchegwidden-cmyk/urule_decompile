package com.bstek.urule;

import com.bstek.urule.model.Label;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/** Utilities for exposing JavaBean properties as URule variables. */
public final class ClassUtils {
   private static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();
   private static final Set<String> MISSING_CLASS_CACHE = ConcurrentHashMap.newKeySet();

   private ClassUtils() {
   }

   /** Writes the variable model for {@code type} as UTF-8 XML. */
   public static void classToXml(Class<?> type, File file) {
      Document document = DocumentHelper.createDocument();
      Element root = document.addElement("variables");
      root.addAttribute("clazz", type.getName());

      for (Variable variable : classToVariables(type)) {
         Element element = root.addElement("variable");
         addAttributeIfPresent(element, "name", variable.getName());
         addAttributeIfPresent(element, "label", variable.getLabel());
         addAttributeIfPresent(element, "defaultValue", variable.getDefaultValue());
         addAttributeIfPresent(element, "type", variable.getType());
         addAttributeIfPresent(element, "act", variable.getAct());
      }

      OutputFormat format = OutputFormat.createPrettyPrint();
      format.setEncoding("utf-8");
      try (FileOutputStream output = new FileOutputStream(file)) {
         XMLWriter writer = new XMLWriter(output, format);
         writer.write(document);
         writer.close();
      } catch (Exception exception) {
         throw new RuntimeException("Could not write variables for " + type.getName() + " to " + file, exception);
      }
   }

   private static void addAttributeIfPresent(Element element, String name, Object value) {
      if (value != null) {
         element.addAttribute(name, String.valueOf(value));
      }
   }

   public static List<Variable> classToVariables(Class<?> type) {
      try {
         return parseClass("", "", type, new ArrayList<>());
      } catch (Exception exception) {
         throw new RuntimeException("Could not inspect JavaBean " + type.getName(), exception);
      }
   }

   public static Class<?> doGetTargetClass(String className, boolean failIfMissing) throws ClassNotFoundException {
      Class<?> cachedClass = CLASS_CACHE.get(className);
      if (cachedClass != null) {
         return cachedClass;
      }
      if (MISSING_CLASS_CACHE.contains(className)) {
         if (failIfMissing) {
            throw new ClassNotFoundException(className);
         }
         return null;
      }

      try {
         Class<?> targetClass = Class.forName(className);
         CLASS_CACHE.put(className, targetClass);
         return targetClass;
      } catch (ClassNotFoundException defaultLoaderException) {
         Class<?> targetClass = loadFromApplicationContext(className, defaultLoaderException, failIfMissing);
         if (targetClass != null) {
            CLASS_CACHE.put(className, targetClass);
         }
         return targetClass;
      }
   }

   private static Class<?> loadFromApplicationContext(
         String className, ClassNotFoundException defaultLoaderException, boolean failIfMissing)
         throws ClassNotFoundException {
      try {
         if (Utils.getApplicationContext() == null) {
            throw defaultLoaderException;
         }
         DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)
               Utils.getApplicationContext().getAutowireCapableBeanFactory();
         return beanFactory.getBeanClassLoader().loadClass(className);
      } catch (ClassNotFoundException applicationLoaderException) {
         MISSING_CLASS_CACHE.add(className);
         if (failIfMissing) {
            throw applicationLoaderException;
         }
         return null;
      }
   }

   public static Class<?> getTargetClass(String className) throws ClassNotFoundException {
      return doGetTargetClass(className, true);
   }

   public static Class<?> getTargetClassDefaultNull(String className) throws ClassNotFoundException {
      return doGetTargetClass(className, false);
   }

   private static List<Variable> parseClass(
         String propertyPath, String labelPath, Class<?> type, Collection<Class<?>> ancestors) throws Exception {
      List<Variable> variables = new ArrayList<>();
      BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
      PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
      if (properties == null || ancestors.contains(type)) {
         return variables;
      }

      for (PropertyDescriptor property : properties) {
         Class<?> propertyType = property.getPropertyType();
         if (propertyType == null) {
            continue;
         }

         String propertyName = property.getName();
         String variableName = propertyPath + propertyName;
         String declaredLabel = getPropertyAnnotationLabel(type, propertyName);
         String localLabel = declaredLabel == null ? variableName : declaredLabel;
         String variableLabel = StringUtils.isBlank(labelPath) ? localLabel : labelPath + localLabel;

         Variable variable = new Variable();
         variable.setName(variableName);
         variable.setUuid(UUID.randomUUID().toString());
         variable.setLabel(variableLabel);
         variable.setType(getDatatype(propertyType));
         variable.setAct(Act.InOut);

         if (!Datatype.Object.equals(variable.getType()) || Object.class.equals(propertyType)) {
            variables.add(variable);
         } else if (!ancestors.contains(propertyType)) {
            ancestors.add(type);
            variables.add(variable);
            variables.addAll(parseClass(
                  variableName + ".", variableLabel + ".", propertyType, ancestors));
            ancestors.remove(type);
         }
      }
      return variables;
   }

   /**
    * A valid JavaBean property does not have to be backed by a field. In that
    * case there simply is no field-level {@link Label} annotation.
    */
   private static String getPropertyAnnotationLabel(Class<?> type, String fieldName) {
      Class<?> currentType = type;
      while (currentType != null && currentType != Object.class) {
         try {
            Field field = currentType.getDeclaredField(fieldName);
            Label label = field.getAnnotation(Label.class);
            return label == null ? null : label.value();
         } catch (NoSuchFieldException exception) {
            currentType = currentType.getSuperclass();
         }
      }
      return null;
   }

   private static Datatype getDatatype(Class<?> type) {
      if (String.class.isAssignableFrom(type)) {
         return Datatype.String;
      } else if (Boolean.class.isAssignableFrom(type) || boolean.class.equals(type)) {
         return Datatype.Boolean;
      } else if (Integer.class.isAssignableFrom(type) || int.class.equals(type)) {
         return Datatype.Integer;
      } else if (Float.class.isAssignableFrom(type) || float.class.equals(type)) {
         return Datatype.Float;
      } else if (Long.class.isAssignableFrom(type) || long.class.equals(type)) {
         return Datatype.Long;
      } else if (BigDecimal.class.isAssignableFrom(type)) {
         return Datatype.BigDecimal;
      } else if (Double.class.isAssignableFrom(type) || double.class.equals(type)) {
         return Datatype.Double;
      } else if (Date.class.isAssignableFrom(type)) {
         return Datatype.Date;
      } else if (List.class.isAssignableFrom(type)) {
         return Datatype.List;
      } else if (Map.class.isAssignableFrom(type)) {
         return Datatype.Map;
      } else if (Set.class.isAssignableFrom(type)) {
         return Datatype.Set;
      } else if (Enum.class.isAssignableFrom(type)) {
         return Datatype.Enum;
      } else if (Character.class.isAssignableFrom(type) || char.class.equals(type)) {
         return Datatype.Char;
      }
      return Datatype.Object;
   }

   public static void cleanClassesCache() {
      CLASS_CACHE.clear();
      MISSING_CLASS_CACHE.clear();
   }
}
