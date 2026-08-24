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
   private Map packetsById = new ConcurrentHashMap();
   private Map packetsByCode = new ConcurrentHashMap();
   private Map serializedKnowledgeByPacketId = new ConcurrentHashMap();

   public PacketData getPacket(long id) {
      return (PacketData)this.packetsById.get(id);
   }

   public PacketData getPacket(String code) {
      return (PacketData)this.packetsByCode.get(code);
   }

   public byte[] getKnowledgeWrapper(long id) {
      return (byte[])this.serializedKnowledgeByPacketId.get(id);
   }

   public void putPacket(long id, PacketData pd) {
      this.packetsById.put(id, pd);
      byte[] bytes = this.resolveByte(pd.getKnowledgePackageWrapper());
      this.serializedKnowledgeByPacketId.put(id, bytes);
   }

   private byte[] resolveByte(KnowledgePackageWrapper knowledgePackageWrapper) {
      JsonMapper.Builder builder = JsonMapper.builder();
      builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      ObjectMapper objectMapper = builder.build();
      objectMapper.setSerializationInclusion(Include.NON_NULL);
      objectMapper.setDateFormat(new SimpleDateFormat(Configure.getDateFormat()));

      try {
         String text = objectMapper.writeValueAsString(knowledgePackageWrapper);
         return Utils.compress(text);
      } catch (JsonProcessingException jsonProcessingException) {
         java.util.logging.Logger.getLogger(MemoryPacketCache.class.getName()).log(java.util.logging.Level.SEVERE, jsonProcessingException.getMessage(), jsonProcessingException);
         throw new InfoException("尝试将知识包序列化并缓存出错:" + jsonProcessingException.getMessage());
      }
   }

   public void clear() {
      this.packetsById.clear();
      this.packetsByCode.clear();
      this.serializedKnowledgeByPacketId.clear();
   }

   public void remove(long id) {
      this.packetsById.remove(id);
      this.serializedKnowledgeByPacketId.remove(id);
   }

   public void remove(String code) {
      this.packetsByCode.remove(code);
   }

   public Map getPacketIdMap() {
      HashMap packetIdMap = new HashMap();
      packetIdMap.putAll(this.packetsById);
      return packetIdMap;
   }

   public Map getPacketCodeMap() {
      HashMap packetCodeMap = new HashMap();
      packetCodeMap.putAll(this.packetsByCode);
      return packetCodeMap;
   }

   public void putPacket(String code, PacketData pd) {
      this.packetsByCode.put(pd.getPacket().getCode(), pd);
   }
}
