package com.bstek.urule.console.database.manager.packet.apply.detail;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.PacketApplyDetail;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketApplyDetailManagerImpl implements PacketApplyDetailManager {
   protected PacketApplyDetailManagerImpl() {
   }

   public void add(PacketApplyDetail var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "insert into URULE_PACKET_APPLY_DETAIL(ID_,APPLY_ID_,PROJECT_ID_,DESC_,CREATE_USER_,CREATE_DATE_) values(?,?,?,?,?,?)";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var1.setId(IDGenerator.getInstance().nextId(IDType.PACKET_APPLY_DETAIL));
         var4.setLong(1, var1.getId());
         var4.setLong(2, var1.getApplyId());
         var4.setLong(3, var1.getProjectId());
         var4.setString(4, var1.getDesc());
         var4.setString(5, var1.getCreateUser());
         var4.setTimestamp(6, new Timestamp((new Date()).getTime()));
         var1.setCreateDate(new Date());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public PacketApplyDetail load(long var1) {
      List var3 = this.newQuery().id(var1).list();
      return var3.size() > 0 ? (PacketApplyDetail)var3.get(0) : null;
   }

   public List loadByApplyId(long var1) {
      return this.newQuery().applyId(var1).list();
   }

   public void deleteByApplyId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_APPLY_DETAIL where APPLY_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_APPLY_DETAIL where PROJECT_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public PacketApplyDetailQuery newQuery() {
      return new PacketApplyDetailQueryImpl();
   }
}
