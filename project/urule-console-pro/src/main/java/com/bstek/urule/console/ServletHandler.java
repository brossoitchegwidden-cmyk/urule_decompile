package com.bstek.urule.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletHandler {
   void init();

   void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception;

   String url();
}
