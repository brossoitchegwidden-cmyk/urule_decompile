package com.bstek.urule.parse.deserializer;

import org.dom4j.Element;

public interface Deserializer<T> {
   T deserialize(Element root);

   boolean support(Element root);
}
