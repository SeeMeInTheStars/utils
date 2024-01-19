package com.example.test.service;

import ch.qos.logback.core.util.ContextUtil;
import com.example.test.request.TestPostRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TestService {

    ReentrantLock lock = new ReentrantLock();
    Integer condition = 1;
    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();
    Condition condition3 = lock.newCondition();


    private ThreadLocal<TestPostRequest> threadLocal;

    public void testThreadLocal() throws InterruptedException {


        callableTask callableTask = new callableTask();
        FutureTask futureTask = new FutureTask<>(callableTask);
        new Thread(futureTask).start();

        callableTask3 callableTask3 = new callableTask3();
        FutureTask futureTask3 = new FutureTask<>(callableTask3);
        new Thread(futureTask3).start();


        callableTask2 callableTask2 = new callableTask2();
        FutureTask futureTask2 = new FutureTask<>(callableTask2);
        new Thread(futureTask2).start();




        Thread.sleep(10000);

    }


    class callableTask implements Callable{

        @Override
        public Object call() throws Exception {
            while(true){
                lock.lock();
                try {
                    condition1.await(1, TimeUnit.SECONDS);

                    if(!condition.equals(1)){
                        condition2.signalAll();
                        continue;
                    }
                    System.out.println(1);
                    condition = 2;
                    condition2.signalAll();
                    Thread.sleep(1000);
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    class callableTask2 implements Callable{

        @Override
        public Object call() throws Exception {
            while(true){
                lock.lock();
                try {
                    condition2.await(1, TimeUnit.SECONDS);
                    if(!condition.equals(2)){
                        condition3.signalAll();
                        continue;
                    }
                    System.out.println(2);
                    condition = 3;
                    condition3.signalAll();
                    Thread.sleep(1000);
                }finally {
                    lock.unlock();
                }
            }
        }
    }
    class callableTask3 implements Callable{

        @Override
        public Object call() throws Exception {
            while(true){
                lock.lock();
                try {
                    condition3.await(1, TimeUnit.SECONDS);
                    if(!condition.equals(3)){
                        condition1.signalAll();
                        continue;
                    }
                    System.out.println(3);
                    condition = 1;
                    condition1.signalAll();
                    Thread.sleep(1000);
                }finally {
                    lock.unlock();
                }

            }
        }
    }



}
