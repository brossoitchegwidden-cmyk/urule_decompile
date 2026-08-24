package com.bstek.urule.console.util;

import java.io.InputStream;

public class UploadFile {
   private String name;
   private InputStream inputStream;

   public UploadFile(String name, InputStream inputStream) {
      this.name = name;
      this.inputStream = inputStream;
   }

   public String getName() {
      return this.name;
   }

   public InputStream getInputStream() {
      return this.inputStream;
   }
}
