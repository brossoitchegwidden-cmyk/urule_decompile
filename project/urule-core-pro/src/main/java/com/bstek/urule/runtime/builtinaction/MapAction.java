package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ActionBean(name = "Map集合", ename = "Map")
public class MapAction {
   @ActionMethod(name = "添加到Map")
   @ActionMethodParameter(names = {"Map对象", "key", "value"}, enames = {"Map", "key", "value"})
   public void put(Map<String, Object> map, String key, Object value) {
      map.put(key, value);
   }

   @ActionMethod(name = "从Map中删除")
   @ActionMethodParameter(names = {"Map对象", "key"}, enames = {"Map", "key"})
   public void remove(Map<String, Object> map, String key) {
      map.remove(key);
   }

   @ActionMethod(name = "指定Key是否存在")
   @ActionMethodParameter(names = {"Map对象", "key"}, enames = {"Map", "key"})
   public boolean containsKey(Map<String, Object> map, String key) {
      return map.containsKey(key);
   }

   @ActionMethod(name = "从Map中取值")
   @ActionMethodParameter(names = {"Map对象", "key"}, enames = {"Map", "key"})
   public Object get(Map<String, Object> map, String key) {
      return map.get(key);
   }

   @ActionMethod(name = "返回Map大小")
   @ActionMethodParameter(names = "Map对象", enames = "Map")
   public int size(Map<String, Object> map) {
      return map.size();
   }

   @ActionMethod(name = "返回Map的Key集合")
   @ActionMethodParameter(names = "Map对象", enames = "Map")
   public Set<String> keys(Map<String, Object> map) {
      return map.keySet();
   }

   @ActionMethod(name = "返回Map的值集合")
   @ActionMethodParameter(names = "Map对象", enames = "Map")
   public Collection<Object> values(Map<String, Object> map) {
      return map.values();
   }

   @ActionMethod(name = "创建一个HashMap实例")
   @ActionMethodParameter(names = {})
   public HashMap<String, Object> newHashMap() {
      return new HashMap<>();
   }
}
