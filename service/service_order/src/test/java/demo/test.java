package demo;

import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author Yu1
 * @date 2022/10/11 - 17:09
 */
public class test {

    @Test
    public void ramdom(){

        System.out.println("顺位: " + (int)Math.ceil(Math.random() * 8));
        System.out.println("顺位: " + (int)Math.ceil(Math.random() * 8));
        System.out.println();
        System.out.println(LocalDateTime.now());
    }
}
