package com.am.telegram.groupstat.logic.report;

import static java.util.stream.Collectors.groupingBy;

import com.am.telegram.groupstat.logic.report.concurrency.ReportSubscriber;
import com.am.telegram.groupstat.logic.report.statistic.GroupStatistic;
import com.am.telegram.groupstat.logic.report.statistic.StatisticService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportService {

  private static final Logger log = LoggerFactory.getLogger(ReportService.class);
  private final AtomicBoolean isReportBeingGenerated = new AtomicBoolean(false);
  private final List<ReportSubscriber> reportSubscribers =
      Collections.synchronizedList(new ArrayList<>());

  private final StatisticService statisticService;

  public ReportService(StatisticService statisticService) {
    this.statisticService = statisticService;
  }

  public void subscribe(ReportSubscriber reportSubscriber) {
    if (reportSubscribers.contains(reportSubscriber)) {
      return;
    }
    reportSubscribers.add(reportSubscriber);
    if (isReportAlreadyGenerated()) {
      log.info("Report is already generated. Sending it to subscribers");
      notifySubscribers();
    } else if (!isReportBeingGenerated.get()) {
      log.info("Starting to generate report");
      Thread thread = new Thread(this::generateReport);
      thread.start();
    }
  }

  private void notifySubscribers() {
    log.info("Notifying subscribers");
    File report = new File(currentReportName());
    reportSubscribers.forEach(s -> s.update(report));
    reportSubscribers.clear();
  }

  private void generateReport() {
    isReportBeingGenerated.set(true);
    log.info("Generating report");

    try (Workbook workbook = new XSSFWorkbook()) {
      List<GroupStatistic> groupStatistics = statisticService.generateCurrentReport();
      Map<String, List<GroupStatistic>> groupStatisticsPerCategory =
          groupStatistics.stream().collect(groupingBy(GroupStatistic::category));
      Sheet sheet =
          workbook.createSheet(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM")));
      Row header = sheet.createRow(0);
      header.createCell(0).setCellValue("Город");
      header.createCell(1).setCellValue("Настоящее значение");
      header.createCell(2).setCellValue("Предыдущее значение");
      header.createCell(3).setCellValue("Разница");

      int rowNumber = 1;
      for (Map.Entry<String, List<GroupStatistic>> category :
          groupStatisticsPerCategory.entrySet()) {
        Row categoryRow = sheet.createRow(rowNumber++);
        categoryRow.createCell(0).setCellValue(category.getKey());
        for (GroupStatistic groupStatistic : category.getValue()) {
          Row groupRow = sheet.createRow(rowNumber++);
          groupStatistic.writeInRow(groupRow);
        }
      }

      save(workbook);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      isReportBeingGenerated.set(false);
    }
    notifySubscribers();
  }

  private boolean isReportAlreadyGenerated() {
    var reportName =
        LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "-report.xlsx";
    return Files.exists(Path.of(reportName));
  }

  private void save(Workbook workbook) {
    try (FileOutputStream fileOut = new FileOutputStream(currentReportName())) {
      workbook.write(fileOut);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String currentReportName() {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "-report.xlsx";
  }
}
