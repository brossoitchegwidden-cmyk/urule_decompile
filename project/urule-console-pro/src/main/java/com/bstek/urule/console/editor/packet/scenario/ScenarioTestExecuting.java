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

public class ScenarioTestExecuting {
   private AssertorEvaluator a = (AssertorEvaluator)Utils.getApplicationContext().getBean("urule.assertorEvaluator");

   public ResultWrapper doTest(TestScenario var1, KnowledgePackage var2, Map var3, boolean var4) throws Exception {
      Long var5 = System.currentTimeMillis();
      Map var6 = this.a(var1);
      KnowledgeSession var7 = KnowledgeSessionFactory.newKnowledgeSession(var2);
      Long var8 = System.currentTimeMillis();
      ResultWrapper var9 = new ResultWrapper();
      var9.setPrepareTime(var8 - var5);
      ArrayList var10 = new ArrayList();
      var9.setResultList(var10);
      long var11 = System.currentTimeMillis();

      for(ScenarioData var14 : (Iterable<ScenarioData>)(Iterable<?>)(var6.values())) {
         List var15 = var14.getInput();
         List var16 = this.a(var15, var3);
         HashMap var17 = new HashMap();
         long var18 = System.currentTimeMillis();

         for(GeneralEntity var21 : (Iterable<GeneralEntity>)(Iterable<?>)(var16)) {
            if (!var21.getTargetClass().equals("参数") && !var21.getTargetClass().equals(HashMap.class.getName())) {
               var7.insert(var21);
            } else {
               var17.putAll(var21);
            }
         }

         Map var28 = var2.getFlowMap();
         if (var28 != null && var28.size() > 0) {
            String var29 = ((FlowDefinition)var28.values().iterator().next()).getId();
            if (var17.size() > 0) {
               var7.startProcess(var29, var17);
            } else {
               var7.startProcess(var29);
            }
         } else if (var17.size() > 0) {
            var7.fireRules(var17);
         } else {
            var7.fireRules();
         }

         long var30 = System.currentTimeMillis();
         long var23 = var30 - var18;
         TestResult var25 = new TestResult();
         var10.add(var25);
         var25.setScenarioId(var14.getScenarioId());
         var25.setScenarioDesc(var14.getScenarioDesc());
         var25.setConsumeTime(var23);
         var25.setInputData(var14.getInput().toString());
         var25.setOutputData(var14.getOutput() != null ? var14.getOutput().toString() : null);
         if (var4) {
            LogManager var26 = var7.getLogManager();
            var25.addLogs(var26.getLogger().getLogs());
         }

         List var31 = this.a(var14.getOutput(), var16, var3, var7.getParameters());
         var25.setValueCompares(var31);
      }

      long var27 = System.currentTimeMillis();
      var9.setTotalTime(var27 - var11);
      return var9;
   }

   private List a(List var1, List var2, Map var3, Map var4) {
      ArrayList var5 = new ArrayList();
      if (var1 == null) {
         return var5;
      } else {
         for(DataObject var7 : (Iterable<DataObject>)(Iterable<?>)(var1)) {
            String var8 = var7.getName();
            Object var9 = null;
            if (var8.equals("参数")) {
               var9 = var4;
            } else {
               VariableCategory var10 = (VariableCategory)var3.get(var8);
               if (var10 == null) {
                  throw new VariableCategoryNotFoundException("变量对象【" + var8 + "】未定义!");
               }

               String var11 = var10.getClazz();
               var9 = this.a(var2, var11);
            }

            for(ObjectField var19 : (Iterable<ObjectField>)(Iterable<?>)(var7.getFields())) {
               String var12 = var19.getName();
               Object var13 = Utils.getObjectProperty(var9, var12);
               String var14 = var19.getValue();
               boolean var15 = this.a.evaluate(var13, var14, var19.getDatatype(), var19.getOp());
               ValueCompare var16 = new ValueCompare();
               var16.setMatched(var15);
               var16.setCategory(var8);
               var16.setFieldName(var19.getLabel());
               var16.setData(this.a(var13));
               var16.setExpectedData(this.a((Object)var14));
               var16.setOp(var19.getOp().toString());
               var5.add(var16);
            }
         }

         return var5;
      }
   }

