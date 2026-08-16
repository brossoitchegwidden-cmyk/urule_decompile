package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

@ActionBean(name = "字符串", ename = "String")
public class StringAction {
   @ActionMethod(name = "去空格")
   @ActionMethodParameter(names = "目标字符串", enames = "str")
   public String trim(String var1) {
      return var1 == null ? var1 : var1.trim();
   }

   @ActionMethod(name = "指定起止的字符串截取")
   @ActionMethodParameter(names = {"目标字符串", "开始位置", "结束位置"}, enames = {"str", "start", "end"})
   public String substring(String var1, int var2, int var3) {
      return var1 == null ? null : var1.substring(var2, var3);
   }

   @ActionMethod(name = "指定开始的字符串截取")
   @ActionMethodParameter(names = {"目标字符串", "开始位置"}, enames = {"str", "start"})
   public String substringForStart(String var1, int var2) {
      return var1 == null ? null : var1.substring(var2);
   }

   @ActionMethod(name = "指定结束的字符串截取")
   @ActionMethodParameter(names = {"目标字符串", "结束位置"}, enames = {"str", "end"})
   public String substringForEnd(String var1, int var2) {
      return var1 == null ? null : var1.substring(0, var2);
   }

   @ActionMethod(name = "转小写")
   @ActionMethodParameter(names = "目标字符串", enames = "str")
   public String toLowerCase(String var1) {
      return var1 == null ? null : var1.toLowerCase();
   }

   @ActionMethod(name = "转大写")
   @ActionMethodParameter(names = "目标字符串", enames = "str")
   public String toUpperCase(String var1) {
      return var1 == null ? null : var1.toUpperCase();
   }

   @ActionMethod(name = "获取长度")
   @ActionMethodParameter(names = "目标字符串", enames = "str")
   public Object length(String var1) {
      return var1 == null ? null : var1.length();
   }

   @ActionMethod(name = "获取字符")
   @ActionMethodParameter(names = {"目标字符串", "位置"}, enames = {"str", "index"})
   public Object charAt(String var1, int var2) {
      return var1 == null ? null : var1.charAt(var2);
   }

   @ActionMethod(name = "字符首次出现位置")
   @ActionMethodParameter(names = {"目标字符串", "要查找的字符串"}, enames = {"str", "targetStr"})
   public Object indexOf(String var1, String var2) {
      return var1 == null ? null : var1.indexOf(var2);
   }

   @ActionMethod(name = "字符最后出现位置")
   @ActionMethodParameter(names = {"目标字符串", "要查找的字符串"}, enames = {"str", "targetStr"})
   public Object lastIndexOf(String var1, String var2) {
      return var1 == null ? null : var1.lastIndexOf(var2);
   }

   @ActionMethod(name = "替换字符串")
   @ActionMethodParameter(names = {"目标字符串", "原字符串", "新字符串"}, enames = {"str", "targetStr", "replacement"})
   public String replace(String var1, String var2, String var3) {
      return var1 == null ? null : var1.replace(var2, var3);
   }

   @ActionMethod(name = "拆分字符串为集合")
   @ActionMethodParameter(names = {"目标字符串", "分隔符"}, enames = {"str", "regex"})
   public List<String> split(String var1, String var2) {
      if (StringUtils.isBlank(var1)) {
         return new ArrayList<>();
      }

      String[] var3 = var1.split(var2);
      ArrayList var4 = new ArrayList();

      for (String var8 : var3) {
         var4.add(var8);
      }

      return var4;
   }
}
