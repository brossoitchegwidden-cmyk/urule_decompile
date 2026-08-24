package com.bstek.urule.console.database.model;

import java.util.List;

public class Page {
   private int pageNumber = 1;
   private int pageSize = 10;
   private long startRow;
   private long endRow;
   private int totalPage;
   private long totalRows;
   private List data;

   public Page(int pageIndex, int pageSize) {
      this.pageNumber = pageIndex;
      this.pageSize = pageSize;
      this.startRow = pageIndex <= 1 ? 0L : (long)((pageIndex - 1) * pageSize);
      this.endRow = (long)(pageSize - 1);
   }

   public int getPageNumber() {
      return this.pageNumber;
   }

   public int getPageSize() {
      return this.pageSize;
   }

   private void initializeState() {
      if (this.pageSize > 0) {
         this.totalPage = Double.valueOf(Math.ceil(Double.valueOf((double)this.totalRows) / (double)this.pageSize)).intValue();
         this.startRow = this.pageNumber <= 1 ? 0L : (long)((this.pageNumber - 1) * this.pageSize);
         this.endRow = this.startRow + (long)this.pageSize - 1L;
      } else {
         this.totalPage = 0;
         this.startRow = 0L;
         this.endRow = (long)(this.pageSize - 1);
      }

      if (this.pageNumber > this.totalPage) {
         this.pageNumber = this.totalPage;
      }

   }

   public long getTotalRows() {
      return this.totalRows;
   }

   public void setTotalRows(long totalRows) {
      this.totalRows = totalRows;
      this.initializeState();
   }

   public List getData() {
      return this.data;
   }

   public void setData(List data) {
      this.data = data;
   }

   public long getStartRow() {
      return this.startRow;
   }

   public long getEndRow() {
      return this.endRow;
   }

   public void setEndRow(long endRow) {
      this.endRow = endRow;
   }

   public int getTotalPage() {
      return this.totalPage;
   }

   public void setTotalPage(int totalPage) {
      this.totalPage = totalPage;
   }

   public void setStartRow(long startRow) {
      this.startRow = startRow;
   }
}
