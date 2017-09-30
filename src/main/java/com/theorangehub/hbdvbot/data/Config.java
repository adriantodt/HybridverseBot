package com.theorangehub.hbdvbot.data;

import lombok.Data;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;

@Data
public class Config {
    public String consoleChannel = "318004158135271425";
    public String dbDb = "mantaro";
    public String dbHost = "localhost";
    public String dbPass;
    public int dbPort = 28015;
    public String dbUser;
    public List<String> owners = new ArrayList<>();
    public String prefix = "!";
    public String token;

    public boolean isOwner(Member member) {
        return isOwner(member.getUser());
    }

    public boolean isOwner(User user) {
        return isOwner(user.getId());
    }

    public boolean isOwner(String id) {
        return owners.contains(id);
    }
}
