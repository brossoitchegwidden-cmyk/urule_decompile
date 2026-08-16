package com.bstek.urule.console.util;

import java.io.InputStream;

public class UploadFile {
   private String a;
   private InputStream b;

   public UploadFile(String var1, InputStream var2) {
      this.a = var1;
      this.b = var2;
   }

   public String getName() {
      return this.a;
   }

   public InputStream getInputStream() {
      return this.b;
   }
}
