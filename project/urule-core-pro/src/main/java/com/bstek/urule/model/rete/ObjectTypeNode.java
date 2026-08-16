package com.bstek.urule.model.rete;

import com.bstek.urule.ClassUtils;
import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.ObjectTypeActivity;
import java.util.Map;

public class ObjectTypeNode extends BaseReteNode {
   public static final String NON_CLASS = "*";
   public static final String NONE_CONDITION = "__*__";
   private String objectTypeClass;
   private NodeType nodeType = NodeType.objectType;

   public ObjectTypeNode() {
      super(0);
   }

   public ObjectTypeNode(String var1, int var2) {
      super(var2);
      this.objectTypeClass = var1;
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   public boolean support(Object var1) {
      return this.support(var1.getClass().getName());
   }

   public boolean support(String var1) {
      return this.objectTypeClass.equals(var1);
   }

   public String getObjectTypeClass() {
      return this.objectTypeClass;
   }

   public void setObjectTypeClass(String var1) {
      this.objectTypeClass = var1;
   }

   @Override
   public Activity newActivity(Map<Object, Object> var1) {
      Class var2 = null;
      ObjectTypeActivity var3 = null;
      if (this.objectTypeClass.equals("__*__")) {
         var3 = new ObjectTypeActivity(this.objectTypeClass);
      } else {
         try {
            if (!this.objectTypeClass.equals("*")) {
               var2 = ClassUtils.getTargetClassDefaultNull(this.objectTypeClass);
            }

            if (var2 == null) {
               var3 = new ObjectTypeActivity(this.objectTypeClass);
            } else {
               var3 = new ObjectTypeActivity(var2);
            }
         } catch (Exception var6) {
            var3 = new ObjectTypeActivity(this.objectTypeClass);
         }
      }

      for (Line var5 : this.lines) {
         var3.addPath(var5.newPath(var1));
      }

      return var3;
   }
}
