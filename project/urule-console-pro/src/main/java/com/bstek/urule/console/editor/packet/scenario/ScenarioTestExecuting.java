package com.bstek.urule.console.editor.packet.scenario;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.manager.packet.scenario.ScenarioManager;
import com.bstek.urule.console.editor.execute.JsonBuilder;
import com.bstek.urule.console.editor.execute.VariableCategoryNotFoundException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.assertor.AssertorEvaluator;
import com.bstek.urule.runtime.log.LogManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Loads scenario cases from the Excel workbook, executes them, and compares
 * actual fact values with the configured expectations.
 */
public class ScenarioTestExecuting {
   private AssertorEvaluator assertorEvaluator = (AssertorEvaluator)Utils.getApplicationContext().getBean("urule.assertorEvaluator");

   public ResultWrapper doTest(TestScenario scenario, KnowledgePackage knowledgePackage, Map variableCategoriesMap, boolean logEnable) throws Exception {
      Long longValue = System.currentTimeMillis();
      Map<String, ScenarioData> scenariosById = this.loadScenarioData(scenario);
      KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
      Long longValue2 = System.currentTimeMillis();
      ResultWrapper resultWrapper = new ResultWrapper();
      resultWrapper.setPrepareTime(longValue2 - longValue);
      ArrayList items = new ArrayList();
      resultWrapper.setResultList(items);
      long longValue3 = System.currentTimeMillis();

      for(ScenarioData scenarioData : scenariosById.values()) {
         List input = scenarioData.getInput();
         List<GeneralEntity> inputEntities = this.buildInputEntities(input, variableCategoriesMap);
         Map<String, Object> parameters = new HashMap<>();
         long executionStartTime = System.currentTimeMillis();

         for(GeneralEntity generalEntity : inputEntities) {
            if (!generalEntity.getTargetClass().equals("参数") && !generalEntity.getTargetClass().equals(HashMap.class.getName())) {
               knowledgeSession.insert(generalEntity);
            } else {
               parameters.putAll(generalEntity);
            }
         }

         Map flowMap = knowledgePackage.getFlowMap();
         if (flowMap != null && flowMap.size() > 0) {
            String id = ((FlowDefinition)flowMap.values().iterator().next()).getId();
            if (parameters.size() > 0) {
               knowledgeSession.startProcess(id, parameters);
            } else {
               knowledgeSession.startProcess(id);
            }
         } else if (parameters.size() > 0) {
            knowledgeSession.fireRules(parameters);
         } else {
            knowledgeSession.fireRules();
         }

         long executionEndTime = System.currentTimeMillis();
         long elapsedTime = executionEndTime - executionStartTime;
         TestResult testResult = new TestResult();
         items.add(testResult);
         testResult.setScenarioId(scenarioData.getScenarioId());
         testResult.setScenarioDesc(scenarioData.getScenarioDesc());
         testResult.setConsumeTime(elapsedTime);
         testResult.setInputData(scenarioData.getInput().toString());
         testResult.setOutputData(scenarioData.getOutput() != null ? scenarioData.getOutput().toString() : null);
         if (logEnable) {
            LogManager logManager = knowledgeSession.getLogManager();
            testResult.addLogs(logManager.getLogger().getLogs());
         }

         List<ValueCompare> valueCompares = this.compareOutputs(scenarioData.getOutput(), inputEntities, variableCategoriesMap, knowledgeSession.getParameters());
         testResult.setValueCompares(valueCompares);
      }

      long longValue4 = System.currentTimeMillis();
      resultWrapper.setTotalTime(longValue4 - longValue3);
      return resultWrapper;
   }

