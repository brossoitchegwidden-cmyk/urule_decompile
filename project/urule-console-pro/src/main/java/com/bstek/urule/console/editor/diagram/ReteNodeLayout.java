package com.bstek.urule.console.editor.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReteNodeLayout {
   public Box layout(NodeInfo rootNode) {
      List children = rootNode.getChildren();
      if (children == null) {
         return null;
      } else {
         HashMap valuesByKey = new HashMap();
         HashMap valuesByKey2 = new HashMap();
         this.groupNodesByLevel(children, valuesByKey2);
         int number = this.maxNodesPerLevel(valuesByKey2) - 1;
         int number2 = number * 30 + number * 50;
         int number3 = number2 / 2 + 50 + 30;
         rootNode.setX(number3);
         rootNode.setY(5);
         this.positionNodes(children, rootNode, valuesByKey2, valuesByKey);
         Box box = new Box();
         box.setWidth(number2 + 100 + 30);
         int number4 = valuesByKey2.size() * 30 * 3 + 100;
         box.setHeight(number4);
         return box;
      }
   }

   private void positionNodes(List items, NodeInfo nodeInfo, Map valuesByKey, Map valuesByKey2) {
      for(int index = 0; index < items.size(); ++index) {
         NodeInfo nodeInfo2 = (NodeInfo)items.get(index);
         int level = nodeInfo2.getLevel();
         nodeInfo2.setY(level * 50 + level * 30);
         List children = nodeInfo2.getChildren();
         int number = 0;
         if (valuesByKey2.containsKey(level)) {
            number = (Integer)valuesByKey2.get(level);
         }

         int number2 = nodeInfo.getX();
         int number3 = ((List)valuesByKey.get(level)).size();
         if (number == 0) {
            if (number3 > 1) {
               int number4 = number3 * 30 + number3 * 50;
               number = number2 - number4 / 2 - 50;
            } else {
               number = number2;
            }
         }

         int number5 = 80 + number;
         if (number3 == 1) {
            number5 = number;
         }

         nodeInfo2.setX(number5);
         valuesByKey2.put(level, number5);
         if (children != null) {
            this.positionNodes(children, nodeInfo, valuesByKey, valuesByKey2);
         }
      }

   }

   private int maxNodesPerLevel(Map valuesByKey) {
      int number = 1;

      for(List items : (Iterable<List>)(Iterable<?>)(valuesByKey.values())) {
         if (items.size() > number) {
            number = items.size();
         }
      }

      return number;
   }

   private void groupNodesByLevel(List items, Map valuesByKey) {
      for(NodeInfo nodeInfo : (Iterable<NodeInfo>)(Iterable<?>)(items)) {
         int level = nodeInfo.getLevel();
         if (valuesByKey.containsKey(level)) {
            List items2 = (List)valuesByKey.get(level);
            items2.add(nodeInfo);
         } else {
            ArrayList items3 = new ArrayList();
            items3.add(nodeInfo);
            valuesByKey.put(level, items3);
         }

         List children = nodeInfo.getChildren();
         if (children != null) {
            this.groupNodesByLevel(children, valuesByKey);
         }
      }

   }
}
