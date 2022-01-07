package cn.mrcode.kafka.collector.web;

import cn.mrcode.kafka.collector.util.InputMDC;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.jboss.logging.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mrcode
 * @date 2021/12/16 21:10
 */
@Slf4j
@RestController
public class IndexController {
    /**
     * [%d{yyyy-MM-dd'T'HH:mm:ss.SSSZZ}]   格式化成 UTC 时间
     * [%level{length=5}] 日志级别
     * [%thread-%tid] 线程 ID
     * [%logger] 日志信息，是哪一个类打印的日志，全限定包名
     * [%X{hostName}]   X 表示自定义参数信息
     * [%X{ip}]
     * [%X{applicationName}]
     * [%F,%L,%C,%M] 类名（无具体包名）、行数、具体包名+类名、方法名
     * [%m] 表示消息的具体内容，比如：我是一条 info 级别的日志
     * ## '%ex' 这里是自定义特殊约定的，两个 ## 分隔，后面的是 $ex 是异常信息，外面携带了单引号，后面会说明它的含义
     * %n 换行符
     *
     * @return
     */
    @RequestMapping(value = "/index")
    public String index() {
        InputMDC.putMDC();
        log.info("我是一条 info 级别的日志");
        log.warn("我是一条 warn 级别的日志");
        log.error("我是一条 error 级别的日志");
        return "index";
    }

    @RequestMapping(value = "/index-error")
    public String error() {
        RuntimeException ex = new RuntimeException("模拟异常信息");
        log.error("我是一条 error 级别的日志", ex);
        return "index";
    }
}
