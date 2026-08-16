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
   private Logger a = Logger.getGlobal();
   private static JsonBuilder b = new JsonBuilder();

   public static JsonBuilder getInstance() {
      return b;
   }

   public Object buildComplexObject(Object var1, Map var2) throws Exception {
      if (var1 instanceof String) {
         String var14 = var1.toString().trim();
         ObjectMapper var16 = JsonMapper.builder().build();
         if (var14.startsWith("{") && var14.endsWith("}")) {
            Map var20 = (Map)var16.readValue(var14, HashMap.class);
            Object var23 = var20.get("data");
            Object var26 = var20.get("name");
            Object var28 = var20.get("fields");
            if (var23 != null && var23 instanceof List) {
               List var30 = (List)var23;
               return this.a(var30, var2);
            } else {
               return var26 != null && var28 != null ? this.a(var2, var20) : var20;
            }
         } else if (var14.startsWith("[") && var14.endsWith("]")) {
            ArrayList var19 = new ArrayList();

            for(Object var27 : (List)var16.readValue(var14, ArrayList.class)) {
               if (var27 instanceof Map) {
                  Map var29 = (Map)var27;
                  Object var32 = var29.get("name");
                  Object var33 = var29.get("fields");
                  if (var32 != null && var33 != null) {
                     var19.add(this.a(var2, var29));
                  } else {
                     var19.add(var27);
                  }
               } else {
                  var19.add(var27);
               }
            }

            return var19;
         } else if (!this.a(var14)) {
            return var14;
         } else {
            try {
               Base64 var18 = new Base64();
               ByteArrayOutputStream var21 = new ByteArrayOutputStream();
               ByteArrayInputStream var24 = new ByteArrayInputStream(var18.decode(var14.getBytes("utf-8")));
               GZIPInputStream var8 = new GZIPInputStream(var24);
               byte[] var9 = new byte[1024];
               int var10 = -1;

               while((var10 = var8.read(var9)) != -1) {
                  var21.write(var9, 0, var10);
               }

               var24.close();
               var8.close();
               String var11 = var21.toString();
               return this.buildComplexObject(var11, var2);
            } catch (Exception var12) {
               this.a.warning(var12.getMessage());
               this.a.warning("Fail to decode data :" + var14 + "");
               return var14;
            }
         }
      } else if (var1 instanceof List) {
         ArrayList var13 = new ArrayList();

         for(Object var6 : (List)var1) {
            if (var6 instanceof Map) {
               Map var7 = (Map)var6;
               var13.add(this.a(var2, var7));
            } else {
               var13.add(var6);
            }
         }

         return var13;
      } else if (var1 instanceof Map) {
         Map var3 = (Map)var1;
         Object var4 = var3.get("name");
         Object var5 = var3.get("fields");
         return var4 != null && var5 != null ? this.a(var2, var3) : var3;
      } else {
         return var1;
      }
   }

   private MultiData a(List var1, Map var2) throws Exception {
      ArrayList var3 = new ArrayList();

      for(Object var5 : var1) {
         if (var5 instanceof List) {
            ArrayList var13 = new ArrayList();
            var3.add(var13);

            for(Object var9 : (List)var5) {
               if (!(var9 instanceof Map)) {
                  this.a.warning("[" + var9 + "] is not a map data!");
               } else {
                  Map var10 = (Map)var9;
                  Object var11 = var10.get("name");
                  Object var12 = var10.get("fields");
                  if (var11 != null && var12 != null) {
                     var13.add(this.a(var2, var10));
                  } else {
                     var13.add(var10);
                  }
               }
            }
         } else {
            Map var6 = (Map)var5;
            Object var7 = var6.get("name");
            Object var8 = var6.get("fields");
            if (var7 != null && var8 != null) {
               var3.add(this.a(var2, var6));
            } else {
               var3.add(var6);
            }
         }
      }

      return new MultiData(var3);
   }

   private boolean a(String var1) {
      String var2 = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
      return var1.matches(var2);
   }

   private Map a(Map var1, Map var2) throws Exception {
      String var3 = (String)var2.get("name");
      Object var4 = var2.get("fields");
      if (StringUtils.isBlank(var3)) {
         if (var4 == null) {
            return var2;
         } else if (!(var4 instanceof List) && !(var4 instanceof Map)) {
            return var2;
         } else {
            throw new RuleException("复杂对象值【" + var2 + "】需要一个名为\"name\"的属性值来标明当前对象类型");
         }
      } else if (var4 != null && (!(var4 instanceof List) || !(var4 instanceof Map))) {
         VariableCategory var5 = (VariableCategory)var1.get(var3);
         String var6 = Configure.getConfigure().getRestParameterName();
         if (var6.equals(var3) && var5 == null) {
            var5 = (VariableCategory)var1.get("参数");
         }

         if (var5 == null) {
            throw new VariableCategoryNotFoundException("变量对象【" + var3 + "】未定义!");
         } else {
            Object var7 = null;
            if (var3.equals(var6)) {
               var7 = new HashMap();
            } else {
               var7 = new GeneralEntity(var5.getClazz());
            }

            if (!(var4 instanceof List)) {
               if (!(var4 instanceof Map)) {
                  throw new RuleException("复杂对象值【" + var2 + "】的\"fields\"的属性值必须是一个对象类型或集合类型.");
               }

               Map var19 = (Map)var4;
               Iterator var21 = var19.keySet().iterator();

               label118:
               while(true) {
                  Object var25;
                  Variable var30;
                  while(true) {
                     if (!var21.hasNext()) {
                        break label118;
                     }

                     String var23 = (String)var21.next();
                     var25 = var19.get(var23);
                     if (var25 != null) {
                        Object var29 = null;

                        try {
                           var30 = this.findVariable(var5, var23);
                           break;
                        } catch (Exception var16) {
                        }
                     }
                  }

                  Datatype var32 = var30.getType();
                  SubObject var33 = this.findSubObject(var30.getName(), (Map)var7);
                  Map var15 = var33.getMap();
                  if (!var32.equals(Datatype.Object) && !var32.equals(Datatype.List)) {
                     var15.put(var33.getName(), var32.convert(var25));
                  } else {
                     var15.put(var33.getName(), this.buildComplexObject(var25, var1));
                  }
               }
            } else {
               for(Map var10 : (Iterable<Map>)(Iterable<?>)((List)var4)) {
                  String var11 = (String)var10.get("name");
                  if (StringUtils.isBlank(var11)) {
                     throw new RuleException("子对象需要有一个名为name的属性来标明对象的具体属性名");
                  }

                  Variable var28 = null;

                  try {
                     var28 = this.findVariable(var5, var11);
                  } catch (Exception var17) {
                     continue;
                  }

                  Object var13 = var10.get("value");
                  if (var13 == null) {
                     var13 = var28.getDefaultValue();
                     if (var13 == null) {
                        continue;
                     }
                  }

                  Datatype var14 = var28.getType();
                  if (!var14.equals(Datatype.Object) && !var14.equals(Datatype.List)) {
                     ((Map)var7).put(var28.getName(), var14.convert(var13));
                  } else {
                     ((Map)var7).put(var28.getName(), this.buildComplexObject(var13, var1));
                  }
               }
            }

            for(Variable var22 : var5.getVariables()) {
               String var24 = var22.getName();
               if (!((Map)var7).containsKey(var24)) {
                  String var26 = var22.getDefaultValue();
                  if (var26 != null) {
                     Datatype var31 = var22.getType();
                     Object var27;
                     if (var31.equals(Datatype.String)) {
                        var27 = var31.convert(var26);
                     } else if (!var31.equals(Datatype.List) && !var31.equals(Datatype.Object) && !var31.equals(Datatype.Set) && !var31.equals(Datatype.Map)) {
                        var27 = var31.convert(var26);
                     } else {
                        var27 = getInstance().buildComplexObject(var26, var1);
                     }

                     ((Map)var7).put(var24, var27);
                  }
               }
            }

            return (Map)var7;
         }
      } else {
         return var2;
      }
   }

   public SubObject findSubObject(String var1, Map var2) {
      Map var3 = var2;
      String[] var4 = var1.split("\\.");

      for(int var5 = 0; var5 < var4.length - 1; ++var5) {
         String var6 = var4[var5];
         if (var2.containsKey(var6)) {
            var3 = (Map)var2.get(var6);
         } else {
            var3.put(var6, new HashMap());
            var3 = (Map)var2.get(var6);
         }
      }

      var1 = var4[var4.length - 1];
      return new SubObject(var1, var3);
   }

   public VariableCategory findVariableCategory(List var1, String var2) {
      for(VariableCategory var4 : (Iterable<VariableCategory>)(Iterable<?>)(var1)) {
         if (var2.equals(var4.getName()) || var2.equals(var4.getClazz())) {
            return var4;
         }
      }

      throw new VariableCategoryNotFoundException("变量对象【" + var2 + "】未定义!");
   }

   public Variable findVariable(VariableCategory var1, String var2) {
      if (var1.getVariableNames().containsKey(var2)) {
         return (Variable)var1.getVariableNames().get(var2);
      } else if (var1.getVariableLabels().containsKey(var2)) {
         return (Variable)var1.getVariableLabels().get(var2);
      } else {
         throw new VariableNotFoundException("变量对象【" + var1.getName() + "】中未定义名为【" + var2 + "】字段！");
      }
   }

   public Map buildVariableCategoriesMap(List var1) {
      HashMap var2 = new HashMap();

      for(VariableCategory var4 : (Iterable<VariableCategory>)(Iterable<?>)(var1)) {
         var2.put(var4.getName(), var4);
      }

      return var2;
   }
}
