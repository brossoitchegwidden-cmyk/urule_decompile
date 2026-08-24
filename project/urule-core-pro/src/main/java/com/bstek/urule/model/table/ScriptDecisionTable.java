package com.bstek.urule.model.table;

import com.bstek.urule.model.rule.Library;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptDecisionTable {
   private List<Row> rows;
   private List<Column> columns;
   private Map<String, ScriptCell> cellMap;
   private List<Library> libraries;

   public List<Row> getRows() {
      return this.rows;
   }

   public void addLibrary(Library library) {
      if (this.libraries == null) {
         this.libraries = new ArrayList<>();
      }

      this.libraries.add(library);
   }

   public void addRow(Row row) {
      if (this.rows == null) {
         this.rows = new ArrayList<>();
      }

      this.rows.add(row);
   }

   public void addColumn(Column col) {
      if (this.columns == null) {
         this.columns = new ArrayList<>();
      }

      this.columns.add(col);
   }

   public void addCell(ScriptCell cell) {
      if (this.cellMap == null) {
         this.cellMap = new HashMap<>();
      }

      this.cellMap.put(this.buildCellKey(cell.getRow(), cell.getCol()), cell);
   }

   public void setRows(List<Row> rows) {
      this.rows = rows;
   }

   public List<Column> getColumns() {
      return this.columns;
   }

   public void setColumns(List<Column> columns) {
      this.columns = columns;
   }

   public Map<String, ScriptCell> getCellMap() {
      return this.cellMap;
   }

   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> libraries) {
      this.libraries = libraries;
   }

   public String buildCellKey(int row, int col) {
      return row + "," + col;
   }
}
