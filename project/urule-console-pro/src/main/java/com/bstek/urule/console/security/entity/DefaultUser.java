package com.bstek.urule.console.security.entity;

import com.bstek.urule.console.database.model.Group;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultUser implements User, Serializable {
   private static final long serialVersionUID = -6556033039846260545L;
   private String name;
   private String desc;
   private List groups = new ArrayList();

   public DefaultUser(String name, String desc, List groups) {
      this.name = name;
      this.desc = desc;
      this.groups = groups;
   }
   public String getName() {
      return this.name;
   }
   public String getDesc() {
      return this.desc;
   }
   public List getGroups() {
      return this.groups;
   }

   public String toString() {
      StringBuffer stringBuffer = new StringBuffer(this.name);
      stringBuffer.append(" desc:");
      stringBuffer.append(this.desc);
      stringBuffer.append(" group:(");

      for(Group group : (Iterable<Group>)(Iterable<?>)(this.groups)) {
         stringBuffer.append(group.toString()).append(";");
      }

      stringBuffer.append(")");
      return stringBuffer.toString();
   }
   public void setGroups(List groups) {
      this.groups = groups;
   }
}
