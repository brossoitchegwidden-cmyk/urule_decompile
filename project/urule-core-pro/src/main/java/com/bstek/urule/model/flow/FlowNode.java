package com.bstek.urule.model.flow;

import com.bstek.urule.Utils;
import com.bstek.urule.model.Node;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.flow.ins.ProcessInstance;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

public abstract class FlowNode implements Node {
   protected String name;
   protected String eventBean;
   protected String x;
   protected String y;
   protected String width;
   protected String height;
   protected static final String EXCEPTION_DATA = "_node_business_exception_";
   protected List<Connection> connections;

   public FlowNode() {
   }

   public FlowNode(String var1) {
      this.name = var1;
   }

   public final void enter(Exception var1, FlowContext var2, FlowInstance var3) {
      if (var1 == null || this instanceof ExceptionNode) {
         if (var3.isDebug()) {
            var2.getLogger().logFlowNode(this, var3.getProcessDefinition().getFile(), true);
         }

         this.enterNode(var1, var2, var3);
      }
   }

   public abstract void enterNode(Exception var1, FlowContext var2, FlowInstance var3);

   protected void leave(String var1, FlowContext var2, FlowInstance var3, Exception var4) {
      this.handleExeption(var4);
      if (var3.isDebug()) {
         var2.getLogger().logFlowNode(this, var3.getProcessDefinition().getFile(), false);
      }

      for (Connection var6 : this.connections) {
         if (var1 != null) {
            String var7 = var6.getName();
            var7 = var7 == null ? var7 : var7.trim();
            if (var1.trim().equals(var7)) {
               var6.execute(var4, var2, var3);
               break;
            }
         } else if (this instanceof DecisionNode) {
            if (var4 == null) {
               break;
            }

            var6.execute(var4, var2, var3);
         } else {
            var6.execute(var4, var2, var3);
            if (var4 == null) {
               break;
            }
         }
      }
   }

   private void handleExeption(Exception var1) {
      if (var1 != null) {
         boolean var2 = false;

         for (Connection var4 : this.connections) {
            FlowNode var5 = var4.getTo();
            if (var5 instanceof ExceptionNode) {
               var2 = true;
               break;
            }
         }

         if (!var2) {
            StringBuilder var6 = new StringBuilder();
            Utils.buildCause(var1, var6);
            throw new RuntimeException(var6.toString(), var1);
         }
      }
   }

   protected void executeNodeEvent(EventType var1, FlowContext var2, ProcessInstance var3) {
      if (!StringUtils.isEmpty(this.eventBean)) {
         ApplicationContext var4 = var2.getApplicationContext();
         NodeEvent var5 = (NodeEvent)var4.getBean(this.eventBean);
         if (var1.equals(EventType.enter)) {
            var5.enter(this, var3, var2);
         } else {
            var5.leave(this, var3, var2);
         }
      }
   }

   public abstract FlowNodeType getType();

   public List<Connection> getConnections() {
      return this.connections;
   }

   public void setConnections(List<Connection> var1) {
      this.connections = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getEventBean() {
      return this.eventBean;
   }

   public void setEventBean(String var1) {
      this.eventBean = var1;
   }

   public String getX() {
      return this.x;
   }

   public void setX(String var1) {
      this.x = var1;
   }

   public String getY() {
      return this.y;
   }

   public void setY(String var1) {
      this.y = var1;
   }

   public String getWidth() {
      return this.width;
   }

   public void setWidth(String var1) {
      this.width = var1;
   }

   public String getHeight() {
      return this.height;
   }

   public void setHeight(String var1) {
      this.height = var1;
   }
}
