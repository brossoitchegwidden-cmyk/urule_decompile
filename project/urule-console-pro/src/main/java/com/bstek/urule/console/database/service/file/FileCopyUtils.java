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
   private static final Log logger = LogFactory.getLog(FileCopyUtils.class);

   public static void replaceAllContent() {
      Map idMap = CopyContextHolder.getIdMap();
      Map fileMap = CopyContextHolder.getFileMap();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(fileMap.values())) {
         String type = ruleFile.getType();
         if (!type.contentEquals(ResourceType.ActionLibrary.name()) && !type.contentEquals(ResourceType.ConstantLibrary.name()) && !type.contentEquals(ResourceType.ParameterLibrary.name()) && !type.contentEquals(ResourceType.VariableLibrary.name())) {
            String content = ruleFile.getContent();

            for(String text : (Iterable<String>)(Iterable<?>)(idMap.keySet())) {
               String text2 = (String)idMap.get(text);
               if (text.startsWith("file=")) {
                  if (type.contentEquals(ResourceType.Flow.name())) {
                     content = content.replace(text, text2);
                  }
               } else {
                  content = content.replace(text, text2);
               }
            }

            FileManager.ins.updateContent(ruleFile.getId(), ruleFile.getCreateUser(), content);
         }
      }

      CopyContextHolder.clear();
   }

   public static RuleFile copyFile(long projectId, long parentId, RuleFile file, String fileName, String account) {
      FileCopyUtils.logger.debug("copyFile:" + file.toString());
      long id = file.getId();
      String content = FileManager.ins.loadContent(Long.valueOf(id));
      if (file.getType().contentEquals(ResourceType.ActionLibrary.name())) {
         String text = "uuid=\"[a-z0-9-]+\"";
         content = replaceAttributeWithNewUuid(content, "uuid", text);
      } else if (file.getType().contentEquals(ResourceType.VariableLibrary.name())) {
         String text2 = "uuid=\"[a-z0-9-]+\"";
         content = replaceAttributeWithNewUuid(content, "uuid", text2);
      } else if (file.getType().contentEquals(ResourceType.ParameterLibrary.name())) {
         String text3 = "uuid=\"[a-z0-9-]+\"";
         content = replaceAttributeWithNewUuid(content, "uuid", text3);
      } else if (file.getType().contentEquals(ResourceType.ConstantLibrary.name())) {
         String text4 = "uuid=\"[a-z0-9-]+\"";
         content = replaceAttributeWithNewUuid(content, "uuid", text4);
      } else if (file.getType().contentEquals(ResourceType.ConditionTemplate.name())) {
         String text5 = "id=\"[a-z0-9-]+\"";
         content = replaceAttributeWithNewUuid(content, "id", text5);
      } else if (file.getType().contentEquals(ResourceType.ActionTemplate.name())) {
         String text6 = "id=\"[a-z0-9-]+\"";
         content = replaceAttributeWithNewUuid(content, "id", text6);
      }

      content = remapPredefinedVariableUuids(content);
      file.setName(fileName);
      file.setProjectId(projectId);
      file.setLatestVersion((String)null);
      file.setDigest((String)null);
      file.setDeleted(false);
      file.setContent(content);
      file.setCreateUser(account);
      if (parentId == 0L) {
         file.setParentId(0L);
         FileManager.ins.add(file);
      } else {
         file.setParentId(parentId);
         FileManager.ins.add(file);
      }

      CopyContextHolder.addId(id, file.getId());
      CopyContextHolder.addFile(id, file);
      SystemLogUtils.addRuleFileOperationLog(file.getType(), "add", file.getId(), String.format("Create a new %s type file %s", file.getType(), file.getName()));
      return file;
   }

   public static RuleFile copyDir(long projectId, long parentId, RuleFile file, String fileName, String account) {
      FileCopyUtils.logger.debug("copyDir:" + file.toString());
      long id = file.getId();
      file.setName(fileName);
      file.setProjectId(projectId);
      file.setDeleted(false);
      file.setCreateUser(account);
      if (parentId == 0L) {
         file.setParentId(0L);
         DirectoryManager.ins.add(file);
      } else {
         file.setParentId(parentId);
         DirectoryManager.ins.add(file);
      }

      SystemLogUtils.addRuleFileOperationLog(file.getType(), "add", file.getId(), String.format("Create a new %s type folder %s", file.getType(), file.getName()));
      ArrayList items = new ArrayList();
      List items2 = DirectoryManager.ins.list(file.getProjectId(), id, file.getType());
      if (items2.size() > 0) {
         for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items2)) {
            if (!ruleFile.isDeleted()) {
               RuleFile ruleFile2 = copyDir(projectId, file.getId(), ruleFile, ruleFile.getName(), account);
               items.add(ruleFile2);
            }
         }
      }

      Object objectValue = null;
      List items3;
      if (ResourceType.General.name().equals(file.getType())) {
         items3 = FileManager.ins.list(file.getProjectId(), id);
      } else {
         items3 = FileManager.ins.list(file.getProjectId(), id, file.getType());
      }

      if (items3.size() > 0) {
         for(RuleFile ruleFile3 : (Iterable<RuleFile>)(Iterable<?>)(items3)) {
            if (!ruleFile3.isDeleted()) {
               RuleFile ruleFile4 = copyFile(projectId, file.getId(), ruleFile3, ruleFile3.getName(), account);
               items.add(ruleFile4);
            }
         }
      }

      file.setChildren(items);
      return file;
   }

   private static String replaceAttributeWithNewUuid(String text, String text2, String text3) {
      Pattern pattern = Pattern.compile(text3);
      Matcher matcher = pattern.matcher(text);
      StringBuffer stringBuffer = new StringBuffer();

      while(matcher.find()) {
         String text4 = text2 + "=\"" + UUID.randomUUID().toString() + "\"";
         matcher.appendReplacement(stringBuffer, text4);
      }

      matcher.appendTail(stringBuffer);
      return stringBuffer.toString();
   }

   private static String remapPredefinedVariableUuids(String text) {
      StringBuffer stringBuffer = new StringBuffer();
      HashMap valuesByKey = new HashMap();
      String text2 = "predefine\\s+uuid=\"[a-z0-9-]+\"";
      Pattern pattern = Pattern.compile(text2);
      Matcher matcher = pattern.matcher(text);

      while(matcher.find()) {
         String trimmedText = matcher.group().trim();
         String substring = trimmedText.substring(9).trim().substring(6, 42);
         String text3 = UUID.randomUUID().toString();
         valuesByKey.put(substring, text3);
         String text4 = "predefine uuid=\"" + text3 + "\"";
         matcher.appendReplacement(stringBuffer, text4);
      }

      matcher.appendTail(stringBuffer);
      String text5 = stringBuffer.toString();
      stringBuffer = new StringBuffer();
      String text6 = "\\s+uuid=\"[a-z0-9-]+\"\\s+type=\"predefine\"";
      Pattern pattern2 = Pattern.compile(text6);
      Matcher matcher2 = pattern2.matcher(text5);

      while(matcher2.find()) {
         String trimmedText2 = matcher2.group().trim();
         String substring2 = trimmedText2.substring(6, 42);
         String text7 = (String)valuesByKey.get(substring2);
         String text8 = " uuid=\"" + text7 + "\" type=\"predefine\"";
         matcher2.appendReplacement(stringBuffer, text8);
      }

      matcher2.appendTail(stringBuffer);
      text5 = stringBuffer.toString();
      stringBuffer = new StringBuffer();
      String text9 = "\\s+uuid=\"[a-z0-9-]+\"\\s+type=\"Predefine\"";
      Pattern pattern3 = Pattern.compile(text9);
      Matcher matcher3 = pattern3.matcher(text5);

      while(matcher3.find()) {
         String trimmedText3 = matcher3.group().trim();
         String substring3 = trimmedText3.substring(6, 42);
         String text10 = (String)valuesByKey.get(substring3);
         String text11 = " uuid=\"" + text10 + "\" type=\"Predefine\"";
         matcher3.appendReplacement(stringBuffer, text11);
      }

      matcher3.appendTail(stringBuffer);
      text5 = stringBuffer.toString();
      stringBuffer = new StringBuffer();
      String text12 = "\\s+uuid=\"[a-z0-9-]+\"\\s+property-uuid=\"[a-z0-9-]+\"\\s+type=\"predefine\"";
      Pattern pattern4 = Pattern.compile(text12);
      Matcher matcher4 = pattern4.matcher(text5);

      while(matcher4.find()) {
         String trimmedText4 = matcher4.group().trim();
         String substring4 = trimmedText4.substring(6, 42);
         String text13 = (String)valuesByKey.get(substring4);
         String text14 = " uuid=\"" + text13 + "\"" + trimmedText4.substring(43);
         matcher4.appendReplacement(stringBuffer, text14);
      }

      matcher4.appendTail(stringBuffer);
      text5 = stringBuffer.toString();
      stringBuffer = new StringBuffer();
      String text15 = "\\s+uuid=\"[a-z0-9-]+\"\\s+property-uuid=\"[a-z0-9-]+\"\\s+type=\"Predefine\"";
      Pattern pattern5 = Pattern.compile(text15);
      Matcher matcher5 = pattern5.matcher(text5);

      while(matcher5.find()) {
         String trimmedText5 = matcher5.group().trim();
         String substring5 = trimmedText5.substring(6, 42);
         String text16 = (String)valuesByKey.get(substring5);
         String text17 = " uuid=\"" + text16 + "\"" + trimmedText5.substring(43);
         matcher5.appendReplacement(stringBuffer, text17);
      }

      matcher5.appendTail(stringBuffer);
      text5 = stringBuffer.toString();
      stringBuffer = new StringBuffer();
      String text18 = "\\s+type=\"Criteria\"\\s+predefine=\"true\"\\s+uuid=\"[a-z0-9-]+\"";
      Pattern pattern6 = Pattern.compile(text18);
      Matcher matcher6 = pattern6.matcher(text5);

      while(matcher6.find()) {
         String trimmedText6 = matcher6.group().trim();
         String substring6 = trimmedText6.substring(trimmedText6.length() - 37, trimmedText6.length() - 1);
         String text19 = (String)valuesByKey.get(substring6);
         String text20 = " type=\"Criteria\" predefine=\"true\" uuid=\"" + text19 + "\"";
         matcher6.appendReplacement(stringBuffer, text20);
      }

      matcher6.appendTail(stringBuffer);
      text5 = stringBuffer.toString();
      stringBuffer = new StringBuffer();
      String text21 = "\\s+type=\"Assignment\"\\s+predefine=\"true\"\\s+uuid=\"[a-z0-9-]{36}\"";
      Pattern pattern7 = Pattern.compile(text21);
      Matcher matcher7 = pattern7.matcher(text5);

      while(matcher7.find()) {
         String trimmedText7 = matcher7.group().trim();
         String substring7 = trimmedText7.substring(trimmedText7.length() - 37, trimmedText7.length() - 1);
         String text22 = (String)valuesByKey.get(substring7);
         String text23 = " type=\"Assignment\" predefine=\"true\" uuid=\"" + text22 + "\"";
         matcher7.appendReplacement(stringBuffer, text23);
      }

      matcher7.appendTail(stringBuffer);
      text5 = stringBuffer.toString();
      return text5;
   }
}
