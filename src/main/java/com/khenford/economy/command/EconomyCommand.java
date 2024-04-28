package com.khenford.economy.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import com.khenford.economy.Main;
import com.khenford.economy.database.Database;
import com.khenford.economy.form.Economy;

import java.util.List;

public class EconomyCommand extends Command {

    public EconomyCommand() {
        super("economy", "you balance money");
        setPermission("economy.command");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player) && args.length > 0) {
            Database db = Main.getDatabase();

            switch (args[0]) {
                case "money":
                    if (args.length < 2) {
                        sender.sendMessage("Usage: /economy money <playerName>");
                        return false;
                    }
                    Player player = Main.getInstance().getServer().getPlayer(args[1]);
                    if (!db.isPlayer(args[1])) {
                        sender.sendMessage("The player is not in the database");
                        return false;
                    }
                    int money = db.getMoney(args[1]);
                    sender.sendMessage("Money for " + args[1] + ": $" + money);
                    break;
                case "addmoney":
                    if (args.length < 3) {
                        sender.sendMessage("Usage: /economy addmoney <playerName> <amount>");
                        return false;
                    }
                    if (!db.isPlayer(args[1])) {
                        sender.sendMessage("The player is not in the database");
                        return false;
                    }
                    try {
                        int addAmount = Integer.parseInt(args[2]);
                        db.addMoney(args[1], addAmount);
                        sender.sendMessage("Added $" + addAmount + " to " + args[1] + "'s balance.");
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Invalid amount.");
                        return false;
                    }
                    break;
                case "setmoney":
                    if (args.length < 3) {
                        sender.sendMessage("Usage: /economy setmoney <playerName> <amount>");
                        return false;
                    }
                    if (!db.isPlayer(args[1])) {
                        sender.sendMessage("The player is not in the database");
                        return false;
                    }
                    try {
                        int setAmount = Integer.parseInt(args[2]);
                        db.setMoney(args[1], setAmount);
                        sender.sendMessage(args[1] + "'s balance set to $" + setAmount);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Invalid amount.");
                        return false;
                    }
                    break;
                case "removemoney":
                    if (args.length < 3) {
                        sender.sendMessage("Usage: /economy removemoney <playerName> <amount>");
                        return false;
                    }
                    if (!db.isPlayer(args[1])) {
                        sender.sendMessage("The player is not in the database");
                        return false;
                    }
                    try {
                        int removeAmount = Integer.parseInt(args[2]);
                        db.removeMoney(args[1], removeAmount);
                        sender.sendMessage("Removed $" + removeAmount + " from " + args[1] + "'s balance.");
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Invalid amount.");
                        return false;
                    }
                    break;
                case "topmoney":
                    if (args.length < 2) {
                        sender.sendMessage("Usage: /economy topmoney <topLimit>");
                        return true;
                    }
                    try {
                        int topLimit = Integer.parseInt(args[1]);
                        List<String> topPlayers = db.topMoney(topLimit);
                        if (topPlayers.isEmpty()) {
                            sender.sendMessage("No players found.");
                        } else {
                            sender.sendMessage("Top " + topLimit + " players:");
                            topPlayers.forEach(sender::sendMessage);
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Invalid number for top limit.");
                        return true;
                    }
                    break;

                default:
                    sender.sendMessage("Unknown command.");
                    return false;
            }
            return true;
        } else if (sender instanceof Player) {
            new Economy((Player) sender);
            return true;
        } else {
            sender.sendMessage("This command can only be executed by players or with specific arguments.");
            return false;
        }
    }
}
