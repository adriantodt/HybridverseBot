package com.theorangehub.hbdvbot.data.entities;

import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.db.ManagedObject;
import lombok.Data;

import static com.rethinkdb.RethinkDB.r;

@Data
public class WikiArtigo implements ManagedObject {
    public static final String DB_TABLE = "wiki";

    private String id, pageId, page;

    @Override
    public void delete() {
        r.table(DB_TABLE).get(getId())
            .delete()
            .runNoReply(HbdvData.conn());
    }

    @Override
    public void save() {
        r.table(DB_TABLE).insert(this)
            .optArg("conflict", "replace")
            .runNoReply(HbdvData.conn());
    }
}
