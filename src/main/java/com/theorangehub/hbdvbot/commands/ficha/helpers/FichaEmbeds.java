package com.theorangehub.hbdvbot.commands.ficha.helpers;

import com.theorangehub.hbdvbot.HbdvBot;
import com.theorangehub.hbdvbot.HbdvCommons;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.data.entities.helper.Rank;
import com.theorangehub.hbdvbot.data.entities.helper.RankCategory;
import com.theorangehub.hbdvbot.data.entities.helper.Ranky;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.Color;
import java.util.Optional;

@RequiredArgsConstructor
public class FichaEmbeds {

    private final Ficha ficha;

    public MessageEmbed atributosEmbed() {
        return base().addField("Atributos:",
            "**Força**: " + (ficha.flagManaBased() ? "??" : ficha.getForça()) + "/10\n" +
                "**Magia**: " + ficha.getMagia() + "/10\n" +
                "**Destreza**: " + ficha.getDestreza() + "/10\n" +
                "**Carisma**: " + ficha.getCarisma() + "/10\n" +
                "**Sorte**: " + ficha.getSorte() + "/10\n" +
                "**Resistência**: " + (ficha.flagManaBased() ? "??" : ficha.getResistência()) + "/10\n" +
                "**Pontos de Mana**: " + ficha.getMáximoDeMana() + "/500\n" +
                "**Técnicas**: " + ficha.getTécnicas() + "/10\n" +
                "**Inteligência**: " + ficha.getInteligência() + "/10\n" +
                "**Pontos de Vida**: " + ficha.getPontosDeVida() + "/50"
            , false
        ).addField("Elementos de Afinidade:",
            "- " + String.join("\n- ", ficha.getElementosDeAfinidade())
            , false
        ).addField(
            "Opções:",
            "\u2b05 - Voltar",
            false
        ).build();
    }

    public MessageEmbed avatarEmbed() {
        EmbedBuilder embed = base().setThumbnail(null).setImage(ficha.getAvatar());

        if (ficha.getAvatar() == null) embed.setDescription("*Não há nada aqui, só poeira.*");

        return embed.addField(
            "Opções:",
            "\u2b05 - Voltar",
            false
        ).build();
    }

    private EmbedBuilder base() {
        return new EmbedBuilder()
            .setAuthor(
                "Ficha: " + ficha.getNome(),
                null,
                HbdvBot.getInstance().getSelfUser().getEffectiveAvatarUrl()
            )
            .setThumbnail(ficha.getAvatar())
            .setColor(ficha.getCor() != null ? Color.decode(ficha.getCor()) : HbdvCommons.HBDV_COLOR)
            .setFooter(
                "ID: " + ficha.getId() + " / Criado por: " + ficha.getNomeCriador(),
                Optional.ofNullable(HbdvBot.getInstance().getUserById(ficha.getCriador()))
                    .map(User::getEffectiveAvatarUrl)
                    .orElse(null)
            );
    }

    public MessageEmbed carteiraEmbed() {
        return base().addField("Na Carteira",
            ficha.getCarteiraMagnuns() + " **Magnuns**\n" +
                ficha.getCarteiraFiriuns() + " **Firiuns**\n" +
                ficha.getCarteiraNidos() + " **Nidos**\n" +
                ficha.getCarteiraBrigões() + " **Brigões**\n" +
                ficha.getCarteiraMídios() + " **Mídios**"
            , false
        ).addField("No Banco",
            ficha.getBancoMagnuns() + " **Magnuns**\n" +
                ficha.getBancoFiriuns() + " **Firiuns**\n" +
                ficha.getBancoNidos() + " **Nidos**\n" +
                ficha.getBancoBrigões() + " **Brigões**\n" +
                ficha.getBancoMídios() + " **Mídios**"
            , false
        ).addField(
            "Opções:",
            "\u2b05 - Voltar",
            false
        ).build();
    }

    public MessageEmbed historiaEmbed() {
        return base()
            .setAuthor(
                ficha.getNome() + ": História",
                null,
                HbdvBot.getInstance().getSelfUser().getEffectiveAvatarUrl()
            )
            .setDescription(ficha.getHistória())
            .addField(
                "Opções:",
                "\u2b05 - Voltar",
                false
            ).build();
    }

