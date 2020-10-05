package com.fly.video;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@SpringBootApplication
@Slf4j
public class VideoPlayerApplication {

    public static void main(String[] args) throws IOException {

        log.info("开始启动nginx...");
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /k cd /d D:\\nginx\\nginx-1.19.3 && start nginx");

        // 获取IP地址
        List<String> networkAddress = getNetworkAddress();
        //打印
        log.info("ip为：{}", networkAddress);

        SpringApplication.run(VideoPlayerApplication.class, args);
    }

    public static List<String> getNetworkAddress() throws SocketException {
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
