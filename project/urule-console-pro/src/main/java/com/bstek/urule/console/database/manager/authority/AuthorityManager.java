package com.bstek.urule.console.database.manager.authority;

import com.bstek.urule.console.database.model.Authority;
import java.sql.Connection;
import java.util.List;

public interface AuthorityManager {
   AuthorityManager ins = new AuthorityManagerImpl();

   /**获取资源授权信息*/
   List getAuthoritysByCode(String roleType, String code);

   /**获取角色的授权信息*/
   List getAuthoritysByRole(String roleType, long roleId);

   /**获取权限配置对象*/
   Authority get(String roleType, long roleId, String code);

   /**添加资源的授权信息*/
   void add(Connection conn, Authority authority);

   /**删除资源的授权信息*/
   void remove(Connection conn, long authId);

   /**删除资源的授权信息*/
   void remove(Connection conn, String roleType, long roleId, String code, String resourceType);

   /**删除角色对应的所有授权信息*/
   void removeByRole(String roleType, long roleId);
}
