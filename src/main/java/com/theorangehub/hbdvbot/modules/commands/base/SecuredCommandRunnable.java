package com.theorangehub.hbdvbot.modules.commands.base;

import com.theorangehub.hbdvbot.modules.commands.CommandPermission;

public interface SecuredCommandRunnable extends ICommandRunnable {
    CommandPermission permission();
}