   private Object a(Object var1) {
      if (var1 == null) {
         return var1;
      } else if (var1 instanceof Number) {
         BigDecimal var5 = Utils.toBigDecimal(var1);
         String var3 = var5.stripTrailingZeros().toPlainString();
         return var3;
      } else if (var1 instanceof String) {
         return var1;
      } else {
         ObjectMapper var2 = JsonMapper.builder().build();

         try {
            return var2.writeValueAsString(var1);
         } catch (Exception var4) {
            throw new RuleException(var4);
         }
      }
   }

   private GeneralEntity a(List var1, String var2) {
      for(GeneralEntity var4 : (Iterable<GeneralEntity>)(Iterable<?>)(var1)) {
         if (var4.getTargetClass().equals(var2)) {
            return var4;
         }
      }

      throw new RuleException("对象类【" + var2 + "】不存在");
   }

   private List a(List var1, Map var2) throws Exception {
      ArrayList var3 = new ArrayList();

      for(DataObject var5 : (Iterable<DataObject>)(Iterable<?>)(var1)) {
         VariableCategory var6 = (VariableCategory)var2.get(var5.getName());
         if (var6 == null) {
            throw new VariableCategoryNotFoundException("变量对象【" + var5.getName() + "】未定义!");
         }

         GeneralEntity var7 = new GeneralEntity(var6.getClazz());
         var3.add(var7);

         for(ObjectField var9 : (Iterable<ObjectField>)(Iterable<?>)(var5.getFields())) {
            String var10 = var9.getValue();
            Variable var11 = JsonBuilder.getInstance().findVariable(var6, var9.getLabel());
            if (var10 == null) {
               var10 = var11.getDefaultValue();
            }

            if (var10 != null) {
               Datatype var12 = var11.getType();
               Object var13 = null;
               if (!var12.equals(Datatype.Object) && !var12.equals(Datatype.List) && !var12.equals(Datatype.Set)) {
                  var13 = var12.convert(var10);
               } else {
                  var13 = JsonBuilder.getInstance().buildComplexObject(var10, var2);
               }

               Object var14 = var7;
               String[] var15 = var11.getName().split("\\.");

               for(int var16 = 0; var16 < var15.length; ++var16) {
                  String var17 = var15[var16];
                  if (var16 == var15.length - 1) {
                     Utils.setObjectProperty(var14, var17, var13);
                     break;
                  }

                  Object var18 = Utils.getObjectProperty(var14, var17);
                  if (var18 == null) {
                     var18 = new HashMap();
                     Utils.setObjectProperty(var14, var17, var18);
                  }

                  var14 = var18;
               }
            }
         }
      }

      for(VariableCategory var20 : (Iterable<VariableCategory>)(Iterable<?>)(var2.values())) {
         if (!var20.getName().equals("参数")) {
            boolean var21 = false;

            for(GeneralEntity var24 : (Iterable<GeneralEntity>)(Iterable<?>)(var3)) {
               if (var24.getTargetClass().equals(var20.getClazz())) {
                  var21 = true;
                  break;
               }
            }

            if (!var21) {
               GeneralEntity var23 = new GeneralEntity(var20.getClazz());
               var3.add(var23);
            }
         }
      }

      return var3;
   }

   private Map a(TestScenario var1) throws Exception {
      byte[] var2 = ScenarioManager.ins.loadExcelFile(var1.getId());
      ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
      XSSFWorkbook var4 = new XSSFWorkbook(var3);
      Map var5 = this.a(var1, var4.getSheetAt(0));
      this.a(var1, var5, var4.getSheetAt(1));
      var4.close();
      var3.close();
      return var5;
   }

