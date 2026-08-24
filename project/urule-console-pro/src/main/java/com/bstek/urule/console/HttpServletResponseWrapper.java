package com.bstek.urule.console;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper {
   private ServletOutputStream outputStream;

   public HttpServletResponseWrapper(HttpServletResponse response) {
      super(response);
   }

   public ServletOutputStream getOutputStream() throws IOException {
      if (this.outputStream == null) {
         this.outputStream = super.getOutputStream();
      }

      return this.outputStream;
   }
}
