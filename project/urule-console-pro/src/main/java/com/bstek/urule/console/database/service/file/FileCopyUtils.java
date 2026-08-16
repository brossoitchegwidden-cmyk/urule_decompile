package com.bstek.urule.console.database.service.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.database.manager.file.DirectoryManager;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.RuleFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileCopyUtils {
   private static final Log a = LogFactory.getLog(FileCopyUtils.class);

   public static void replaceAllContent() {
      Map var0 = CopyContextHolder.getIdMap();
      Map var1 = CopyContextHolder.getFileMap();

      for(RuleFile var3 : (Iterable<RuleFile>)(Iterable<?>)(var1.values())) {
         String var4 = var3.getType();
         if (!var4.contentEquals(ResourceType.ActionLibrary.name()) && !var4.contentEquals(ResourceType.ConstantLibrary.name()) && !var4.contentEquals(ResourceType.ParameterLibrary.name()) && !var4.contentEquals(ResourceType.VariableLibrary.name())) {
            String var5 = var3.getContent();

            for(String var7 : (Iterable<String>)(Iterable<?>)(var0.keySet())) {
               String var8 = (String)var0.get(var7);
               if (var7.startsWith("file=")) {
                  if (var4.contentEquals(ResourceType.Flow.name())) {
                     var5 = var5.replace(var7, var8);
                  }
               } else {
                  var5 = var5.replace(var7, var8);
               }
            }

            FileManager.ins.updateContent(var3.getId(), var3.getCreateUser(), var5);
         }
      }

      CopyContextHolder.clear();
   }

   public static RuleFile copyFile(long var0, long var2, RuleFile var4, String var5, String var6) {
      a.debug("copyFile:" + var4.toString());
      long var7 = var4.getId();
      String var9 = FileManager.ins.loadContent(Long.valueOf(var7));
      if (var4.getType().contentEquals(ResourceType.ActionLibrary.name())) {
         String var10 = "uuid=\"[a-z0-9-]+\"";
         var9 = a(var9, "uuid", var10);
      } else if (var4.getType().contentEquals(ResourceType.VariableLibrary.name())) {
         String var12 = "uuid=\"[a-z0-9-]+\"";
         var9 = a(var9, "uuid", var12);
      } else if (var4.getType().contentEquals(ResourceType.ParameterLibrary.name())) {
         String var13 = "uuid=\"[a-z0-9-]+\"";
         var9 = a(var9, "uuid", var13);
      } else if (var4.getType().contentEquals(ResourceType.ConstantLibrary.name())) {
         String var14 = "uuid=\"[a-z0-9-]+\"";
         var9 = a(var9, "uuid", var14);
      } else if (var4.getType().contentEquals(ResourceType.ConditionTemplate.name())) {
         String var15 = "id=\"[a-z0-9-]+\"";
         var9 = a(var9, "id", var15);
      } else if (var4.getType().contentEquals(ResourceType.ActionTemplate.name())) {
         String var16 = "id=\"[a-z0-9-]+\"";
         var9 = a(var9, "id", var16);
      }

      var9 = a(var9);
      var4.setName(var5);
      var4.setProjectId(var0);
      var4.setLatestVersion((String)null);
      var4.setDigest((String)null);
      var4.setDeleted(false);
      var4.setContent(var9);
      var4.setCreateUser(var6);
      if (var2 == 0L) {
         var4.setParentId(0L);
         FileManager.ins.add(var4);
      } else {
         var4.setParentId(var2);
         FileManager.ins.add(var4);
      }

      CopyContextHolder.addId(var7, var4.getId());
      CopyContextHolder.addFile(var7, var4);
      SystemLogUtils.addRuleFileOperationLog(var4.getType(), "add", var4.getId(), String.format("Create a new %s type file %s", var4.getType(), var4.getName()));
      return var4;
   }

   public static RuleFile copyDir(long var0, long var2, RuleFile var4, String var5, String var6) {
      a.debug("copyDir:" + var4.toString());
      long var7 = var4.getId();
      var4.setName(var5);
      var4.setProjectId(var0);
      var4.setDeleted(false);
      var4.setCreateUser(var6);
      if (var2 == 0L) {
         var4.setParentId(0L);
         DirectoryManager.ins.add(var4);
      } else {
         var4.setParentId(var2);
         DirectoryManager.ins.add(var4);
      }

      SystemLogUtils.addRuleFileOperationLog(var4.getType(), "add", var4.getId(), String.format("Create a new %s type folder %s", var4.getType(), var4.getName()));
      ArrayList var9 = new ArrayList();
      List var10 = DirectoryManager.ins.list(var4.getProjectId(), var7, var4.getType());
      if (var10.size() > 0) {
         for(RuleFile var12 : (Iterable<RuleFile>)(Iterable<?>)(var10)) {
            if (!var12.isDeleted()) {
               RuleFile var13 = copyDir(var0, var4.getId(), var12, var12.getName(), var6);
               var9.add(var13);
            }
         }
      }

      Object var15 = null;
      List var16;
      if (ResourceType.General.name().equals(var4.getType())) {
         var16 = FileManager.ins.list(var4.getProjectId(), var7);
      } else {
         var16 = FileManager.ins.list(var4.getProjectId(), var7, var4.getType());
      }

      if (var16.size() > 0) {
         for(RuleFile var18 : (Iterable<RuleFile>)(Iterable<?>)(var16)) {
            if (!var18.isDeleted()) {
               RuleFile var14 = copyFile(var0, var4.getId(), var18, var18.getName(), var6);
               var9.add(var14);
            }
         }
      }

      var4.setChildren(var9);
      return var4;
   }

   private static String a(String var0, String var1, String var2) {
      Pattern var3 = Pattern.compile(var2);
      Matcher var4 = var3.matcher(var0);
      StringBuffer var5 = new StringBuffer();

      while(var4.find()) {
         String var6 = var1 + "=\"" + UUID.randomUUID().toString() + "\"";
         var4.appendReplacement(var5, var6);
      }

      var4.appendTail(var5);
      return var5.toString();
   }

   private static String a(String var0) {
      StringBuffer var1 = new StringBuffer();
      HashMap var2 = new HashMap();
      String var3 = "predefine\\s+uuid=\"[a-z0-9-]+\"";
      Pattern var4 = Pattern.compile(var3);
      Matcher var5 = var4.matcher(var0);

      while(var5.find()) {
         String var6 = var5.group().trim();
         String var7 = var6.substring(9).trim().substring(6, 42);
         String var8 = UUID.randomUUID().toString();
         var2.put(var7, var8);
         String var9 = "predefine uuid=\"" + var8 + "\"";
         var5.appendReplacement(var1, var9);
      }

      var5.appendTail(var1);
      String var35 = var1.toString();
      var1 = new StringBuffer();
      String var42 = "\\s+uuid=\"[a-z0-9-]+\"\\s+type=\"predefine\"";
      Pattern var43 = Pattern.compile(var42);
      Matcher var44 = var43.matcher(var35);

      while(var44.find()) {
         String var10 = var44.group().trim();
         String var11 = var10.substring(6, 42);
         String var12 = (String)var2.get(var11);
         String var13 = " uuid=\"" + var12 + "\" type=\"predefine\"";
         var44.appendReplacement(var1, var13);
      }

      var44.appendTail(var1);
      var35 = var1.toString();
      var1 = new StringBuffer();
      String var45 = "\\s+uuid=\"[a-z0-9-]+\"\\s+type=\"Predefine\"";
      Pattern var46 = Pattern.compile(var45);
      Matcher var47 = var46.matcher(var35);

      while(var47.find()) {
         String var48 = var47.group().trim();
         String var14 = var48.substring(6, 42);
         String var15 = (String)var2.get(var14);
         String var16 = " uuid=\"" + var15 + "\" type=\"Predefine\"";
         var47.appendReplacement(var1, var16);
      }

      var47.appendTail(var1);
      var35 = var1.toString();
      var1 = new StringBuffer();
      String var49 = "\\s+uuid=\"[a-z0-9-]+\"\\s+property-uuid=\"[a-z0-9-]+\"\\s+type=\"predefine\"";
      Pattern var50 = Pattern.compile(var49);
      Matcher var51 = var50.matcher(var35);

      while(var51.find()) {
         String var52 = var51.group().trim();
         String var17 = var52.substring(6, 42);
         String var18 = (String)var2.get(var17);
         String var19 = " uuid=\"" + var18 + "\"" + var52.substring(43);
         var51.appendReplacement(var1, var19);
      }

      var51.appendTail(var1);
      var35 = var1.toString();
      var1 = new StringBuffer();
      String var53 = "\\s+uuid=\"[a-z0-9-]+\"\\s+property-uuid=\"[a-z0-9-]+\"\\s+type=\"Predefine\"";
      Pattern var54 = Pattern.compile(var53);
      Matcher var55 = var54.matcher(var35);

      while(var55.find()) {
         String var56 = var55.group().trim();
         String var20 = var56.substring(6, 42);
         String var21 = (String)var2.get(var20);
         String var22 = " uuid=\"" + var21 + "\"" + var56.substring(43);
         var55.appendReplacement(var1, var22);
      }

      var55.appendTail(var1);
      var35 = var1.toString();
      var1 = new StringBuffer();
      String var57 = "\\s+type=\"Criteria\"\\s+predefine=\"true\"\\s+uuid=\"[a-z0-9-]+\"";
      Pattern var58 = Pattern.compile(var57);
      Matcher var59 = var58.matcher(var35);

      while(var59.find()) {
         String var60 = var59.group().trim();
         String var23 = var60.substring(var60.length() - 37, var60.length() - 1);
         String var24 = (String)var2.get(var23);
         String var25 = " type=\"Criteria\" predefine=\"true\" uuid=\"" + var24 + "\"";
         var59.appendReplacement(var1, var25);
      }

      var59.appendTail(var1);
      var35 = var1.toString();
      var1 = new StringBuffer();
      String var61 = "\\s+type=\"Assignment\"\\s+predefine=\"true\"\\s+uuid=\"[a-z0-9-]{36}\"";
      Pattern var62 = Pattern.compile(var61);
      Matcher var63 = var62.matcher(var35);

      while(var63.find()) {
         String var64 = var63.group().trim();
         String var26 = var64.substring(var64.length() - 37, var64.length() - 1);
         String var27 = (String)var2.get(var26);
         String var28 = " type=\"Assignment\" predefine=\"true\" uuid=\"" + var27 + "\"";
         var63.appendReplacement(var1, var28);
      }

      var63.appendTail(var1);
      var35 = var1.toString();
      return var35;
   }
}
