package com.bstek.urule.console.database.manager.authority;

import com.bstek.urule.console.database.model.Authority;
import com.bstek.urule.console.database.model.Role;
import java.util.List;

public interface AuthorityService {
   AuthorityService ins = new AuthorityServiceImpl();

   /**获取角色的授权信息*/
   List getAuthoritysByRole(String roleType, long roleId);

   /**获取权限配置对象*/
   Authority get(String roleType, long roleId, String code);

   /**添加资源的授权信息*/
   void add(Authority authority);

   /**删除资源的授权信息*/
   void remove(long authId);

   /**删除角色对应的所有授权信息*/
   void removeByRole(String roleType, long roleId);

   /**获取团队角色的权限配置*/
   List getGroupModels(Role role);

   /**获取项目角色的权限配置*/
   List getProjectModels(Role role);

   void storePermissions(long roleId, List models);

   void initPermissions(long roleId, List models);
}
