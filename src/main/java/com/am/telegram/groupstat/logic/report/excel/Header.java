package com.am.telegram.groupstat.logic.report.excel;

import org.apache.poi.xssf.usermodel.XSSFRow;

public class Header {

  private final XSSFRow xssfRow;

  public Header(XSSFRow xssfRow) {
    this.xssfRow = xssfRow;
  }

  public void add() {
    xssfRow.createCell(0).setCellValue("Город");
    xssfRow.createCell(1).setCellValue("Текущий месяц");
    xssfRow.createCell(2).setCellValue("Предыдущий месяц");
    xssfRow.createCell(3).setCellValue("Дельта");
  }
}
