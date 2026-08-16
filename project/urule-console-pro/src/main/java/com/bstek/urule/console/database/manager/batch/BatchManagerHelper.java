package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.database.manager.batch.provider.ProviderFieldManager;
import com.bstek.urule.console.database.manager.batch.provider.ProviderManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverFieldManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverItemManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverManager;
import com.bstek.urule.console.database.manager.log.batch.BatchLogManager;
import com.bstek.urule.console.database.manager.log.batch.BatchSkipLogManager;

public class BatchManagerHelper {
   public static void removeResolver(Long var0) {
      ResolverFieldManager.ins.removeByResolverId(var0);
      ResolverItemManager.ins.removeByResolverId(var0);
      ResolverManager.ins.remove(var0);
   }

   public static void removeResolverItem(Long var0) {
      ResolverFieldManager.ins.removeByItemId(var0);
      ResolverItemManager.ins.remove(var0);
   }

   public static void removeByProjectId(Long var0) {
      BatchSkipLogManager.ins.removeByProject(var0);
      BatchLogManager.ins.removeByProject(var0);
      ResolverFieldManager.ins.removeByProjectId(var0);
      ResolverItemManager.ins.removeByProjectId(var0);
      ResolverManager.ins.removeByProjectId(var0);
      ProviderFieldManager.ins.removeByProjectId(var0);
      ProviderManager.ins.removeByProjectId(var0);
      BatchManager.ins.removeByProjectId(var0);
   }

   public static void removeByGroupId(String var0) {
      BatchSkipLogManager.ins.removeByGroupId(var0);
      BatchLogManager.ins.removeByGroupId(var0);
      ResolverFieldManager.ins.removeByGroupId(var0);
      ResolverItemManager.ins.removeByGroupId(var0);
      ResolverManager.ins.removeByGroupId(var0);
      ProviderFieldManager.ins.removeByGroupId(var0);
      ProviderManager.ins.removeByGroupId(var0);
      BatchManager.ins.removeByGroupId(var0);
   }
}
