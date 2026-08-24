package com.bstek.urule.runtime.service;

import com.bstek.urule.SpringBootHome;
import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class LocalKnowledgePackageFileServiceImpl implements KnowledgePackageFileService {
   private String knowledgePackageFileStorePath;

   @Override
   public KnowledgePackage loadKnowledgePackage(String packageId) {
      if (!this.isEnable()) {
         return null;
      } else {
         File file = this.executeServiceOperation(packageId);
         if (!file.exists()) {
            throw new RuleException("本地配置的知识包文件存储目录【" + this.knowledgePackageFileStorePath + "】中，知识包【" + packageId + "】对应的文件不存在!");
         } else {
            return this.executeServiceOperation(file, packageId);
         }
      }
   }
   @Override
   public KnowledgePackage verifyKnowledgePackage(String packageId, long fileModifyDate) {
      if (!this.isEnable()) {
         return null;
      }

      File file = this.executeServiceOperation(packageId);
      if (!file.exists()) {
         return null;
      }

      long longValue = file.lastModified();
      return longValue == fileModifyDate ? null : this.executeServiceOperation(file, packageId);
   }

   @Override
   public boolean isEnable() {
      return this.knowledgePackageFileStorePath != null;
   }

   private File executeServiceOperation(String text) {
      String text2 = text + ".data";
      String text3 = this.knowledgePackageFileStorePath + "/" + text2;
      return new File(text3);
   }

   private KnowledgePackage executeServiceOperation(File file, String text) {
      FileInputStream fileInputStream = null;

      try {
         fileInputStream = new FileInputStream(file);
         String text2 = Utils.uncompress(IOUtils.toByteArray(fileInputStream));
         KnowledgePackage knowledgePackage = Utils.stringToKnowledgePackage(text2);
         KnowledgePackageImpl knowledgePackageImpl = (KnowledgePackageImpl)knowledgePackage;
         knowledgePackageImpl.setPackageInfo(text);
         long longValue = file.lastModified();
         knowledgePackageImpl.setTimestamp(longValue);
         return knowledgePackageImpl;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         IOUtils.closeQuietly(fileInputStream);
      }
   }

   public void setKnowledgePackageFileStorePath(String knowledgePackageFileStorePath) {
      System.out.println("[URULE-CORE]urule.knowledgePackageFileStorePath:" + knowledgePackageFileStorePath);
      if (!StringUtils.isBlank(knowledgePackageFileStorePath)) {
         if (knowledgePackageFileStorePath.startsWith("${")) {
            this.knowledgePackageFileStorePath = null;
         } else {
            this.knowledgePackageFileStorePath = knowledgePackageFileStorePath;
            File file = new File(knowledgePackageFileStorePath);
            if (file.exists()) {
               return;
            }

            SpringBootHome springBootHome = new SpringBootHome();
            File springbootJarHomeDir = springBootHome.findSpringbootJarHomeDir(this.getClass());
            String absolutePath = springbootJarHomeDir.getAbsolutePath();
            if (this.knowledgePackageFileStorePath.startsWith("/")) {
               this.knowledgePackageFileStorePath = absolutePath + this.knowledgePackageFileStorePath;
            } else {
               this.knowledgePackageFileStorePath = absolutePath + "/" + this.knowledgePackageFileStorePath;
            }

            File file2 = new File(this.knowledgePackageFileStorePath);
            if (!file2.exists()) {
               file2.mkdirs();
            }

            try {
               this.knowledgePackageFileStorePath = file2.getCanonicalPath();
            } catch (IOException iOException) {
            }
         }
      }
   }
}
