package org.apache.dubbo.demo.provider;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author spilledyear
 * @date 2019-01-09 10:23
 */
public class CompletableFutureTest {
    public static void main(String[] args) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
//            completableFuture.completeExceptionally(new RuntimeException("error"));
//            completableFuture.complete(Thread.currentThread().getName());
            completableFuture.complete(doSomethingElse());
        }).start();

//        doSomethingElse();

        System.out.println("我是线程：" + Thread.currentThread().getName());

        try {
            // completableFuture.get() 方法会阻塞
            System.out.println(completableFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * 做你想做的耗时操作
     */
    public static String doSomethingElse() {
        try {
            System.out.println("【开始】做你想做的耗时操作");
            Thread.sleep(3000);
            System.out.println("【结束】做你想做的耗时操作");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "耗时操作返回的结果";
    }


    @Test
    public void testGet() {

        // 开启一个异步计算
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            return Thread.currentThread().getName();
        });


        try {
            System.out.println(completableFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testComplete() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName();
        });

        // whenComplete不会阻塞
        future.whenComplete((name, error) -> System.out.println(Thread.currentThread().getName() + "    " + name));

        System.out.println("做一些其它的操作");
        System.out.println(future.get());
//        Thread.sleep(3000);
    }


    @Test
    public void testApply() throws ExecutionException, InterruptedException {
        /**
         * 没有以Async结尾的，会用当前线程
         * ForkJoinPool.commonPool-worker-1   :   main   :   ForkJoinPool.commonPool-worker-1   :   main
         */

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return Thread.currentThread().getName();
        });

        CompletableFuture<String> f = future
                .thenApply(preName -> preName + "   :   " + Thread.currentThread().getName())
                .thenApplyAsync(preName -> preName + "   :   " + Thread.currentThread().getName())
                .thenApply(preName -> preName + "   :   " + Thread.currentThread().getName());
        System.out.println(f.get());
    }


    @Test
    public void testAccept() throws ExecutionException, InterruptedException {
        /**
         *
         * 只会对计算结果消费不会返回任何结果的方法
         * thenAccept、thenAcceptAsync、thenAcceptAsync
         */

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return Thread.currentThread().getName();
        });

        future.thenAccept(preName -> System.out.println(preName + "：" + Thread.currentThread().getName()));
        future.thenAcceptAsync(preName -> System.out.println(preName + "：" + Thread.currentThread().getName()));
    }


    @Test
    public void testAcceptBoth() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return 10;
        });

        System.out.println(
                future.thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
                    return 20;
                }), (x, y) -> System.out.println(x + y)).get()
        );
    }


}
