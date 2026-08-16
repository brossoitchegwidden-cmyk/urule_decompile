package com.bstek.urule.console.anonymous;

import com.bstek.urule.exception.RuleException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

public class ValidateUtils {
   private static Logger a = Logger.getGlobal();

   public static boolean validateUserPwd(HttpServletRequest var0, String var1, String var2) {
      String var3 = var0.getParameter("_u");
      String var4 = var0.getParameter("_p");
      if (var3 != null && var4 != null) {
         try {
            var3 = URLDecoder.decode(var3, "utf-8");
            var4 = URLDecoder.decode(var4, "utf-8");
            if (var3.equals(var1) && var4.equals(var2)) {
               return true;
            } else {
               a.warning("User or password is invalid.");
               return false;
            }
         } catch (UnsupportedEncodingException var6) {
            throw new RuleException(var6);
         }
      } else {
         a.warning("User and password can not be null.");
         return false;
      }
   }
}
