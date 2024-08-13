package com.am.telegram.groupstat.logic.report.excel;

import com.am.telegram.groupstat.logic.report.statistic.BasicGroupStatistic;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Sheet {
  private final XSSFSheet xssfSheet;

  public Sheet(XSSFSheet xssfSheet) {
    this.xssfSheet = xssfSheet;
  }

  public void addHeader() {
    Header header = new Header(xssfSheet.createRow(0));
    header.add();
  }

  public void addData(List<BasicGroupStatistic> groupStatistics) {
    for (int i = 0; i < groupStatistics.size(); i++) {
      groupStatistics.get(i).writeInRow(xssfSheet.createRow(i + 1));
    }
  }
}
