package com.am.telegram.groupstat.logic.report.statistic;

import com.am.telegram.groupstat.logic.group.GroupDTO;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetChatMemberCount;
import com.pengrad.telegrambot.response.GetChatMemberCountResponse;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticRepository {

  private static final Logger log = LoggerFactory.getLogger(StatisticRepository.class);
  private static final String SELECT_STATISTICS_FOR_GIVEN_DATE =
      """
             SELECT GROUP_INTERNAL_NAME, STATISTIC_ID, STATISTIC_MEASURED_AT, STATISTIC_MEMBER_COUNT
             FROM GROUP_MONTH_STATISTIC JOIN GROUPS on GROUP_MONTH_STATISTIC.STATISTIC_GROUP_ID = GROUPS.GROUP_ID
            where STATISTIC_MEASURED_AT = ?
            """;
  private final TelegramBot bot;

  public StatisticRepository(TelegramBot bot) {
    this.bot = bot;
  }

  public Map<String, GroupMonthStatisticDTO> findStatisticsForDate(
      LocalDate date, Connection connection) throws SQLException {
    Map<String, GroupMonthStatisticDTO> groupMonthStatisticDTOMap = new HashMap<>();
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(SELECT_STATISTICS_FOR_GIVEN_DATE)) {
      preparedStatement.setDate(1, Date.valueOf(date));
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          GroupMonthStatisticDTO groupMonthStatisticDTO = new GroupMonthStatisticDTO();
          groupMonthStatisticDTO.setGroupStatisticId(rs.getInt("STATISTIC_ID"));
          groupMonthStatisticDTO.setMeasuredAt(rs.getDate("STATISTIC_MEASURED_AT").toLocalDate());
          groupMonthStatisticDTO.setMemberCount(rs.getInt("STATISTIC_MEMBER_COUNT"));

          groupMonthStatisticDTOMap.put(
              rs.getString("GROUP_INTERNAL_NAME"), groupMonthStatisticDTO);
        }
      }
    }
    return groupMonthStatisticDTOMap;
  }

  public Map<String, GroupMonthStatisticDTO> measureCurrentCountOfUsers(List<GroupDTO> groupDTOs)
      throws InterruptedException {
    Map<String, GroupMonthStatisticDTO> currentStatistics = new HashMap<>();
    log.info("Querying current user numbers from groups");
    for (GroupDTO groupDTO : groupDTOs) {
      log.info("Sending query for group {}", groupDTO.getGroupName());
      GetChatMemberCountResponse response =
          bot.execute(new GetChatMemberCount(groupDTO.getGroupName()));
      GroupMonthStatisticDTO groupMonthStatisticDTO = new GroupMonthStatisticDTO();
      groupMonthStatisticDTO.setGroupId(groupDTO.getGroupId());
      groupMonthStatisticDTO.setMeasuredAt(LocalDate.now());
      groupMonthStatisticDTO.setMemberCount(response.count());
      currentStatistics.put(groupDTO.getGroupName(), groupMonthStatisticDTO);
      Thread.sleep(2000);
    }
    return currentStatistics;
  }

  public boolean existMeasurementsForDate(LocalDate date, Connection connection)
      throws SQLException {
    try (PreparedStatement selectStatistics =
        connection.prepareStatement(SELECT_STATISTICS_FOR_GIVEN_DATE)) {
      selectStatistics.setDate(1, Date.valueOf(date));
      return selectStatistics.executeQuery().next();
    }
  }

  public void persistMeasurements(
      Collection<GroupMonthStatisticDTO> values, Connection connection) {
    String sql =
        "INSERT INTO GROUP_MONTH_STATISTIC ( STATISTIC_GROUP_ID, STATISTIC_MEASURED_AT, STATISTIC_MEMBER_COUNT ) VALUES (?, ?, ?) ";
    try (PreparedStatement insertStatistic = connection.prepareStatement(sql)) {
      for (GroupMonthStatisticDTO dto : values) {
        insertStatistic.setInt(1, dto.getGroupId());
        insertStatistic.setDate(2, Date.valueOf(dto.getMeasuredAt()));
        insertStatistic.setInt(3, Integer.parseInt(dto.getMemberCount()));
        insertStatistic.addBatch();
      }
      insertStatistic.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
