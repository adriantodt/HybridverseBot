package com.theorangehub.hbdvbot.modules.commands;

import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public enum CommandPermission {
    USUÁRIO {
        @Override
        public boolean test(Member member) {
            return true;
        }
    },
    ADMINISTRADOR {
        @Override
        public boolean test(Member member) {
            return member.isOwner() || member.hasPermission(Permission.ADMINISTRATOR) ||
                member.hasPermission(Permission.MANAGE_SERVER) || DONO.test(member);
        }
    },
    DONO {
        @Override
        public boolean test(Member member) {
            return HbdvData.config().get().isOwner(member);
        }
    };

    public abstract boolean test(Member member);

    @Override
    public String toString() {
        return HbdvUtils.capitalize(name().toLowerCase());
    }
}
