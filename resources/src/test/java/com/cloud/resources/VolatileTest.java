package com.cloud.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VolatileTest {

    volatile int num = 0;
    @Test
    public void intVolatileTest() {
        new Thread(()->{
            System.out.println("start");
            while (num == 0);
            System.out.println("end");
        }).start();

        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("num=1");
            num = 1;
        }).start();
    }

}
