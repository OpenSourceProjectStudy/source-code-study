package io.github.lcn29.completable.future;

import java.util.concurrent.CompletableFuture;

/**
 * TODO
 *
 * @author canxin.li
 * @date 2025-01-16 19:27:47
 */
public class CompletableFutureStudy {

    public static void main(String[] args) {
        System.out.println("Main 方法执行线程:" + Thread.currentThread().getName());
        // runAsync();
        // supplyAsync();
        // thenAccept();
        // thenAcceptAsync();
        //thenRun();
        //thenRunAsync();
        //whenComplete();
        //whenCompleteAsync();
        //handle();
        handleAsync();
    }

    private static void runAsync() {
        // 异步执行, 通过 ForkJoinPool.commonPool() 线程池执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
            System.out.println("message");
        });

        // 当前线程挂起，等待执行结果
        voidCompletableFuture.join();
        System.out.println("Main 方法执行结束");
    }

    private static void supplyAsync() {
        // 异步执行, 通过 ForkJoinPool.commonPool() 线程池执行上面的 Lambda 表达式, 然后返回一个有返回值的 CompletableFuture
        // 可以指定返回值
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("沉睡结束");
            return "ok";
        });
        try {
            // 等待完成返回结果
            // get() 有个重载方法, 可以指定超时时间
            // complete(默认值) 没有完成, 返回默认值
            System.out.println(stringCompletableFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void thenAccept() {
        // 这样子类似于链式执行, 本身还是在当前线程执行
        CompletableFuture<String> messageCompletableFuture = CompletableFuture.completedFuture("message");
        // thenAccept 同步执行, 会先执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<Void> voidCompletableFuture = messageCompletableFuture.thenAccept(msg -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
            System.out.println(msg);
        });
        // 本质: thenAccept 会在上面的 CompletableFuture 执行完成后执行, 是同一个线程
    }

    private static void thenAcceptAsync() {
        // 这样子类似于链式执行, 本身还是在当前线程执行
        CompletableFuture<String> messageCompletableFuture = CompletableFuture.completedFuture("message");

        // thenAccept 异步执行, 通过 ForkJoinPool.commonPool() 线程池执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<Void> voidCompletableFuture = messageCompletableFuture.thenAcceptAsync(msg -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
            System.out.println(msg);
        });
        // 当前线程挂起，等待执行结果
        voidCompletableFuture.join();
        System.out.println("Main 方法执行结束");
        // 本质: thenAcceptAsync 会在上面的 CompletableFuture 执行完成, 通过另一个线程执行
    }

    private static void thenRun() {

        // 这样子类似于链式执行, 本身还是在当前线程执行
        CompletableFuture<String> messageCompletableFuture = CompletableFuture.completedFuture("message");

        // thenApply 同步执行, 会先执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<Void> stringCompletableFuture = messageCompletableFuture.thenRun(() -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
        });
        // 本质: thenRun 会在上面的 CompletableFuture 执行完成后执行, 是同一个线程, 无返回值
    }

    private static void thenRunAsync() {

        // 这样子类似于链式执行, 本身还是在当前线程执行
        CompletableFuture<String> messageCompletableFuture = CompletableFuture.completedFuture("message");

        // thenApply 同步执行, 会先执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<Void> stringCompletableFuture = messageCompletableFuture.thenRunAsync(() -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
        });
        // 本质: thenRunAsync 会在上面的 CompletableFuture 执行完成, 通过另一个线程执行, 无返回值
    }

    private static void whenComplete() {

        // 这样子类似于链式执行, 本身还是在当前线程执行
        CompletableFuture<String> messageCompletableFuture = CompletableFuture.completedFuture("message");

        // thenApply 同步执行, 会先执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<String> stringCompletableFuture = messageCompletableFuture.whenComplete((msg, e) -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
        });
        try {
            System.out.println(stringCompletableFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 本质: whenComplete 会在上面的 CompletableFuture 执行完成后执行, 是同一个线程, 将上一个的结果/异常传递给返回给调用方
    }

    private static void whenCompleteAsync() {

        // 这样子类似于链式执行, 本身还是在当前线程执行
        CompletableFuture<String> messageCompletableFuture = CompletableFuture.completedFuture("message");

        // thenApply 同步执行, 会先执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<String> stringCompletableFuture = messageCompletableFuture.whenCompleteAsync((msg, e) -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
        });
        try {
            System.out.println(stringCompletableFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 本质: whenComplete 会在上面的 CompletableFuture 通过另一个线程执行, 将上一个的结果/异常传递给返回给调用方
    }

    private static void handle() {

        // 这样子类似于链式执行, 本身还是在当前线程执行
        CompletableFuture<String> messageCompletableFuture = CompletableFuture.completedFuture("message");

        // thenApply 同步执行, 会先执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<String> stringCompletableFuture = messageCompletableFuture.handle((msg, e) -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
            // 返回一个新的值
            return msg + "/////";
        });
        try {
            System.out.println(stringCompletableFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 本质: handle 会在上面的 CompletableFuture 执行完成后执行, 是同一个线程, 将上一个异常/ 自身的返回值传递给返回给调用方
    }

    private static void handleAsync() {

        // 这样子类似于链式执行, 本身还是在当前线程执行
        CompletableFuture<String> messageCompletableFuture = CompletableFuture.completedFuture("message");

        // thenApply 同步执行, 会先执行上面的 Lambda 表达式, 然后返回一个返回值为 void 的 CompletableFuture
        CompletableFuture<String> stringCompletableFuture = messageCompletableFuture.handleAsync((msg, e) -> {
            System.out.println("CompletableFuture 执行的线程 " + Thread.currentThread().getName());
            // 返回一个新的值
            return msg + "/////000";
        });
        try {
            System.out.println(stringCompletableFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 本质: handle 会在上面的 CompletableFuture 通过另一个线程执行, 将上一个异常/ 自身的返回值传递给返回给调用方
    }
}
