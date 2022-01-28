package de.borekking.mcPolls.commands;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.commands.pollCommands.PollCommand;

import de.borekking.mcPolls.commands.pollCommands.commands.CreatePollCommand;
import de.borekking.mcPolls.commands.pollCommands.commands.PollResultsCommand;
import de.borekking.mcPolls.poll.PollManager;
import de.borekking.mcPolls.util.JavaUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class PollCommandManager implements CommandExecutor {

    public PollCommandManager(PollManager pollManager) {
        new CreatePollCommand();
        new PollResultsCommand(pollManager);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
            this.sendUsage(sender);
            return false;
        }

        PollCommand command = PollCommand.getCommandByName(args[0]);
        if (command == null) {
            Main.sendMessage(sender, ChatColor.GRAY + "Could not find command " + Main.COLOR_CODE + args[0] + ChatColor.GRAY + ".");
            return false;
        }

        // Check if command has permission and if sender does not have it.
        if (!this.senderHasPermission(sender, command)) {
            Main.sendMessage(sender, ChatColor.RED + "You don't have the required permission to use this command!");
            return false;
        }

        command.use(sender, JavaUtils.removeFront(args, 1));
        return true;
    }

    private void sendUsage(CommandSender sender) {
        // List of Command the Sender can use.
        List<PollCommand> availableCommands = PollCommand.getCommands().stream().filter(command -> this.senderHasPermission(sender, command)).collect(Collectors.toList());

        Main.sendMessage(sender, ChatColor.GRAY + "----------");

        if (availableCommands.isEmpty()) Main.sendMessage(sender, ChatColor.GRAY + "Nothing here :/");
        else availableCommands.forEach(command -> Main.sendMessage(sender, Main.COLOR_CODE + command.getIdentifier() + ChatColor.GRAY + " (" + command.getUsage() + ")"));

        Main.sendMessage(sender, ChatColor.GRAY + "----------");
    }

    private boolean senderHasPermission(CommandSender sender, PollCommand command) {
        return sender.isOp() || !command.hasPermission() || sender.hasPermission(command.getPermission());
    }
}
