package com.theorangehub.hbdvbot.commands.datas;

import com.theorangehub.hbdvbot.commands.datas.calendars.AfterPlagueCalendar;
import com.theorangehub.hbdvbot.commands.datas.calendars.ApexCalendar;
import com.theorangehub.hbdvbot.commands.datas.calendars.RealLifeEquivalentCalendar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HbdvCalendars {
    public static final AfterPlagueCalendar AFTER_PLAGUE = new AfterPlagueCalendar();
    public static final ApexCalendar APEX = new ApexCalendar();
    public static final RealLifeEquivalentCalendar REAL_LIFE = new RealLifeEquivalentCalendar();

    public static final Map<String, HbdvCalendar> REGISTRY = new ConcurrentHashMap<String, HbdvCalendar>() {{
        put("afterplague", AFTER_PLAGUE);
        put("after_plague", AFTER_PLAGUE);
        put("ap", AFTER_PLAGUE);

        put("apex", APEX);
        put("apx", APEX);

        put("reallife", REAL_LIFE);
        put("real_life", REAL_LIFE);
        put("rl", REAL_LIFE);
    }};
}
