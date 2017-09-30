package com.theorangehub.hbdvbot.modules.commands.base;

import com.theorangehub.hbdvbot.modules.commands.CommandPermission;

public abstract class AbstractCommand implements AssistedCommand {
    private final CommandPermission permission;

    public AbstractCommand() {
        this(CommandPermission.USER);
    }

    public AbstractCommand(CommandPermission permission) {
        this.permission = permission;
    }

    @Override
    public CommandPermission permission() {
        return permission;
    }
}
