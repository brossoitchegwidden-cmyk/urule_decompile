package com.bstek.urule.model.scorecard;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Predefine;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScorecardDefinition {
   private String name;
   private Integer salience;
   private Date effectiveDate;
   private Date expiresDate;
   private Boolean enabled;
   private Boolean debug;
   private String attributeColName;
   private String attributeColWidth;
   private String attributeColVariableCategory;
   private String attributeColVariableCategoryUuid;
   private String conditionColName;
   private String conditionColWidth;
   private String scoreColName;
   private String scoreColWidth;
   private boolean weightSupport;
   private ScoringType scoringType;
   private String scoringBean;
   private AssignTargetType assignTargetType;
   private String remark;
   private String quickTestData;
   private String keyLabel;
   private String keyName;
   private String variableCategory;
   private String variableName;
   private String variableLabel;
   private Datatype datatype;
   private String categoryUuid;
   private String uuid;
   private String keyCategoryUuid;
   private String keyUuid;
   private List<CardCell> cells;
   private List<CustomCol> customCols;
   private List<AttributeRow> rows;
   private List<Library> libraries;
   private List<Predefine> predefines;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

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

   public String getAttributeColName() {
      return this.attributeColName;
   }

   public void setAttributeColName(String attributeColName) {
      this.attributeColName = attributeColName;
   }

   public String getAttributeColWidth() {
      return this.attributeColWidth;
   }

   public void setAttributeColWidth(String attributeColWidth) {
      this.attributeColWidth = attributeColWidth;
   }

   public String getAttributeColVariableCategory() {
      return this.attributeColVariableCategory;
   }

   public void setAttributeColVariableCategory(String attributeColVariableCategory) {
      this.attributeColVariableCategory = attributeColVariableCategory;
   }

   public String getAttributeColVariableCategoryUuid() {
      return this.attributeColVariableCategoryUuid;
   }

   public void setAttributeColVariableCategoryUuid(String attributeColVariableCategoryUuid) {
      this.attributeColVariableCategoryUuid = attributeColVariableCategoryUuid;
   }

   public String getConditionColName() {
      return this.conditionColName;
   }

   public void setConditionColName(String conditionColName) {
      this.conditionColName = conditionColName;
   }

   public String getConditionColWidth() {
      return this.conditionColWidth;
   }

   public void setConditionColWidth(String conditionColWidth) {
      this.conditionColWidth = conditionColWidth;
   }

   public String getScoreColName() {
      return this.scoreColName;
   }

   public void setScoreColName(String scoreColName) {
      this.scoreColName = scoreColName;
   }

   public String getScoreColWidth() {
      return this.scoreColWidth;
   }

   public void setScoreColWidth(String scoreColWidth) {
      this.scoreColWidth = scoreColWidth;
   }

   public boolean isWeightSupport() {
      return this.weightSupport;
   }

   public void setWeightSupport(boolean weightSupport) {
      this.weightSupport = weightSupport;
   }

   public ScoringType getScoringType() {
      return this.scoringType;
   }

   public void setScoringType(ScoringType scoringType) {
      this.scoringType = scoringType;
   }

   public String getScoringBean() {
      return this.scoringBean;
   }

   public void setScoringBean(String scoringBean) {
      this.scoringBean = scoringBean;
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

   public AssignTargetType getAssignTargetType() {
      return this.assignTargetType;
   }

   public void setAssignTargetType(AssignTargetType assignTargetType) {
      this.assignTargetType = assignTargetType;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String variableName) {
      this.variableName = variableName;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String variableLabel) {
      this.variableLabel = variableLabel;
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

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   public List<CustomCol> getCustomCols() {
      return this.customCols;
   }

   public void setCustomCols(List<CustomCol> customCols) {
      this.customCols = customCols;
   }

   public List<AttributeRow> getRows() {
      return this.rows;
   }

   public void setRows(List<AttributeRow> rows) {
      this.rows = rows;
   }

   public List<Predefine> getPredefines() {
      return this.predefines;
   }

   public void setPredefines(List<Predefine> predefines) {
      this.predefines = predefines;
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

   public List<CardCell> getCells() {
      return this.cells;
   }

   public void setCells(List<CardCell> cells) {
      this.cells = cells;
   }
}
