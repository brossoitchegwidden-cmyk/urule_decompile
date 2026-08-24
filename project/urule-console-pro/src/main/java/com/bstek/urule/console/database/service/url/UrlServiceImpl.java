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
   private ClientProvider clientProvider;
   private ClusterProvider clusterProvider;

   protected UrlServiceImpl() {
      ApplicationContext applicationContext = Utils.getApplicationContext();
      Collection clientProviders = applicationContext.getBeansOfType(ClientProvider.class).values();
      if (clientProviders.size() > 0) {
         this.clientProvider = (ClientProvider)clientProviders.iterator().next();
      }

      Collection clusterProviders = applicationContext.getBeansOfType(ClusterProvider.class).values();
      if (clusterProviders.size() > 0) {
         this.clusterProvider = (ClusterProvider)clusterProviders.iterator().next();
      }

   }

   public UrlData load(UrlType type, String groupId) {
      if (type.equals(UrlType.client) && this.clientProvider != null) {
         List clients = this.clientProvider.loadClients(groupId);
         if (clients != null && clients.size() > 0) {
            return new UrlData(clients, true);
         }
      } else if (type.equals(UrlType.cluster) && this.clusterProvider != null) {
         List clusters = this.clusterProvider.loadClusters(groupId);
         if (clusters != null && clusters.size() > 0) {
            return new UrlData(clusters, true);
         }
      }

      List items = UrlManager.ins.newQuery().groupId(groupId).type(type).list();
      return new UrlData(items, false);
   }
}
