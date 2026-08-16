package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionBean(name = "List集合", ename = "List")
public class ListAction {
   @ActionMethod(name = "求List大小")
   @ActionMethodParameter(names = "集合对象", enames = "List")
   public int size(List<Object> var1) {
      return var1.size();
   }

   @ActionMethod(name = "取List中指定位置对象")
   @ActionMethodParameter(names = {"集合对象", "索引"}, enames = {"List", "index"})
   public Object get(List<Object> var1, int var2) {
      return var1.get(var2);
   }

   @ActionMethod(name = "求List中所有的数字最大值")
   @ActionMethodParameter(names = "包含所有数字的集合对象", enames = "List")
   public Number max(List<Object> var1) {
      if (var1.size() == 0) {
         return null;
      }

      double var2 = Utils.toBigDecimal(var1.get(0)).doubleValue();

      for (Object var5 : var1) {
         BigDecimal var6 = Utils.toBigDecimal(var5);
         var2 = Math.max(var2, var6.doubleValue());
      }

      return var2;
   }

   @ActionMethod(name = "求List中所有的数字最小值")
   @ActionMethodParameter(names = "包含所有数字的集合对象", enames = "List")
   public Number min(List<Object> var1) {
      if (var1.size() == 0) {
         return null;
      }

      double var2 = Utils.toBigDecimal(var1.get(0)).doubleValue();

      for (Object var5 : var1) {
         BigDecimal var6 = Utils.toBigDecimal(var5);
         var2 = Math.min(var2, var6.doubleValue());
      }

      return var2;
   }

   @ActionMethod(name = "向List中添加对象")
   @ActionMethodParameter(names = {"集合对象", "要添加的对象"}, enames = {"List", "Object"})
   public void add(List<Object> var1, Object var2) {
      var1.add(var2);
   }

   @ActionMethod(name = "集合排序")
   @ActionMethodParameter(names = {"集合对象", "属性名", "排序方式"}, enames = {"List", "propertyName", "sortType"})
   public List<Object> sort(List<Object> var1, String var2, String var3) {
      boolean var4 = this.a(var3);
      Collections.sort(var1, new ListAction$1(this, var2, var4));
      return var1;
   }

   private boolean a(String var1) {
      return var1 == null ? true : var1.equals("1") || var1.equals("true") || var1.equals("正序");
   }

   @ActionMethod(name = "抽取集合属性")
   @ActionMethodParameter(names = {"集合对象", "属性名"}, enames = {"List", "propertyName"})
   public List<Object> retrive(List<Object> var1, String var2) {
      ArrayList var3 = new ArrayList();
      if (var1 == null) {
         return var3;
      }

      for (Object var5 : var1) {
         Object var6 = Utils.getObjectProperty(var5, var2);
         var3.add(var6);
      }

      return var3;
   }

   @ActionMethod(name = "从List中删除对象")
   @ActionMethodParameter(names = {"集合对象", "要删除的对象"}, enames = {"List", "Object"})
   public void remove(List<Object> var1, Object var2) {
      var1.remove(var2);
   }

   @ActionMethod(name = "指定对象是否存在")
   @ActionMethodParameter(names = {"集合对象", "要判断的对象"}, enames = {"List", "Object"})
   public boolean contains(List<Object> var1, Object var2) {
      return var1.contains(var2);
   }

   @ActionMethod(name = "List是否为空")
   @ActionMethodParameter(names = "集合对象", enames = "List")
   public boolean isEmpty(List<Object> var1) {
      return var1.isEmpty();
   }

   @ActionMethod(name = "实例化一个ArrayList")
   @ActionMethodParameter(names = {})
   public ArrayList<Object> newArrayListInstance() {
      return new ArrayList<>();
   }
}
