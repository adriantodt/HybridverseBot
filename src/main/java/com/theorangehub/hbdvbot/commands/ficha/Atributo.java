package com.theorangehub.hbdvbot.commands.ficha;

import com.theorangehub.hbdvbot.data.entities.Ficha;

public enum Atributo {
    //region Atributos: Força/Magia/Destreza/Carisma/Sorte
    força {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setForça(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getForça();
        }
    },
    magia {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setMagia(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getMagia();
        }
    },
    destreza {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setDestreza(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getDestreza();
        }
    },
    carisma {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarisma(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getCarisma();
        }
    },
    sorte {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setSorte(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getSorte();
        }
    },
    //endregion
    //region Atributos: Resistência/Máximo de Mana/Técnicas/Inteligência/Pontos de Vida
    resistência {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setResistência(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getResistência();
        }
    },
    máximoDeMana {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setMáximoDeMana(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getMáximoDeMana();
        }
    },
    técnicas {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setTécnicas(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getTécnicas();
        }
    },
    inteligência {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setInteligência(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getInteligência();
        }
    },
    pontosDeVida {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setPontosDeVida(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getPontosDeVida();
        }
    },
    //endregion
    //region Atributos: Idade/Altura/Peso
    idade {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setIdade(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getIdade();
        }
    },
    altura {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setAltura(v.doubleValue());
        }

        @Override
        public Double get(Ficha ficha) {
            return ficha.getAltura();
        }
    },
    peso {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setPeso(v.doubleValue());
        }

        @Override
        public Double get(Ficha ficha) {
            return ficha.getPeso();
        }
    },
    //endregion
    //region Levels: Alquimia/Encantamentos/Energia/Fluxos/Invocação
    lvlAlquimia {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlAlquimia(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlAlquimia();
        }
    },
    lvlEncantamentos {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlEncantamentos(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlEncantamentos();
        }
    },
    lvlEnergia {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlEnergia(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlEnergia();
        }
    },
    lvlFluxos {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlFluxos(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlFluxos();
        }
    },
    lvlInvocação {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlInvocação(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlInvocação();
        }
    },
    //endregion
    //region Levels: Lógica/Mana/Matéria/Nulos/Vida
    lvlLógica {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlLógica(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlLógica();
        }
    },
    lvlMana {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlMana(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlMana();
        }
    },
    lvlMatéria {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlMatéria(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlMatéria();
        }
    },
    lvlNatureza {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlNatureza(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlNatureza();
        }
    },
    lvlNulos {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlNulos(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlNulos();
        }
    },
    lvlVida {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setLvlVida(v.intValue());
        }

        @Override
        public Integer get(Ficha ficha) {
            return ficha.getLvlVida();
        }
    },
    //endregion
    //region Carteira: Magnuns/Firiuns/Nidos/Brigões/Mídios
    carteiraMagnuns {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraMagnuns(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getCarteiraMagnuns();
        }
    },
    carteiraFiriuns {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraFiriuns(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getCarteiraFiriuns();
        }
    },
    carteiraNidos {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraNidos(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getCarteiraNidos();
        }
    },
    carteiraBrigões {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraBrigões(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getCarteiraBrigões();
        }
    },
    carteiraMídios {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraMídios(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getCarteiraMídios();
        }
    },
    //endregion
    //region Banco: Magnuns/Firiuns/Nidos/Brigões/Mídios
    bancoMagnuns {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setBancoMagnuns(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getBancoMagnuns();
        }
    },
    bancoFiriuns {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraFiriuns(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getBancoFiriuns();
        }
    },
    bancoNidos {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraNidos(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getBancoNidos();
        }
    },
    bancoBrigões {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraBrigões(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getBancoBrigões();
        }
    },
    bancoMídios {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraMídios(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getBancoMídios();
        }
    },
    //endregion
    //region Compat: Dinheiro
    dinheiro {
        @Override
        public void set(Ficha ficha, Number v) {
            ficha.setCarteiraMagnuns(v.longValue());
        }

        @Override
        public Long get(Ficha ficha) {
            return ficha.getCarteiraMagnuns();
        }
    };
    //endregion

    public abstract Number get(Ficha ficha);

    public abstract void set(Ficha ficha, Number v);
}
