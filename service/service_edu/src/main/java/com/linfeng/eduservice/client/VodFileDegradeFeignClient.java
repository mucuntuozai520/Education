package com.linfeng.eduservice.client;

import com.linfeng.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @author Yu1
 * @date 2022/10/13 - 16:50
 */
@Component
public class VodFileDegradeFeignClient implements VodClient {
   //出错之后会执行
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }


}
