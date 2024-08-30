package com.am.telegram.groupstat.logic.group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository {

  public List<GroupDTO> listAllGroups(Connection con) throws SQLException {
    List<GroupDTO> groups = new ArrayList<>();
    try (PreparedStatement ps =
            con.prepareStatement(
                "SELECT GROUP_ID, GROUP_CITY, GROUP_CATEGORY FROM GROUPS ORDER BY group_id");
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

  public List<GroupDTO> findAllGroups(Connection connection) throws SQLException {
    String query =
        "SELECT GROUP_ID, GROUP_INTERNAL_NAME, GROUP_CITY, GROUP_CATEGORY, GROUP_ADDED_AT, GROUP_ADDED_BY FROM groups";
    List<GroupDTO> groupDTOs = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery()) {
      while (rs.next()) {
        groupDTOs.add(mapToDTO(rs));
      }
    }

    return groupDTOs;
  }

  private GroupDTO mapToDTO(ResultSet rs) throws SQLException {
    GroupDTO groupDTO = new GroupDTO();
    groupDTO.setGroupId(rs.getInt("GROUP_ID"));
    groupDTO.setGroupName(rs.getString("GROUP_INTERNAL_NAME"));
    groupDTO.setGroupCity(rs.getString("GROUP_CITY"));
    groupDTO.setGroupCategory(rs.getString("GROUP_CATEGORY"));
    groupDTO.setAddedAt(rs.getTimestamp("GROUP_ADDED_AT").toLocalDateTime());
    groupDTO.setAddedBy(rs.getString("GROUP_ADDED_BY"));
    return groupDTO;
  }
}
