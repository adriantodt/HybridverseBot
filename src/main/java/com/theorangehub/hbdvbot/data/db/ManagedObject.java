package com.theorangehub.hbdvbot.data.db;

import com.theorangehub.hbdvbot.data.HbdvData;

public interface ManagedObject {
    void delete();

    void save();

    default void deleteAsync() {
        HbdvData.queue(this::delete);
    }

    default void saveAsync() {
        HbdvData.queue(this::save);
    }
}
