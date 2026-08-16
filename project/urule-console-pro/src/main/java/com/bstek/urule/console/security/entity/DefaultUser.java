package com.bstek.urule.console.security.entity;

import com.bstek.urule.console.database.model.Group;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultUser implements User, Serializable {
   private static final long a = -6556033039846260545L;
   private String b;
   private String c;
   private List d = new ArrayList();

   public DefaultUser(String var1, String var2, List var3) {
      this.b = var1;
      this.c = var2;
      this.d = var3;
   }

   public String getName() {
      return this.b;
   }

   public String getDesc() {
      return this.c;
   }

   public List getGroups() {
      return this.d;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(this.b);
      var1.append(" desc:");
      var1.append(this.c);
      var1.append(" group:(");

      for(Group var3 : (Iterable<Group>)(Iterable<?>)(this.d)) {
         var1.append(var3.toString()).append(";");
      }

      var1.append(")");
      return var1.toString();
   }

   public void setGroups(List var1) {
      this.d = var1;
   }
}
