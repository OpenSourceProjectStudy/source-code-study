package io.github.lcn29.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-03-03 17:22:30
 */
public class LoggerService {

    private static final LoggerService SERVICE = new LoggerService();

    private final Logger logger;

    private LoggerService() {
        ServiceLoader<Logger> loader = ServiceLoader.load(Logger.class);
        List<Logger> list = new ArrayList<>();

        for (Logger logger : loader) {
            list.add(logger);
        }

        if (list.isEmpty()) {
            logger = null;
        } else {
            logger = list.get(0);
        }
    }

    public static LoggerService getService() {
        return SERVICE;
    }

    public void info(String msg) {
        if (logger == null) {
            System.out.println("info 中没有发现 Logger 服务提供者");
            return;
        }
        logger.info(msg);
    }

    public void debug(String msg) {
        if (logger == null) {
            System.out.println("debug 中没有发现 Logger 服务提供者");
            return;
        }
        logger.debug(msg);
    }
}
