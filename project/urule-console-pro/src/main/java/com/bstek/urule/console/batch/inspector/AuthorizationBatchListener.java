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
   private static final String a = "urule_authorization";

   public void beforeExecute(BatchContext var1) {
      HttpServletRequest var2 = RequestHolder.getRequest();
      if (var2 != null) {
         var1.getParamValueMap().put("urule_authorization", var2.getHeader("Authorization"));
      }

   }

   public void onExecute(BatchContext var1) {
      Batch var2 = var1.getBatch();
      BatchResult var3 = var1.getResult();

      try {
         RestTemplate var4 = new RestTemplate();
         HttpHeaders var5 = new HttpHeaders();
         var5.set("Authorization", (String)var1.getParamValueMap().get("urule_authorization"));
         var5.setContentType(MediaType.APPLICATION_JSON);
         HttpEntity var6 = new HttpEntity(var3, var5);
         var4.postForLocation(var2.getCallbackUrl(), var6, new Object[0]);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
