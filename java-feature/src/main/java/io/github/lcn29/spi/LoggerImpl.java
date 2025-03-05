package io.github.lcn29.spi;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-03-03 17:29:41
 */
public class LoggerImpl implements Logger {

    @Override
    public void info(String msg) {
        System.out.println("实现类 info 打印: --->" + msg);
    }

    @Override
    public void debug(String msg) {
        System.out.println("实现类 debug 打印: --->" + msg);
    }

}
