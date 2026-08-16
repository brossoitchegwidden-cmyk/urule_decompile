package com.bstek.urule.console.editor.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReteNodeLayout {
   private final int a = 50;
   private final int b = 50;
   private final int c = 30;
   private final int d = 30;

   public Box layout(NodeInfo var1) {
      List var2 = var1.getChildren();
      if (var2 == null) {
         return null;
      } else {
         HashMap var3 = new HashMap();
         HashMap var4 = new HashMap();
         this.a(var2, var4);
         int var5 = this.a(var4) - 1;
         int var6 = var5 * 30 + var5 * 50;
         int var7 = var6 / 2 + 50 + 30;
         var1.setX(var7);
         var1.setY(5);
         this.a(var2, var1, var4, var3);
         Box var8 = new Box();
         var8.setWidth(var6 + 100 + 30);
         int var9 = var4.size() * 30 * 3 + 100;
         var8.setHeight(var9);
         return var8;
      }
   }

   private void a(List var1, NodeInfo var2, Map var3, Map var4) {
      for(int var5 = 0; var5 < var1.size(); ++var5) {
         NodeInfo var6 = (NodeInfo)var1.get(var5);
         int var7 = var6.getLevel();
         var6.setY(var7 * 50 + var7 * 30);
         List var8 = var6.getChildren();
         int var9 = 0;
         if (var4.containsKey(var7)) {
            var9 = (Integer)var4.get(var7);
         }

         int var10 = var2.getX();
         int var11 = ((List)var3.get(var7)).size();
         if (var9 == 0) {
            if (var11 > 1) {
               int var12 = var11 * 30 + var11 * 50;
               var9 = var10 - var12 / 2 - 50;
            } else {
               var9 = var10;
            }
         }

         int var13 = 80 + var9;
         if (var11 == 1) {
            var13 = var9;
         }

         var6.setX(var13);
         var4.put(var7, var13);
         if (var8 != null) {
            this.a(var8, var2, var3, var4);
         }
      }

   }

   private int a(Map var1) {
      int var2 = 1;

      for(List var4 : (Iterable<List>)(Iterable<?>)(var1.values())) {
         if (var4.size() > var2) {
            var2 = var4.size();
         }
      }

      return var2;
   }

   private void a(List var1, Map var2) {
      for(NodeInfo var4 : (Iterable<NodeInfo>)(Iterable<?>)(var1)) {
         int var5 = var4.getLevel();
         if (var2.containsKey(var5)) {
            List var6 = (List)var2.get(var5);
            var6.add(var4);
         } else {
            ArrayList var7 = new ArrayList();
            var7.add(var4);
            var2.put(var5, var7);
         }

         List var8 = var4.getChildren();
         if (var8 != null) {
            this.a(var8, var2);
         }
      }

   }
}
