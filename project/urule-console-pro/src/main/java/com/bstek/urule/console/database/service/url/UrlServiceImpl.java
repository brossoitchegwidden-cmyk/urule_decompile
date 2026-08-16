package com.bstek.urule.console.database.service.url;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ClientProvider;
import com.bstek.urule.console.ClusterProvider;
import com.bstek.urule.console.database.manager.url.UrlManager;
import com.bstek.urule.console.database.model.UrlType;
import java.util.Collection;
import java.util.List;
import org.springframework.context.ApplicationContext;

public class UrlServiceImpl implements UrlService {
   private ClientProvider a;
   private ClusterProvider b;

   protected UrlServiceImpl() {
      ApplicationContext var1 = Utils.getApplicationContext();
      Collection var2 = var1.getBeansOfType(ClientProvider.class).values();
      if (var2.size() > 0) {
         this.a = (ClientProvider)var2.iterator().next();
      }

      Collection var3 = var1.getBeansOfType(ClusterProvider.class).values();
      if (var3.size() > 0) {
         this.b = (ClusterProvider)var3.iterator().next();
      }

   }

   public UrlData load(UrlType var1, String var2) {
      if (var1.equals(UrlType.client) && this.a != null) {
         List var4 = this.a.loadClients(var2);
         if (var4 != null && var4.size() > 0) {
            return new UrlData(var4, true);
         }
      } else if (var1.equals(UrlType.cluster) && this.b != null) {
         List var3 = this.b.loadClusters(var2);
         if (var3 != null && var3.size() > 0) {
            return new UrlData(var3, true);
         }
      }

      List var5 = UrlManager.ins.newQuery().groupId(var2).type(var1).list();
      return new UrlData(var5, false);
   }
}
