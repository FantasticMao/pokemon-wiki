package cn.fantasticmao.pokemon.spider;

import cn.fantasticmao.pokemon.spider.task1.*;
import cn.fantasticmao.pokemon.spider.task2.PokemonMoveDetailSpiderScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Starter
 *
 * @author maodh
 * @since 2018/7/29
 */
class Starter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) throws Exception {
        //task1();

        task2();

        PokemonDataSource.INSTANCE.shutDownDataSource();
    }

    static private void task1() throws InterruptedException {
        // 1. 初始化线程池
        UncaughtExceptionThreadFactory threadFactory = new UncaughtExceptionThreadFactory(1);
        ExecutorService executorService = Executors.newFixedThreadPool(6, threadFactory);

        // 2. 添加爬虫任务
        final CountDownLatch doneSignal = new CountDownLatch(5);
        executorService.execute(new PokemonListSpider(doneSignal));
        executorService.execute(new PokemonAbilityListSpider(doneSignal));
        executorService.execute(new PokemonNatureListSpider(doneSignal));
        executorService.execute(new PokemonBaseStatListSpider(doneSignal));
        executorService.execute(new PokemonMoveListSpider(doneSignal));

        // 3. 结束任务
        doneSignal.await();
        executorService.shutdownNow();
    }

    static private void task2() throws InterruptedException {
        // 1. 初始化线程池
        UncaughtExceptionThreadFactory threadFactory = new UncaughtExceptionThreadFactory(2);
        ExecutorService executorService = Executors.newCachedThreadPool(threadFactory);

        // 2. 添加爬虫任务
        new PokemonMoveDetailSpiderScheduler().start(executorService);
        //new PokemonAbilityDetailSpiderScheduler().start(executorService);

        // 3. 结束任务
        executorService.shutdownNow();
    }

    static class UncaughtExceptionThreadFactory implements ThreadFactory {
        private AtomicInteger atomicInteger;
        private final int poolNumber;

        UncaughtExceptionThreadFactory(int poolNumber) {
            this.atomicInteger = new AtomicInteger(1);
            this.poolNumber = poolNumber;
        }

        @Override
        public Thread newThread(@Nonnull Runnable runnable) {
            Thread thread = new Thread(runnable, String.format("Spider-Task-%d-Thread-%d", poolNumber, atomicInteger.getAndIncrement()));
            thread.setUncaughtExceptionHandler((t, e) -> LOGGER.error(t.getName(), e));
            return thread;
        }
    }
}