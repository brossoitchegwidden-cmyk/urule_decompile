package com.bstek.urule.console.editor.execute;

import com.bstek.urule.console.config.Configure;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class JsonBuilder {
   private Logger logger = Logger.getGlobal();
   private static JsonBuilder instance = new JsonBuilder();

   public static JsonBuilder getInstance() {
      return JsonBuilder.instance;
   }

   public Object buildComplexObject(Object value, Map variableCategoriesMap) throws Exception {
      if (value instanceof String) {
         String complexObject = value.toString().trim();
         ObjectMapper objectMapper = JsonMapper.builder().build();
         if (complexObject.startsWith("{") && complexObject.endsWith("}")) {
            Map valuesByKey = (Map)objectMapper.readValue(complexObject, HashMap.class);
            Object objectValue = valuesByKey.get("data");
            Object name = valuesByKey.get("name");
            Object fields = valuesByKey.get("fields");
            if (objectValue != null && objectValue instanceof List) {
               List objectValue2 = (List)objectValue;
               return this.buildMultiData(objectValue2, variableCategoriesMap);
            } else {
               return name != null && fields != null ? this.buildTypedObject(variableCategoriesMap, valuesByKey) : valuesByKey;
            }
         } else if (complexObject.startsWith("[") && complexObject.endsWith("]")) {
            ArrayList complexObject2 = new ArrayList();

            for(Object objectValue3 : (List)objectMapper.readValue(complexObject, ArrayList.class)) {
               if (objectValue3 instanceof Map) {
                  Map objectValue32 = (Map)objectValue3;
                  Object name2 = objectValue32.get("name");
                  Object fields2 = objectValue32.get("fields");
                  if (name2 != null && fields2 != null) {
                     complexObject2.add(this.buildTypedObject(variableCategoriesMap, objectValue32));
                  } else {
                     complexObject2.add(objectValue3);
                  }
               } else {
                  complexObject2.add(objectValue3);
               }
            }

            return complexObject2;
         } else if (!this.isBase64(complexObject)) {
            return complexObject;
         } else {
            try {
               Base64 base64 = new Base64();
               ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
               ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(base64.decode(complexObject.getBytes("utf-8")));
               GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
               byte[] bytes = new byte[1024];
               int number = -1;

               while((number = gZIPInputStream.read(bytes)) != -1) {
                  byteArrayOutputStream.write(bytes, 0, number);
               }

               byteArrayInputStream.close();
               gZIPInputStream.close();
               String text = byteArrayOutputStream.toString();
               return this.buildComplexObject(text, variableCategoriesMap);
            } catch (Exception exception) {
               this.logger.warning(exception.getMessage());
               this.logger.warning("Fail to decode data :" + complexObject + "");
               return complexObject;
            }
         }
      } else if (value instanceof List) {
         ArrayList complexObject3 = new ArrayList();

         for(Object objectValue4 : (List)value) {
            if (objectValue4 instanceof Map) {
               Map objectValue42 = (Map)objectValue4;
               complexObject3.add(this.buildTypedObject(variableCategoriesMap, objectValue42));
            } else {
               complexObject3.add(objectValue4);
            }
         }

         return complexObject3;
      } else if (value instanceof Map) {
         Map valuesByKey2 = (Map)value;
         Object name3 = valuesByKey2.get("name");
         Object fields3 = valuesByKey2.get("fields");
         return name3 != null && fields3 != null ? this.buildTypedObject(variableCategoriesMap, valuesByKey2) : valuesByKey2;
      } else {
         return value;
      }
   }

   private MultiData buildMultiData(List items, Map valuesByKey) throws Exception {
      ArrayList items2 = new ArrayList();

      for(Object objectValue : items) {
         if (objectValue instanceof List) {
            ArrayList items3 = new ArrayList();
            items2.add(items3);

            for(Object objectValue2 : (List)objectValue) {
               if (!(objectValue2 instanceof Map)) {
                  this.logger.warning("[" + objectValue2 + "] is not a map data!");
               } else {
                  Map objectValue22 = (Map)objectValue2;
                  Object name = objectValue22.get("name");
                  Object fields = objectValue22.get("fields");
                  if (name != null && fields != null) {
                     items3.add(this.buildTypedObject(valuesByKey, objectValue22));
                  } else {
                     items3.add(objectValue22);
                  }
               }
            }
         } else {
            Map objectValue3 = (Map)objectValue;
            Object name2 = objectValue3.get("name");
            Object fields2 = objectValue3.get("fields");
            if (name2 != null && fields2 != null) {
               items2.add(this.buildTypedObject(valuesByKey, objectValue3));
            } else {
               items2.add(objectValue3);
            }
         }
      }

      return new MultiData(items2);
   }

   private boolean isBase64(String text) {
      String text2 = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
      return text.matches(text2);
   }

   private Map buildTypedObject(Map valuesByKey, Map valuesByKey2) throws Exception {
      String name2 = (String)valuesByKey2.get("name");
      Object fields = valuesByKey2.get("fields");
      if (StringUtils.isBlank(name2)) {
         if (fields == null) {
            return valuesByKey2;
         } else if (!(fields instanceof List) && !(fields instanceof Map)) {
            return valuesByKey2;
         } else {
            throw new RuleException("复杂对象值【" + valuesByKey2 + "】需要一个名为\"name\"的属性值来标明当前对象类型");
         }
      } else if (fields != null && (!(fields instanceof List) || !(fields instanceof Map))) {
         VariableCategory variableCategory = (VariableCategory)valuesByKey.get(name2);
         String restParameterName = Configure.getConfigure().getRestParameterName();
         if (restParameterName.equals(name2) && variableCategory == null) {
            variableCategory = (VariableCategory)valuesByKey.get("参数");
         }

         if (variableCategory == null) {
            throw new VariableCategoryNotFoundException("变量对象【" + name2 + "】未定义!");
         } else {
            Object generalEntity = null;
            if (name2.equals(restParameterName)) {
               generalEntity = new HashMap();
            } else {
               generalEntity = new GeneralEntity(variableCategory.getClazz());
            }

            if (!(fields instanceof List)) {
               if (!(fields instanceof Map)) {
                  throw new RuleException("复杂对象值【" + valuesByKey2 + "】的\"fields\"的属性值必须是一个对象类型或集合类型.");
               }

               Map fields2 = (Map)fields;
               Iterator iterator = fields2.keySet().iterator();

               label118:
               while(true) {
                  Object objectValue;
                  Variable variable;
                  while(true) {
                     if (!iterator.hasNext()) {
                        break label118;
                     }

                     String text = (String)iterator.next();
                     objectValue = fields2.get(text);
                     if (objectValue != null) {
                        Object objectValue2 = null;

                        try {
                           variable = this.findVariable(variableCategory, text);
                           break;
                        } catch (Exception exception) {
                        }
                     }
                  }

                  Datatype type = variable.getType();
                  SubObject subObject = this.findSubObject(variable.getName(), (Map)generalEntity);
                  Map map = subObject.getMap();
                  if (!type.equals(Datatype.Object) && !type.equals(Datatype.List)) {
                     map.put(subObject.getName(), type.convert(objectValue));
                  } else {
                     map.put(subObject.getName(), this.buildComplexObject(objectValue, valuesByKey));
                  }
               }
            } else {
               for(Map valuesByKey3 : (Iterable<Map>)(Iterable<?>)((List)fields)) {
                  String name3 = (String)valuesByKey3.get("name");
                  if (StringUtils.isBlank(name3)) {
                     throw new RuleException("子对象需要有一个名为name的属性来标明对象的具体属性名");
                  }

                  Variable variable2 = null;

                  try {
                     variable2 = this.findVariable(variableCategory, name3);
                  } catch (Exception exception2) {
                     continue;
                  }

                  Object defaultValue2 = valuesByKey3.get("value");
                  if (defaultValue2 == null) {
                     defaultValue2 = variable2.getDefaultValue();
                     if (defaultValue2 == null) {
                        continue;
                     }
                  }

                  Datatype type2 = variable2.getType();
                  if (!type2.equals(Datatype.Object) && !type2.equals(Datatype.List)) {
                     ((Map)generalEntity).put(variable2.getName(), type2.convert(defaultValue2));
                  } else {
                     ((Map)generalEntity).put(variable2.getName(), this.buildComplexObject(defaultValue2, valuesByKey));
                  }
               }
            }

            for(Variable variable3 : variableCategory.getVariables()) {
               String name = variable3.getName();
               if (!((Map)generalEntity).containsKey(name)) {
                  String defaultValue = variable3.getDefaultValue();
                  if (defaultValue != null) {
                     Datatype type3 = variable3.getType();
                     Object complexObject;
                     if (type3.equals(Datatype.String)) {
                        complexObject = type3.convert(defaultValue);
                     } else if (!type3.equals(Datatype.List) && !type3.equals(Datatype.Object) && !type3.equals(Datatype.Set) && !type3.equals(Datatype.Map)) {
                        complexObject = type3.convert(defaultValue);
                     } else {
                        complexObject = getInstance().buildComplexObject(defaultValue, valuesByKey);
                     }

                     ((Map)generalEntity).put(name, complexObject);
                  }
               }
            }

            return (Map)generalEntity;
         }
      } else {
         return valuesByKey2;
      }
   }

   public SubObject findSubObject(String name, Map map) {
      Map map2 = map;
      String[] parts = name.split("\\.");

      for(int index = 0; index < parts.length - 1; ++index) {
         String text = parts[index];
         if (map.containsKey(text)) {
            map2 = (Map)map.get(text);
         } else {
            map2.put(text, new HashMap());
            map2 = (Map)map.get(text);
         }
      }

      name = parts[parts.length - 1];
      return new SubObject(name, map2);
   }

   public VariableCategory findVariableCategory(List variableCategories, String name) {
      for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableCategories)) {
         if (name.equals(variableCategory.getName()) || name.equals(variableCategory.getClazz())) {
            return variableCategory;
         }
      }

      throw new VariableCategoryNotFoundException("变量对象【" + name + "】未定义!");
   }

   public Variable findVariable(VariableCategory vc, String fieldName) {
      if (vc.getVariableNames().containsKey(fieldName)) {
         return (Variable)vc.getVariableNames().get(fieldName);
      } else if (vc.getVariableLabels().containsKey(fieldName)) {
         return (Variable)vc.getVariableLabels().get(fieldName);
      } else {
         throw new VariableNotFoundException("变量对象【" + vc.getName() + "】中未定义名为【" + fieldName + "】字段！");
      }
   }

   public Map buildVariableCategoriesMap(List variableCategories) {
      HashMap variableCategoriesMap = new HashMap();

      for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableCategories)) {
         variableCategoriesMap.put(variableCategory.getName(), variableCategory);
      }

      return variableCategoriesMap;
   }
}
