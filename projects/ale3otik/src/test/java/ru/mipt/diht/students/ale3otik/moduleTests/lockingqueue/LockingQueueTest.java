package ru.mipt.diht.students.ale3otik.moduletests.lockingqueue;

import org.junit.Before;
import org.junit.Test;
import ru.mipt.diht.students.ale3otik.threads.lockingqueue.LockingQueue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by alex on 06.12.15.
 */
public class LockingQueueTest {
    private static int MAX_QUEUE_SIZE = 30;
    private List<Integer> baseList;
    private LockingQueue<Integer> queue;

    @Before
    public void setUp() {
        baseList = new LinkedList<>();
        for (int i = 0; i < MAX_QUEUE_SIZE - 10; ++i) {
            baseList.add(i);
        }

        queue = new LockingQueue<>(MAX_QUEUE_SIZE);
    }

    private static class ThreadTaker extends Thread {
        volatile LockingQueue<Integer> queue;
        volatile List<Integer> answer = Arrays.asList(-1);

        ThreadTaker(LockingQueue<Integer> rcvQueue) {
            queue = rcvQueue;
        }

        @Override
        public void run() {
            try {
                answer = queue.take(4, 300);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                assert true;
            }
        }
    }

    private static class ThreadOffer extends Thread {
        volatile LockingQueue<Integer> queue;
        List<Integer> toAdd;

        ThreadOffer(LockingQueue<Integer> rcvQueue, List<Integer> toAddList) {
            queue = rcvQueue;
            toAdd = toAddList;
        }

        @Override
        public void run() {
            try {
                queue.offer(toAdd, 300);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                assert true;
            }
        }
    }

    @Test(timeout = 1000)
    public void testSimpleRequestQueue() throws Exception {
        queue.offer(baseList);
        assertThat(queue.take(baseList.size()), equalTo(baseList));
    }

    @Test(timeout = 1000)
    public void testTakeDelay() throws Exception {
        queue.offer(Arrays.asList(0, 0));
        ThreadTaker taker = new ThreadTaker(queue);
        taker.start();
        queue.offer(baseList);
        Thread.sleep(100);
        assertThat(taker.answer, equalTo(Arrays.asList(0, 0, 0, 1)));
        assertThat(queue.take(1), equalTo(Arrays.asList(2)));
    }

    @Test(timeout = 1000)
    public void testTakeDelaySkip() throws Exception {
        queue.offer(Arrays.asList(0, 1));
        List<Integer> answer = queue.take(4, 200);
        assertThat(queue.take(2), equalTo(Arrays.asList(0, 1)));
        assertThat(answer, nullValue());
    }

    @Test(timeout = 1000)
    public void testOfferDelay() throws Exception {
        queue.offer(baseList);
        ThreadOffer offer = new ThreadOffer(queue, baseList);
        offer.start();

        queue.take(20);
        Thread.sleep(100);
        assertThat(queue.take(20), equalTo(baseList));
    }

    @Test(timeout = 1000)
    public void testOfferDelaySkip() throws Exception {
        queue.offer(baseList);
        queue.offer(baseList, 200);
        queue.offer(Arrays.asList(0, 1));
        assertThat(queue.take(20), equalTo(baseList));
        assertThat(queue.take(2), equalTo(Arrays.asList(0, 1)));
    }
}

