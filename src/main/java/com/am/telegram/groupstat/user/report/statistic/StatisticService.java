package com.am.telegram.groupstat.user.report.statistic;

import com.am.telegram.groupstat.user.report.group.GroupDTO;
import com.am.telegram.groupstat.user.report.group.GroupRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticService {

    private final GroupRepository groupRepository;
    private final StatisticRepository statisticRepository;
    private final DataSource ds;

    public StatisticService(GroupRepository groupRepository, StatisticRepository statisticRepository, DataSource ds) {
        this.groupRepository = groupRepository;
        this.statisticRepository = statisticRepository;
        this.ds = ds;
    }

    public List<GroupStatistic> generateCurrentReport() {
        try (Connection connection = ds.getConnection()) {
            List<GroupDTO> groupDTOs = groupRepository.findAllGroups(connection);

            Map<String, GroupMonthStatisticDTO> currentStatistics;
            if (statisticRepository.existMeasurementsForDate(LocalDate.now(), connection)) {
                currentStatistics = statisticRepository.findMeasurementsForDate(LocalDate.now(), connection);
            } else {
                currentStatistics = statisticRepository.measureCurrentCountOfUsers(groupDTOs);
                statisticRepository.persistMeasurements(currentStatistics.values(), connection);
            }
            Map<String, GroupMonthStatisticDTO> previousMonthStatistics = statisticRepository.getPreviousMonthStatistics(connection);
            List<GroupStatistic> groupStatistics = new ArrayList<>();

            for (GroupDTO groupDTO : groupDTOs) {
                groupStatistics.add(new GroupStatistic(groupDTO, previousMonthStatistics.get(groupDTO.getInternalName()), currentStatistics.get(groupDTO.getInternalName())));
            }

            return groupStatistics;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
