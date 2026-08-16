package com.bstek.urule.console.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
   public static String stringToMD5(String var0) {
      byte[] var5 = null;

      try {
         var5 = MessageDigest.getInstance("md5").digest(var0.getBytes());
      } catch (NoSuchAlgorithmException var4) {
         throw new RuntimeException("没有这个md5算法！");
      }

      String var2 = (new BigInteger(1, var5)).toString(16);

      for(int var3 = 0; var3 < 32 - var2.length(); ++var3) {
         var2 = "0" + var2;
      }

      return var2;
   }
}
