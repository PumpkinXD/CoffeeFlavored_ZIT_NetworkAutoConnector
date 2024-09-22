package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConfigManager {
    private static Config config;

    public static ReadWriteLock getLock() {
        return lock;
    }

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        ConfigManager.config = config;
    }
}
