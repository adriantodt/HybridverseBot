package com.theorangehub.hbdvbot.modules.commands;

import com.theorangehub.hbdvbot.data.HbdvData;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public enum CommandPermission {
    USER() {
        @Override
        public boolean test(Member member) {
            return true;
        }
    },
    ADMIN() {
        @Override
        public boolean test(Member member) {
            return member.isOwner() || member.hasPermission(Permission.ADMINISTRATOR) ||
                member.hasPermission(Permission.MANAGE_SERVER) || OWNER.test(member);
        }
    },
    OWNER() {
        @Override
        public boolean test(Member member) {
            return HbdvData.config().get().isOwner(member);
        }
    };

    public abstract boolean test(Member member);

    @Override
    public String toString() {
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
