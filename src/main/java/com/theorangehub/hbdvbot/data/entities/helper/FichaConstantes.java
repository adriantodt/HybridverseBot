package com.theorangehub.hbdvbot.data.entities.helper;

public class FichaConstantes {

    public enum Flags {
        /**
         * Indica que o Personagem é Humano do <b>Todt Adventure World</b>, portanto deve ser caracterizado por uma Etnia especifica no lugar de raça primária.
         */
        ADVWORLD,
        /**
         * Indica que o Personagem é foi anteriormente preso e participou da <b>Morgoth Party</b>, portanto habilitando a parte de Morgoth da ficha.
         */
        MORGOTH,
        /**
         * Indica que o Personagem sofreu a <b>Hybridverse Plague</b>, portanto habilitando a parte de Hybridverse da ficha.
         */
        PRAGA,
        /**
         * Indica que o Personagem é <b>Mana-based</b>, como a Nightny, e portanto tem Força e Resistência desconhecidas.
         */
        MANA_BASED;

        public final int BIT;

        Flags() {
            BIT = 1 << ordinal();
        }

        public boolean is(int flags) {
            return (BIT & flags) != 0;
        }
    }

    public static int DEFAULT_FLAGS = 5; //0b101; ADVWORLD | PRAGA

    public static int flags(Flags... flags) {
        int b = 0;
        for (Flags f : flags) b |= f.BIT;
        return b;
    }
}
