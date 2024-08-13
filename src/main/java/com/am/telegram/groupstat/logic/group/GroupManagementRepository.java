package com.am.telegram.groupstat.logic.group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupManagementRepository {

  public List<GroupDTO> listAllGroups(Connection con) throws SQLException {
    List<GroupDTO> groups = new ArrayList<>();
    try (PreparedStatement ps =
            con.prepareStatement(
                "SELECT GROUP_ID, GROUP_CITY, GROUP_CATEGORY FROM GROUPS ORDER BY group_id ASC");
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        GroupDTO group = new GroupDTO();
        group.setGroupId(rs.getInt("GROUP_ID"));
        group.setGroupCity(rs.getString("GROUP_CITY"));
        group.setGroupCategory(rs.getString("GROUP_CATEGORY"));
        groups.add(group);
      }
      return groups;
    }
  }

  public Optional<GroupDTO> findGroupById(Connection con, int groupId) throws SQLException {
    String sql =
        "SELECT GROUP_ID,GROUP_INTERNAL_NAME, GROUP_CITY, GROUP_CATEGORY, GROUP_ADDED_AT, GROUP_ADDED_BY FROM GROUPS WHERE GROUP_ID = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, groupId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          GroupDTO groupDTO = new GroupDTO();
          groupDTO.setGroupId(rs.getInt("GROUP_ID"));
          groupDTO.setGroupName(rs.getString("GROUP_INTERNAL_NAME"));
          groupDTO.setGroupCity(rs.getString("GROUP_CITY"));
          groupDTO.setGroupCategory(rs.getString("GROUP_CATEGORY"));
          groupDTO.setAddedAt(rs.getTimestamp("GROUP_ADDED_AT").toLocalDateTime());
          groupDTO.setAddedBy(rs.getString("GROUP_ADDED_BY"));
          return Optional.of(groupDTO);
        }
        return Optional.empty();
      }
    }
  }

  public void update(Connection con, GroupDTO groupDTO) throws SQLException {
    String sql =
        """
    UPDATE GROUPS SET GROUP_INTERNAL_NAME = ?, GROUP_CITY = ?, GROUP_CATEGORY = ? WHERE GROUP_ID = ?;
""";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, groupDTO.getGroupName());
      ps.setString(2, groupDTO.getGroupCity());
      ps.setString(3, groupDTO.getGroupCategory());
      ps.setInt(4, groupDTO.getGroupId());
      ps.executeUpdate();
    }
  }

  public void persist(Connection con, GroupDTO groupDTO) throws SQLException {
    String sql =
        """
            INSERT INTO GROUPS(GROUP_INTERNAL_NAME, GROUP_CITY, GROUP_CATEGORY, GROUP_ADDED_AT, GROUP_ADDED_BY)
            VALUES (?, ?, ?, ?, ?);
          """;

    try (PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, groupDTO.getGroupName());
      ps.setString(2, groupDTO.getGroupCity());
      ps.setString(3, groupDTO.getGroupCategory());
      ps.setTimestamp(4, Timestamp.valueOf(groupDTO.getAddedAt()));
      ps.setString(5, groupDTO.getAddedBy());
      ps.executeUpdate();
    }
  }
}
