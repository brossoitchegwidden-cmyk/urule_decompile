package com.bstek.urule.model.function;

import com.bstek.urule.runtime.WorkingMemory;

public interface FunctionDescriptor {
   /**通过定义一个Argument实例来定义函数所采用的参数，该方法必须要返回一个Argument对象，不能返回null。*/
   Argument getArgument();

   /**函数运行时要执行的方法*/
   Object doFunction(Object object, String property, WorkingMemory workingMemory);

   /**返回函数名，建议返回一个首字母大写的英文名，该名称不能与既有函数同名*/
   String getName();

   /**返回函数显示名称，该名称不能与既有函数Label同名*/
   String getLabel();

   boolean isDisabled();
}
