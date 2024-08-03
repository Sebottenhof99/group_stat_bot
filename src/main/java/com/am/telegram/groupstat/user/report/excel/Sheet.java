package com.am.telegram.groupstat.user.report.excel;

import com.am.telegram.groupstat.user.report.statistic.BasicGroupStatistic;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;

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
