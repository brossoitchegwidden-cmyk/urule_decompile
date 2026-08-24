package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.database.manager.batch.provider.ProviderFieldManager;
import com.bstek.urule.console.database.manager.batch.provider.ProviderManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverFieldManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverItemManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverManager;
import com.bstek.urule.console.database.manager.log.batch.BatchLogManager;
import com.bstek.urule.console.database.manager.log.batch.BatchSkipLogManager;

public class BatchManagerHelper {
   public static void removeResolver(Long resolverId) {
      ResolverFieldManager.ins.removeByResolverId(resolverId);
      ResolverItemManager.ins.removeByResolverId(resolverId);
      ResolverManager.ins.remove(resolverId);
   }

   public static void removeResolverItem(Long itemId) {
      ResolverFieldManager.ins.removeByItemId(itemId);
      ResolverItemManager.ins.remove(itemId);
   }

   public static void removeByProjectId(Long projectId) {
      BatchSkipLogManager.ins.removeByProject(projectId);
      BatchLogManager.ins.removeByProject(projectId);
      ResolverFieldManager.ins.removeByProjectId(projectId);
      ResolverItemManager.ins.removeByProjectId(projectId);
      ResolverManager.ins.removeByProjectId(projectId);
      ProviderFieldManager.ins.removeByProjectId(projectId);
      ProviderManager.ins.removeByProjectId(projectId);
      BatchManager.ins.removeByProjectId(projectId);
   }

   public static void removeByGroupId(String groupId) {
      BatchSkipLogManager.ins.removeByGroupId(groupId);
      BatchLogManager.ins.removeByGroupId(groupId);
      ResolverFieldManager.ins.removeByGroupId(groupId);
      ResolverItemManager.ins.removeByGroupId(groupId);
      ResolverManager.ins.removeByGroupId(groupId);
      ProviderFieldManager.ins.removeByGroupId(groupId);
      ProviderManager.ins.removeByGroupId(groupId);
      BatchManager.ins.removeByGroupId(groupId);
   }
}
