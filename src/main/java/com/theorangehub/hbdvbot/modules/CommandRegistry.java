package com.theorangehub.hbdvbot.modules;

import com.theorangehub.hbdvbot.modules.commands.base.Command;

import java.util.Map;

public interface CommandRegistry {
    Map<String, Command> commands();

    void register(String s, Command c);
}
