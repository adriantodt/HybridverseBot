package com.theorangehub.hbdvbot.data.entities;

import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.db.ManagedObject;
import com.theorangehub.hbdvbot.data.entities.helper.FichaConstantes.Flags;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;
import static com.theorangehub.hbdvbot.data.db.ManagedDatabase.LOG_WORKER;
import static com.theorangehub.hbdvbot.data.entities.helper.FichaConstantes.DEFAULT_FLAGS;

@Data
public class Ficha implements ManagedObject {
    public static final String DB_TABLE = "fichas";

    private String afinidadeMágica;
    private double altura, peso;
    private String amuletoDeAfeto;
    private String avatar;
    private long bancoMagnuns, bancoFiriuns, bancoNidos, bancoBrigões, bancoMídios;
    private String benção;
    private long carteiraMagnuns, carteiraFiriuns, carteiraNidos, carteiraBrigões, carteiraMídios;
    private String causaDaPrisão;
    private String cor;
    private String criador, nomeCriador;
    private String dataDeAniversário;
    private List<String> elementosDeAfinidade = new LinkedList<>();
    private String famíliaOuDescendência;
    private int flags = DEFAULT_FLAGS;
    private int força, resistência, magia, máximoDeMana, destreza, técnicas, carisma, inteligência, sorte, pontosDeVida;
    private String história;
    private String id;
    private int idade;
    private List<String> inventário = new LinkedList<>();
    private int lvlAlquimia, lvlEncantamentos, lvlEnergia, lvlFluxos, lvlInvocação, lvlLógica, lvlMana, lvlMatéria, lvlNatureza, lvlNulos, lvlVida;
    private int lvlTrazitano, lvlIlisiano, lvlHibiano, lvlMidianês, lvlBriguiano, lvlPorfiriano, lvlKaotine;
    private List<String> moradias = new LinkedList<>();
    private String nome;
    private String pet;
    private boolean petÉMontaria;
    private String profissão;
    private String raçaPrimária;
    private String raçaSecondária;
    private String sexoGêneroOrientação;
    private String universoDeOrigem;

    //region @region database interaction
    @Override
    public void delete() {
        r.table(DB_TABLE).get(getId()).delete().runNoReply(HbdvData.conn());

        r.table(DB_TABLE + "_log").insert(
            r.hashMap("id", LOG_WORKER.generate())
                .with("fichaId", this.getId())
                .with("ficha", null)
        ).runNoReply(HbdvData.conn());
    }

    @Override
    public void save() {
        r.table(DB_TABLE).insert(this)
            .optArg("conflict", "replace")
            .runNoReply(HbdvData.conn());

        r.table(DB_TABLE + "_log").insert(
            r.hashMap("id", LOG_WORKER.generate())
                .with("fichaId", this.getId())
                .with("ficha", this)
        ).runNoReply(HbdvData.conn());
    }
    //endregion

    public String displayInventário() {
        if (inventário.isEmpty()) return "*Não há nada aqui, só poeira*";
        return HbdvUtils.indexedToString(inventário);
    }

    public String displayMoradias() {
        if (moradias.isEmpty()) return "*A rua é sua casa, não?*";
        return HbdvUtils.indexedToString(moradias);
    }

    public String displayRaça() {
        if (raçaSecondária != null) return raçaPrimária + "/" + raçaSecondária;
        return raçaPrimária;
    }

    public String displaySeleção() {
        return displayRaça() + "; " +
            idade + " anos; " +
            "por " + nomeCriador;
    }

    public boolean flagAdvWorld() {
        return Flags.ADVWORLD.is(flags);
    }

    public boolean flagMaldição() {
        return Flags.MALDICAO.is(flags);
    }

    public boolean flagManaBased() {
        return Flags.MANA_BASED.is(flags);
    }

    public boolean flagMorgothParty() {
        return Flags.MORGOTH.is(flags);
    }
}
