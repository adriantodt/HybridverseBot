package com.theorangehub.hbdvbot.data.db;

import br.com.brjdevs.java.snowflakes.Snowflakes;
import br.com.brjdevs.java.snowflakes.entities.Config;
import br.com.brjdevs.java.snowflakes.entities.Worker;
import com.theorangehub.hbdvbot.data.entities.Dados;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.data.entities.WikiArtigo;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

import java.util.List;

import static com.theorangehub.hbdvbot.utils.HbdvUtils.escapeRegex;
import static com.rethinkdb.RethinkDB.r;

public class ManagedDatabase {
    public static final Config HBDV_FACTORY = Snowflakes.config(1495900000L, 2L, 2L, 12L);
    public static final Worker ID_WORKER = HBDV_FACTORY.worker(0, 0), LOG_WORKER = HBDV_FACTORY.worker(0, 2);

    private final Connection conn;

    public ManagedDatabase(Connection conn) {
        this.conn = conn;
    }

    public Dados getDadosPorId(String id) {
        return r.table(Dados.DB_TABLE).get(id).run(conn, Dados.class);
    }

    public Ficha getFichaLog(String id) {
        return r.table(Ficha.DB_TABLE + "_log")
            .get(id).getField("ficha")
            .run(conn, Ficha.class);
    }

    public Ficha getFichaPorId(String id) {
        return r.table(Ficha.DB_TABLE).get(id).run(conn, Ficha.class);
    }

    public List<Ficha> getFichasPorNome(String nome) {
        String pattern = escapeRegex(nome.toLowerCase());
        Cursor<Ficha> c = r.table(Ficha.DB_TABLE)
            .filter(ficha -> ficha.g("nome").downcase().match(pattern))
            .run(conn, Ficha.class);
        return c.toList();
    }

    public WikiArtigo getWikiById(String id) {
        return r.table(WikiArtigo.DB_TABLE).get(id).run(conn, Ficha.class);
    }

    public List<WikiArtigo> searchWiki(String term) {
        String pattern = escapeRegex(term.toLowerCase());

        Cursor<WikiArtigo> c = r.table(WikiArtigo.DB_TABLE)
            .filter(article -> article.g("embed").downcase().match(pattern))
            .run(conn, WikiArtigo.class);

        return c.toList();
    }

}
