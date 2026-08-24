package com.bstek.urule.builder.resource;

import org.dom4j.Element;

public interface ResourceBuilder<T> {
   T build(Element root, String file);

   boolean support(Element root);

   ResourceType getType();
}
