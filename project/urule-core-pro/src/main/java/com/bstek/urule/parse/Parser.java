package com.bstek.urule.parse;

import org.dom4j.Element;

public interface Parser<T> {
   boolean support(String name);

   T parse(Element element);
}
