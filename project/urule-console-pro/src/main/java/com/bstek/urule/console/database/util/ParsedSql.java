package com.bstek.urule.console.database.util;

import java.util.ArrayList;
import java.util.List;

public class ParsedSql {
   private String originalSql;
   private List parameterNames = new ArrayList();
   private List parameterIndexes = new ArrayList();
   private int namedParameterCount;
   private int unnamedParameterCount;
   private int totalParameterCount;

   ParsedSql(String text) {
      this.originalSql = text;
   }

   public String getOriginalSql() {
      return this.originalSql;
   }

   void addNamedParameter(String parameterName, int startIndex, int endIndex) {
      this.parameterNames.add(parameterName);
      this.parameterIndexes.add(new int[]{startIndex, endIndex});
   }

   /**Repeated occurrences of the same parameter name are included here.*/
   public List getParameterNames() {
      return this.parameterNames;
   }

   /**Return the parameter indexes for the specified parameter.*/
   public int[] getParameterIndexes(int parameterPosition) {
      return (int[])this.parameterIndexes.get(parameterPosition);
   }

   void setNamedParameterCount(int count) {
      this.namedParameterCount = count;
   }

   /**Each parameter name counts once; repeated occurrences do not count here.*/
   public int getNamedParameterCount() {
      return this.namedParameterCount;
   }

   void setUnnamedParameterCount(int count) {
      this.unnamedParameterCount = count;
   }

   public int getUnnamedParameterCount() {
      return this.unnamedParameterCount;
   }

   void setTotalParameterCount(int count) {
      this.totalParameterCount = count;
   }

   /**Repeated occurrences of the same parameter name do count here.*/
   public int getTotalParameterCount() {
      return this.totalParameterCount;
   }

   /**Exposes the original SQL String.*/
   public String toString() {
      return this.originalSql;
   }
}
