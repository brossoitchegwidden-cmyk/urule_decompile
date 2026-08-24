package com.bstek.urule.console.anonymous.knowledge;

import com.bstek.urule.Configure;
import com.bstek.urule.Utils;
import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.anonymous.ValidateUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketData;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

public class LoadKnowledgeServletHandler extends AnonymousServletHandler {
   public static final String URL = "/loadknowledge";
   private RemoteDynamicJarsBuilder remoteDynamicJarsBuilder = ServiceUtils.getRemoteDynamicJarsBuilder();

   public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      if (ValidateUtils.validateUserPwd(req, this.remoteDynamicJarsBuilder.getUser(), this.remoteDynamicJarsBuilder.getPwd())) {
         String parameter = req.getParameter("packageId");
         if (StringUtils.isEmpty(parameter)) {
            String text = "<h1>PackageId can not be null<h1>";
            this.writeResponse(resp, text);
         } else {
            parameter = Utils.decodeURL(parameter);
            String parameter2 = req.getParameter("timestamp");
            PacketData packet = PacketCache.ins.getPacket(parameter);
            if (packet == null) {
               long longValue = 0L;

               try {
                  longValue = Long.valueOf(parameter);
               } catch (NumberFormatException numberFormatException) {
                  throw new RuleException("Package [" + parameter + "] not exist");
               }

               packet = PacketCache.ins.getPacket(longValue);
            }

            if (packet == null) {
               throw new RuleException("Package [" + parameter + "] not exist");
            } else {
               KnowledgePackageWrapper knowledgePackageWrapper = packet.getKnowledgePackageWrapper();
               KnowledgePackage knowledgePackage = knowledgePackageWrapper.getKnowledgePackage();
               boolean flag = false;
               String parameter3 = req.getParameter("debug");
               if (parameter3 != null && parameter3.equals("true") && Utils.isDebug()) {
                  flag = true;
               }

               if (StringUtils.isNotEmpty(parameter2)) {
                  long longValue2 = Long.valueOf(parameter2);
                  long timestamp = knowledgePackage.getTimestamp();
                  if (timestamp > longValue2) {
                     if (flag) {
                        this.writeObjectToJson(resp, (Object)knowledgePackageWrapper);
                     } else {
                        this.writeResponse(resp, packet);
                     }
                  }
               } else if (flag) {
                  this.writeObjectToJson(resp, (Object)knowledgePackageWrapper);
               } else {
                  this.writeResponse(resp, packet);
               }

            }
         }
      }
   }

   private void writeResponse(HttpServletResponse httpServletResponse, String text) throws IOException {
      httpServletResponse.setContentType("text/html");
      PrintWriter writer = httpServletResponse.getWriter();
      writer.write("<html>");
      writer.write("<header>");
      writer.write("</header>");
      writer.write("<body>");
      writer.write(text);
      writer.write("</body>");
      writer.write("</html>");
      writer.flush();
      writer.close();
   }

   private void writeResponse(HttpServletResponse httpServletResponse, PacketData packetData) throws IOException {
      httpServletResponse.setContentType("text/json");
      httpServletResponse.setCharacterEncoding("UTF-8");
      byte[] knowledgeContent = PacketCache.ins.getKnowledgeContent(packetData.getPacket().getId());
      if (knowledgeContent == null) {
         JsonMapper.Builder builder = JsonMapper.builder();
         builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
         ObjectMapper objectMapper = builder.build();
         objectMapper.setSerializationInclusion(Include.NON_NULL);
         objectMapper.setDateFormat(new SimpleDateFormat(Configure.getDateFormat()));
         String text = objectMapper.writeValueAsString(packetData.getKnowledgePackageWrapper());
         knowledgeContent = Utils.compress(text);
      }

      ServletOutputStream outputStream = httpServletResponse.getOutputStream();
      ((OutputStream)outputStream).write(knowledgeContent);
      ((OutputStream)outputStream).flush();
      ((OutputStream)outputStream).close();
   }

   public String url() {
      return "/loadknowledge";
   }
}
