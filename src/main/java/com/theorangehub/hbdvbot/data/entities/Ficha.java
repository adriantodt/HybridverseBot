package com.theorangehub.hbdvbot.data.entities;

import com.theorangehub.hbdvbot.data.db.ManagedObject;
import com.theorangehub.hbdvbot.data.entities.helper.FichaConstantes.Flags;
import com.theorangehub.hbdvbot.data.HbdvData;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

import static com.theorangehub.hbdvbot.data.db.ManagedDatabase.LOG_WORKER;
import static com.theorangehub.hbdvbot.data.entities.helper.FichaConstantes.DEFAULT_FLAGS;
import static com.rethinkdb.RethinkDB.r;

@Data
public class Ficha implements ManagedObject {
    public static final String DB_TABLE = "fichas";

    private String afinidadeMágica;
    private double altura, peso;
    private String amuletoDeAfeto;
    private String avatar;
    private String benção;
    private String causaDaPrisão;
    private String cor;
    private String criador, nomeCriador;
    private String dataDeAniversário;
    private long cMagnuns, cFiriuns, cNidos, cBrigões, cMídios;
    private long bMagnuns, bFiriuns, bNidos, bBrigões, bMídios;
    private List<String> elementosDeAfinidade = new LinkedList<>();
    private String famíliaOuDescendência;
    private int flags = DEFAULT_FLAGS;
    private int força, resistência, magia, máximoDeMana, destreza, artesMarciais, carisma, inteligência, sorte, pontosDeVida;
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

    public String displayMoradias() {
        if (moradias.isEmpty()) return "Nenhuma";
        return "- " + String.join("\n- ", moradias);
    }

    public String displayRaça() {
        if (raçaSecondária != null) return raçaPrimária + "/" + raçaSecondária;
        return raçaPrimária;
    }

    public String displayToString() {
        return nome + " (" + idade + " anos; por " + nomeCriador + ")";
    }

    public boolean flagAdvWorld() {
        return Flags.ADVWORLD.is(flags);
    }

    public boolean flagManaBased() {
        return Flags.MANA_BASED.is(flags);
    }

    public boolean flagMorgothParty() {
        return Flags.MORGOTH.is(flags);
    }

    public boolean flagPraga() {
        return Flags.PRAGA.is(flags);
    }
}
