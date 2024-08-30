package com.am.telegram.groupstat.logic.report.statistic;

import com.am.telegram.groupstat.logic.group.GroupDTO;
import com.am.telegram.groupstat.logic.group.GroupRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.sql.DataSource;

public class StatisticService {

  private final GroupRepository groupRepository;
  private final StatisticRepository statisticRepository;
  private final DataSource ds;

  public StatisticService(
      GroupRepository groupRepository, StatisticRepository statisticRepository, DataSource ds) {
    this.groupRepository = groupRepository;
    this.statisticRepository = statisticRepository;
    this.ds = ds;
  }

  public List<GroupStatistic> generateCurrentReport() {
    try (Connection connection = ds.getConnection()) {
      List<GroupDTO> groupDTOs = groupRepository.findAllGroups(connection);
      return groupedStatistics(
          groupDTOs, previousMonthStatistics(connection), currentStatistics(connection, groupDTOs));
    } catch (SQLException e) {
      throw new StatisticsException("Could not generate current report due to sql issue", e);
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      throw new StatisticsException("Could not generate report due to thread issue", e);
    }
  }

  private List<GroupStatistic> groupedStatistics(
      List<GroupDTO> groupDTOs,
      Map<String, GroupMonthStatisticDTO> previousMonthStatistics,
      Map<String, GroupMonthStatisticDTO> currentStatistics) {
    List<GroupStatistic> groupStatistics = new ArrayList<>();
    for (GroupDTO groupDTO : groupDTOs) {
      groupStatistics.add(
          new BasicGroupStatistic(
              groupDTO,
              previousMonthStatistics.get(groupDTO.getGroupName()),
              currentStatistics.get(groupDTO.getGroupName())));
    }

    return groupStatistics;
  }

  private Map<String, GroupMonthStatisticDTO> previousMonthStatistics(Connection connection)
      throws SQLException {
    LocalDate endOfPreviousMonth = YearMonth.now().minusMonths(1).atEndOfMonth();
    return statisticRepository.findStatisticsForDate(endOfPreviousMonth, connection);
  }

  private Map<String, GroupMonthStatisticDTO> currentStatistics(
      Connection connection, List<GroupDTO> groupDTOs)
      throws SQLException, ExecutionException, InterruptedException {
    Map<String, GroupMonthStatisticDTO> currentStatistics;
    if (statisticRepository.existMeasurementsForDate(LocalDate.now(), connection)) {
      currentStatistics = statisticRepository.findStatisticsForDate(LocalDate.now(), connection);
    } else {
      currentStatistics = statisticRepository.measureCurrentCountOfUsers(groupDTOs);
      statisticRepository.persistMeasurements(currentStatistics.values(), connection);
    }
    return currentStatistics;
  }
}
