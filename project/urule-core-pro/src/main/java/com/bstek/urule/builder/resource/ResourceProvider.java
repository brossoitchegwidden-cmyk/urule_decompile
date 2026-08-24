package com.bstek.urule.builder.resource;

public interface ResourceProvider {
   Resource provide(long fileId, String version);
}
