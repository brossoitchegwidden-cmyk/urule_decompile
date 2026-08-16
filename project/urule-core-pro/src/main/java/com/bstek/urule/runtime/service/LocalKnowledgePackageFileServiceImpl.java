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
   private String a;

   @Override
   public KnowledgePackage loadKnowledgePackage(String var1) {
      if (!this.isEnable()) {
         return null;
      } else {
         File var2 = this.a(var1);
         if (!var2.exists()) {
            throw new RuleException("本地配置的知识包文件存储目录【" + this.a + "】中，知识包【" + var1 + "】对应的文件不存在!");
         } else {
            return this.a(var2, var1);
         }
      }
   }

   @Override
   public KnowledgePackage verifyKnowledgePackage(String var1, long var2) {
      if (!this.isEnable()) {
         return null;
      }

      File var4 = this.a(var1);
      if (!var4.exists()) {
         return null;
      }

      long var5 = var4.lastModified();
      return var5 == var2 ? null : this.a(var4, var1);
   }

   @Override
   public boolean isEnable() {
      return this.a != null;
   }

   private File a(String var1) {
      String var2 = var1 + ".data";
      String var3 = this.a + "/" + var2;
      return new File(var3);
   }

   private KnowledgePackage a(File var1, String var2) {
      FileInputStream var3 = null;

      try {
         var3 = new FileInputStream(var1);
         String var4 = Utils.uncompress(IOUtils.toByteArray(var3));
         KnowledgePackage var5 = Utils.stringToKnowledgePackage(var4);
         KnowledgePackageImpl var6 = (KnowledgePackageImpl)var5;
         var6.setPackageInfo(var2);
         long var7 = var1.lastModified();
         var6.setTimestamp(var7);
         return var6;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         IOUtils.closeQuietly(var3);
      }
   }

   public void setKnowledgePackageFileStorePath(String var1) {
      System.out.println("[URULE-CORE]urule.knowledgePackageFileStorePath:" + var1);
      if (!StringUtils.isBlank(var1)) {
         if (var1.startsWith("${")) {
            this.a = null;
         } else {
            this.a = var1;
            File var2 = new File(var1);
            if (var2.exists()) {
               return;
            }

            SpringBootHome var3 = new SpringBootHome();
            File var4 = var3.findSpringbootJarHomeDir(this.getClass());
            String var5 = var4.getAbsolutePath();
            if (this.a.startsWith("/")) {
               this.a = var5 + this.a;
            } else {
               this.a = var5 + "/" + this.a;
            }

            File var6 = new File(this.a);
            if (!var6.exists()) {
               var6.mkdirs();
            }

            try {
               this.a = var6.getCanonicalPath();
            } catch (IOException var8) {
            }
         }
      }
   }
}
