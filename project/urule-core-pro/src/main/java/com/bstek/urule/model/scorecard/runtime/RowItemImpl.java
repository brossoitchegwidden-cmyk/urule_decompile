package com.bstek.urule.model.scorecard.runtime;

import java.util.ArrayList;
import java.util.List;

public class RowItemImpl implements RowItem {
   private int rowNumber;
   private Object score;
   private Object actualScore;
   private String weight;
   private List<CellItem> cellItems;

   @Override
   public int getRowNumber() {
      return this.rowNumber;
   }

   public void setRowNumber(int rowNumber) {
      this.rowNumber = rowNumber;
   }

   @Override
   public Object getScore() {
      return this.score;
   }

   public void setScore(Object score) {
      this.score = score;
   }

   @Override
   public Object getActualScore() {
      return this.actualScore;
   }

   @Override
   public void setActualScore(Object actualScore) {
      this.actualScore = actualScore;
   }

   @Override
   public String getWeight() {
      return this.weight;
   }

   public void setWeight(String weight) {
      this.weight = weight;
   }

   @Override
   public List<CellItem> getCellItems() {
      return this.cellItems;
   }

   public void setCellItems(List<CellItem> cellItems) {
      this.cellItems = cellItems;
   }

   public void addCellItem(CellItem cellItem) {
      if (this.cellItems == null) {
         this.cellItems = new ArrayList<>();
      }

      this.cellItems.add(cellItem);
   }
}