    public MessageEmbed initialEmbed() {
        return base().addField("Geral",
            "**Nome**: " + ficha.getNome() + "\n" +
                "**Idade**: " + ficha.getIdade() + " anos\n" +
                "**" + (ficha.flagAdvWorld() ? "Etnia/Raça Animal" : "Raça") + "**: " + ficha.displayRaça() + "\n" +
                "**Sexo/Gênero/Orientação**: " + ficha.getSexoGêneroOrientação() + "\n" +
                "**Afinidade Mágica**: " + ficha.getAfinidadeMágica() + "\n" +
                "**Profissão**: " + ficha.getProfissão() + "\n" +
                "**Altura/Peso**: " + String.format("%.2fm/%.1fkg", ficha.getAltura(), ficha.getPeso())
            , false
        ).addField(
            "Opções:",
            "\uD83D\uDDF3 - Atributos\n" +
                "\uD83C\uDFC5 - Ranks\n" +
                "\uD83D\uDCB0 - Carteira\n" +
                "\uD83D\uDCD5 - História\n" +
                "<:hbdvround:328180364608667650> - Morgoth/Hybridverse\n" +
                "\uD83C\uDCCF - Miscelânea\n" +
                "\uD83D\uDCF7 - Avatar\n",
            false
        ).build();
    }

    public MessageEmbed miscEmbed() {
        EmbedBuilder embed = base()
            .addField("Inventário:", ficha.displayInventário(), false)
            .addField("Moradias:", ficha.displayMoradias(), false);

        if (ficha.getDataDeAniversário() != null && !ficha.getDataDeAniversário().isEmpty()) {
            embed.addField("Data de Aniversário:", ficha.getDataDeAniversário(), false);
        }

        if (ficha.getBenção() != null && !ficha.getBenção().isEmpty()) {
            embed.addField("Benção:", ficha.getBenção(), false);
        }

        if (ficha.getFamíliaOuDescendência() != null && !ficha.getFamíliaOuDescendência().isEmpty()) {
            embed.addField("Família/Descendência:", ficha.getFamíliaOuDescendência(), false);
        }

        if (ficha.getPet() != null && !ficha.getPet().isEmpty()) {
            embed.addField("Pet" + (ficha.isPetÉMontaria() ? " (Montável):" : ":"), ficha.getPet(), false);
        }

        return embed.addField(
            "Opções:",
            "\u2b05 - Voltar",
            false
        ).build();
    }

    public MessageEmbed partiesEmbed() {
        EmbedBuilder embed = base();
        if (ficha.flagMorgothParty()) {
            embed.addField("Ficha de Morgoth:",
                "**Causa da Prisão**: " + ficha.getCausaDaPrisão() + "\n" +
                    "**Universo de Origem**: " + ficha.getUniversoDeOrigem()
                , false
            );
        }

        if (ficha.flagMaldição()) {
            embed.addField("Hybridverse:",
                "**Amuleto de Afeto**: " + ficha.getAmuletoDeAfeto()
                , false
            );
        }

        if (embed.getFields().isEmpty() && embed.getDescriptionBuilder().length() == 0) {
            embed.setDescription("*Não há nada aqui, só poeira.*");
        }

        return embed.addField(
            "Opções",
            "\u2b05 - Voltar",
            false
        ).build();
    }

    public MessageEmbed ranksEmbed() {
        return base().addField("Ranks:",
            "**Alquimia**: " + Ranky.display(ficha.getLvlAlquimia()) + "\n" +
                "**Encantamentos**: " + Ranky.display(ficha.getLvlEncantamentos()) + "\n" +
                "**Energia**: " + Rank.display(ficha.getLvlEnergia()) + "\n" +
                "**Fluxos**: " + Rank.display(ficha.getLvlFluxos()) + "\n" +
                "**Invocação**: " + Ranky.display(ficha.getLvlInvocação()) + "\n" +
                "**Lógica**: " + Rank.display(ficha.getLvlLógica()) + "\n" +
                "**Mana**: " + Rank.display(ficha.getLvlMana()) + "\n" +
                "**Matéria**: " + Rank.display(ficha.getLvlMatéria()) + "\n" +
                "**Natureza**: " + Rank.display(ficha.getLvlNatureza()) + "\n" +
                "**Nulos**: " + Rank.display(ficha.getLvlNulos()) + "\n" +
                "**Vida**: " + Rank.display(ficha.getLvlVida())
            , false
        ).addField("Línguas:",
            "**Trazitano**: " + RankCategory.display(ficha.getLvlTrazitano()) + "\n" +
                "**Ilisiano**: " + RankCategory.display(ficha.getLvlIlisiano()) + "\n" +
                "**Hibiano**: " + RankCategory.display(ficha.getLvlHibiano()) + "\n" +
                "**Midianês**: " + RankCategory.display(ficha.getLvlMidianês()) + "\n" +
                "**Briguiano**: " + RankCategory.display(ficha.getLvlBriguiano()) + "\n" +
                "**Porfiriano**: " + RankCategory.display(ficha.getLvlPorfiriano()) + "\n\n" +
                "**Kaotine**: " + RankCategory.display(ficha.getLvlKaotine())
            , false
        ).addField(
            "Opções:",
            "\u2b05 - Voltar",
            false
        ).build();
    }
}
