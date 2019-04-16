package jun.spring.etc;

import jun.spring.model.concurrent.SyncCounter;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static jun.spring.model.concurrent.SyncCounter.staticSum;
import static junit.framework.TestCase.assertEquals;

public class ShardBeanTests {
    /**
     *  synchronized key word
     *  Instance methods
     *  Static methods
     *  Code blocks
     */
    @Test
    public void synchronizedTest() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        SyncCounter syncCounter = new SyncCounter();

        IntStream.range(0, 30000)
                .forEach(count -> service.submit(syncCounter::calculate));

        service.awaitTermination(1000, TimeUnit.MILLISECONDS);

//        assertEquals(30000, syncCounter.getSum());
    }

    @Test
    public void 멀티쓰레드_스태틱메소드_테스트() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        IntStream.range(0, 30000)
                .forEach(count -> service.submit(SyncCounter::syncStaticCalculate));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);

        assertEquals(30000, staticSum);
    }

    @Test
    public void 멀티_쓰레드_SyncBlock_테스트() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        SyncCounter counter = new SyncCounter();

        IntStream.range(0, 50000)
                .forEach(count ->
                        service.submit(counter::syncCalculate));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertEquals(50000, counter.getSum());
    }

    @Test
    public void BlockingTest() throws InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        SyncCounter counter = new SyncCounter();
        service.submit(() -> {
            synchronized (counter) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter.calculate();
            }
        });

        System.out.println("1");
        counter.calculate();
        System.out.println("2");
        service.awaitTermination(3000, TimeUnit.SECONDS);
        System.out.println(counter.getSum());

    }



}

