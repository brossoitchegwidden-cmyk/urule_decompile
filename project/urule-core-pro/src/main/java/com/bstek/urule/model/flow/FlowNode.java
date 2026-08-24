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
   protected String xCoordinate;
   protected String yCoordinate;
   protected String width;
   protected String height;
   protected static final String EXCEPTION_DATA = "_node_business_exception_";
   protected List<Connection> connections;

   public FlowNode() {
   }

   public FlowNode(String name) {
      this.name = name;
   }

   public final void enter(Exception ex, FlowContext context, FlowInstance instance) {
      if (ex == null || this instanceof ExceptionNode) {
         if (instance.isDebug()) {
            context.getLogger().logFlowNode(this, instance.getProcessDefinition().getFile(), true);
         }

         this.enterNode(ex, context, instance);
      }
   }

   public abstract void enterNode(Exception ex, FlowContext context, FlowInstance instance);

   protected void leave(String connectionName, FlowContext context, FlowInstance instance, Exception ex) {
      this.handleExeption(ex);
      if (instance.isDebug()) {
         context.getLogger().logFlowNode(this, instance.getProcessDefinition().getFile(), false);
      }

      for (Connection connection : this.connections) {
         if (connectionName != null) {
            String name = connection.getName();
            name = name == null ? name : name.trim();
            if (connectionName.trim().equals(name)) {
               connection.execute(ex, context, instance);
               break;
            }
         } else if (this instanceof DecisionNode) {
            if (ex == null) {
               break;
            }

            connection.execute(ex, context, instance);
         } else {
            connection.execute(ex, context, instance);
            if (ex == null) {
               break;
            }
         }
      }
   }

   private void handleExeption(Exception exception) {
      if (exception != null) {
         boolean flag = false;

         for (Connection connection : this.connections) {
            FlowNode to = connection.getTo();
            if (to instanceof ExceptionNode) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            StringBuilder stringBuilder = new StringBuilder();
            Utils.buildCause(exception, stringBuilder);
            throw new RuntimeException(stringBuilder.toString(), exception);
         }
      }
   }

   protected void executeNodeEvent(EventType type, FlowContext context, ProcessInstance instance) {
      if (!StringUtils.isEmpty(this.eventBean)) {
         ApplicationContext applicationContext = context.getApplicationContext();
         NodeEvent nodeEvent = (NodeEvent)applicationContext.getBean(this.eventBean);
         if (type.equals(EventType.enter)) {
            nodeEvent.enter(this, instance, context);
         } else {
            nodeEvent.leave(this, instance, context);
         }
      }
   }

   public abstract FlowNodeType getType();

   public List<Connection> getConnections() {
      return this.connections;
   }

   public void setConnections(List<Connection> connections) {
      this.connections = connections;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getEventBean() {
      return this.eventBean;
   }

   public void setEventBean(String eventBean) {
      this.eventBean = eventBean;
   }

   public String getX() {
      return this.xCoordinate;
   }

   public void setX(String text) {
      this.xCoordinate = text;
   }

   public String getY() {
      return this.yCoordinate;
   }

   public void setY(String text) {
      this.yCoordinate = text;
   }

   public String getWidth() {
      return this.width;
   }

   public void setWidth(String width) {
      this.width = width;
   }

   public String getHeight() {
      return this.height;
   }

   public void setHeight(String height) {
      this.height = height;
   }
}
