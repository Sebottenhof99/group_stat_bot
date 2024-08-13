package com.am.telegram.groupstat.logic.report.statistic;

import com.am.telegram.groupstat.logic.report.group.GroupDTO;
import org.apache.poi.ss.usermodel.Row;

public class BasicGroupStatistic implements GroupStatistic {

  private final GroupDTO groupDTO;
  private final GroupMonthStatisticDTO previousMonthStatisticDTO;
  private final GroupMonthStatisticDTO currentMonthStatisticDTO;

  public BasicGroupStatistic(
      GroupDTO groupDTO,
      GroupMonthStatisticDTO previousMonthStatisticDTO,
      GroupMonthStatisticDTO currentMonthStatisticDTO) {
    this.groupDTO = groupDTO;
    this.previousMonthStatisticDTO = previousMonthStatisticDTO;
    this.currentMonthStatisticDTO = currentMonthStatisticDTO;
  }

  @Override
  public void writeInRow(Row row) {
    row.createCell(0).setCellValue(groupDTO.getCity());
    row.createCell(1).setCellValue(currentMonthStatisticDTO.getMemberCount());
    row.createCell(2)
        .setCellValue(
            previousMonthStatisticDTO == null ? "0" : previousMonthStatisticDTO.getMemberCount());
    row.createCell(3)
        .setCellValue(
            Integer.parseInt(currentMonthStatisticDTO.getMemberCount())
                - Integer.parseInt(
                    previousMonthStatisticDTO == null
                        ? "0"
                        : previousMonthStatisticDTO.getMemberCount()));
  }

  public String category() {
    return groupDTO.getCategory();
  }
}
