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

   public ObjectTypeNode(String objectTypeClass, int id) {
      super(id);
      this.objectTypeClass = objectTypeClass;
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   public boolean support(Object object) {
      return this.support(object.getClass().getName());
   }

   public boolean support(String className) {
      return this.objectTypeClass.equals(className);
   }

   public String getObjectTypeClass() {
      return this.objectTypeClass;
   }

   public void setObjectTypeClass(String objectTypeClass) {
      this.objectTypeClass = objectTypeClass;
   }

   @Override
   public Activity newActivity(Map<Object, Object> context) {
      Class targetClassDefaultNull = null;
      ObjectTypeActivity objectTypeActivity = null;
      if (this.objectTypeClass.equals("__*__")) {
         objectTypeActivity = new ObjectTypeActivity(this.objectTypeClass);
      } else {
         try {
            if (!this.objectTypeClass.equals("*")) {
               targetClassDefaultNull = ClassUtils.getTargetClassDefaultNull(this.objectTypeClass);
            }

            if (targetClassDefaultNull == null) {
               objectTypeActivity = new ObjectTypeActivity(this.objectTypeClass);
            } else {
               objectTypeActivity = new ObjectTypeActivity(targetClassDefaultNull);
            }
         } catch (Exception exception) {
            objectTypeActivity = new ObjectTypeActivity(this.objectTypeClass);
         }
      }

      for (Line line : this.lines) {
         objectTypeActivity.addPath(line.newPath(context));
      }

      return objectTypeActivity;
   }
}
