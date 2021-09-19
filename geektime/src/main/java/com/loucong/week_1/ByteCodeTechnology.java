package com.loucong.week_1;

/**
 * @author liqin 2021/9/17 22:12
 */
public class ByteCodeTechnology {

    public static void main(String[] args) {

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                continue;
            }
            sum += i;
        }
        System.out.println(sum);
    }

    public void test() {
        int a = 1;
        int b = 2;
        int c = (a + b) * 5;
    }
}
