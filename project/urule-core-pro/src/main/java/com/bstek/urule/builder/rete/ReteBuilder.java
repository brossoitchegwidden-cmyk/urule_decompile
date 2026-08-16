package com.bstek.urule.builder.rete;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.Node;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.AndNode;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.JunctionNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.MutexReteUnit;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.OrNode;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.ReteUnit;
import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.model.rete.TerminalNode;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.Met;
import com.bstek.urule.model.rule.loop.LoopRule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ReteBuilder implements ApplicationContextAware {
   public static final String BEAN_ID = "urule.reteBuilder";
   private static Collection<CriterionBuilder> a;

   public Rete buildRete(List<Rule> var1, ResourceLibrary var2) {
      return this.a(var1, var2);
   }

   public Rete buildRete(Rule var1, ResourceLibrary var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1);
      return this.a(var3, var2);
   }

   private Rete a(List<Rule> var1, ResourceLibrary var2) {
      ArrayList var3 = new ArrayList();
      Rete var4 = new Rete(var3, var2);
      BuildContextImpl var5 = new BuildContextImpl(var2, var3);
      HashMap var6 = new HashMap();
      HashMap var7 = new HashMap();
      ArrayList var8 = new ArrayList();
      var4.setAllRuleData(var8);

      for (Rule var10 : var1) {
         if (!this.a(var10)) {
            if (StringUtils.isNotBlank(var10.getPendedGroup())) {
               List var11 = (List)var7.get(var10.getPendedGroup());
               if (var11 == null) {
                  var11 = new ArrayList();
                  var7.put(var10.getPendedGroup(), var11);
               }

               var11.add(var10);
            } else if (StringUtils.isNotBlank(var10.getMutexGroup())) {
               List var12 = (List)var6.get(var10.getMutexGroup());
               if (var12 == null) {
                  var12 = new ArrayList();
                  var6.put(var10.getMutexGroup(), var12);
               }

               var12.add(var10);
            } else {
               if (!var10.isTargetResource(ResourceType.Flow)) {
                  var8.add(new RuleData(var10));
               }

               TerminalNode var13 = new TerminalNode(var10, var5.nextId());
               this.a(var10, var5, var13);
               var10.setLhs(null);
            }
         }
      }

      var4.setPendedGroupRetesMap(this.a(var7, var5, true));
      var4.setMutexGroupRetesMap(this.a(var6, var5, false));
      return var4;
   }

   private boolean a(Rule var1) {
      if (var1.getEnabled() != null && !var1.getEnabled()) {
         return true;
      }

      Date var2 = var1.getExpiresDate();
      if (var2 != null) {
         Date var3 = new Date();
         if (var2.compareTo(var3) < 0) {
            return true;
         }
      }

      return false;
   }

   private Map<String, List<ReteUnit>> a(Map<String, List<Rule>> var1, BuildContext var2, boolean var3) {
      if (var1.size() == 0) {
         return null;
      }

      ResourceLibrary var4 = var2.getResourceLibrary();
      HashMap var5 = new HashMap();

      for (String var7 : var1.keySet()) {
         List var8 = (List)var1.get(var7);
         this.a(var8);
         HashMap var9 = new HashMap();

         for (Rule var11 : (Iterable<Rule>)(Iterable<?>)(var8)) {
            String var12 = var11.getMutexGroup();
            if (var3 && StringUtils.isNotBlank(var12)) {
               List var22 = (List)var9.get(var12);
               if (var22 == null) {
                  var22 = new ArrayList();
                  var9.put(var12, var22);
               }

               var22.add(var11);
            } else {
               List var13 = (List)var5.get(var7);
               if (var13 == null) {
                  var13 = new ArrayList();
                  var5.put(var7, var13);
               }

               ArrayList var14 = new ArrayList();
               Rete var15 = new Rete(var14, var4);
               BuildContextImpl var16 = new BuildContextImpl(var14, var2);
               TerminalNode var17 = new TerminalNode(var11, var16.nextId());
               this.a(var11, var16, var17);
               ReteUnit var18 = new ReteUnit(var15, var11.getName());
               var18.setEffectiveDate(var11.getEffectiveDate());
               var18.setExpiresDate(var11.getExpiresDate());
               var13.add(var18);
               var11.setLhs(null);
               var2 = var16;
            }
         }

         Map var19 = this.a(var2, var4, var9);
         List var20 = (List)var5.get(var7);
         if (var20 == null) {
            var20 = new ArrayList();
            var5.put(var7, var20);
         }

         for (String var23 : (Iterable<String>)(Iterable<?>)(var19.keySet())) {
            List var24 = (List)var19.get(var23);
            var20.add(new MutexReteUnit(var23, var24));
         }
      }

      return var5;
   }

   private Map<String, List<ReteUnit>> a(BuildContext var1, ResourceLibrary var2, Map<String, List<Rule>> var3) {
      HashMap var4 = new HashMap();

      for (String var6 : var3.keySet()) {
         List var7 = (List)var4.get(var6);
         if (var7 == null) {
            var7 = new ArrayList();
            var4.put(var6, var7);
         }

         for (Rule var10 : (Iterable<Rule>)(Iterable<?>)((List)var3.get(var6))) {
            ArrayList var11 = new ArrayList();
            Rete var12 = new Rete(var11, var2);
            BuildContextImpl var13 = new BuildContextImpl(var11, var1);
            TerminalNode var14 = new TerminalNode(var10, var13.nextId());
            this.a(var10, var13, var14);
            ReteUnit var15 = new ReteUnit(var12, var10.getName());
            var7.add(var15);
            var10.setLhs(null);
            var1 = var13;
         }
      }

      return var4;
   }

   private void a(List<Rule> var1) {
      Collections.sort(var1, new ReteBuilder$1(this));
   }

   private void a(Rule var1, BuildContext var2, TerminalNode var3) {
      var2.setCurrentRule(var1);
      Lhs var4 = var1.getLhs();
      if (!(var1 instanceof LoopRule) && var4 != null && var4.getCriterion() != null) {
         Criterion var14 = var4.getCriterion();

         for (BaseReteNode var8 : buildCriterion(var2, var14)) {
            if (var8 instanceof AndNode || var8 instanceof OrNode) {
               JunctionNode var9 = (JunctionNode)var8;
               List var10 = var9.getToConnections();
               if (var10.size() == 1) {
                  Line var11 = (Line)var10.get(0);
                  Node var12 = var11.getFrom();
                  if (var12 instanceof CriteriaNode) {
                     CriteriaNode var13 = (CriteriaNode)var12;
                     var13.getLines().remove(var11);
                     var8 = var13;
                  }
               }
            }

            var8.addLine(var3);
         }

         Other var15 = var1.getOther();
         if (var15 != null && var15.getActions() != null && var15.getActions().size() > 0) {
            var1.setWithElse(true);
            Utils.buildElseRule(var1);
            ObjectTypeNode var16 = var2.buildObjectTypeNode("__*__");
            var16.addLine(var3);
         }
      } else {
         ObjectTypeNode var5 = var2.buildObjectTypeNode("__*__");
         var5.addLine(var3);
      }
   }

   public static List<BaseReteNode> buildCriterion(BuildContext var0, Criterion var1) {
      if (var1 instanceof Met) {
         return MetBuilder.ins.buildCriterion((BaseCriterion)var1, null, var0);
      }

      for (CriterionBuilder var3 : a) {
         if (var3.support(var1)) {
            return var3.buildCriterion((BaseCriterion)var1, var0);
         }
      }

      throw new RuleException("Unknow criterion : " + var1);
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      a = var1.getBeansOfType(CriterionBuilder.class).values();
   }
}
