package com.linfeng.eduorder.service;

import com.linfeng.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-10-11
 */
public interface PayLogService extends IService<PayLog> {

    Map createQRCode(String orderNo);

    Map<String,String> queryPayStatus(String orderNo);

    void updateOrdersStatus(Map<String,String> map);
}
