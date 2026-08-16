package com.bstek.urule.console.batch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;

public class JsonUtils {
   public static ObjectMapper getObjectJsonMapper() {
      ObjectMapper var0 = new ObjectMapper();
      var0.setDateFormat(new SimpleDateFormat("yyy-MM-dd HH:mm:ss"));
      return var0;
   }
}
