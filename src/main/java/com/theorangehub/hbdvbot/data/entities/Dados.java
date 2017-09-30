package com.theorangehub.hbdvbot.data.entities;

import com.theorangehub.hbdvbot.data.entities.helper.Const;
import com.theorangehub.hbdvbot.data.entities.helper.Dado;
import com.theorangehub.hbdvbot.data.db.ManagedObject;
import com.theorangehub.hbdvbot.data.HbdvData;
import gnu.trove.list.TIntList;
import gnu.trove.list.linked.TIntLinkedList;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.rethinkdb.RethinkDB.r;

@Data
@Accessors(chain = true)
public class Dados implements ManagedObject {
    public static final String DB_TABLE = "dados";

    private Map<String, Const> consts = new LinkedHashMap<>();
    private List<Dado> dados = new LinkedList<>();
    private String id;
    private Integer min, max;

    //region @region database interaction
    @Override
    public void delete() {
        r.table(DB_TABLE).get(getId()).delete().runNoReply(HbdvData.conn());
    }

    @Override
    public void save() {
        r.table(DB_TABLE).insert(this)
            .optArg("conflict", "replace")
            .runNoReply(HbdvData.conn());
    }
    //endregion

    public TIntList calculate(Ficha ficha) {
        for (Const c : consts.values()) {
            LinkedHashMap<String, Const> merge = new LinkedHashMap<>(consts);
            merge.putAll(c.getConsts());
            c.setConsts(merge);
        }

        TIntList results = new TIntLinkedList();

        for (Dado dado : dados) {
            LinkedHashMap<String, Const> merge = new LinkedHashMap<>(consts);
            merge.putAll(dado.getConsts());
            dado.setConsts(merge);

            if (dado.getMin() == null) dado.setMin(min);
            if (dado.getMax() == null) dado.setMax(max);

            results.add(dado.calculate(ficha));
        }

        return results;
    }
}
