package com.mytlx.arcane.snippets.test;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-24 15:41
 */
public class FinallyTest {
    public static void main(String[] args) {
        System.out.println(test());
        System.out.println(test2());
    }

    private static int test() {
        int a = 10;
        try {
            System.out.println(a / 0);
            a = 20;
        } catch (Exception e) {
            a = 30;
            return a;
        } finally {
            a = 40;
        }
        return a;
    }

    private static int test2() {
        int a = 10;
        try {
            a = 20;
            return a;
        } catch (Exception e) {
            a = 30;
            return a;
        } finally {
            a = 40;
        }
    }

    public void test3() throws Exception {

    }

    static class Annoyance extends Exception { }

    static class Sneeze extends Annoyance { }

    static class Human {
        public static void main(String[] args) {
            try {
                try {
                    throw new Sneeze();
                } catch (Annoyance e) {
                    System.out.println("catch annoy");
                    throw e;
                }
            } catch (Sneeze s) {
                System.out.println("catch sneeze");
                return;
            } finally {
                System.out.println("hello world");
            }
        }
    }
}
