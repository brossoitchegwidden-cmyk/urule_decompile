package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

@ActionBean(name = "日期", ename = "Date")
public class DateAction {
   @ActionMethod(name = "取指定月份天数")
   @ActionMethodParameter(names = {"开始日期", "结束日期", "月份"}, enames = {"startDate", "endDate", "month"})
   public int buildIncludeMonthDays(Object start, Object end, String month) {
      return this.countDaysByMonth(start, end, month, true);
   }

   @ActionMethod(name = "取非指定月份天数")
   @ActionMethodParameter(names = {"开始日期", "结束日期", "月份"}, enames = {"startDate", "endDate", "month"})
   public int buildExcludeMonthDays(Object start, Object end, String month) {
      return this.countDaysByMonth(start, end, month, false);
   }

   private int countDaysByMonth(Object objectValue, Object objectValue2, String text, boolean flag) {
      if (objectValue == null) {
         throw new RuleException("开始日期不能为空！");
      }

      if (objectValue2 == null) {
         throw new RuleException("开始日期不能为空！");
      }

      Date time = this.normalizeDate(objectValue);
      Date dateValue = this.normalizeDate(objectValue2);
      if (time.compareTo(dateValue) > 0) {
         throw new RuleException("开始日期必须要小于结束日期！");
      }

      int number = 0;
      int number2 = 0;
      String[] parts = text.split(",");
      List items = Arrays.asList(parts);

      while (time.compareTo(dateValue) <= 0) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(time);
         int number3 = calendar.get(2) + 1;
         if (items.contains(String.valueOf(number3))) {
            if (flag) {
               number++;
            }
         } else if (!flag) {
            number2++;
         }

         calendar.add(5, 1);
         time = calendar.getTime();
      }

