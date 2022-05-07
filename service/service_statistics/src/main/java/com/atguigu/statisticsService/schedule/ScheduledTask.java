package com.atguigu.statisticsService.schedule;

import com.atguigu.statisticsService.service.StatisticsDailyService;
import com.atguigu.statisticsService.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author liuzixuan
 * @ClassName ScheduledTask
 * @Date 20211015
 */
@Component
public class ScheduledTask {
    @Autowired
    StatisticsDailyService statisticsDailyService;
    // cron七子表达式：第七位不要添加，默认为当今年，加了会报错
    // 每日下午四点零8执行,定时统计各个注册登录播放等等数据
    @Scheduled(cron = "0 8 16 * * ?")
    public void task(){
        // 获取上一条的日期
        Date date = DateUtil.addDays(new Date(), -1);
        String day = DateUtil.formatDate(date);
        statisticsDailyService.getStatistics(day);
    }
}
