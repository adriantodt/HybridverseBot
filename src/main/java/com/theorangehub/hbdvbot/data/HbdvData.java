package com.theorangehub.hbdvbot.data;

import com.theorangehub.hbdvbot.data.db.ManagedDatabase;
import com.theorangehub.hbdvbot.utils.data.GsonDataManager;
import com.rethinkdb.net.Connection;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.rethinkdb.RethinkDB.r;

public class HbdvData {
    private static GsonDataManager<Config> config;
    private static Connection conn;
    private static ManagedDatabase db;
    private static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    public static GsonDataManager<Config> config() {
        if (config == null) config = new GsonDataManager<>(Config.class, "config.json", Config::new);
        return config;
    }

    public static Connection conn() {
        Config c = config().get();
        if (conn == null) {
            conn = r.connection().hostname(c.dbHost).port(c.dbPort).db(c.dbDb).user(c.dbUser, c.dbPass).connect();
        }
        return conn;
    }

    public static ManagedDatabase db() {
        if (db == null) db = new ManagedDatabase(conn());
        return db;
    }

    public static ScheduledExecutorService getExecutor() {
        return exec;
    }

    public static void queue(Callable<?> action) {
        getExecutor().submit(action);
    }

    public static void queue(Runnable runnable) {
        getExecutor().submit(runnable);
    }
}
