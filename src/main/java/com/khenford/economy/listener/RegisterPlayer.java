package com.khenford.economy.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

import com.khenford.economy.Main;

public class RegisterPlayer implements Listener {

    @EventHandler
    public void register(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!Main.getDatabase().isPlayer(player.getName())) {
            Main.getDatabase().registerPlayer(player.getName(), 0);
            System.out.println("Registered player " + player.getName());
        }
    }
}
