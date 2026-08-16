package com.bstek.urule.builder;

import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceBuilder;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableLibrary;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.model.template.ActionTemplateUnit;
import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import com.bstek.urule.runtime.BuiltInActionLibraryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

public class ResourceLibraryBuilder extends AbstractBuilder {
   private static final Log c = LogFactory.getLog(ResourceLibraryBuilder.class);
   public static final String BEAN_ID = "urule.resourceLibraryBuilder";
   private BuiltInActionLibraryBuilder d;

   public ResourceLibrary buildResourceLibrary(Collection<Library> var1, List<Predefine> var2) {
      if (var1 == null) {
         var1 = Collections.EMPTY_LIST;
      }

      if (!ParsePhaseHolder.isParsePhase()) {
         this.a(var1);
      }

      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      ResourceBase var9 = this.newResourceBase();

      for (Library var11 : var1) {
         try {
            var9.addResource(var11.getId(), var11.getVersion());
         } catch (RuleException var24) {
            throw new RuleException(String.format("类库【%d，%s】加载失败，异常消息:\r\n%s", var11.getId(), var11.getPath(), var24.getMessage()));
         }
      }

      for (Resource var28 : var9.getResources()) {
         String var12 = var28.getContent();
         Element var13 = this.a(var12);

         for (ResourceBuilder var15 : this.b) {
            if (var15.support(var13)) {
               String var16 = var28.getPath();
               if (var28.getVersion() != null) {
                  var16 = var16 + ":" + var28.getVersion();
               }

               if (c.isDebugEnabled()) {
                  c.debug("build resource... " + var28.toString());
               }

               Object var17 = var15.build(var13, var16);
               ResourceType var18 = var15.getType();
               if (var18.equals(ResourceType.ActionLibrary)) {
                  ActionLibrary var39 = (ActionLibrary)var17;
                  var6.add(var39);
                  break;
               }

               if (var18.equals(ResourceType.VariableLibrary)) {
                  VariableLibrary var38 = (VariableLibrary)var17;
                  var7.add(var38);
               } else if (var18.equals(ResourceType.ConstantLibrary)) {
                  ConstantLibrary var37 = (ConstantLibrary)var17;
                  var5.add(var37);
               } else if (var18.equals(ResourceType.ParameterLibrary)) {
                  VariableCategory var36 = (VariableCategory)var17;
                  var8.add(var36);
               } else if (var18.equals(ResourceType.ConditionTemplate)) {
                  ConditionTemplate var35 = (ConditionTemplate)var17;
                  List var40 = var35.getTemplates();
                  if (var40 != null) {
                     for (ConditionTemplateUnit var42 : (Iterable<ConditionTemplateUnit>)(Iterable<?>)(var40)) {
                        String var43 = String.valueOf(var28.getId());
                        if (var28.getVersion() != null) {
                           var43 = var43 + ":" + var28.getVersion();
                        }

                        var42.setPath(var43);
                     }
                  }

                  var3.add(var35);
               } else if (var18.equals(ResourceType.ActionTemplate)) {
                  ActionTemplate var19 = (ActionTemplate)var17;
                  List var20 = var19.getTemplates();
                  if (var20 != null) {
                     for (ActionTemplateUnit var22 : (Iterable<ActionTemplateUnit>)(Iterable<?>)(var20)) {
                        String var23 = String.valueOf(var28.getId());
                        if (var28.getVersion() != null) {
                           var23 = var23 + ":" + var28.getVersion();
                        }

                        var22.setPath(var23);
                     }
                  }

                  var4.add(var19);
               }
               break;
            }
         }
      }

      if (var8.size() > 0) {
         VariableCategory var26 = (VariableCategory)var8.get(0);

         for (VariableCategory var32 : (Iterable<VariableCategory>)(Iterable<?>)(var8)) {
            if (!var32.equals(var26) && var32.getVariables() != null) {
               for (Variable var34 : var32.getVariables()) {
                  var26.addVariable(var34);
               }
            }
         }

         VariableLibrary var30 = new VariableLibrary();
         var30.addVariableCategory(var26);
         var7.add(var30);
      }

      if (!CopyLibPhaseHolder.isCopyLib()) {
         List var27 = this.d.getBuiltInActions();
         if (var27.size() > 0) {
            ActionLibrary var31 = new ActionLibrary();
            var31.setSpringBeans(var27);
            var6.add(var31);
         }
      }

      return new ResourceLibrary(var7, var6, var5, var3, var4, var2);
   }

   private void a(Collection<Library> var1) {
      ResourceBase var2 = this.newResourceBase();

      for (Library var4 : var1) {
         if (var4.getType().equals(LibraryType.ConditionTemplate)) {
            var2.addResource(var4.getId(), var4.getVersion());
         } else if (var4.getType().equals(LibraryType.ActionTemplate)) {
            var2.addResource(var4.getId(), var4.getVersion());
         }
      }

      ArrayList var14 = new ArrayList();

      for (Resource var5 : var2.getResources()) {
         String var6 = var5.getPath();
         if (var5.getVersion() != null) {
            var6 = var6 + ":" + var5.getVersion();
         }

         String var7 = var5.getContent();
         Element var8 = this.a(var7);

         for (ResourceBuilder var10 : this.b) {
            if (var10.support(var8)) {
               Object var11 = var10.build(var8, var6);
               ResourceType var12 = var10.getType();
               if (var12.equals(ResourceType.ConditionTemplate)) {
                  ConditionTemplate var13 = (ConditionTemplate)var11;
                  if (var13.getLibraries() != null) {
                     var14.addAll(var13.getLibraries());
                  }
               } else if (var12.equals(ResourceType.ActionTemplate)) {
                  ActionTemplate var21 = (ActionTemplate)var11;
                  if (var21.getLibraries() != null) {
                     var14.addAll(var21.getLibraries());
                  }
               }
               break;
            }
         }
      }

      for (Library var17 : (Iterable<Library>)(Iterable<?>)(var14)) {
         boolean var18 = false;

         for (Library var20 : var1) {
            if (var17.getId() == var20.getId()) {
               var18 = true;
               break;
            }
         }

         if (!var18) {
            var1.add(var17);
         }
      }
   }

   public void setBuiltInActionLibraryBuilder(BuiltInActionLibraryBuilder var1) {
      this.d = var1;
   }
}
