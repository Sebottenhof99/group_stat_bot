package com.am.telegram.groupstat.user.report.statistic;

import com.am.telegram.groupstat.user.report.group.GroupDTO;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetChatMemberCount;
import com.pengrad.telegrambot.response.GetChatMemberCountResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticRepository {

    private final TelegramBot bot;

    public StatisticRepository(TelegramBot bot) {
        this.bot = bot;
    }


    public Map<String, GroupMonthStatisticDTO> getPreviousMonthStatistics(Connection connection) throws SQLException {
        String sql = """
                SELECT GROUP_INTERNAL_NAME, STATISTIC_ID, STATISTIC_MEASURED_AT, STATISTIC_MEMBER_COUNT 
                FROM GROUP_MONTH_STATISTIC JOIN GROUPS on GROUP_MONTH_STATISTIC.STATISTIC_GROUP_ID = GROUPS.GROUP_ID
               """;

        Map<String, GroupMonthStatisticDTO> groupMonthStatisticDTOMap = new HashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                GroupMonthStatisticDTO groupMonthStatisticDTO = new GroupMonthStatisticDTO();
                groupMonthStatisticDTO.setGroupStatisticId(rs.getInt("STATISTIC_ID"));
                groupMonthStatisticDTO.setMeasuredAt(rs.getDate("STATISTIC_MEASURED_AT").toLocalDate());
                groupMonthStatisticDTO.setMemberCount(rs.getInt("STATISTIC_MEMBER_COUNT"));

                groupMonthStatisticDTOMap.put(rs.getString("GROUP_INTERNAL_NAME"), groupMonthStatisticDTO);

            }
        }
        return groupMonthStatisticDTOMap;
    }

    public Map<String, GroupMonthStatisticDTO> measureCurrentCountOfUsers(List<GroupDTO> groupDTOs) throws InterruptedException {
        Map<String, GroupMonthStatisticDTO> currentStatistics = new HashMap<>();
        for (GroupDTO groupDTO : groupDTOs) {
            GetChatMemberCountResponse response = bot.execute(new GetChatMemberCount(groupDTO.getInternalName()));
            System.out.println(groupDTO.getInternalName() + " request of quering: " + response.toString());
            GroupMonthStatisticDTO groupMonthStatisticDTO = new GroupMonthStatisticDTO();
            groupMonthStatisticDTO.setGroupId(groupDTO.getId());
            groupMonthStatisticDTO.setMeasuredAt(LocalDate.now());
            groupMonthStatisticDTO.setMemberCount(response.count());
            currentStatistics.put(groupDTO.getInternalName(), groupMonthStatisticDTO);
            Thread.sleep(1000);
            System.out.println("Pause");
        }
        return currentStatistics;
    }

    public boolean existMeasurementsForDate(LocalDate date, Connection connection) {
        return false;
    }

    public Map<String, GroupMonthStatisticDTO> findMeasurementsForDate(LocalDate now, Connection connection) {
        return null;
    }

    public void persistMeasurements(Collection<GroupMonthStatisticDTO> values, Connection connection) {

    }
}
