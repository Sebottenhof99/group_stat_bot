package com.am.telegram.groupstat.logic.report;

import static java.util.stream.Collectors.groupingBy;

import com.am.telegram.groupstat.logic.report.concurrency.ReportSubscriber;
import com.am.telegram.groupstat.logic.report.excel.CurrentMonthSheet;
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
      var sheet = new CurrentMonthSheet(workbook);
      sheet.addHeader();

      List<GroupStatistic> groupStatistics = statisticService.generateCurrentReport();
      Map<String, List<GroupStatistic>> groupStatisticsPerCategory =
          groupStatistics.stream().collect(groupingBy(GroupStatistic::category));

      for (Map.Entry<String, List<GroupStatistic>> category :
          groupStatisticsPerCategory.entrySet()) {
        sheet.addCategory(category.getKey(), category.getValue());
      }

      save(workbook);
    } catch (IOException e) {
      throw new ReportGenerationException("Could not create and handle workbook", e);
    } finally {
      isReportBeingGenerated.set(false);
    }
    notifySubscribers();
  }

  private boolean isReportAlreadyGenerated() {
    return Files.exists(Path.of(currentReportName()));
  }

  private void save(Workbook workbook) {
    try (FileOutputStream fileOut = new FileOutputStream(currentReportName())) {
      workbook.write(fileOut);
    } catch (IOException e) {
      throw new ReportGenerationException("Could not save workbook", e);
    }
  }

  private String currentReportName() {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "-report.xlsx";
  }
}
