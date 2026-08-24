package com.bstek.urule.console.database.service.reference;

import java.util.List;

public interface ReferenceService {
   ReferenceService ins = new ReferenceServiceImpl();

   /**查看文件引用*/
   List uuid(long projectId, long id, String uuid);

   /**查看知识包引用*/
   List packet(long projectId, long id, String code);
}
