package com.bstek.urule.model.table;

import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTable {
   private Integer salience;
   private String mutexGroup;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   private String remark;
   private String quickTestData;
   private List<Row> rows;
   private List<Column> columns;
   private Map<String, Cell> cellMap;
   private List<Library> libraries;
   private PredefineGroupDefinition predefineGroup;

   public Integer getSalience() {
      return this.salience;
   }

   public void setSalience(Integer salience) {
      this.salience = salience;
   }

   public String getMutexGroup() {
      return this.mutexGroup;
   }

   public void setMutexGroup(String mutexGroup) {
      this.mutexGroup = mutexGroup;
   }

   public Date getEffectiveDate() {
      return this.effectiveDate;
   }

   public void setEffectiveDate(Date effectiveDate) {
      this.effectiveDate = effectiveDate;
   }

   public Date getExpiresDate() {
      return this.expiresDate;
   }

   public void setExpiresDate(Date expiresDate) {
      this.expiresDate = expiresDate;
   }

   public Boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
   }

   public Boolean getDebug() {
      return this.debug;
   }

   public void setDebug(Boolean debug) {
      this.debug = debug;
   }

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public String getQuickTestData() {
      return this.quickTestData;
   }

   public void setQuickTestData(String quickTestData) {
      this.quickTestData = quickTestData;
   }

   public List<Row> getRows() {
      return this.rows;
   }

   public void addLibrary(Library library) {
      if (this.libraries == null) {
         this.libraries = new ArrayList<>();
      }

      this.libraries.add(library);
   }

   public void addRow(Row row) {
      if (this.rows == null) {
         this.rows = new ArrayList<>();
      }

      this.rows.add(row);
   }

   public void addColumn(Column col) {
      if (this.columns == null) {
         this.columns = new ArrayList<>();
      }

      this.columns.add(col);
   }

   public void addCell(Cell cell) {
      if (this.cellMap == null) {
         this.cellMap = new HashMap<>();
      }

      this.cellMap.put(this.buildCellKey(cell.getRow(), cell.getCol()), cell);
   }

   public void setRows(List<Row> rows) {
      this.rows = rows;
   }

   public List<Column> getColumns() {
      return this.columns;
   }

   public void setColumns(List<Column> columns) {
      this.columns = columns;
   }

   public Map<String, Cell> getCellMap() {
      return this.cellMap;
   }

   public PredefineGroupDefinition getPredefineGroup() {
      return this.predefineGroup;
   }

   public void setPredefineGroup(PredefineGroupDefinition predefineGroup) {
      this.predefineGroup = predefineGroup;
   }

   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> libraries) {
      this.libraries = libraries;
   }

   public String buildCellKey(int row, int col) {
      return row + "," + col;
   }
}
