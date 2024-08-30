package com.am.telegram.groupstat.logic.report.excel;

import org.apache.poi.ss.usermodel.Row;

public class Header {

  private final Row row;

  public Header(Row row) {
    this.row = row;
  }

  public void add() {
    row.createCell(0).setCellValue("Город");
    row.createCell(1).setCellValue("Текущий месяц");
    row.createCell(2).setCellValue("Предыдущий месяц");
    row.createCell(3).setCellValue("Дельта");
  }
}
