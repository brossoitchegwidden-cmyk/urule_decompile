package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Filter {
   private String uuid;
   private int index;
   private String name;
   private List filterItems = new ArrayList();

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List getItems() {
      return this.filterItems;
   }

   public void setItems(List items) {
      this.filterItems = items;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }
}
