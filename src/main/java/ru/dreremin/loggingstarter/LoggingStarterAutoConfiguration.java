package ru.dreremin.loggingstarter;

public class LoggingStarterAutoConfiguration {

    public static void println(String s) {
        System.out.format("Печать из библиотеки logging-starter: %s%n", s);
    }

}
