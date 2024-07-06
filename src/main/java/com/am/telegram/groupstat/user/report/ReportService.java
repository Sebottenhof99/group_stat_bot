package com.am.telegram.groupstat.user.report;

import com.am.telegram.groupstat.user.report.statistic.GroupStatistic;
import com.am.telegram.groupstat.user.report.statistic.StatisticService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class ReportService {

    private final StatisticService statisticService;


    public ReportService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    public void createCurrentReport() throws IOException {
        List<GroupStatistic> groupStatistics = statisticService.generateCurrentReport();
        Map<String, List<GroupStatistic>> groupStatisticsPerCategory = groupStatistics.stream()
                .collect(groupingBy(GroupStatistic::category));

        int rowNumber = 1;

        //create a workbook
       try(Workbook workbook = new XSSFWorkbook()){
           //create a sheet in the workbook(you can give it a name)
           Sheet sheet = workbook.createSheet("juni");
           Row header = sheet.createRow(0);
           header.createCell(0).setCellValue("Город");
           header.createCell(1).setCellValue("Настоящее значение");
           header.createCell(2).setCellValue("Предыдущее значение");
           header.createCell(3).setCellValue("Разница");

           for (String category : groupStatisticsPerCategory.keySet()) {
               Row categoryRow = sheet.createRow(rowNumber++);
               categoryRow.createCell(0).setCellValue(category);
               for (GroupStatistic groupStatistic : groupStatisticsPerCategory.get(category)) {
                   Row groupRow = sheet.createRow(rowNumber++);
                   groupStatistic.writeRowInExcel(groupRow);

               }
           }
           sheet.autoSizeColumn(0);
           sheet.autoSizeColumn(1);
           sheet.autoSizeColumn(2);
           sheet.autoSizeColumn(3);
            save(workbook);
       }
    }

    private void save(Workbook workbook ){
        try (FileOutputStream fileOut = new FileOutputStream("report.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
