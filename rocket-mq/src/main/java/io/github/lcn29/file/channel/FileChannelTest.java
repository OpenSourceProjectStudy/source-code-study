package io.github.lcn29.file.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * TODO
 *
 * @author lcn29
 * @date 2025-01-08 15:50:45
 */
public class FileChannelTest {

    private static final String RESOURCE_NAME = "fileChannel/file-channel-test.txt";

    private static final String RESOURCE_WRITE_NAME = "./file-channel-write.txt";

    public static void main(String[] args) throws Exception {
        // fileChannelTest(RESOURCE_NAME);
        mappedByteBufferTest(RESOURCE_NAME);
    }


    private static void a(String fileName, long fileSize) throws IOException {

        File file = new File(fileName);
        // 文件通道
        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
    }

    /**
     * 通过 FileChannel 读取文件内容到 String 中
     */
    private static void fileChannelTest(String filePath) throws URISyntaxException, IOException {
        FileChannel fileChannel = createFileChannel(filePath, StandardOpenOption.READ);
        // 创建一个足够大的 ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
        fileChannel.read(buffer);
        // 切换到读取模式
        buffer.flip();
        // 将 ByteBuffer 的内容转换为 String
        String fileContent = StandardCharsets.UTF_8.decode(buffer).toString();
        // 输出读取到的内容
        System.out.println(fileContent);
        fileChannel.close();
    }

    /**
     * 通过 MappedByteBuffer 读取文件内容到 String 中
     */
    private static void mappedByteBufferTest(String filePath) throws URISyntaxException, IOException {

        FileChannel fileChannel = createFileChannel(filePath, StandardOpenOption.READ);
        MappedByteBuffer data = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

        // 这个会导致 pos 等变化
        String fileContent = StandardCharsets.UTF_8.decode(data).toString();
        System.out.println(fileContent);

        // 重置会初始状态
        data.flip();
        // 文件在 resource 没法修改，所以写到当前目录
        FileChannel writeChannel = FileChannel.open(Paths.get(RESOURCE_WRITE_NAME), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        writeChannel.write(data);

        writeChannel.close();
        fileChannel.close();
    }

    /**
     * 创建 FileChannel
     */
    private static FileChannel createFileChannel(String filePath, OpenOption... options) throws URISyntaxException, IOException {
        URL resource = FileChannelTest.class.getClassLoader().getResource(filePath);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found!");
        }
        Path resourcePath = Paths.get(resource.toURI());
        return FileChannel.open(resourcePath, options);
    }

}
