package com.fly.video;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
@Slf4j
public class LifeCycleListener implements DisposableBean {

    @Value("${nginx.path}")
    private String nginxPath;

    @EventListener(classes = ApplicationReadyEvent.class)
    public void startNginx() throws IOException {
        log.info("开始启动nginx...");
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /k cd /d " + nginxPath + " && start nginx");

        log.info("启动nginx完成");

        // 获取IP地址
        List<String> networkAddress = getNetworkAddress();
        //打印
        log.info("ip为：{}", networkAddress);
    }


    @Override
    public void destroy() throws Exception {

        log.info("开始停止nginx...");
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /k cd /d "
                + nginxPath
                + " && nginx.exe -s stop");
        log.info("停止nginx完成");
    }


    private List<String> getNetworkAddress() throws SocketException {
        List<String> result = new ArrayList<>();
        Enumeration<NetworkInterface> netInterfaces;

        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> addresses=ni.getInetAddresses();
            while(addresses.hasMoreElements()){
                ip = addresses.nextElement();
                if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                    result.add(ip.getHostAddress());
                }
            }
        }
        return result;

    }

}