   private List<ValueCompare> compareOutputs(List<DataObject> outputObjects, List<GeneralEntity> inputEntities, Map<String, VariableCategory> categoriesByName, Map<String, Object> parameters) {
      List<ValueCompare> valueCompares = new ArrayList<>();
      if (outputObjects == null) {
         return valueCompares;
      } else {
         for(DataObject dataObject : outputObjects) {
            String name = dataObject.getName();
            Object actualObject = null;
            if (name.equals("参数")) {
               actualObject = parameters;
            } else {
               VariableCategory variableCategory = categoriesByName.get(name);
               if (variableCategory == null) {
                  throw new VariableCategoryNotFoundException("变量对象【" + name + "】未定义!");
               }

               String clazz = variableCategory.getClazz();
               actualObject = this.findEntity(inputEntities, clazz);
            }

            for(ObjectField objectField : (Iterable<ObjectField>)(Iterable<?>)(dataObject.getFields())) {
               String fieldName = objectField.getName();
               Object actualValue = Utils.getObjectProperty(actualObject, fieldName);
               String expectedValue = objectField.getValue();
               boolean matched = this.assertorEvaluator.evaluate(actualValue, expectedValue, objectField.getDatatype(), objectField.getOp());
               ValueCompare valueCompare = new ValueCompare();
               valueCompare.setMatched(matched);
               valueCompare.setCategory(name);
               valueCompare.setFieldName(objectField.getLabel());
               valueCompare.setData(this.formatValue(actualValue));
               valueCompare.setExpectedData(this.formatValue(expectedValue));
               valueCompare.setOp(objectField.getOp().toString());
               valueCompares.add(valueCompare);
            }
         }

         return valueCompares;
      }
   }

   private Object formatValue(Object objectValue) {
      if (objectValue == null) {
         return objectValue;
      } else if (objectValue instanceof Number) {
         BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
         String text = decimalValue.stripTrailingZeros().toPlainString();
         return text;
      } else if (objectValue instanceof String) {
         return objectValue;
      } else {
         ObjectMapper objectMapper = JsonMapper.builder().build();

         try {
            return objectMapper.writeValueAsString(objectValue);
         } catch (Exception exception) {
            throw new RuleException(exception);
         }
      }
   }

   private GeneralEntity findEntity(List<GeneralEntity> entities, String className) {
      for(GeneralEntity generalEntity : entities) {
         if (generalEntity.getTargetClass().equals(className)) {
            return generalEntity;
         }
      }

      throw new RuleException("对象类【" + className + "】不存在");
   }

   private List<GeneralEntity> buildInputEntities(List<DataObject> inputObjects, Map<String, VariableCategory> categoriesByName) throws Exception {
      List<GeneralEntity> entities = new ArrayList<>();

      for(DataObject dataObject : inputObjects) {
         VariableCategory variableCategory = categoriesByName.get(dataObject.getName());
         if (variableCategory == null) {
            throw new VariableCategoryNotFoundException("变量对象【" + dataObject.getName() + "】未定义!");
         }

         GeneralEntity generalEntity = new GeneralEntity(variableCategory.getClazz());
         entities.add(generalEntity);

         for(ObjectField objectField : (Iterable<ObjectField>)(Iterable<?>)(dataObject.getFields())) {
            String defaultValue = objectField.getValue();
            Variable variable = JsonBuilder.getInstance().findVariable(variableCategory, objectField.getLabel());
            if (defaultValue == null) {
               defaultValue = variable.getDefaultValue();
            }

            if (defaultValue != null) {
               Datatype type = variable.getType();
               Object complexObject = null;
               if (!type.equals(Datatype.Object) && !type.equals(Datatype.List) && !type.equals(Datatype.Set)) {
                  complexObject = type.convert(defaultValue);
               } else {
                  complexObject = JsonBuilder.getInstance().buildComplexObject(defaultValue, categoriesByName);
               }

               Object objectProperty2 = generalEntity;
               String[] parts = variable.getName().split("\\.");

               for(int index = 0; index < parts.length; ++index) {
                  String text = parts[index];
                  if (index == parts.length - 1) {
                     Utils.setObjectProperty(objectProperty2, text, complexObject);
                     break;
                  }

                  Object objectProperty = Utils.getObjectProperty(objectProperty2, text);
                  if (objectProperty == null) {
                     objectProperty = new HashMap();
                     Utils.setObjectProperty(objectProperty2, text, objectProperty);
                  }

                  objectProperty2 = objectProperty;
               }
            }
         }
      }

      for(VariableCategory variableCategory : categoriesByName.values()) {
         if (!variableCategory.getName().equals("参数")) {
            boolean found = false;

            for(GeneralEntity entity : entities) {
               if (entity.getTargetClass().equals(variableCategory.getClazz())) {
                  found = true;
                  break;
               }
            }

            if (!found) {
               GeneralEntity entity = new GeneralEntity(variableCategory.getClazz());
               entities.add(entity);
            }
         }
      }

      return entities;
   }

