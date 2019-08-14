import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Buffer 的基本用法
 * 使用 Buffer 读写数据一般遵循以下四个步骤：
 *
 * 写入数据到 Buffer
 * 调用flip()方法
 * 从 Buffer 中读取数据
 * 调用clear()方法或者compact()方法
 * 当向 buffer 写入数据时，buffer 会记录下写了多少数据。一旦要读取数据，需要通过 flip() 方法将 Buffer 从写模式切换到读模式。在读模式下，可以读取之前写入到 buffer 的所有数据。
 *
 * 一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。
 * 有两种方式能清空缓冲区：调用 clear() 或 compact() 方法。clear() 方法会清空整个缓冲区。
 * compact() 方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
 */

public class Nio {

    public static void main(String[] args) {

//
//        //使用Paths工具类的get()方法创建
//        Path path = Paths.get("D:\\XMind\\bcl-java.txt");
///*        //使用FileSystems工具类创建
//        Path path2 = FileSystems.getDefault().getPath("c:\\data\\myfile.txt");*/
//        System.out.println("文件名：" + path.getFileName());
//        System.out.println("名称元素的数量：" + path.getNameCount());
//        System.out.println("父路径：" + path.getParent());
//        System.out.println("根路径：" + path.getRoot());
//        System.out.println("是否是绝对路径：" + path.isAbsolute());
//        //startsWith()方法的参数既可以是字符串也可以是Path对象
//        System.out.println("是否是以为给定的路径D:开始：" + path.startsWith("D:\\") );
//        System.out.println("该路径的字符串形式：" + path.toString());

        File file = new File("");
//        file.getPath();
//        Files.copy(new Path())

//        Path path = Files.walkFileTree();
//        path.get

        readNIO();
    }


    private static void readNIO() {
        String pathname = "C:\\Users\\Administrator\\Desktop\\test.txt";
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(pathname));
            FileChannel channel = fin.getChannel();

            int capacity = 100;// 字节
            ByteBuffer bf = ByteBuffer.allocate(capacity);
            System.out.println("限制是：" + bf.limit() + " 容量是：" + bf.capacity()
                    + " 位置是：" + bf.position());
            int length = -1;

            while ((length = channel.read(bf)) != -1) {
                System.out.println("-------------限制是：" + bf.limit() + " 容量是：" + bf.capacity()
                        + " 位置是：" + bf.position());
                /*
                 * 注意，读取后，将位置置为0，将limit置为容量, 以备下次读入到字节缓冲中，从0开始存储
                 */
                bf.clear();
                byte[] bytes = bf.array();
                System.out.write(bytes, 0, length);
                System.out.println();

                System.out.println("限制是：" + bf.limit() + " 容量是：" + bf.capacity()
                        + " 位置是：" + bf.position());

            }

            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void writeNIO() {
        String filename = "out.txt";
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(new File(filename));
            FileChannel channel = fos.getChannel();
            ByteBuffer src = Charset.forName("utf8").encode("你好你好你好你好你好");
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            System.out.println("初始化容量和limit：" + src.capacity() + ","
                    + src.limit());
            int length = 0;

            while ((length = channel.write(src)) != 0) {
                /*
                 * 注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                 */
                System.out.println("写入长度:" + length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void testReadAndWriteNIO() {
        String pathname = "C:\\Users\\adew\\Desktop\\test.txt";
        FileInputStream fin = null;

        String filename = "test-out.txt";
        FileOutputStream fos = null;
        try {
            fin = new FileInputStream(new File(pathname));
            FileChannel channel = fin.getChannel();

            int capacity = 100;// 字节
            ByteBuffer bf = ByteBuffer.allocate(capacity);
            System.out.println("限制是：" + bf.limit() + "容量是：" + bf.capacity()+ "位置是：" + bf.position());
            int length = -1;

            fos = new FileOutputStream(new File(filename));
            FileChannel outchannel = fos.getChannel();


            while ((length = channel.read(bf)) != -1) {

                //将当前位置置为limit，然后设置当前位置为0，也就是从0到limit这块，都写入到同道中
                bf.flip();

                int outlength =0;
                while((outlength=outchannel.write(bf)) != 0){
                    System.out.println("读，"+length+"写,"+outlength);
                }

                //将当前位置置为0，然后设置limit为容量，也就是从0到limit（容量）这块，
                //都可以利用，通道读取的数据存储到
                //0到limit这块
                bf.clear();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    @SuppressWarnings("resource")
//    public static void main(String[] args) {
//        testReadAndWriteNIO();
//    }
}
