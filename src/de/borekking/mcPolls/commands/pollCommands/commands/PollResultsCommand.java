package de.borekking.mcPolls.commands.pollCommands.commands;


import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.commands.pollCommands.PollCommand;
import de.borekking.mcPolls.inventory.inventories.PollResultsInventory;
import de.borekking.mcPolls.poll.PollManager;
import de.borekking.mcPolls.poll.types.EndedPoll;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PollResultsCommand extends PollCommand {

    private final PollManager pollManager;

    public PollResultsCommand(PollManager pollManager) {
        super("result", "View a Poll's Results.", "/poll result <poll-name>", "poll.result");

        this.pollManager = pollManager;
    }

    @Override
    public void use(CommandSender sender, String[] args) {
        // Only Players can view results
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can do this!");
            return;
        }

        // Get Player
        Player player = (Player) sender;

        // Check to less args
        if (args.length < 1) {
            this.sendUsage(player);
            return;
        }

        // Get Poll
        String pollName = args[0];
        EndedPoll poll = this.pollManager.getEndedPoll(pollName);

        // Check Poll null
        if (poll == null) {
            Main.sendMessage(player, ChatColor.RED + "Poll \"" + pollName + "\" could not be found.");
            return;
        }

        // Open Inventory
        new PollResultsInventory(player, poll);
    }

    private void sendUsage(Player p) {
        Main.sendMessage(p, "Please provide a Poll.", "Usage: " + this.getUsage());
    }
}
