package com.bstek.urule.console.batch.writer;

import com.bstek.urule.Utils;
import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.filter.PropertyFilter;
import com.bstek.urule.console.batch.filter.PropertyFilterUtils;
import com.bstek.urule.console.batch.filter.WriterFilter;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItemField;
import com.bstek.urule.console.database.model.batch.Filter;
import com.bstek.urule.console.database.model.batch.FilterItem;
import com.bstek.urule.console.database.model.batch.FilterType;
import com.bstek.urule.console.editor.execute.JsonBuilder;
import com.bstek.urule.console.editor.execute.VariableCategoryNotFoundException;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.runtime.KnowledgePackage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WriterUtils {
   private static Log logger = LogFactory.getLog(WriterUtils.class);
   private static final String DOT = ".";
   private static final String PARAMETER = "parameter";

   public static void write(BatchContext batchContext, Writer writer, Map stmtMap, Map outParams, GeneralEntity data, Map resultMap) throws WriterException {
      try {
         BatchDataResolver dataResolver = batchContext.getBatch().getDataResolver();

         for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(dataResolver.getItems())) {
            BatchItemResult batchItemResult = (BatchItemResult)resultMap.get(batchDataResolverItem.getName());
            ArrayList items = new ArrayList();
            List items2 = findListSourceFields((KnowledgePackage)batchContext.getKnowledgePackage(), (BatchDataResolverItem)batchDataResolverItem, (List)items, (Map)outParams, (GeneralEntity)data);
            if (items2.size() <= 0 || items != null && items.size() != 0) {
               if (items2.size() > 0) {
                  for(int index = 0; index < items.size(); ++index) {
                     Object objectValue = items.get(index);
                     GeneralEntity generalEntity = new GeneralEntity();

                     for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(items2)) {
                        String text = batchDataResolverItemField.getSrcProperty().split("\\.")[2];
                        if (objectValue instanceof GeneralEntity) {
                           generalEntity.put(batchDataResolverItemField.getSrcProperty(), ((GeneralEntity)objectValue).get(text));
                        } else {
                           Field declaredField = objectValue.getClass().getDeclaredField(text);
                           declaredField.setAccessible(true);
                           Object objectValue2 = declaredField.get(objectValue);
                           if ("Boolean".equals(batchDataResolverItemField.getDataType()) && objectValue2 instanceof String) {
                              generalEntity.put(batchDataResolverItemField.getSrcProperty(), Boolean.valueOf(objectValue2.toString()));
                           } else {
                              generalEntity.put(batchDataResolverItemField.getSrcProperty(), objectValue2);
                           }

                           generalEntity.put(batchDataResolverItemField.getSrcProperty(), declaredField.get(objectValue));
                        }
                     }

                     populateRecordValues(batchContext, generalEntity, data, outParams, batchDataResolverItem, items2);
                     batchItemResult.setReadCount(batchItemResult.getReadCount() + 1);
                     if (!passesFilters((BatchContext)batchContext, (BatchDataResolverItem)batchDataResolverItem, (GeneralEntity)data, (Map)outParams, (Map)generalEntity)) {
                        batchItemResult.setFilterCount(batchItemResult.getFilterCount() + 1);
                     } else {
                        storeRecord(writer, stmtMap, dataResolver, batchDataResolverItem, generalEntity);
                     }
                  }
               } else {
                  GeneralEntity generalEntity2 = new GeneralEntity();
                  populateRecordValues(batchContext, generalEntity2, data, outParams, batchDataResolverItem, (List)null);
                  batchItemResult.setReadCount(batchItemResult.getReadCount() + 1);
                  if (!passesFilters((BatchContext)batchContext, (BatchDataResolverItem)batchDataResolverItem, (GeneralEntity)data, (Map)outParams, (Map)generalEntity2)) {
                     batchItemResult.setFilterCount(batchItemResult.getFilterCount() + 1);
                  } else {
                     storeRecord(writer, stmtMap, dataResolver, batchDataResolverItem, generalEntity2);
                  }
               }
            }
         }

      } catch (Exception exception) {
         throw new WriterException(exception.getMessage(), exception, data);
      }
   }

   private static List findListSourceFields(KnowledgePackage knowledgePackage, BatchDataResolverItem batchDataResolverItem, List items, Map valuesByKey, GeneralEntity generalEntity) {
      VariableCategory variableCategory = null;
      VariableCategoryNotFoundException variableCategoryNotFoundException2 = null;

      try {
         variableCategory = JsonBuilder.getInstance().findVariableCategory(knowledgePackage.getVariableCategories(), "参数");
      } catch (VariableCategoryNotFoundException variableCategoryNotFoundException) {
         variableCategoryNotFoundException2 = variableCategoryNotFoundException;
      }

      ArrayList items2 = new ArrayList();

      for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(batchDataResolverItem.getFields())) {
         String srcProperty = batchDataResolverItemField.getSrcProperty();
         if (srcProperty.indexOf(".") != -1) {
            List items3 = Arrays.asList(srcProperty.split("\\."));
            if (items3.size() > 2) {
               boolean flag = "parameter".equals(items3.get(0));
               if (flag) {
                  if (variableCategory == null) {
                     throw variableCategoryNotFoundException2;
                  }

                  String text = (String)items3.get(1);
                  Variable variable = JsonBuilder.getInstance().findVariable(variableCategory, text);
                  if (variable.getType() == Datatype.List) {
                     items2.add(batchDataResolverItemField);
                     if (items.size() == 0) {
                        Object objectValue = valuesByKey.get(text);
                        if (objectValue instanceof List) {
                           items.addAll((List)objectValue);
                        }
                     }
                  }
               } else {
                  VariableCategory variableCategory2 = JsonBuilder.getInstance().findVariableCategory(knowledgePackage.getVariableCategories(), (String)items3.get(0));
                  String text2 = (String)items3.get(1);
                  Variable variable2 = JsonBuilder.getInstance().findVariable(variableCategory2, text2);
                  if (variable2.getType() == Datatype.List) {
                     items2.add(batchDataResolverItemField);
                     if (items.size() == 0) {
                        Object objectValue2 = generalEntity.get(text2);
                        if (objectValue2 instanceof List) {
                           items.addAll((List)objectValue2);
                        }
                     }
                  }
               }
            }
         }
      }

      return items2;
   }

   protected static void populateRecordValues(BatchContext batchContext, Map valuesByKey, GeneralEntity generalEntity, Map valuesByKey2, BatchDataResolverItem batchDataResolverItem, List items2) {
      VariableCategory parameterVariableCategory = batchContext.getParameterVariableCategory();
      VariableCategory providerVariableCategory = batchContext.getProviderVariableCategory();

      for(BatchDataResolverItemField batchDataResolverItemField : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(batchDataResolverItem.getFields())) {
         boolean flag = false;
         if (items2 != null) {
            for(BatchDataResolverItemField batchDataResolverItemField2 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(items2)) {
               if (batchDataResolverItemField2.getSrcProperty().equals(batchDataResolverItemField.getSrcProperty())) {
                  flag = true;
                  break;
               }
            }
         }

         if (!flag) {
            String srcProperty = batchDataResolverItemField.getSrcProperty();
            if (srcProperty.indexOf(".") == -1) {
               valuesByKey.put(srcProperty, generalEntity.get(srcProperty));
            } else {
               List items = Arrays.asList(srcProperty.split("\\."));
               boolean flag2 = "parameter".equals(items.get(0));
               if (flag2) {
                  copyNestedProperty((Map)valuesByKey, (BatchDataResolverItemField)batchDataResolverItemField, (List)items, (VariableCategory)parameterVariableCategory, (Object)valuesByKey2);
               } else {
                  copyNestedProperty((Map)valuesByKey, (BatchDataResolverItemField)batchDataResolverItemField, (List)items, (VariableCategory)providerVariableCategory, (Object)generalEntity);
               }
            }
         }
      }

   }

   private static void copyNestedProperty(Map valuesByKey, BatchDataResolverItemField batchDataResolverItemField, List items, VariableCategory variableCategory, Object objectValue) {
      String srcProperty = batchDataResolverItemField.getSrcProperty();
      String text = (String)items.get(1);
      Object objectValue2 = null;
      if (objectValue instanceof GeneralEntity) {
         GeneralEntity generalEntity = (GeneralEntity)objectValue;
         objectValue2 = generalEntity.get(text);
      } else if (objectValue instanceof HashMap) {
         HashMap valuesByKey2 = (HashMap)objectValue;
         objectValue2 = valuesByKey2.get(text);
      }

      if (objectValue2 != null) {
         Variable variable = JsonBuilder.getInstance().findVariable(variableCategory, text);
         if (!variable.getType().equals(Datatype.List)) {
            if (variable.getType().equals(Datatype.Object)) {
               String text2 = (String)items.get(2);
               if (objectValue2 instanceof GeneralEntity) {
                  GeneralEntity generalEntity2 = (GeneralEntity)objectValue2;
                  valuesByKey.put(srcProperty, generalEntity2.get(text2));
               } else {
                  try {
                     Field declaredField = objectValue2.getClass().getDeclaredField(text2);
                     declaredField.setAccessible(true);
                     Object objectValue3 = declaredField.get(objectValue2);
                     if ("Boolean".equals(batchDataResolverItemField.getDataType()) && objectValue3 instanceof String) {
                        valuesByKey.put(batchDataResolverItemField.getSrcProperty(), Boolean.valueOf(objectValue3.toString()));
                     } else {
                        valuesByKey.put(batchDataResolverItemField.getSrcProperty(), objectValue3);
                     }
                  } catch (Exception exception) {
                     WriterUtils.logger.error(exception);
                     java.util.logging.Logger.getLogger(WriterUtils.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
                  }
               }
            } else if (variable.getType().equals(Datatype.Map)) {
               String text3 = (String)items.get(2);
               Map objectValue22 = (Map)objectValue2;
               valuesByKey.put(srcProperty, objectValue22.get(text3));
            } else {
               valuesByKey.put(srcProperty, objectValue2);
            }
         }

      }
   }

   protected static boolean passesFilters(BatchContext batchContext, BatchDataResolverItem batchDataResolverItem, GeneralEntity generalEntity, Map valuesByKey, Map valuesByKey2) {
      if (batchDataResolverItem.getFilters().size() == 0) {
         return true;
      } else {
         VariableCategory parameterVariableCategory = batchContext.getParameterVariableCategory();
         VariableCategory providerVariableCategory = batchContext.getProviderVariableCategory();
         boolean flag = true;

         for(Filter filter : (Iterable<Filter>)(Iterable<?>)(batchDataResolverItem.getFilters())) {
            for(FilterItem filterItem : (Iterable<FilterItem>)(Iterable<?>)(filter.getItems())) {
               if (filterItem.getType() == FilterType.bean) {
                  WriterFilter writerFilter = (WriterFilter)Utils.getApplicationContext().getBean(filterItem.getValue());
                  boolean flag2 = writerFilter.filter(batchContext, batchDataResolverItem, generalEntity, valuesByKey, valuesByKey2);
                  if (!flag2) {
                     flag = false;
                     break;
                  }
               } else if (filterItem.getType() == FilterType.property) {
                  PropertyFilter itemObject = (PropertyFilter)filterItem.getItemObject();
                  String property = itemObject.getProperty();
                  Object objectValue = null;
                  if (property.indexOf(".") == -1) {
                     objectValue = generalEntity.get(property);
                  } else {
                     List items = Arrays.asList(property.split("\\."));
                     boolean flag3 = "parameter".equals(items.get(0));
                     if (flag3) {
                        objectValue = resolveNestedProperty((List)items, (VariableCategory)parameterVariableCategory, (Object)valuesByKey, (String)property, (Map)valuesByKey2);
                     } else {
                        objectValue = resolveNestedProperty((List)items, (VariableCategory)providerVariableCategory, (Object)generalEntity, (String)property, (Map)valuesByKey2);
                     }
                  }

                  boolean flag4 = PropertyFilterUtils.valueMatchFilters(itemObject, objectValue);
                  if (!flag4) {
                     flag = false;
                     break;
                  }
               }

               if (!flag) {
                  break;
               }
            }
         }

         return flag;
      }
   }

   private static Object resolveNestedProperty(List items, VariableCategory variableCategory, Object objectValue, String text, Map valuesByKey) {
      String text2 = (String)items.get(1);
      Object objectValue2 = null;
      if (objectValue instanceof GeneralEntity) {
         GeneralEntity generalEntity = (GeneralEntity)objectValue;
         objectValue2 = generalEntity.get(text2);
      } else if (objectValue instanceof HashMap) {
         HashMap valuesByKey2 = (HashMap)objectValue;
         objectValue2 = valuesByKey2.get(text2);
      }

      if (objectValue2 == null) {
         return null;
      } else {
         Variable variable = JsonBuilder.getInstance().findVariable(variableCategory, text2);
         if (variable.getType().equals(Datatype.List)) {
            return valuesByKey.get(text);
         } else if (variable.getType().equals(Datatype.Object)) {
            String text3 = (String)items.get(2);
            if (objectValue2 instanceof GeneralEntity) {
               GeneralEntity generalEntity2 = (GeneralEntity)objectValue2;
               return generalEntity2.get(text3);
            } else {
               try {
                  Field declaredField = objectValue2.getClass().getDeclaredField(text3);
                  declaredField.setAccessible(true);
                  Object objectValue3 = declaredField.get(objectValue2);
                  return objectValue3;
               } catch (Exception exception) {
                  WriterUtils.logger.error(exception);
                  java.util.logging.Logger.getLogger(WriterUtils.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
                  return null;
               }
            }
         } else if (variable.getType().equals(Datatype.Map)) {
            String text4 = (String)items.get(2);
            Map objectValue22 = (Map)objectValue2;
            return objectValue22.get(text4);
         } else {
            return objectValue2;
         }
      }
   }

   private static void storeRecord(Writer writer, Map valuesByKey, BatchDataResolver batchDataResolver, BatchDataResolverItem batchDataResolverItem, GeneralEntity generalEntity) throws Exception {
      writer.storeRecord(valuesByKey, batchDataResolver, batchDataResolverItem, generalEntity);
   }
}
