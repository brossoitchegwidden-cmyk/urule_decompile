package com.bstek.urule.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletHandler {
   void init();

   void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;

   String url();
}
