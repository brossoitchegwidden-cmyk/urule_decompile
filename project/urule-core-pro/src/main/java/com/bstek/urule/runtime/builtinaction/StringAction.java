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
   public String trim(String str) {
      return str == null ? str : str.trim();
   }

   @ActionMethod(name = "指定起止的字符串截取")
   @ActionMethodParameter(names = {"目标字符串", "开始位置", "结束位置"}, enames = {"str", "start", "end"})
   public String substring(String str, int start, int end) {
      return str == null ? null : str.substring(start, end);
   }

   @ActionMethod(name = "指定开始的字符串截取")
   @ActionMethodParameter(names = {"目标字符串", "开始位置"}, enames = {"str", "start"})
   public String substringForStart(String str, int start) {
      return str == null ? null : str.substring(start);
   }

   @ActionMethod(name = "指定结束的字符串截取")
   @ActionMethodParameter(names = {"目标字符串", "结束位置"}, enames = {"str", "end"})
   public String substringForEnd(String str, int end) {
      return str == null ? null : str.substring(0, end);
   }

   @ActionMethod(name = "转小写")
   @ActionMethodParameter(names = "目标字符串", enames = "str")
   public String toLowerCase(String str) {
      return str == null ? null : str.toLowerCase();
   }

   @ActionMethod(name = "转大写")
   @ActionMethodParameter(names = "目标字符串", enames = "str")
   public String toUpperCase(String str) {
      return str == null ? null : str.toUpperCase();
   }

   @ActionMethod(name = "获取长度")
   @ActionMethodParameter(names = "目标字符串", enames = "str")
   public Object length(String str) {
      return str == null ? null : str.length();
   }

   @ActionMethod(name = "获取字符")
   @ActionMethodParameter(names = {"目标字符串", "位置"}, enames = {"str", "index"})
   public Object charAt(String str, int index) {
      return str == null ? null : str.charAt(index);
   }

   @ActionMethod(name = "字符首次出现位置")
   @ActionMethodParameter(names = {"目标字符串", "要查找的字符串"}, enames = {"str", "targetStr"})
   public Object indexOf(String str, String targetStr) {
      return str == null ? null : str.indexOf(targetStr);
   }

   @ActionMethod(name = "字符最后出现位置")
   @ActionMethodParameter(names = {"目标字符串", "要查找的字符串"}, enames = {"str", "targetStr"})
   public Object lastIndexOf(String str, String targetStr) {
      return str == null ? null : str.lastIndexOf(targetStr);
   }

   @ActionMethod(name = "替换字符串")
   @ActionMethodParameter(names = {"目标字符串", "原字符串", "新字符串"}, enames = {"str", "targetStr", "replacement"})
   public String replace(String str, String oldStr, String newStr) {
      return str == null ? null : str.replace(oldStr, newStr);
   }

   @ActionMethod(name = "拆分字符串为集合")
   @ActionMethodParameter(names = {"目标字符串", "分隔符"}, enames = {"str", "regex"})
   public List<String> split(String str, String regex) {
      if (StringUtils.isBlank(str)) {
         return new ArrayList<>();
      }

      String[] parts = str.split(regex);
      ArrayList splitResult = new ArrayList();

      for (String text : parts) {
         splitResult.add(text);
      }

      return splitResult;
   }
}
