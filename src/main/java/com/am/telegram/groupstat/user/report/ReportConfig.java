package com.am.telegram.groupstat.user.report;

import com.am.telegram.groupstat.user.report.group.GroupRepository;
import com.am.telegram.groupstat.user.report.statistic.StatisticRepository;
import com.am.telegram.groupstat.user.report.statistic.StatisticService;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ReportConfig {

    @Bean
    public GroupRepository groupRepository() {
        return new GroupRepository();
    }

    @Bean
    public StatisticRepository statisticRepository(TelegramBot bot) {
        return new StatisticRepository(bot);
    }

    @Bean
    public StatisticService statisticService(GroupRepository groupRepository, StatisticRepository statisticRepository, DataSource ds) {
        return new StatisticService(groupRepository, statisticRepository, ds);
    }

    @Bean
    public ReportService reportService(StatisticService statisticServices) {
        return new ReportService(statisticServices);
    }
}
