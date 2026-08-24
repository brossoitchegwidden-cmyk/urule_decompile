package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rete.jsondeserializer.math.AbsoluteMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.DownRoundMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.ExtremumFunctionMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.FractionMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.LnMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.LogMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.MathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.NRadicalMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.PiMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.PowerMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.RadicalMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.SigmaMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.TriangleFunctionMathDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.math.UpRoundMathDeserializer;
import com.bstek.urule.model.rule.MathValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;

public class MathValueDeserializer implements ValueDeserializer {
   private Map<MathType, MathDeserializer> deserializersByMathType = new HashMap<>();

   public MathValueDeserializer() {
      AbsoluteMathDeserializer absoluteMathDeserializer = new AbsoluteMathDeserializer();
      this.deserializersByMathType.put(absoluteMathDeserializer.getType(), absoluteMathDeserializer);
      FractionMathDeserializer fractionMathDeserializer = new FractionMathDeserializer();
      this.deserializersByMathType.put(fractionMathDeserializer.getType(), fractionMathDeserializer);
      LnMathDeserializer lnMathDeserializer = new LnMathDeserializer();
      this.deserializersByMathType.put(lnMathDeserializer.getType(), lnMathDeserializer);
      LogMathDeserializer logMathDeserializer = new LogMathDeserializer();
      this.deserializersByMathType.put(logMathDeserializer.getType(), logMathDeserializer);
      NRadicalMathDeserializer nRadicalMathDeserializer = new NRadicalMathDeserializer();
      this.deserializersByMathType.put(nRadicalMathDeserializer.getType(), nRadicalMathDeserializer);
      RadicalMathDeserializer radicalMathDeserializer = new RadicalMathDeserializer();
      this.deserializersByMathType.put(radicalMathDeserializer.getType(), radicalMathDeserializer);
      PiMathDeserializer piMathDeserializer = new PiMathDeserializer();
      this.deserializersByMathType.put(piMathDeserializer.getType(), piMathDeserializer);
      PowerMathDeserializer powerMathDeserializer = new PowerMathDeserializer();
      this.deserializersByMathType.put(powerMathDeserializer.getType(), powerMathDeserializer);
      SigmaMathDeserializer sigmaMathDeserializer = new SigmaMathDeserializer();
      this.deserializersByMathType.put(sigmaMathDeserializer.getType(), sigmaMathDeserializer);
      TriangleFunctionMathDeserializer triangleFunctionMathDeserializer = new TriangleFunctionMathDeserializer();
      this.deserializersByMathType.put(triangleFunctionMathDeserializer.getType(), triangleFunctionMathDeserializer);
      ExtremumFunctionMathDeserializer extremumFunctionMathDeserializer = new ExtremumFunctionMathDeserializer();
      this.deserializersByMathType.put(extremumFunctionMathDeserializer.getType(), extremumFunctionMathDeserializer);
      UpRoundMathDeserializer upRoundMathDeserializer = new UpRoundMathDeserializer();
      this.deserializersByMathType.put(upRoundMathDeserializer.getType(), upRoundMathDeserializer);
      DownRoundMathDeserializer downRoundMathDeserializer = new DownRoundMathDeserializer();
      this.deserializersByMathType.put(downRoundMathDeserializer.getType(), downRoundMathDeserializer);
   }

   @Override
   public Value deserialize(JsonNode jsonNode) {
      MathValue mathValue = new MathValue();
      JsonNode mathSign2 = jsonNode.get("mathSign");
      String jsonValue = JsonUtils.getJsonValue(mathSign2, "type");
      MathType mathType = MathType.valueOf(jsonValue);
      MathSign mathSign = this.deserializersByMathType.get(mathType).deserialize(mathSign2);
      mathValue.setMathSign(mathSign);
      mathValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      return mathValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.Math);
   }
}
