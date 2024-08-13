package com.am.telegram.groupstat.logic.report.excel;

import com.am.telegram.groupstat.logic.report.statistic.BasicGroupStatistic;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelReport {

  private final Sheet sheet;

  public ExcelReport(Workbook workbook) {
    this.sheet = new Sheet((XSSFSheet) workbook.createSheet(""));
  }

  public void generate(List<BasicGroupStatistic> groupStatistics) {
    sheet.addHeader();
    sheet.addData(groupStatistics);
  }
}
