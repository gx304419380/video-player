package com.fly.video;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LifeCycleListener implements DisposableBean {


    @Override
    public void destroy() throws Exception {

        log.info("开始停止nginx...");
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /k cd /d D:\\nginx\\nginx-1.19.3 && " +
                "nginx.exe -s stop");
        log.info("停止nginx完成");
    }


}
