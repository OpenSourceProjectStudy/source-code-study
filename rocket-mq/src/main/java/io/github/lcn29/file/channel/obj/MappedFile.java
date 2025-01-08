package io.github.lcn29.file.channel.obj;

import io.github.lcn29.base.ReferenceResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RocketMQ 中的 MappedFile 类
 *
 * @author lcn29
 * @date 2025-01-08 18:59:51
 */
public class MappedFile extends ReferenceResource {


    /**
     * 所有 MappedFile 实例已使用字节总数
     */
    private static final AtomicLong TOTAL_MAPPED_VIRTUAL_MEMORY = new AtomicLong(0);

    /**
     * 所有 MappedFile 个数
     */
    private static final AtomicInteger TOTAL_MAPPED_FILES = new AtomicInteger(0);

    /**
     * 文件总大小
     */
    protected int fileSize;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件对象
     */
    private File file;

    /**
     * 当前文件起始的写入字节数
     * <p>
     * RocketMQ 的设计:
     * 一个文件的名字就是当前文件存储的第一条消息的偏移量,
     * 通过这个偏移量 + 一个文件大小, 就可以获取到下一个文件的名字
     */
    private long fileFromOffset;

    /**
     * 文件通道
     */
    protected FileChannel fileChannel;

    /**
     * 实际就是操作系统的 PageCache (文件内容加载到内存中的对象)
     */
    private MappedByteBuffer mappedByteBuffer;

    public MappedFile(final String fileName, final int fileSize) throws IOException {
        init(fileName, fileSize);
    }


    @Override
    public boolean cleanup(long currentRef) {
        return true;
    }

    private void init(final String fileName, final int fileSize) throws IOException {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = new File(fileName);

        // commitLog 的文件名就是当前文件开始的消息偏移量
        this.fileFromOffset = Long.parseLong(this.file.getName());
        boolean ok = false;

        // 确保文件目录没有问题
        ensureDirOK(this.file.getParent());

        try {
            // 文件通道
            this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
            // 通过 FileChannel 创建 MappedByteBuffer, 将文件映射到内存中, 也就是使用 mmap 技术
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            // 全局记录用了多少文件映射内存
            TOTAL_MAPPED_VIRTUAL_MEMORY.addAndGet(fileSize);
            // 全局记录文件映射内存的文件个数
            TOTAL_MAPPED_FILES.incrementAndGet();
            ok = true;
        } catch (FileNotFoundException e) {

            throw e;
        } catch (IOException e) {

            throw e;
        } finally {
            if (!ok && this.fileChannel != null) {
                this.fileChannel.close();
            }
        }
    }

    public static void ensureDirOK(final String dirName) {
        if (dirName != null) {
            if (dirName.contains(",")) {
                String[] dirs = dirName.trim().split(",");
                for (String dir : dirs) {
                    createDirIfNotExist(dir);
                }
            } else {
                createDirIfNotExist(dirName);
            }
        }
    }

    private static void createDirIfNotExist(String dirName) {
        File f = new File(dirName);
        if (!f.exists()) {
            boolean result = f.mkdirs();
        }
    }
}
