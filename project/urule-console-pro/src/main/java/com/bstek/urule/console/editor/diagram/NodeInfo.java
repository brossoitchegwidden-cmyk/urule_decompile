package com.bstek.urule.console.editor.diagram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public class NodeInfo {
   private int id;
   @JsonIgnore
   private int level;
   private String label;
   private String title;
   private String color;
   private int xCoordinate;
   private int yCoordinate;
   private int width;
   private int height;
   private int roundCorner;
   private List children;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public String getColor() {
      return this.color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public int getX() {
      return this.xCoordinate;
   }

   public void setX(int xCoordinate) {
      this.xCoordinate = xCoordinate;
   }

   public int getY() {
      return this.yCoordinate;
   }

   public void setY(int yCoordinate) {
      this.yCoordinate = yCoordinate;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public int getRoundCorner() {
      return this.roundCorner;
   }

   public void setRoundCorner(int roundCorner) {
      this.roundCorner = roundCorner;
   }

   public List getChildren() {
      return this.children;
   }

   public void addChild(NodeInfo nodeInfo) {
      if (this.children == null) {
         this.children = new ArrayList();
      }

      this.children.add(nodeInfo);
   }
}