   private Map<String, ScenarioData> loadScenarioData(TestScenario testScenario) throws Exception {
      byte[] excelFile = ScenarioManager.ins.loadExcelFile(testScenario.getId());
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelFile);
      XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(byteArrayInputStream);
      Map<String, ScenarioData> scenariosById = this.parseInputSheet(testScenario, xSSFWorkbook.getSheetAt(0));
      this.parseOutputSheet(testScenario, scenariosById, xSSFWorkbook.getSheetAt(1));
      xSSFWorkbook.close();
      byteArrayInputStream.close();
      return scenariosById;
   }

   private Map<String, ScenarioData> parseInputSheet(TestScenario testScenario, XSSFSheet xSSFSheet) {
      List inputData = testScenario.getInputData();
      XSSFRow row = xSSFSheet.getRow(0);
      List mergedRegions = xSSFSheet.getMergedRegions();
      int number = 2;
      ArrayList items = new ArrayList();

      for(Cell cell = row.getCell(number); cell != null; cell = row.getCell(number)) {
         String text = this.getCellValue(cell);
         if (text == null) {
            throw new RuleException("1行" + (number + 1) + "列单元格内容不能为空");
         }

         SimulateData simulateData = this.findSimulateData(inputData, text);
         if (simulateData == null) {
            throw new RuleException("输入数据对象第1行第" + (number + 1) + "列值为\"" + text + "\"在配置中未定义!");
         }

         SimulateData simulateData2 = new SimulateData();
         simulateData2.setName(text);
         simulateData2.setFields(simulateData.getFields());
         items.add(simulateData2);
         number += this.getColumnSpan(mergedRegions, 0, number);
      }

      number = 2;
      XSSFRow row2 = xSSFSheet.getRow(1);

      for(SimulateData simulateData3 : (Iterable<SimulateData>)(Iterable<?>)(items)) {
         List fields = simulateData3.getFields();
         ArrayList items2 = new ArrayList();

         for(int index = 0; index < fields.size(); ++index) {
            Cell cell2 = row2.getCell(number);
            String cellValue = this.getCellValue(cell2);
            if (cellValue != null) {
               DataField dataField = this.findDataField(fields, cellValue, false);
               if (dataField == null) {
                  throw new RuleException("输入数据对象第2行第" + (number + 1) + "列值为\"" + cellValue + "\"在配置对象\"" + simulateData3.getName() + "\"中不存在!");
               }

               items2.add(dataField);
               ++number;
            }
         }

         simulateData3.setFields(items2);
      }

      LinkedHashMap valuesByKey = new LinkedHashMap();
      int lastRowNum = xSSFSheet.getLastRowNum();

      for(int index2 = 2; index2 <= lastRowNum; ++index2) {
         XSSFRow row3 = xSSFSheet.getRow(index2);
         if (row3 != null) {
            Cell cell3 = row3.getCell(0);
            String cellValue2 = this.getCellValue(cell3);
            if (!StringUtils.isBlank(cellValue2)) {
               Cell cell4 = row3.getCell(1);
               String cellValue3 = this.getCellValue(cell4);
               if (valuesByKey.containsKey(cellValue2)) {
                  throw new RuleException("场景数据定义中场景ID【" + cellValue2 + "】存在重复值");
               }

               ScenarioData scenarioData = new ScenarioData();
               valuesByKey.put(cellValue2, scenarioData);
               scenarioData.setScenarioId(cellValue2);
               scenarioData.setScenarioDesc(cellValue3);
               ArrayList items3 = new ArrayList();
               scenarioData.setInput(items3);
               number = 2;

               for(SimulateData simulateData4 : (Iterable<SimulateData>)(Iterable<?>)(items)) {
                  DataObject dataObject = new DataObject();
                  dataObject.setName(simulateData4.getName());
                  items3.add(dataObject);
                  ArrayList items4 = new ArrayList();
                  dataObject.setFields(items4);

                  for(DataField dataField2 : (Iterable<DataField>)(Iterable<?>)(simulateData4.getFields())) {
                     ObjectField objectField = new ObjectField();
                     items4.add(objectField);
                     objectField.setLabel(dataField2.getLabel());
                     objectField.setName(dataField2.getName());
                     objectField.setOp(dataField2.getOp());
                     objectField.setDatatype(dataField2.getDatatype());
                     Cell cell5 = row3.getCell(number);
                     String cellValue4 = this.getCellValue(cell5);
                     objectField.setValue(cellValue4);
                     ++number;
                  }
               }
            }
         }
      }

      return valuesByKey;
   }

   private DataField findDataField(List<DataField> fields, String columnHeader, boolean includeOperator) {
      for(DataField dataField : fields) {
         if (includeOperator) {
            String text = "\"" + dataField.getLabel() + "\"" + dataField.getOp();
            if (text.equals(columnHeader)) {
               return dataField;
            }
         } else if (dataField.getLabel().equals(columnHeader)) {
            return dataField;
         }
      }

      return null;
   }

   private SimulateData findSimulateData(List<SimulateData> definitions, String name) {
      for(SimulateData simulateData : definitions) {
         if (name.equals(simulateData.getName())) {
            return simulateData;
         }
      }

      return null;
   }

   private String getCellValue(Cell cell) {
      if (cell == null) {
         return null;
      } else {
         CellType cellTypeEnum = cell.getCellTypeEnum();
         switch (cellTypeEnum) {
            case _NONE:
               return null;
            case BLANK:
               return null;
            case BOOLEAN:
               return String.valueOf(cell.getBooleanCellValue());
            case ERROR:
               return null;
            case FORMULA:
               return String.valueOf(cell.getCellFormula());
            case NUMERIC:
               return this.getNumericCellValue(cell);
            case STRING:
               return cell.getStringCellValue();
            default:
               return null;
         }
      }
   }

   private String getNumericCellValue(Cell cell) {
      if (DateUtil.isCellDateFormatted(cell)) {
         Date dateCellValue = cell.getDateCellValue();
         if (dateCellValue != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(dateCellValue);
         } else {
            return null;
         }
      } else {
         return String.valueOf(cell.getNumericCellValue());
      }
   }

   private void parseOutputSheet(TestScenario testScenario, Map<String, ScenarioData> scenariosById, XSSFSheet xSSFSheet) {
      List outputData = testScenario.getOutputData();
      XSSFRow row = xSSFSheet.getRow(0);
      List mergedRegions = xSSFSheet.getMergedRegions();
      int number = 1;
      ArrayList items = new ArrayList();

      for(int index = 0; index < outputData.size(); ++index) {
         Cell cell = row.getCell(number);
         if (cell == null) {
            throw new RuleException("[预期结果]中第2行" + (number + 1) + "列单元格不存在!");
         }

         String text = this.getCellValue(cell);
         if (text == null) {
            throw new RuleException("1行" + (number + 1) + "列单元格内容不能为空");
         }

         SimulateData simulateData = this.findSimulateData(outputData, text);
         if (simulateData == null) {
            throw new RuleException("[预期结果]中第1行" + (number + 1) + "列值为\"" + text + "\"与配置中未定义!");
         }

         SimulateData simulateData2 = new SimulateData();
         simulateData2.setName(text);
         simulateData2.setFields(simulateData.getFields());
         items.add(simulateData2);
         number += this.getColumnSpan(mergedRegions, 0, number);
      }

      number = 1;
      XSSFRow row2 = xSSFSheet.getRow(1);

      for(SimulateData simulateData3 : (Iterable<SimulateData>)(Iterable<?>)(items)) {
         List fields = simulateData3.getFields();
         ArrayList items2 = new ArrayList();

         for(int index2 = 0; index2 < fields.size(); ++index2) {
            Cell cell2 = row2.getCell(number);
            if (cell2 == null) {
               throw new RuleException("[预期结果]中第2行" + (number + 1) + "列单元格不存在!");
            }

            String cellValue = this.getCellValue(cell2);
            DataField dataField = this.findDataField(fields, cellValue, true);
            if (dataField == null) {
               throw new RuleException("[预期结果]中第2行" + (number + 1) + "列值为\"" + cellValue + "\"在配置对象\"" + simulateData3.getName() + "\"中不存在!");
            }

            items2.add(dataField);
            ++number;
         }

         simulateData3.setFields(items2);
      }

      int lastRowNum = xSSFSheet.getLastRowNum();

      for(int index3 = 2; index3 <= lastRowNum; ++index3) {
         XSSFRow row3 = xSSFSheet.getRow(index3);
         Cell cell3 = row3.getCell(0);
         if (cell3 == null) {
            throw new RuleException("[预期结果]中第" + (index3 + 1) + "行1列单元格不存在，此单元格应该定义对应的场景ID，请检查您的Excel.");
         }

         String cellValue2 = this.getCellValue(cell3);
         if (!StringUtils.isBlank(cellValue2)) {
            if (!scenariosById.containsKey(cellValue2)) {
               throw new RuleException("[预期结果]中使用的场景ID【" + cellValue2 + "】在场景数据中未定义，请检查您的Excel.");
            }

            ScenarioData scenarioData = scenariosById.get(cellValue2);
            ArrayList items3 = new ArrayList();
            scenarioData.setOutput(items3);
            number = 1;

            for(SimulateData simulateData4 : (Iterable<SimulateData>)(Iterable<?>)(items)) {
               DataObject dataObject = new DataObject();
               items3.add(dataObject);
               dataObject.setName(simulateData4.getName());
               ArrayList items4 = new ArrayList();
               dataObject.setFields(items4);

               for(DataField dataField2 : (Iterable<DataField>)(Iterable<?>)(simulateData4.getFields())) {
                  ObjectField objectField = new ObjectField();
                  items4.add(objectField);
                  objectField.setLabel(dataField2.getLabel());
                  objectField.setName(dataField2.getName());
                  objectField.setOp(dataField2.getOp());
                  objectField.setDatatype(dataField2.getDatatype());
                  Cell cell4 = row3.getCell(number);
                  if (cell4 == null) {
                     throw new RuleException("[预期结果]中第" + (index3 + 1) + "行" + (number + 1) + "列单元格不存在，请检查您的Excel.");
                  }

                  String cellValue3 = this.getCellValue(cell4);
                  objectField.setValue(cellValue3);
                  ++number;
               }
            }
         }
      }

   }

   private int getColumnSpan(List<CellRangeAddress> mergedRegions, int rowIndex, int columnIndex) {
      for(CellRangeAddress cellRangeAddress : mergedRegions) {
         if (cellRangeAddress.getFirstRow() == rowIndex && cellRangeAddress.getFirstColumn() == columnIndex) {
            return cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1;
         }
      }

      return 1;
   }
}
