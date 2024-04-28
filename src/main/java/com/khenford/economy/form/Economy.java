package com.khenford.economy.form;

import cn.nukkit.Player;
import com.formconstructor.form.SimpleForm;
import com.formconstructor.form.element.simple.Button;
import com.formconstructor.form.element.simple.ImageType;
import com.khenford.economy.Main;
import com.khenford.economy.form.buttons.TopPlayers;

public class Economy {
    public Economy(Player player) {
        SimpleForm form = new SimpleForm("Economy");
        form.addContent("You balance: "+ Main.getDatabase().getMoney(player.getName()));
        form.addButton(new Button("Top players")
                .setImage(ImageType.PATH, "textures/items/diamond")
                .onClick((pl, b) -> {
                    new TopPlayers(pl);
                }));
        form.send(player);
    }
}
