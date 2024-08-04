package com.am.telegram.groupstat.user.assistant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssistantRepository {

  private static final String sql =
      """
              SELECT USER_ASSISTANT_ACTIVE_OPERATION, USER_ASSISTANT_LAST_GIVEN_ANSWER
              FROM USER_ASSISTANT
              WHERE USER_ASSISTANT_USER_ID = ?
              """;

  public AssistantDTO findByUserId(Connection con, int userId) throws SQLException {
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          AssistantDTO dto = new AssistantDTO();
          dto.setActiveOperation(rs.getString("USER_ASSISTANT_ACTIVE_OPERATION"));
          dto.setLastGivenAnswer(rs.getString("USER_ASSISTANT_LAST_GIVEN_ANSWER"));
          return dto;
        }
        return new AssistantDTO();
      }
    }
  }

  public void update(Connection con, AssistantDTO assistantDTO, int userId) throws SQLException {
    String update =
        """
    UPDATE USER_ASSISTANT
    SET USER_ASSISTANT_ACTIVE_OPERATION = ?, USER_ASSISTANT_LAST_GIVEN_ANSWER = ?
    WHERE USER_ASSISTANT_USER_ID = ?
""";

    try (PreparedStatement ps = con.prepareStatement(update)) {
      ps.setString(1, assistantDTO.getActiveOperation());
      ps.setString(2, assistantDTO.getLastGivenAnswer());
      ps.setInt(3, userId);
      ps.executeUpdate();
    }
  }

  public boolean existAssistant(Connection con, int userId) throws SQLException {
    String existEntry =
        """
            SELECT USER_ASSISTANT_ID FROM USER_ASSISTANT where USER_ASSISTANT_USER_ID = ?
            """;
    try (PreparedStatement ps = con.prepareStatement(existEntry)) {
      ps.setInt(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    }
  }

  public void persist(Connection con, AssistantDTO assistantDTO, int userId) throws SQLException {
    String insertAssistant =
        """
                INSERT INTO USER_ASSISTANT(USER_ASSISTANT_USER_ID, USER_ASSISTANT_ACTIVE_OPERATION, USER_ASSISTANT_LAST_GIVEN_ANSWER) VALUES(?, ?, ?)
                """;
    try (PreparedStatement ps = con.prepareStatement(insertAssistant)) {
      ps.setInt(1, userId);
      ps.setString(2, assistantDTO.getActiveOperation());
      ps.setString(3, assistantDTO.getLastGivenAnswer());
      ps.executeUpdate();
    }
  }
}
