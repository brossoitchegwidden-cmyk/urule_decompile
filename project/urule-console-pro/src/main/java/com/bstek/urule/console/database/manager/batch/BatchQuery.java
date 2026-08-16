package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface BatchQuery {
   BatchQuery id(Long var1);

   BatchQuery nameLike(String var1);

   BatchQuery descLike(String var1);

   BatchQuery projectId(Long var1);

   BatchQuery enable(Boolean var1);

   BatchQuery async(Boolean var1);

   BatchQuery status(BatchStatus var1);

   BatchQuery packetId(Long var1);

   BatchQuery createUserLike(String var1);

   List list();

   void page(Page var1);
}
