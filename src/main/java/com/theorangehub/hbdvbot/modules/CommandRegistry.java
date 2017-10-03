package com.theorangehub.hbdvbot.modules;

import com.theorangehub.hbdvbot.modules.commands.base.ICommand;

import java.util.Map;

public interface CommandRegistry {
    Map<String, ICommand> commands();

    void register(String s, ICommand c);
}
