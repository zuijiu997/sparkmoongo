import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueDemo {
    public static void main(String[] args) {

        // take, poll, peek 等读操作的方法需要获取到这个锁
        ReentrantLock takeLock = new ReentrantLock();

// 如果读操作的时候队列是空的，那么等待 notEmpty 条件
        Condition notEmpty = takeLock.newCondition();

        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(10);
        try {
            arrayBlockingQueue.put("1");
            arrayBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //非阻塞队列
        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
    }
}
