package com.bstek.urule.console.batch.inspector;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.util.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class DefaultBatchListener implements BatchListener {
   public void beforeExecute(BatchContext var1) {
   }

   public void onExecute(BatchContext var1) {
      Batch var2 = var1.getBatch();
      BatchResult var3 = var1.getResult();
      if (var2.isAsync() && !StringUtils.isBlank(var2.getCallbackUrl())) {
         try {
            RestTemplate var4 = new RestTemplate();
            HttpHeaders var5 = new HttpHeaders();
            var5.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity var6 = new HttpEntity(var3, var5);
            var4.postForLocation(var1.getBatch().getCallbackUrl(), var6, new Object[0]);
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }
}
