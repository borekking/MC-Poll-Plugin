package de.borekking.mcPolls.commands.pollCommands.commands;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.commands.pollCommands.PollCommand;
import de.borekking.mcPolls.inventory.inventories.CreatePollInventory;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreatePollCommand extends PollCommand {

    public CreatePollCommand() {
        super("create", "Create a new Poll.", "/poll create", "poll.create");
    }

    @Override
    public void use(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Main.sendMessage(sender, ChatColor.RED + "You can only do this as a player.");
            return;
        }

        new CreatePollInventory((Player) sender);
    }
}
