package com.bstek.urule.console.editor.json;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class JsonServletHandler extends ApiServletHandler {
   public void convert(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("json");
      if (StringUtils.isBlank(parameter)) {
         throw new RuleException("JSON不能为空！");
      } else {
         parameter = parameter.trim();
         StringBuilder stringBuilder = new StringBuilder();

         try {
            ObjectMapper objectMapper = JsonMapper.builder().build();
            if (parameter.startsWith("[") && parameter.endsWith("]")) {
               List items = (List)objectMapper.readValue(parameter, ArrayList.class);
               stringBuilder.append(this.convertListToVariableDefinitionJson(items));
            } else {
               if (!parameter.startsWith("{") || !parameter.endsWith("}")) {
                  throw new RuleException("不支持的JSON格式");
               }

               Map valuesByKey = (Map)objectMapper.readValue(parameter, HashMap.class);
               stringBuilder.append(this.convertMapToVariableDefinitionJson(valuesByKey));
            }
         } catch (Exception exception) {
            throw new RuleException("不支持的JSON格式");
         }

         HashMap valuesByKey2 = new HashMap();
         valuesByKey2.put("json", stringBuilder.toString());
         this.writeObjectToJson(resp, valuesByKey2);
      }
   }

   public void gzip(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("json");
      if (StringUtils.isBlank(parameter)) {
         throw new RuleException("JSON不能为空！");
      } else {
         parameter = parameter.trim();
         HashMap valuesByKey = new HashMap();
         valuesByKey.put("data", this.doGzip(parameter));
         this.writeObjectToJson(resp, valuesByKey);
      }
   }

   public String doGzip(String json) throws Exception {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
      gZIPOutputStream.write(json.getBytes("utf-8"));
      gZIPOutputStream.close();
      Base64 base64 = new Base64();
      return base64.encodeAsString(byteArrayOutputStream.toByteArray());
   }

   private String convertMapToVariableDefinitionJson(Map valuesByKey) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{");
      stringBuilder.append("\"name\"");
      stringBuilder.append(":");
      stringBuilder.append("\"待填写的变量分类名\"");
      stringBuilder.append(",");
      stringBuilder.append("\"fields\"");
      stringBuilder.append(":");
      stringBuilder.append("{");
      int number = 0;

      for(String text : (Iterable<String>)(Iterable<?>)(valuesByKey.keySet())) {
         Object objectValue = valuesByKey.get(text);
         if (objectValue != null) {
            if (number > 0) {
               stringBuilder.append(",");
            }

            stringBuilder.append("\"" + text + "\"");
            stringBuilder.append(":");
            if (objectValue instanceof Map) {
               stringBuilder.append(this.convertMapToVariableDefinitionJson((Map)objectValue));
            } else if (objectValue instanceof List) {
               stringBuilder.append(this.convertListToVariableDefinitionJson((List)objectValue));
            } else if (objectValue instanceof Number) {
               stringBuilder.append(objectValue);
            } else {
               stringBuilder.append("\"" + objectValue + "\"");
            }

            ++number;
         }
      }

      stringBuilder.append("}");
      stringBuilder.append("}");
      return stringBuilder.toString();
   }

   private String convertListToVariableDefinitionJson(List items) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[");
      int number = 0;

      for(Map valuesByKey : (Iterable<Map>)(Iterable<?>)(items)) {
         if (number > 0) {
            stringBuilder.append(",");
         }

         stringBuilder.append(this.convertMapToVariableDefinitionJson(valuesByKey));
         ++number;
      }

      stringBuilder.append("]");
      return stringBuilder.toString();
   }

   public String url() {
      return "/json";
   }
}
