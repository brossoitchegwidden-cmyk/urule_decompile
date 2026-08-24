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
   public void beforeExecute(BatchContext batchContext) {
   }

   public void onExecute(BatchContext batchContext) {
      Batch batch = batchContext.getBatch();
      BatchResult batchResult = batchContext.getResult();
      if (batch.isAsync() && !StringUtils.isBlank(batch.getCallbackUrl())) {
         try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(batchResult, httpHeaders);
            restTemplate.postForLocation(batchContext.getBatch().getCallbackUrl(), httpEntity, new Object[0]);
         } catch (Exception exception) {
            java.util.logging.Logger.getLogger(DefaultBatchListener.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
         }

      }
   }
}
