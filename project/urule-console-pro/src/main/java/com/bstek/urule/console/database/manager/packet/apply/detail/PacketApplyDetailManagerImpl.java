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

   public void add(PacketApplyDetail detail) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "insert into URULE_PACKET_APPLY_DETAIL(ID_,APPLY_ID_,PROJECT_ID_,DESC_,CREATE_USER_,CREATE_DATE_) values(?,?,?,?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         detail.setId(IDGenerator.getInstance().nextId(IDType.PACKET_APPLY_DETAIL));
         preparedStatement.setLong(1, detail.getId());
         preparedStatement.setLong(2, detail.getApplyId());
         preparedStatement.setLong(3, detail.getProjectId());
         preparedStatement.setString(4, detail.getDesc());
         preparedStatement.setString(5, detail.getCreateUser());
         preparedStatement.setTimestamp(6, new Timestamp((new Date()).getTime()));
         detail.setCreateDate(new Date());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public PacketApplyDetail load(long id) {
      List items = this.newQuery().id(id).list();
      return items.size() > 0 ? (PacketApplyDetail)items.get(0) : null;
   }

   public List loadByApplyId(long applyId) {
      return this.newQuery().applyId(applyId).list();
   }

   public void deleteByApplyId(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_PACKET_APPLY_DETAIL where APPLY_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_PACKET_APPLY_DETAIL where PROJECT_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public PacketApplyDetailQuery newQuery() {
      return new PacketApplyDetailQueryImpl();
   }
}
