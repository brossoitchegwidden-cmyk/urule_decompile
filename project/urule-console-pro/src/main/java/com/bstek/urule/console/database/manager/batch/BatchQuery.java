package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface BatchQuery {
   BatchQuery id(Long id);

   BatchQuery nameLike(String nameLike);

   BatchQuery descLike(String descLike);

   BatchQuery projectId(Long projectId);

   BatchQuery enable(Boolean enable);

   BatchQuery async(Boolean async);

   BatchQuery status(BatchStatus status);

   BatchQuery packetId(Long packetId);

   BatchQuery createUserLike(String createUser);

   List list();

   void page(Page page);
}
