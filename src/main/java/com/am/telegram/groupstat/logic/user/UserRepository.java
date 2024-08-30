package com.am.telegram.groupstat.logic.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

  public List<UserDTO> findRegularUsers(Connection connection) throws SQLException {
    String sql = """
        SELECT STAT_USER_ID, STAT_USERS_CHAT_ID, STAT_USER_NAME, STAT_USER_ADDED_AT
        STAT_USER_ADDED_BY, STAT_USER_IS_ADMIN, STAT_USER_HAS_READ_ACCESS
        STAT_USER_IS_SUBSCRIBED FROM STAT_USERS WHERE STAT_USER_HAS_READ_ACCESS = true AND
        STAT_USER_IS_ADMIN = false ORDER BY STAT_USER_NAME DESC
        """;
    return requestUsers(connection, sql);
  }

  public List<UserDTO> findAdmins(Connection connection) throws SQLException {
    String sql = """
        SELECT STAT_USER_ID, STAT_USERS_CHAT_ID, STAT_USER_NAME, STAT_USER_ADDED_AT,
        STAT_USER_ADDED_BY, STAT_USER_IS_ADMIN, STAT_USER_HAS_READ_ACCESS, STAT_USER_IS_SUBSCRIBED
        FROM STAT_USERS WHERE STAT_USER_IS_ADMIN = true ORDER BY STAT_USER_NAME DESC
        """;
    return requestUsers(connection, sql);
  }

  public Optional<UserDTO> findUserByName(Connection con, String userName) throws SQLException {
    String sql = """
        SELECT STAT_USER_ID, STAT_USERS_CHAT_ID, STAT_USER_NAME, STAT_USER_ADDED_AT, STAT_USER_ADDED_BY, 
        STAT_USER_IS_ADMIN, STAT_USER_HAS_READ_ACCESS, STAT_USER_IS_SUBSCRIBED
        FROM STAT_USERS 
        WHERE STAT_USER_NAME = ?
        """;
    try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
      preparedStatement.setString(1, userName);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapToDTO(rs));
        }
        return Optional.empty();
      }
    }
  }

  public void persist(Connection con, UserDTO userDTO) throws SQLException {
    String sql = """
        INSERT INTO STAT_USERS(STAT_USER_NAME, STAT_USERS_CHAT_ID, STAT_USER_ADDED_AT,
        STAT_USER_ADDED_BY, STAT_USER_IS_ADMIN, STAT_USER_HAS_READ_ACCESS, STAT_USER_IS_SUBSCRIBED) 
        VALUES(?, ?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, userDTO.getUserName());
      if (userDTO.getChatId() == 0) {
        ps.setNull(2, Types.BIGINT);
      } else {
        ps.setLong(2, userDTO.getChatId());
      }
      ps.setTimestamp(3, Timestamp.valueOf(userDTO.getAddedAt()));
      ps.setString(4, userDTO.getAddedBy());
      ps.setBoolean(5, userDTO.isAdmin());
      ps.setBoolean(6, userDTO.isHasReadAccess());
      ps.setBoolean(7, userDTO.isSubscribed());
      ps.executeUpdate();
    }
  }

  public void removeByUserId(Connection con, int userId) throws SQLException {
    String sql = "DELETE FROM STAT_USERS WHERE STAT_USER_ID = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.executeUpdate();
    }
  }

  public void update(Connection con, UserDTO userDTO) throws SQLException {
    String sql = """
        UPDATE STAT_USERS SET STAT_USERS_CHAT_ID = ?, STAT_USER_NAME = ?, STAT_USER_ADDED_AT = ?, 
        STAT_USER_ADDED_BY = ?, STAT_USER_IS_ADMIN = ?, STAT_USER_HAS_READ_ACCESS = ?,
        STAT_USER_IS_SUBSCRIBED = ?
        WHERE STAT_USER_ID = ?
        """;

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setLong(1, userDTO.getChatId());
      ps.setString(2, userDTO.getUserName());
      ps.setTimestamp(3, Timestamp.valueOf(userDTO.getAddedAt()));
      ps.setString(4, userDTO.getAddedBy());
      ps.setBoolean(5, userDTO.isAdmin());
      ps.setBoolean(6, userDTO.isHasReadAccess());
      ps.setBoolean(7, userDTO.isSubscribed());
      ps.setInt(8, userDTO.getUserId());
      ps.executeUpdate();
    }
  }

  public List<UserDTO> findSubscribers(Connection con) {
    String sql = """
        SELECT STAT_USER_ID, STAT_USERS_CHAT_ID, STAT_USER_NAME, STAT_USER_ADDED_AT, STAT_USER_ADDED_BY,
        STAT_USER_IS_ADMIN, STAT_USER_HAS_READ_ACCESS, STAT_USER_IS_SUBSCRIBED 
        FROM STAT_USERS
        WHERE STAT_USER_IS_SUBSCRIBED = true
        """;
    try (PreparedStatement preparedStatement = con.prepareStatement(sql);
         ResultSet rs = preparedStatement.executeQuery()) {

      List<UserDTO> subscribers = new ArrayList<>();
      while (rs.next()) {
        subscribers.add(mapToDTO(rs));
      }
      return subscribers;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<UserDTO> requestUsers(Connection connection, String sql) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
         ResultSet rs = preparedStatement.executeQuery()) {
      List<UserDTO> users = new ArrayList<>();
      while (rs.next()) {
        UserDTO userDTO = mapToDTO(rs);
        users.add(userDTO);
      }

      return users;
    }
  }

  private UserDTO mapToDTO(ResultSet rs) throws SQLException {
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(rs.getInt("STAT_USER_ID"));
    userDTO.setChatId(rs.getLong("STAT_USERS_CHAT_ID"));
    userDTO.setUserName(rs.getString("STAT_USER_NAME"));
    userDTO.setAddedAt(rs.getTimestamp("STAT_USER_ADDED_AT").toLocalDateTime());
    userDTO.setAddedBy(rs.getString("STAT_USER_ADDED_BY"));
    userDTO.setAdmin(rs.getBoolean("STAT_USER_IS_ADMIN"));
    userDTO.setHasReadAccess(rs.getBoolean("STAT_USER_HAS_READ_ACCESS"));
    userDTO.setSubscribed(rs.getBoolean("STAT_USER_IS_SUBSCRIBED"));
    return userDTO;
  }
}
