package com.am.telegram.groupstat.user.report.statistic;

import com.am.telegram.groupstat.user.report.group.GroupDTO;
import org.apache.poi.ss.usermodel.Row;

public class GroupStatistic {

    private final GroupDTO groupDTO;
    private final GroupMonthStatisticDTO previousMonthStatisticDTO;
    private final GroupMonthStatisticDTO currentMonthStatisticDTO;

    public GroupStatistic(GroupDTO groupDTO, GroupMonthStatisticDTO previousMonthStatisticDTO, GroupMonthStatisticDTO currentMonthStatisticDTO) {
        this.groupDTO = groupDTO;
        this.previousMonthStatisticDTO = previousMonthStatisticDTO;
        this.currentMonthStatisticDTO = currentMonthStatisticDTO;
    }

    public void writeRowInExcel(Row row) {
        String format = String.format("%s: current: %d, previous: %d, diff: %d", groupDTO.getCity(),
                currentMonthStatisticDTO.getMemberCount(),
                previousMonthStatisticDTO.getMemberCount(),
                (currentMonthStatisticDTO.getMemberCount()- previousMonthStatisticDTO.getMemberCount())
                );
        row.createCell(0).setCellValue(groupDTO.getCity());
        row.createCell(1).setCellValue(Integer.toString(currentMonthStatisticDTO.getMemberCount()));
        row.createCell(2).setCellValue(Integer.toString(previousMonthStatisticDTO.getMemberCount()));
        row.createCell(3).setCellValue(Integer.toString(currentMonthStatisticDTO.getMemberCount()- previousMonthStatisticDTO.getMemberCount()));
    }

    public String category() {
        return groupDTO.getCategory();
    }

}
