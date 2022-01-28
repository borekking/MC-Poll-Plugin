package de.borekking.mcPolls.commands;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.inventory.inventories.VoteInventory;
import de.borekking.mcPolls.poll.types.ActivePoll;
import de.borekking.mcPolls.poll.PollManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {

    private final PollManager pollManager;

    public VoteCommand(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only Players can vote
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can do this!");
            return false;
        }

        // Get Player
        Player player = (Player) sender;

        // Check to less args
        if (args.length < 1) {
            this.sendUsage(player);
            return false;
        }

        // Get Poll
        String pollName = args[0];
        ActivePoll poll = this.pollManager.getActivePoll(pollName);

        // Check Poll null
        if (poll == null) {
            Main.sendMessage(player, ChatColor.RED + "Poll \"" + pollName + "\" could not be found.");
            return false;
        }

        // Open Inventory
        new VoteInventory(player, poll, this.pollManager);
        return true;
    }

    private void sendUsage(Player p) {
        Main.sendMessage(p, "Please provide a Poll to vote in.", "Usage: /vote <poll-name>");
    }
}
