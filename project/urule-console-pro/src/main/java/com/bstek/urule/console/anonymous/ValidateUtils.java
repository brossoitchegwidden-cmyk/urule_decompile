package com.bstek.urule.console.anonymous;

import com.bstek.urule.exception.RuleException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

public class ValidateUtils {
   private static Logger logger = Logger.getGlobal();

   public static boolean validateUserPwd(HttpServletRequest req, String defineUser, String definePwd) {
      String parameter = req.getParameter("_u");
      String parameter2 = req.getParameter("_p");
      if (parameter != null && parameter2 != null) {
         try {
            parameter = URLDecoder.decode(parameter, "utf-8");
            parameter2 = URLDecoder.decode(parameter2, "utf-8");
            if (parameter.equals(defineUser) && parameter2.equals(definePwd)) {
               return true;
            } else {
               ValidateUtils.logger.warning("User or password is invalid.");
               return false;
            }
         } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuleException(unsupportedEncodingException);
         }
      } else {
         ValidateUtils.logger.warning("User and password can not be null.");
         return false;
      }
   }
}