      return flag ? number : number2;
   }

   private Date normalizeDate(Object objectValue) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      if (objectValue instanceof Date) {
         Date objectValue2 = (Date)objectValue;
         String text = simpleDateFormat.format(objectValue2);

         try {
            return simpleDateFormat.parse(text);
         } catch (ParseException parseException) {
            throw new RuleException(parseException);
         }
      } else {
         String text2 = objectValue.toString();

         try {
            return simpleDateFormat.parse(text2);
         } catch (ParseException parseException2) {
            simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

            try {
               return simpleDateFormat.parse(text2);
            } catch (ParseException parseException3) {
               throw new RuleException("不能将[" + objectValue + "]解析成日期");
            }
         }
      }
   }

   @ActionMethod(name = "解析字符串为日期")
   @ActionMethodParameter(names = {"日期字符串", "格式"}, enames = {"string", "pattern"})
   public Date formatString(String dateStr, String pattern) {
      if (StringUtils.isBlank(dateStr)) {
         return null;
      }

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

      try {
         return simpleDateFormat.parse(dateStr);
      } catch (ParseException parseException) {
         throw new RuleException(parseException);
      }
   }

   @ActionMethod(name = "当前日期")
   @ActionMethodParameter(names = {})
   public Date getDate() {
      return new Date();
   }

   @ActionMethod(name = "格式化日期")
   @ActionMethodParameter(names = {"目标日期", "格式"}, enames = {"Date", "pattern"})
   public String format(Date date, String pattern) {
      if (date == null) {
         return null;
      }

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
      return simpleDateFormat.format(date);
   }

   @ActionMethod(name = "加日期")
   @ActionMethodParameter(names = {"目标日期", "年数", "月数", "天数", "小时", "分钟", "秒数"}, enames = {"years", "months", "days", "hours", "minutes", "seconds"})
   public Date addDate(Date date, int years, int months, int days, int hours, int minutes, int seconds) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(1, years);
      calendar.add(2, months);
      calendar.add(5, days);
      calendar.add(11, hours);
      calendar.add(12, minutes);
      calendar.add(13, seconds);
      return calendar.getTime();
   }

   @ActionMethod(name = "日期加年")
   @ActionMethodParameter(names = {"目标日期", "年数"}, enames = {"Date", "years"})
   public Date addDateForYear(Date date, int years) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(1, years);
      return calendar.getTime();
   }

   @ActionMethod(name = "日期加月")
   @ActionMethodParameter(names = {"目标日期", "月数"}, enames = {"Date", "months"})
   public Date addDateForMonth(Date date, int months) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(2, months);
      return calendar.getTime();
   }

   @ActionMethod(name = "日期加天")
   @ActionMethodParameter(names = {"目标日期", "天数"}, enames = {"Date", "days"})
   public Date addDateForDay(Date date, int days) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(5, days);
      return calendar.getTime();
   }

   @ActionMethod(name = "日期加小时")
   @ActionMethodParameter(names = {"目标日期", "小时数"}, enames = {"Date", "hours"})
   public Date addDateForHour(Date date, int hours) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(11, hours);
      return calendar.getTime();
   }

   @ActionMethod(name = "日期加分钟")
   @ActionMethodParameter(names = {"目标日期", "分钟数"}, enames = {"Date", "minutes"})
   public Date addDateForMinute(Date date, int minutes) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(12, minutes);
      return calendar.getTime();
   }

   @ActionMethod(name = "日期加秒")
   @ActionMethodParameter(names = {"目标日期", "秒数"}, enames = {"Date", "seconds"})
   public Date addDateForSecond(Date date, int seconds) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(13, seconds);
      return calendar.getTime();
   }

   @ActionMethod(name = "减日期")
   @ActionMethodParameter(names = {"目标日期", "年数", "月数", "天数", "小时", "分钟", "秒数"}, enames = {"years", "months", "days", "hours", "minutes", "seconds"})
   public Date subDate(Date date, int years, int months, int days, int hours, int minutes, int seconds) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(1, -years);
      calendar.add(2, -months);
      calendar.add(5, -days);
      calendar.add(11, -hours);
      calendar.add(12, -minutes);
      calendar.add(13, -seconds);
      return calendar.getTime();
   }

   @ActionMethod(name = "减日期减年")
   @ActionMethodParameter(names = {"目标日期", "年数"}, enames = {"Date", "years"})
   public Date subDateForYear(Date date, int years) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(1, -years);
      return calendar.getTime();
   }

   @ActionMethod(name = "减日期减月")
   @ActionMethodParameter(names = {"目标日期", "月数"}, enames = {"Date", "months"})
   public Date subDateForMonth(Date date, int months) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(2, -months);
      return calendar.getTime();
   }

   @ActionMethod(name = "减日期减天")
   @ActionMethodParameter(names = {"目标日期", "天数"}, enames = {"Date", "days"})
   public Date subDateForDay(Date date, int days) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(5, -days);
      return calendar.getTime();
   }

   @ActionMethod(name = "减日期减小时")
   @ActionMethodParameter(names = {"目标日期", "小时"}, enames = {"Date", "hours"})
   public Date subDateForHour(Date date, int hours) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(11, -hours);
      return calendar.getTime();
   }

   @ActionMethod(name = "减日期减分钟")
   @ActionMethodParameter(names = {"目标日期", "分钟"}, enames = {"Date", "minutes"})
   public Date subDateForMinute(Date date, int minutes) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(12, -minutes);
      return calendar.getTime();
   }

   @ActionMethod(name = "减日期减秒")
   @ActionMethodParameter(names = {"目标日期", "秒数"}, enames = {"Date", "seconds"})
   public Date subDateForSecond(Date date, int seconds) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(13, -seconds);
      return calendar.getTime();
   }

   @ActionMethod(name = "取年份")
   @ActionMethodParameter(names = "目标日期", enames = "Date")
   public Object getYear(Date date) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(1);
   }

   @ActionMethod(name = "取月份")
   @ActionMethodParameter(names = "目标日期", enames = "Date")
   public Object getMonth(Date date) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(2);
   }

   @ActionMethod(name = "取星期")
   @ActionMethodParameter(names = "目标日期", enames = "Date")
   public Object getWeek(Date date) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(7);
   }

   @ActionMethod(name = "取天")
   @ActionMethodParameter(names = "目标日期", enames = "Date")
   public Object getay(Date date) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(5);
   }

   @ActionMethod(name = "取小时")
   @ActionMethodParameter(names = "目标日期", enames = "Date")
   public Object getHour(Date date) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(11);
   }

   @ActionMethod(name = "取分钟")
   @ActionMethodParameter(names = "目标日期", enames = "Date")
   public Object getMinute(Date date) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(12);
   }

   @ActionMethod(name = "取秒")
   @ActionMethodParameter(names = "目标日期", enames = "Date")
   public Object getSecond(Date date) {
      if (date == null) {
         return null;
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(13);
   }

   @ActionMethod(name = "日期相减返回毫秒")
   @ActionMethodParameter(names = {"日期", "减去的日期"}, enames = {"Date1", "Date2"})
   public Object dateDifMillSecond(Date d1, Date d2) {
      if (d1 != null && d2 != null) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(d1);
         Calendar calendar2 = Calendar.getInstance();
         calendar2.setTime(d2);
         long timeInMillis = calendar.getTimeInMillis();
         long timeInMillis2 = calendar2.getTimeInMillis();
         return timeInMillis - timeInMillis2;
      } else {
         return null;
      }
   }

   @ActionMethod(name = "日期相减返回秒")
   @ActionMethodParameter(names = {"日期", "减去的日期"}, enames = {"Date1", "Date2"})
   public Object dateDifSecond(Date d1, Date d2) {
      if (d1 != null && d2 != null) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(d1);
         Calendar calendar2 = Calendar.getInstance();
         calendar2.setTime(d2);
         long timeInMillis = calendar.getTimeInMillis();
         long timeInMillis2 = calendar2.getTimeInMillis();
         return (timeInMillis - timeInMillis2) / 1000L;
      } else {
         return null;
      }
   }

   @ActionMethod(name = "日期相减返回分钟")
   @ActionMethodParameter(names = {"日期", "减去的日期"}, enames = {"Date1", "Date2"})
   public Object dateDifMinute(Date d1, Date d2) {
      if (d1 != null && d2 != null) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(d1);
         Calendar calendar2 = Calendar.getInstance();
         calendar2.setTime(d2);
         long timeInMillis = calendar.getTimeInMillis();
         long timeInMillis2 = calendar2.getTimeInMillis();
         return (timeInMillis - timeInMillis2) / 60000L;
      } else {
         return null;
      }
   }

   @ActionMethod(name = "日期相减返回小时")
   @ActionMethodParameter(names = {"日期", "减去的日期"}, enames = {"Date1", "Date2"})
   public Object dateDifHour(Date d1, Date d2) {
      if (d1 != null && d2 != null) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(d1);
         Calendar calendar2 = Calendar.getInstance();
         calendar2.setTime(d2);
         long timeInMillis = calendar.getTimeInMillis();
         long timeInMillis2 = calendar2.getTimeInMillis();
         return (timeInMillis - timeInMillis2) / 3600000L;
      } else {
         return null;
      }
   }

   @ActionMethod(name = "日期相减返回天")
   @ActionMethodParameter(names = {"日期", "减去的日期"}, enames = {"Date1", "Date2"})
   public Object dateDifDay(Date d1, Date d2) {
      if (d1 != null && d2 != null) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(d1);
         Calendar calendar2 = Calendar.getInstance();
         calendar2.setTime(d2);
         long timeInMillis = calendar.getTimeInMillis();
         long timeInMillis2 = calendar2.getTimeInMillis();
         return (timeInMillis - timeInMillis2) / 86400000L;
      } else {
         return null;
      }
   }

   @ActionMethod(name = "日期相减返回星期")
   @ActionMethodParameter(names = {"日期", "减去的日期"}, enames = {"Date1", "Date2"})
   public Object dateDifWeek(Date d1, Date d2) {
      if (d1 != null && d2 != null) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(d1);
         Calendar calendar2 = Calendar.getInstance();
         calendar2.setTime(d2);
         long timeInMillis = calendar.getTimeInMillis();
         long timeInMillis2 = calendar2.getTimeInMillis();
         return (timeInMillis - timeInMillis2) / 604800000L;
      } else {
         return null;
      }
   }

   @ActionMethod(name = "日期相减返回月")
   @ActionMethodParameter(names = {"日期", "减去的日期"}, enames = {"Date1", "Date2"})
   public Object dateDifMonth(Date d1, Date d2) {
      if (d1 != null && d2 != null) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(d1);
         Calendar calendar2 = Calendar.getInstance();
         calendar2.setTime(d2);
         int number = calendar.get(1);
         int number2 = calendar2.get(1);
         int number3 = calendar.get(2);
         int number4 = calendar2.get(2);
         int dateDifMonthResult = 12 * (number - number2) + (number3 - number4);
         return dateDifMonthResult;
      } else {
         return null;
      }
   }
}
