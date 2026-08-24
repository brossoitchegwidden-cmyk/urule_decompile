package com.bstek.urule.console.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Legacy MD5 helper retained for compatibility with stored URule values. */
public final class MD5Utils {
   private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

   private MD5Utils() {
   }

   public static String stringToMD5(String value) {
      try {
         byte[] digest = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
         char[] encoded = new char[digest.length * 2];
         for (int index = 0; index < digest.length; index++) {
            int unsignedByte = digest[index] & 0xff;
            encoded[index * 2] = HEX_DIGITS[unsignedByte >>> 4];
            encoded[index * 2 + 1] = HEX_DIGITS[unsignedByte & 0x0f];
         }
         return new String(encoded);
      } catch (NoSuchAlgorithmException exception) {
         throw new IllegalStateException("The JVM does not provide the required MD5 algorithm", exception);
      }
   }
}
