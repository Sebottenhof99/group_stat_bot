package com.am.telegram.groupstat.logic.group;

import static java.util.stream.Collectors.groupingBy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;

public class GroupManagementService {

  private final DataSource ds;
  private final GroupManagementRepository groupManagementRepository;

  public GroupManagementService(
      DataSource ds, GroupManagementRepository groupManagementRepository) {
    this.ds = ds;
    this.groupManagementRepository = groupManagementRepository;
  }

  public String groups() {
    try (Connection con = ds.getConnection()) {
      List<GroupDTO> groupDTOS = groupManagementRepository.listAllGroups(con);
      StringBuilder groupsAsString = new StringBuilder();
      Map<String, List<GroupDTO>> groupsByCategory =
          groupDTOS.stream().collect(groupingBy(GroupDTO::getGroupCategory));
      for (Map.Entry<String, List<GroupDTO>> entry : groupsByCategory.entrySet()) {
        groupsAsString
            .append("\n")
            .append(entry.getKey())
            .append("(")
            .append(entry.getValue().size())
            .append(")")
            .append(":\n")
            .append(
                entry.getValue().stream()
                    .map(groupDTO -> groupDTO.getGroupId() + ". " + groupDTO.getGroupCity())
                    .collect(Collectors.joining("\n")));
      }

      return groupsAsString.toString();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void save(GroupDTO groupDTO) {

    try (Connection con = ds.getConnection()) {
      if (groupDTO.getGroupId() > 0) {
        groupManagementRepository.update(con, groupDTO);
      } else {
        groupManagementRepository.persist(con, groupDTO);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
