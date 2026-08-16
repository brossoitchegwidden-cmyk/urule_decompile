package com.bstek.urule.console;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper {
   private ServletOutputStream a;

   public HttpServletResponseWrapper(HttpServletResponse var1) {
      super(var1);
   }

   public ServletOutputStream getOutputStream() throws IOException {
      if (this.a == null) {
         this.a = super.getOutputStream();
      }

      return this.a;
   }
}
