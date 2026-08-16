package com.bstek.urule.builder.table;

import com.bstek.urule.model.crosstab.CrossCell;
import com.bstek.urule.model.crosstab.ValueCrossCell;
import com.bstek.urule.model.library.Datatype;
import java.util.ArrayList;
import java.util.List;

class CellRange {
   private int a;
   private int b;
   private boolean c;
   private String d;
   private String e;
   private String f;
   private String g;
   private String h;
   private String i;
   private String j;
   private Datatype k;
   private String l;
   private String m;
   private Datatype n;
   private String o;
   private String p;
   private String q;
   private String r;
   private String s;
   private CellRange t;
   private CrossCell u;
   private List<CellRange> v = new ArrayList<>();

   public String getKeyName() {
      return this.d;
   }

   public void setKeyName(String var1) {
      this.d = var1;
   }

   public String getKeyLabel() {
      return this.e;
   }

   public void setKeyLabel(String var1) {
      this.e = var1;
   }

   public int getStart() {
      return this.a;
   }

   public void setStart(int var1) {
      this.a = var1;
   }

   public int getEnd() {
      return this.b;
   }

   public void setEnd(int var1) {
      this.b = var1;
   }

   public String getVariableCategory() {
      return this.f;
   }

   public void setVariableCategory(String var1) {
      this.f = var1;
   }

   public String getVariableName() {
      return this.g;
   }

   public void setVariableName(String var1) {
      this.g = var1;
   }

   public String getVariableLabel() {
      return this.h;
   }

   public void setVariableLabel(String var1) {
      this.h = var1;
   }

   public String getCategoryUuid() {
      return this.i;
   }

   public void setCategoryUuid(String var1) {
      this.i = var1;
   }

   public String getUuid() {
      return this.j;
   }

   public void setUuid(String var1) {
      this.j = var1;
   }

   public Datatype getDatatype() {
      return this.k;
   }

   public void setDatatype(Datatype var1) {
      this.k = var1;
   }

   public String getPredefineUuid() {
      return this.l;
   }

   public void setPredefineUuid(String var1) {
      this.l = var1;
   }

   public String getPredefineName() {
      return this.m;
   }

   public void setPredefineName(String var1) {
      this.m = var1;
   }

   public Datatype getPredefineDatatype() {
      return this.n;
   }

   public void setPredefineDatatype(Datatype var1) {
      this.n = var1;
   }

   public String getPredefineVariableCategory() {
      return this.o;
   }

   public void setPredefineVariableCategory(String var1) {
      this.o = var1;
   }

   public String getPredefineVariableCategoryUuid() {
      return this.p;
   }

   public void setPredefineVariableCategoryUuid(String var1) {
      this.p = var1;
   }

   public String getPredefinePropertyName() {
      return this.q;
   }

   public void setPredefinePropertyName(String var1) {
      this.q = var1;
   }

   public String getPredefinePropertyLabel() {
      return this.r;
   }

   public void setPredefinePropertyLabel(String var1) {
      this.r = var1;
   }

   public String getPredefinePropertyUuid() {
      return this.s;
   }

   public void setPredefinePropertyUuid(String var1) {
      this.s = var1;
   }

   public boolean isValueCell() {
      return this.c;
   }

   public void setValueCell(boolean var1) {
      this.c = var1;
   }

   public CellRange getParentRange() {
      return this.t;
   }

   public void setParentRange(CellRange var1) {
      this.t = var1;
   }

   public CrossCell getCell() {
      return this.u;
   }

   public void setCell(CrossCell var1) {
      if (var1 instanceof ValueCrossCell) {
         this.setValueCell(true);
      }

      this.u = var1;
   }

   public List<CellRange> getChildren() {
      return this.v;
   }

   public void addChildRange(CellRange var1) {
      var1.setParentRange(this);
      this.v.add(var1);
   }
}
