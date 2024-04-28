package com.khenford.economy.form.buttons;

import cn.nukkit.Player;
import com.formconstructor.form.SimpleForm;
import com.khenford.economy.Main;

public class TopPlayers {
    public TopPlayers(Player player) {
        SimpleForm form = new SimpleForm("Top players");
        form.addContent("Top balance money player\n"+ Main.getDatabase().topMoney(10));
        form.send(player);
    }
}
