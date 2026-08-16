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
   private static final Log a = LogFactory.getLog(ServerCacheManager.class);
   private DynamicSpringConfigLoader b = ServiceUtils.getDynamicSpringConfigLoader();

   private boolean a(String var1) {
      if (var1 != null) {
         try {
            var1 = URLDecoder.decode(var1, "utf-8");
         } catch (UnsupportedEncodingException var3) {
            a.error(var3);
         }

         if (var1.contentEquals(Utils.SystemId)) {
            return true;
         }
      }

      return false;
   }

   public void recacheAllPackets(String var1) {
      if (!this.a(var1)) {
         ((PacketCacheImpl)PacketCache.ins).doRecacheAllPackets();
         IDGenerator.getInstance().clean();
      }
   }

   public void reloadPacket(String var1, long var2) {
      if (!this.a(var1)) {
         ((PacketCacheImpl)PacketCache.ins).doReloadPacket(var2);
      }
   }

   public void removePacket(String var1) {
      ((PacketCacheImpl)PacketCache.ins).removePacket(var1);
   }

   public void syncPacketForRemoveProject(String var1, long var2) {
      if (!this.a(var1)) {
         ((PacketCacheImpl)PacketCache.ins).doRemoveProjectPackets(var2);
      }
   }

   public void reloadDynamicJars(String var1) throws Exception {
      if (!this.a(var1)) {
         String var2 = this.b.buildDynamicJarsStoreDirectPath();
         int var3 = DynamicJarManager.ins.createJarFiles(var2);
         if (var3 != 0) {
            this.b.loadDynamicJars(var2);
         }
      }
   }
}
