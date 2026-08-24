package com.bstek.urule.runtime;

import com.bstek.urule.model.library.Datatype;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParameterManager {
   private static ParameterManager instance = new ParameterManager();

   private ParameterManager() {
   }

   public static ParameterManager getInstance() {
      return ParameterManager.instance;
   }

   public void initKnowledgePackageParameters(KnowledgePackage knowledgePackage, Map<String, Object> initParameters) {
      Map parameters = knowledgePackage.getParameters();
      if (parameters != null) {
         for (String text : (Iterable<String>)(Iterable<?>)(parameters.keySet())) {
            Datatype datatype = Datatype.valueOf((String)parameters.get(text));
            if (datatype.equals(Datatype.Integer)) {
               initParameters.put(text, 0);
            } else if (datatype.equals(Datatype.Long)) {
               initParameters.put(text, 0);
            } else if (datatype.equals(Datatype.Double)) {
               initParameters.put(text, 0);
            } else if (datatype.equals(Datatype.Float)) {
               initParameters.put(text, 0);
            } else if (datatype.equals(Datatype.Boolean)) {
               initParameters.put(text, false);
            } else if (datatype.equals(Datatype.List)) {
               initParameters.put(text, new ArrayList());
            } else if (datatype.equals(Datatype.Set)) {
               initParameters.put(text, new HashSet());
            } else if (datatype.equals(Datatype.Map)) {
               initParameters.put(text, new HashMap());
            }
         }
      }
   }

   public void clearInitParameters(Map<String, Object> initParameters) {
      ArrayList items = new ArrayList();

      for (String text : initParameters.keySet()) {
         Object objectValue = initParameters.get(text);
         if (objectValue != null) {
            if (objectValue instanceof List) {
               ((List)objectValue).clear();
            } else if (objectValue instanceof Set) {
               ((Set)objectValue).clear();
            } else if (objectValue instanceof Map) {
               ((Map)objectValue).clear();
            } else if (objectValue instanceof Number) {
               initParameters.put(text, 0);
            } else if (objectValue instanceof Boolean) {
               initParameters.put(text, false);
            } else if (objectValue instanceof String) {
               items.add(text);
            }
         }
      }

      for (String text2 : (Iterable<String>)(Iterable<?>)(items)) {
         initParameters.remove(text2);
      }
   }
}
