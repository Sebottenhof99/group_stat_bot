package com.am.telegram.groupstat.logic.group;

import static java.util.stream.Collectors.groupingBy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;

public class GroupService {

  private final DataSource ds;
  private final GroupRepository groupRepository;

  public GroupService(DataSource ds, GroupRepository groupRepository) {
    this.ds = ds;
    this.groupRepository = groupRepository;
  }

  public String groups() {
    try (Connection con = ds.getConnection()) {
      List<GroupDTO> groupDTOS = groupRepository.listAllGroups(con);
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
        groupRepository.update(con, groupDTO);
      } else {
        groupRepository.persist(con, groupDTO);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
