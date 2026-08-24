package com.bstek.urule.model.crosstab;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrosstabDefinition {
   private Integer salience;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   private String remark;
   private String quickTestData;
   private String assignTargetType;
   private String assignVariableCategory;
   private String assignVariable;
   private String assignVariableLabel;
   private Datatype assignDatatype;
   private String keyLabel;
   private String keyName;
   private String keyCategoryUuid;
   private String keyUuid;
   private String categoryUuid;
   private String uuid;
   private HeaderCell headerCell;
   private List<CrossCell> cells;
   private List<CrossRow> rows;
   private List<CrossColumn> columns;
   private List<Library> libraries;
   private PredefineGroupDefinition predefineGroup;

   public Integer getSalience() {
      return this.salience;
   }

   public void setSalience(Integer salience) {
      this.salience = salience;
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

   public String getQuickTestData() {
      return this.quickTestData;
   }

   public void setQuickTestData(String quickTestData) {
      this.quickTestData = quickTestData;
   }

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String keyLabel) {
      this.keyLabel = keyLabel;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String keyCategoryUuid) {
      this.keyCategoryUuid = keyCategoryUuid;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String keyUuid) {
      this.keyUuid = keyUuid;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String categoryUuid) {
      this.categoryUuid = categoryUuid;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getAssignTargetType() {
      return this.assignTargetType;
   }

   public void setAssignTargetType(String assignTargetType) {
      this.assignTargetType = assignTargetType;
   }

   public String getAssignVariableCategory() {
      return this.assignVariableCategory;
   }

   public void setAssignVariableCategory(String assignVariableCategory) {
      this.assignVariableCategory = assignVariableCategory;
   }

   public String getAssignVariable() {
      return this.assignVariable;
   }

   public void setAssignVariable(String assignVariable) {
      this.assignVariable = assignVariable;
   }

   public String getAssignVariableLabel() {
      return this.assignVariableLabel;
   }

   public void setAssignVariableLabel(String assignVariableLabel) {
      this.assignVariableLabel = assignVariableLabel;
   }

   public Datatype getAssignDatatype() {
      return this.assignDatatype;
   }

   public void setAssignDatatype(Datatype assignDatatype) {
      this.assignDatatype = assignDatatype;
   }

   public HeaderCell getHeaderCell() {
      return this.headerCell;
   }

   public void setHeaderCell(HeaderCell headerCell) {
      this.headerCell = headerCell;
   }

   public List<CrossCell> getCells() {
      return this.cells;
   }

   public void setCells(List<CrossCell> cells) {
      this.cells = cells;
   }

   public List<CrossRow> getRows() {
      return this.rows;
   }

   public void setRows(List<CrossRow> rows) {
      this.rows = rows;
   }

   public List<CrossColumn> getColumns() {
      return this.columns;
   }

   public void setColumns(List<CrossColumn> columns) {
      this.columns = columns;
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

   public void addLibrary(Library library) {
      if (this.libraries == null) {
         this.libraries = new ArrayList<>();
      }

      this.libraries.add(library);
   }
}
