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
   public int size(List<Object> list) {
      return list.size();
   }

   @ActionMethod(name = "取List中指定位置对象")
   @ActionMethodParameter(names = {"集合对象", "索引"}, enames = {"List", "index"})
   public Object get(List<Object> list, int index) {
      return list.get(index);
   }

   @ActionMethod(name = "求List中所有的数字最大值")
   @ActionMethodParameter(names = "包含所有数字的集合对象", enames = "List")
   public Number max(List<Object> list) {
      if (list.size() == 0) {
         return null;
      }

      double maxResult = Utils.toBigDecimal(list.get(0)).doubleValue();

      for (Object objectValue : list) {
         BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
         maxResult = Math.max(maxResult, decimalValue.doubleValue());
      }

      return maxResult;
   }

   @ActionMethod(name = "求List中所有的数字最小值")
   @ActionMethodParameter(names = "包含所有数字的集合对象", enames = "List")
   public Number min(List<Object> list) {
      if (list.size() == 0) {
         return null;
      }

      double minResult = Utils.toBigDecimal(list.get(0)).doubleValue();

      for (Object objectValue : list) {
         BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
         minResult = Math.min(minResult, decimalValue.doubleValue());
      }

      return minResult;
   }

   @ActionMethod(name = "向List中添加对象")
   @ActionMethodParameter(names = {"集合对象", "要添加的对象"}, enames = {"List", "Object"})
   public void add(List<Object> list, Object obj) {
      list.add(obj);
   }

   @ActionMethod(name = "集合排序")
   @ActionMethodParameter(names = {"集合对象", "属性名", "排序方式"}, enames = {"List", "propertyName", "sortType"})
   public List<Object> sort(List<Object> list, String propertyName, String type) {
      boolean flag = this.evaluateCondition(type);
      Collections.sort(list, new PropertyComparator(propertyName, flag));
      return list;
   }

   private boolean evaluateCondition(String text) {
      return text == null ? true : text.equals("1") || text.equals("true") || text.equals("正序");
   }

   @ActionMethod(name = "抽取集合属性")
   @ActionMethodParameter(names = {"集合对象", "属性名"}, enames = {"List", "propertyName"})
   public List<Object> retrive(List<Object> list, String propertyName) {
      ArrayList retriveResult = new ArrayList();
      if (list == null) {
         return retriveResult;
      }

      for (Object objectValue : list) {
         Object objectProperty = Utils.getObjectProperty(objectValue, propertyName);
         retriveResult.add(objectProperty);
      }

      return retriveResult;
   }

   @ActionMethod(name = "从List中删除对象")
   @ActionMethodParameter(names = {"集合对象", "要删除的对象"}, enames = {"List", "Object"})
   public void remove(List<Object> list, Object obj) {
      list.remove(obj);
   }

   @ActionMethod(name = "指定对象是否存在")
   @ActionMethodParameter(names = {"集合对象", "要判断的对象"}, enames = {"List", "Object"})
   public boolean contains(List<Object> list, Object obj) {
      return list.contains(obj);
   }

   @ActionMethod(name = "List是否为空")
   @ActionMethodParameter(names = "集合对象", enames = "List")
   public boolean isEmpty(List<Object> list) {
      return list.isEmpty();
   }

   @ActionMethod(name = "实例化一个ArrayList")
   @ActionMethodParameter(names = {})
   public ArrayList<Object> newArrayListInstance() {
      return new ArrayList<>();
   }
}