   private Map a(TestScenario var1, XSSFSheet var2) {
      List var3 = var1.getInputData();
      XSSFRow var4 = var2.getRow(0);
      List var5 = var2.getMergedRegions();
      int var6 = 2;
      ArrayList var7 = new ArrayList();

      for(Cell var8 = var4.getCell(var6); var8 != null; var8 = var4.getCell(var6)) {
         String var9 = this.a(var8);
         if (var9 == null) {
            throw new RuleException("1行" + (var6 + 1) + "列单元格内容不能为空");
         }

         SimulateData var10 = this.b(var3, var9);
         if (var10 == null) {
            throw new RuleException("输入数据对象第1行第" + (var6 + 1) + "列值为\"" + var9 + "\"在配置中未定义!");
         }

         SimulateData var11 = new SimulateData();
         var11.setName(var9);
         var11.setFields(var10.getFields());
         var7.add(var11);
         var6 += this.a(var5, 0, var6);
      }

      var6 = 2;
      XSSFRow var32 = var2.getRow(1);

      for(SimulateData var35 : (Iterable<SimulateData>)(Iterable<?>)(var7)) {
         List var12 = var35.getFields();
         ArrayList var13 = new ArrayList();

         for(int var14 = 0; var14 < var12.size(); ++var14) {
            Cell var15 = var32.getCell(var6);
            String var16 = this.a(var15);
            if (var16 != null) {
               DataField var17 = this.a(var12, var16, false);
               if (var17 == null) {
                  throw new RuleException("输入数据对象第2行第" + (var6 + 1) + "列值为\"" + var16 + "\"在配置对象\"" + var35.getName() + "\"中不存在!");
               }

               var13.add(var17);
               ++var6;
            }
         }

         var35.setFields(var13);
      }

      LinkedHashMap var34 = new LinkedHashMap();
      int var36 = var2.getLastRowNum();

      for(int var37 = 2; var37 <= var36; ++var37) {
         XSSFRow var38 = var2.getRow(var37);
         if (var38 != null) {
            Cell var39 = var38.getCell(0);
            String var40 = this.a(var39);
            if (!StringUtils.isBlank(var40)) {
               Cell var41 = var38.getCell(1);
               String var42 = this.a(var41);
               if (var34.containsKey(var40)) {
                  throw new RuleException("场景数据定义中场景ID【" + var40 + "】存在重复值");
               }

               ScenarioData var18 = new ScenarioData();
               var34.put(var40, var18);
               var18.setScenarioId(var40);
               var18.setScenarioDesc(var42);
               ArrayList var19 = new ArrayList();
               var18.setInput(var19);
               var6 = 2;

               for(SimulateData var21 : (Iterable<SimulateData>)(Iterable<?>)(var7)) {
                  DataObject var22 = new DataObject();
                  var22.setName(var21.getName());
                  var19.add(var22);
                  ArrayList var23 = new ArrayList();
                  var22.setFields(var23);

                  for(DataField var26 : (Iterable<DataField>)(Iterable<?>)(var21.getFields())) {
                     ObjectField var27 = new ObjectField();
                     var23.add(var27);
                     var27.setLabel(var26.getLabel());
                     var27.setName(var26.getName());
                     var27.setOp(var26.getOp());
                     var27.setDatatype(var26.getDatatype());
                     Cell var28 = var38.getCell(var6);
                     String var29 = this.a(var28);
                     var27.setValue(var29);
                     ++var6;
                  }
               }
            }
         }
      }

      return var34;
   }

   private DataField a(List var1, String var2, boolean var3) {
      for(DataField var5 : (Iterable<DataField>)(Iterable<?>)(var1)) {
         if (var3) {
            String var6 = "\"" + var5.getLabel() + "\"" + var5.getOp();
            if (var6.equals(var2)) {
               return var5;
            }
         } else if (var5.getLabel().equals(var2)) {
            return var5;
         }
      }

      return null;
   }

   private SimulateData b(List var1, String var2) {
      for(SimulateData var4 : (Iterable<SimulateData>)(Iterable<?>)(var1)) {
         if (var2.equals(var4.getName())) {
            return var4;
         }
      }

      return null;
   }

   private String a(Cell var1) {
      if (var1 == null) {
         return null;
      } else {
         CellType var2 = var1.getCellTypeEnum();
         switch (var2) {
            case _NONE:
               return null;
            case BLANK:
               return null;
            case BOOLEAN:
               return String.valueOf(var1.getBooleanCellValue());
            case ERROR:
               return null;
            case FORMULA:
               return String.valueOf(var1.getCellFormula());
            case NUMERIC:
               return this.b(var1);
            case STRING:
               return var1.getStringCellValue();
            default:
               return null;
         }
      }
   }

   private String b(Cell var1) {
      if (DateUtil.isCellDateFormatted(var1)) {
         Date var2 = var1.getDateCellValue();
         if (var2 != null) {
            SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return var3.format(var2);
         } else {
            return null;
         }
      } else {
         return String.valueOf(var1.getNumericCellValue());
      }
   }

