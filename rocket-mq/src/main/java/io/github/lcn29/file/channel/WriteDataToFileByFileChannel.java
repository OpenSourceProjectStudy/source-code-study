package io.github.lcn29.file.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-01-08 19:19:21
 */
public class WriteDataToFileByFileChannel {

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
     * 文件通道
     */
    protected FileChannel fileChannel;

    /**
     * 实际就是操作系统的 PageCache (文件内容加载到内存中的对象)
     */
    private MappedByteBuffer mappedByteBuffer;

    /**
     * 当前 MappedFile 对象当前写指针, 下次写数据从此开始写入
     */
    protected final AtomicInteger wrotePosition = new AtomicInteger(0);


    public static void main(String[] args) throws IOException {

        // 1 个字节 = 8 位即 1 byte
        // 1024 * 1024 * 10 = 10M
        int fileSize = 1024 * 1024 * 10;
        WriteDataToFileByFileChannel writeDataToFileByFileChannel = new WriteDataToFileByFileChannel("./test.txt", fileSize);

        int onceWriteDataSize = 1024;
        int currentWriteDateSize = 0;

        while (currentWriteDateSize < fileSize) {
            int dataLength = writeDataToFileByFileChannel.getWrotePosition() + onceWriteDataSize <= writeDataToFileByFileChannel.getFileSize()
                    ? onceWriteDataSize : writeDataToFileByFileChannel.getFileSize() - writeDataToFileByFileChannel.getWrotePosition();
            writeDataToFileByFileChannel.writeDataToFile(randomData(dataLength), dataLength);
            currentWriteDateSize += dataLength;
        }
        writeDataToFileByFileChannel.close();
        System.out.println("write data to file success");
    }

    public WriteDataToFileByFileChannel(String fileName, int fileSize) throws IOException {
        init(fileName, fileSize);
    }

    public int getWrotePosition() {
        return wrotePosition.get();
    }

    public int getFileSize() {
        return fileSize;
    }

    public void close() {
        if (fileChannel != null) {
            try {
                fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void init(String fileName, int fileSize) throws IOException {

        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = new File(fileName);

        try {
            // 文件通道
            this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
            // 通过 FileChannel 创建 MappedByteBuffer, 将文件映射到内存中, 也就是使用 mmap 技术
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
        } catch (IOException e) {
            throw e;
        } finally {
            if (this.fileChannel != null) {
                this.fileChannel.close();
            }
        }
    }

    private void writeDataToFile(ByteBuffer byteBuffer, int dataLength) {

        ByteBuffer slice = this.mappedByteBuffer.slice();
        slice.position(wrotePosition.get());
        slice.put(byteBuffer);

        mappedByteBuffer.force();
        wrotePosition.addAndGet(dataLength);
    }

    private static ByteBuffer randomData(int needDataSize) {
        Random random = new Random();
        ByteBuffer allocate = ByteBuffer.allocate(needDataSize);
        allocate.put((byte) random.nextInt(256));
        return allocate;
    }


}
