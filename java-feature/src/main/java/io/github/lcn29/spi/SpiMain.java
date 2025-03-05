package io.github.lcn29.spi;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-03-03 17:20:43
 */
public class SpiMain {

    public static void main(String[] args) {
        // 在 resources 里面新建 META-INF/services/io.github.lcn29.spi.Logger 文件
        // 文件内容为 io.github.lcn29.spi.LoggerImpl
        LoggerService loggerService = LoggerService.getService();
        loggerService.info("info");
        loggerService.debug("debug");
    }
}
