package com.linfeng.staservice.service;

import com.linfeng.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-10-13
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void registerCount(String day);

    //图表显示，返回两部分数据，日期json数据，数量json数据
    Map<String,Object> getShowData(String type, String begin, String end);
}
