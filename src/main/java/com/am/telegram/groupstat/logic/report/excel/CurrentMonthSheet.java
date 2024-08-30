package com.am.telegram.groupstat.logic.report.excel;

import com.am.telegram.groupstat.logic.report.statistic.GroupStatistic;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class CurrentMonthSheet {
  private final Sheet sheet;
  private int rowIndex = 1;

  public CurrentMonthSheet(Workbook workbook) {
    this.sheet = workbook.createSheet(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM")));
  }

  public void addHeader() {
    Header header = new Header(sheet.createRow(0));
    header.add();
  }

  public void addCategory(String categoryName, List<GroupStatistic> basicGroupStatistics) {
    Row categoryRow = sheet.createRow(rowIndex++);
    categoryRow.createCell(0).setCellValue(categoryName);
    addData(basicGroupStatistics);
  }

  public void addData(List<GroupStatistic> groupStatistics) {
    groupStatistics.forEach(
        groupStatistic -> groupStatistic.writeInRow(sheet.createRow(rowIndex++)));
  }
}
