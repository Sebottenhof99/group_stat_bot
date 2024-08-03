package com.am.telegram.groupstat.user.report.statistic;

import org.apache.poi.ss.usermodel.Row;

public interface GroupStatistic {
    void writeInRow(Row row);

    String category();
}
