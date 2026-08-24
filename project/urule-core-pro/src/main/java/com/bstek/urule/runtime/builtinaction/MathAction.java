package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@ActionBean(name = "数学函数", ename = "Math")
public class MathAction {
   @ActionMethod(name = "求绝对值")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number abs(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return Math.abs(decimalValue.doubleValue());
   }

   @ActionMethod(name = "求最大值")
   @ActionMethodParameter(names = {"数字1", "数字2"}, enames = {"number1", "number2"})
   public Number max(Object obj, Object obj1) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      BigDecimal decimalValue2 = Utils.toBigDecimal(obj1);
      return Math.max(decimalValue.doubleValue(), decimalValue2.doubleValue());
   }

   @ActionMethod(name = "求最小值")
   @ActionMethodParameter(names = {"数字1", "数字2"}, enames = {"number1", "number2"})
   public Number min(Object obj, Object obj1) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      BigDecimal decimalValue2 = Utils.toBigDecimal(obj1);
      return Math.min(decimalValue.doubleValue(), decimalValue2.doubleValue());
   }

   @ActionMethod(name = "求正弦")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number in(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return Math.sin(decimalValue.doubleValue());
   }

   @ActionMethod(name = "求余弦")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number cos(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return Math.cos(decimalValue.doubleValue());
   }

   @ActionMethod(name = "求正切")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number tan(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return Math.tan(decimalValue.doubleValue());
   }

   @ActionMethod(name = "求余切")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number cot(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return 1.0 / Math.tan(decimalValue.doubleValue());
   }

   @ActionMethod(name = "求e为底的对数")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number log(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return Math.log(decimalValue.doubleValue());
   }

   @ActionMethod(name = "求10为底的对数")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number log10(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return Math.log10(decimalValue.doubleValue());
   }

   @ActionMethod(name = "向下取整")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number floor(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return new BigDecimal(Math.floor(decimalValue.doubleValue())).stripTrailingZeros();
   }

   @ActionMethod(name = "向上取整")
   @ActionMethodParameter(names = "数字", enames = "number")
   public Number round(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return new BigDecimal(Math.round(decimalValue.doubleValue())).stripTrailingZeros();
   }

   /**一个为了与老版本兼容的方法，老版本中误将“向上取整”写成“四舍五入”，为了让老版本用户不产生错误，这里保留一个方法*/
   public Number halfUp(Object obj) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return new BigDecimal(Math.round(decimalValue.doubleValue())).stripTrailingZeros();
   }

   @ActionMethod(name = "四舍五入")
   @ActionMethodParameter(names = {"数字", "小数位数"}, enames = {"number", "scale"})
   public Number halfUp(Object obj, int scale) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return decimalValue.setScale(scale, RoundingMode.HALF_UP);
   }

   @ActionMethod(name = "求指数")
   @ActionMethodParameter(names = {"底数", "幂"}, enames = {"number", "n"})
   public Double exp(Object obj, double doubleValue) {
      BigDecimal decimalValue = Utils.toBigDecimal(obj);
      return Math.pow(decimalValue.doubleValue(), doubleValue);
   }
}
