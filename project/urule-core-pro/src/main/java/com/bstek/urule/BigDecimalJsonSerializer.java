package com.bstek.urule;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalJsonSerializer extends StdSerializer<BigDecimal> {
   private static final long a = 3075284108960107323L;

   public BigDecimalJsonSerializer() {
      this(BigDecimal.class);
   }

   public BigDecimalJsonSerializer(Class<BigDecimal> var1) {
      super(var1);
   }

   public void serialize(BigDecimal var1, JsonGenerator var2, SerializerProvider var3) throws IOException {
      if (var1 != null) {
         String var4 = var1.stripTrailingZeros().toPlainString();
         var2.writeString(var4);
      }
   }
}
