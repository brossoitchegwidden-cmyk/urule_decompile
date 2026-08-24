package com.bstek.urule.console.batch.inspector;

import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.database.model.batch.Batch;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class AuthorizationBatchListener implements BatchListener {
   private static final String URULE_AUTHORIZATION = "urule_authorization";

   public void beforeExecute(BatchContext batchContext) {
      HttpServletRequest request = RequestHolder.getRequest();
      if (request != null) {
         batchContext.getParamValueMap().put("urule_authorization", request.getHeader("Authorization"));
      }

   }

   public void onExecute(BatchContext batchContext) {
      Batch batch = batchContext.getBatch();
      BatchResult batchResult = batchContext.getResult();

      try {
         RestTemplate restTemplate = new RestTemplate();
         HttpHeaders httpHeaders = new HttpHeaders();
         httpHeaders.set("Authorization", (String)batchContext.getParamValueMap().get("urule_authorization"));
         httpHeaders.setContentType(MediaType.APPLICATION_JSON);
         HttpEntity httpEntity = new HttpEntity(batchResult, httpHeaders);
         restTemplate.postForLocation(batch.getCallbackUrl(), httpEntity, new Object[0]);
      } catch (Exception exception) {
         java.util.logging.Logger.getLogger(AuthorizationBatchListener.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
      }

   }
}
