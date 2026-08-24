package com.bstek.urule.console.cache;

import com.bstek.urule.Utils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketCacheImpl;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.manager.jar.DynamicJarManager;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerCacheManager {
   private static final Log logger = LogFactory.getLog(ServerCacheManager.class);
   private DynamicSpringConfigLoader dynamicSpringConfigLoader = ServiceUtils.getDynamicSpringConfigLoader();

   private boolean evaluateCondition(String text) {
      if (text != null) {
         try {
            text = URLDecoder.decode(text, "utf-8");
         } catch (UnsupportedEncodingException unsupportedEncodingException) {
            ServerCacheManager.logger.error(unsupportedEncodingException);
         }

         if (text.contentEquals(Utils.SystemId)) {
            return true;
         }
      }

      return false;
   }

   /**集群服务器重新加载所有知识包*/
   public void recacheAllPackets(String systemId) {
      if (!this.evaluateCondition(systemId)) {
         ((PacketCacheImpl)PacketCache.ins).doRecacheAllPackets();
         IDGenerator.getInstance().clean();
      }
   }

   /**集群服务器知识包缓存更新*/
   public void reloadPacket(String systemId, long id) {
      if (!this.evaluateCondition(systemId)) {
         ((PacketCacheImpl)PacketCache.ins).doReloadPacket(id);
      }
   }

   /**知识包编码修改*/
   public void removePacket(String code) {
      ((PacketCacheImpl)PacketCache.ins).removePacket(code);
   }

   /**集群服务器项目删除时缓存清除*/
   public void syncPacketForRemoveProject(String systemId, long projectId) {
      if (!this.evaluateCondition(systemId)) {
         ((PacketCacheImpl)PacketCache.ins).doRemoveProjectPackets(projectId);
      }
   }

   /**集群服务器动态加载jar*/
   public void reloadDynamicJars(String systemId) throws Exception {
      if (!this.evaluateCondition(systemId)) {
         String dynamicJarsStoreDirectPath = this.dynamicSpringConfigLoader.buildDynamicJarsStoreDirectPath();
         int jarFiles = DynamicJarManager.ins.createJarFiles(dynamicJarsStoreDirectPath);
         if (jarFiles != 0) {
            this.dynamicSpringConfigLoader.loadDynamicJars(dynamicJarsStoreDirectPath);
         }
      }
   }
}
