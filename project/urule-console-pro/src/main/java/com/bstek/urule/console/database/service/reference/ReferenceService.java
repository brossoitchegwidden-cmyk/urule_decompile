package com.bstek.urule.console.database.service.reference;

import java.util.List;

public interface ReferenceService {
   ReferenceService ins = new ReferenceServiceImpl();

   List uuid(long var1, long var3, String var5);

   List packet(long var1, long var3, String var5);
}
