package com.bstek.urule.console.cache.packet;

import com.bstek.urule.Configure;
import com.bstek.urule.Utils;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryPacketCache {
   private Map a = new ConcurrentHashMap();
   private Map b = new ConcurrentHashMap();
   private Map c = new ConcurrentHashMap();

   public PacketData getPacket(long var1) {
      return (PacketData)this.a.get(var1);
   }

   public PacketData getPacket(String var1) {
      return (PacketData)this.b.get(var1);
   }

   public byte[] getKnowledgeWrapper(long var1) {
      return (byte[])this.c.get(var1);
   }

   public void putPacket(long var1, PacketData var3) {
      this.a.put(var1, var3);
      byte[] var4 = this.a(var3.getKnowledgePackageWrapper());
      this.c.put(var1, var4);
   }

   private byte[] a(KnowledgePackageWrapper var1) {
      JsonMapper.Builder var2 = JsonMapper.builder();
      var2.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      ObjectMapper var3 = var2.build();
      var3.setSerializationInclusion(Include.NON_NULL);
      var3.setDateFormat(new SimpleDateFormat(Configure.getDateFormat()));

      try {
         String var4 = var3.writeValueAsString(var1);
         return Utils.compress(var4);
      } catch (JsonProcessingException var6) {
         var6.printStackTrace();
         throw new InfoException("尝试将知识包序列化并缓存出错:" + var6.getMessage());
      }
   }

   public void clear() {
      this.a.clear();
      this.b.clear();
      this.c.clear();
   }

   public void remove(long var1) {
      this.a.remove(var1);
      this.c.remove(var1);
   }

   public void remove(String var1) {
      this.b.remove(var1);
   }

   public Map getPacketIdMap() {
      HashMap var1 = new HashMap();
      var1.putAll(this.a);
      return var1;
   }

   public Map getPacketCodeMap() {
      HashMap var1 = new HashMap();
      var1.putAll(this.b);
      return var1;
   }

   public void putPacket(String var1, PacketData var2) {
      this.b.put(var2.getPacket().getCode(), var2);
   }
}
