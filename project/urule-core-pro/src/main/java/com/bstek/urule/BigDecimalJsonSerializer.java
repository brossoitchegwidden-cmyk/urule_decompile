package com.bstek.urule;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalJsonSerializer extends StdSerializer<BigDecimal> {
   private static final long serialVersionUID = 3075284108960107323L;

   public BigDecimalJsonSerializer() {
      this(BigDecimal.class);
   }

   public BigDecimalJsonSerializer(Class<BigDecimal> valueType) {
      super(valueType);
   }

   public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value != null) {
         String text = value.stripTrailingZeros().toPlainString();
         gen.writeString(text);
      }
   }
}
