package com.khenford.economy;

import cn.nukkit.plugin.PluginBase;
import com.khenford.economy.command.EconomyCommand;
import com.khenford.economy.database.Database;
import com.khenford.economy.listener.RegisterPlayer;

public class Main extends PluginBase {

    private static Main instance;
    private static Database database;

    @Override
    public void onEnable() {
        Main.instance = this;
        database = new Database();
        getServer().getCommandMap().register("economy", new EconomyCommand());
        getServer().getPluginManager().registerEvents(new RegisterPlayer(), this);
    }

    public static Database getDatabase() {
        return Main.database;
    }

    public static Main getInstance() {
        return Main.instance;
    }
}