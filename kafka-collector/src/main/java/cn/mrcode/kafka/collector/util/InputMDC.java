package cn.mrcode.kafka.collector.util;

import cn.hutool.core.net.NetUtil;
import lombok.SneakyThrows;
import org.slf4j.MDC;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author mrcode
 * @date 2021/12/21 21:26
 */
@Component
public class InputMDC implements EnvironmentAware {
    private static Environment environment;

    /**
     * 实现 spring context 的 EnvironmentAware，这个会将解析后的配置文件信息放在该对象中，通过这个对象就能拿到配置文件的信息
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        InputMDC.environment = environment;
    }

    public static void putMDC() {
        // NetUtil 也有获取 hostName 方法，但是它获取到的是局域网的 IP 信息，这个 IP 信息对应的 hostName 是空的
        // 所以用 jdk 自带的获取，它获取到的 IP 是 127.0.0.1 的，包含正确的 主机名称
        MDC.put("hostName", getLocalHostName());
        MDC.put("ip", NetUtil.getLocalhostStr());
        MDC.put("applicationName", environment.getProperty("spring.application.name"));
    }

    @SneakyThrows
    public static String getLocalHostName() {
        InetAddress localHost = InetAddress.getLocalHost();
        return localHost.getHostName();
    }
}