   private void a(TestScenario var1, Map var2, XSSFSheet var3) {
      List var4 = var1.getOutputData();
      XSSFRow var5 = var3.getRow(0);
      List var6 = var3.getMergedRegions();
      int var7 = 1;
      ArrayList var8 = new ArrayList();

      for(int var9 = 0; var9 < var4.size(); ++var9) {
         Cell var10 = var5.getCell(var7);
         if (var10 == null) {
            throw new RuleException("[预期结果]中第2行" + (var7 + 1) + "列单元格不存在!");
         }

         String var11 = this.a(var10);
         if (var11 == null) {
            throw new RuleException("1行" + (var7 + 1) + "列单元格内容不能为空");
         }

         SimulateData var12 = this.b(var4, var11);
         if (var12 == null) {
            throw new RuleException("[预期结果]中第1行" + (var7 + 1) + "列值为\"" + var11 + "\"与配置中未定义!");
         }

         SimulateData var13 = new SimulateData();
         var13.setName(var11);
         var13.setFields(var12.getFields());
         var8.add(var13);
         var7 += this.a(var6, 0, var7);
      }

      var7 = 1;
      XSSFRow var28 = var3.getRow(1);

      for(SimulateData var31 : (Iterable<SimulateData>)(Iterable<?>)(var8)) {
         List var33 = var31.getFields();
         ArrayList var35 = new ArrayList();

         for(int var14 = 0; var14 < var33.size(); ++var14) {
            Cell var15 = var28.getCell(var7);
            if (var15 == null) {
               throw new RuleException("[预期结果]中第2行" + (var7 + 1) + "列单元格不存在!");
            }

            String var16 = this.a(var15);
            DataField var17 = this.a(var33, var16, true);
            if (var17 == null) {
               throw new RuleException("[预期结果]中第2行" + (var7 + 1) + "列值为\"" + var16 + "\"在配置对象\"" + var31.getName() + "\"中不存在!");
            }

            var35.add(var17);
            ++var7;
         }

         var31.setFields(var35);
      }

      int var30 = var3.getLastRowNum();

      for(int var32 = 2; var32 <= var30; ++var32) {
         XSSFRow var34 = var3.getRow(var32);
         Cell var36 = var34.getCell(0);
         if (var36 == null) {
            throw new RuleException("[预期结果]中第" + (var32 + 1) + "行1列单元格不存在，此单元格应该定义对应的场景ID，请检查您的Excel.");
         }

         String var37 = this.a(var36);
         if (!StringUtils.isBlank(var37)) {
            if (!var2.containsKey(var37)) {
               throw new RuleException("[预期结果]中使用的场景ID【" + var37 + "】在场景数据中未定义，请检查您的Excel.");
            }

            ScenarioData var38 = (ScenarioData)var2.get(var37);
            ArrayList var39 = new ArrayList();
            var38.setOutput(var39);
            var7 = 1;

            for(SimulateData var18 : (Iterable<SimulateData>)(Iterable<?>)(var8)) {
               DataObject var19 = new DataObject();
               var39.add(var19);
               var19.setName(var18.getName());
               ArrayList var20 = new ArrayList();
               var19.setFields(var20);

               for(DataField var22 : (Iterable<DataField>)(Iterable<?>)(var18.getFields())) {
                  ObjectField var23 = new ObjectField();
                  var20.add(var23);
                  var23.setLabel(var22.getLabel());
                  var23.setName(var22.getName());
                  var23.setOp(var22.getOp());
                  var23.setDatatype(var22.getDatatype());
                  Cell var24 = var34.getCell(var7);
                  if (var24 == null) {
                     throw new RuleException("[预期结果]中第" + (var32 + 1) + "行" + (var7 + 1) + "列单元格不存在，请检查您的Excel.");
                  }

                  String var25 = this.a(var24);
                  var23.setValue(var25);
                  ++var7;
               }
            }
         }
      }

   }

   private int a(List var1, int var2, int var3) {
      for(CellRangeAddress var5 : (Iterable<CellRangeAddress>)(Iterable<?>)(var1)) {
         if (var5.getFirstRow() == var2 && var5.getFirstColumn() == var3) {
            return var5.getLastColumn() - var5.getFirstColumn() + 1;
         }
      }

      return 1;
   }
}
