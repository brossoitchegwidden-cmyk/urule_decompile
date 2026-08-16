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
   private static Log a = LogFactory.getLog(WriterUtils.class);
   private static final String b = ".";
   private static final String c = "\\.";
   private static final String d = "parameter";

   public static void write(BatchContext var0, Writer var1, Map var2, Map var3, GeneralEntity var4, Map var5) throws WriterException {
      try {
         BatchDataResolver var6 = var0.getBatch().getDataResolver();

         for(BatchDataResolverItem var9 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var6.getItems())) {
            BatchItemResult var10 = (BatchItemResult)var5.get(var9.getName());
            ArrayList var11 = new ArrayList();
            List var12 = a((KnowledgePackage)var0.getKnowledgePackage(), (BatchDataResolverItem)var9, (List)var11, (Map)var3, (GeneralEntity)var4);
            if (var12.size() <= 0 || var11 != null && var11.size() != 0) {
               if (var12.size() > 0) {
                  for(int var22 = 0; var22 < var11.size(); ++var22) {
                     Object var14 = var11.get(var22);
                     GeneralEntity var15 = new GeneralEntity();

                     for(BatchDataResolverItemField var17 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var12)) {
                        String var18 = var17.getSrcProperty().split("\\.")[2];
                        if (var14 instanceof GeneralEntity) {
                           var15.put(var17.getSrcProperty(), ((GeneralEntity)var14).get(var18));
                        } else {
                           Field var19 = var14.getClass().getDeclaredField(var18);
                           var19.setAccessible(true);
                           Object var20 = var19.get(var14);
                           if ("Boolean".equals(var17.getDataType()) && var20 instanceof String) {
                              var15.put(var17.getSrcProperty(), Boolean.valueOf(var20.toString()));
                           } else {
                              var15.put(var17.getSrcProperty(), var20);
                           }

                           var15.put(var17.getSrcProperty(), var19.get(var14));
                        }
                     }

                     a(var0, var15, var4, var3, var9, var12);
                     var10.setReadCount(var10.getReadCount() + 1);
                     if (!a((BatchContext)var0, (BatchDataResolverItem)var9, (GeneralEntity)var4, (Map)var3, (Map)var15)) {
                        var10.setFilterCount(var10.getFilterCount() + 1);
                     } else {
                        a(var1, var2, var6, var9, var15);
                     }
                  }
               } else {
                  GeneralEntity var13 = new GeneralEntity();
                  a(var0, var13, var4, var3, var9, (List)null);
                  var10.setReadCount(var10.getReadCount() + 1);
                  if (!a((BatchContext)var0, (BatchDataResolverItem)var9, (GeneralEntity)var4, (Map)var3, (Map)var13)) {
                     var10.setFilterCount(var10.getFilterCount() + 1);
                  } else {
                     a(var1, var2, var6, var9, var13);
                  }
               }
            }
         }

      } catch (Exception var21) {
         throw new WriterException(var21.getMessage(), var21, var4);
      }
   }

   private static List a(KnowledgePackage var0, BatchDataResolverItem var1, List var2, Map var3, GeneralEntity var4) {
      VariableCategory var5 = null;
      VariableCategoryNotFoundException var6 = null;

      try {
         var5 = JsonBuilder.getInstance().findVariableCategory(var0.getVariableCategories(), "参数");
      } catch (VariableCategoryNotFoundException var17) {
         var6 = var17;
      }

      ArrayList var7 = new ArrayList();

      for(BatchDataResolverItemField var9 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var1.getFields())) {
         String var10 = var9.getSrcProperty();
         if (var10.indexOf(".") != -1) {
            List var11 = Arrays.asList(var10.split("\\."));
            if (var11.size() > 2) {
               boolean var12 = "parameter".equals(var11.get(0));
               if (var12) {
                  if (var5 == null) {
                     throw var6;
                  }

                  String var13 = (String)var11.get(1);
                  Variable var14 = JsonBuilder.getInstance().findVariable(var5, var13);
                  if (var14.getType() == Datatype.List) {
                     var7.add(var9);
                     if (var2.size() == 0) {
                        Object var15 = var3.get(var13);
                        if (var15 instanceof List) {
                           var2.addAll((List)var15);
                        }
                     }
                  }
               } else {
                  VariableCategory var18 = JsonBuilder.getInstance().findVariableCategory(var0.getVariableCategories(), (String)var11.get(0));
                  String var19 = (String)var11.get(1);
                  Variable var20 = JsonBuilder.getInstance().findVariable(var18, var19);
                  if (var20.getType() == Datatype.List) {
                     var7.add(var9);
                     if (var2.size() == 0) {
                        Object var16 = var4.get(var19);
                        if (var16 instanceof List) {
                           var2.addAll((List)var16);
                        }
                     }
                  }
               }
            }
         }
      }

      return var7;
   }

   protected static void a(BatchContext var0, Map var1, GeneralEntity var2, Map var3, BatchDataResolverItem var4, List var5) {
      VariableCategory var6 = var0.getParameterVariableCategory();
      VariableCategory var7 = var0.getProviderVariableCategory();

      for(BatchDataResolverItemField var9 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var4.getFields())) {
         boolean var10 = false;
         if (var5 != null) {
            for(BatchDataResolverItemField var12 : (Iterable<BatchDataResolverItemField>)(Iterable<?>)(var5)) {
               if (var12.getSrcProperty().equals(var9.getSrcProperty())) {
                  var10 = true;
                  break;
               }
            }
         }

         if (!var10) {
            String var15 = var9.getSrcProperty();
            if (var15.indexOf(".") == -1) {
               var1.put(var15, var2.get(var15));
            } else {
               List var13 = Arrays.asList(var15.split("\\."));
               boolean var14 = "parameter".equals(var13.get(0));
               if (var14) {
                  a((Map)var1, (BatchDataResolverItemField)var9, (List)var13, (VariableCategory)var6, (Object)var3);
               } else {
                  a((Map)var1, (BatchDataResolverItemField)var9, (List)var13, (VariableCategory)var7, (Object)var2);
               }
            }
         }
      }

   }

   private static void a(Map var0, BatchDataResolverItemField var1, List var2, VariableCategory var3, Object var4) {
      String var5 = var1.getSrcProperty();
      String var6 = (String)var2.get(1);
      Object var7 = null;
      if (var4 instanceof GeneralEntity) {
         GeneralEntity var8 = (GeneralEntity)var4;
         var7 = var8.get(var6);
      } else if (var4 instanceof HashMap) {
         HashMap var13 = (HashMap)var4;
         var7 = var13.get(var6);
      }

      if (var7 != null) {
         Variable var14 = JsonBuilder.getInstance().findVariable(var3, var6);
         if (!var14.getType().equals(Datatype.List)) {
            if (var14.getType().equals(Datatype.Object)) {
               String var9 = (String)var2.get(2);
               if (var7 instanceof GeneralEntity) {
                  GeneralEntity var10 = (GeneralEntity)var7;
                  var0.put(var5, var10.get(var9));
               } else {
                  try {
                     Field var16 = var7.getClass().getDeclaredField(var9);
                     var16.setAccessible(true);
                     Object var11 = var16.get(var7);
                     if ("Boolean".equals(var1.getDataType()) && var11 instanceof String) {
                        var0.put(var1.getSrcProperty(), Boolean.valueOf(var11.toString()));
                     } else {
                        var0.put(var1.getSrcProperty(), var11);
                     }
                  } catch (Exception var12) {
                     a.error(var12);
                     var12.printStackTrace();
                  }
               }
            } else if (var14.getType().equals(Datatype.Map)) {
               String var15 = (String)var2.get(2);
               Map var17 = (Map)var7;
               var0.put(var5, var17.get(var15));
            } else {
               var0.put(var5, var7);
            }
         }

      }
   }

   protected static boolean a(BatchContext var0, BatchDataResolverItem var1, GeneralEntity var2, Map var3, Map var4) {
      if (var1.getFilters().size() == 0) {
         return true;
      } else {
         VariableCategory var5 = var0.getParameterVariableCategory();
         VariableCategory var6 = var0.getProviderVariableCategory();
         boolean var7 = true;

         for(Filter var9 : (Iterable<Filter>)(Iterable<?>)(var1.getFilters())) {
            for(FilterItem var11 : (Iterable<FilterItem>)(Iterable<?>)(var9.getItems())) {
               if (var11.getType() == FilterType.bean) {
                  WriterFilter var12 = (WriterFilter)Utils.getApplicationContext().getBean(var11.getValue());
                  boolean var13 = var12.filter(var0, var1, var2, var3, var4);
                  if (!var13) {
                     var7 = false;
                     break;
                  }
               } else if (var11.getType() == FilterType.property) {
                  PropertyFilter var17 = (PropertyFilter)var11.getItemObject();
                  String var18 = var17.getProperty();
                  Object var14 = null;
                  if (var18.indexOf(".") == -1) {
                     var14 = var2.get(var18);
                  } else {
                     List var15 = Arrays.asList(var18.split("\\."));
                     boolean var16 = "parameter".equals(var15.get(0));
                     if (var16) {
                        var14 = a((List)var15, (VariableCategory)var5, (Object)var3, (String)var18, (Map)var4);
                     } else {
                        var14 = a((List)var15, (VariableCategory)var6, (Object)var2, (String)var18, (Map)var4);
                     }
                  }

                  boolean var20 = PropertyFilterUtils.valueMatchFilters(var17, var14);
                  if (!var20) {
                     var7 = false;
                     break;
                  }
               }

               if (!var7) {
                  break;
               }
            }
         }

         return var7;
      }
   }

   private static Object a(List var0, VariableCategory var1, Object var2, String var3, Map var4) {
      String var5 = (String)var0.get(1);
      Object var6 = null;
      if (var2 instanceof GeneralEntity) {
         GeneralEntity var7 = (GeneralEntity)var2;
         var6 = var7.get(var5);
      } else if (var2 instanceof HashMap) {
         HashMap var12 = (HashMap)var2;
         var6 = var12.get(var5);
      }

      if (var6 == null) {
         return null;
      } else {
         Variable var13 = JsonBuilder.getInstance().findVariable(var1, var5);
         if (var13.getType().equals(Datatype.List)) {
            return var4.get(var3);
         } else if (var13.getType().equals(Datatype.Object)) {
            String var14 = (String)var0.get(2);
            if (var6 instanceof GeneralEntity) {
               GeneralEntity var16 = (GeneralEntity)var6;
               return var16.get(var14);
            } else {
               try {
                  Field var15 = var6.getClass().getDeclaredField(var14);
                  var15.setAccessible(true);
                  Object var10 = var15.get(var6);
                  return var10;
               } catch (Exception var11) {
                  a.error(var11);
                  var11.printStackTrace();
                  return null;
               }
            }
         } else if (var13.getType().equals(Datatype.Map)) {
            String var8 = (String)var0.get(2);
            Map var9 = (Map)var6;
            return var9.get(var8);
         } else {
            return var6;
         }
      }
   }

   private static void a(Writer var0, Map var1, BatchDataResolver var2, BatchDataResolverItem var3, GeneralEntity var4) throws Exception {
      var0.storeRecord(var1, var2, var3, var4);
   }
}
