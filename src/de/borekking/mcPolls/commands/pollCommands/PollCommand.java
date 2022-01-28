package de.borekking.mcPolls.commands.pollCommands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class PollCommand {

    /*
     * SIMPLE Command System (not for more complex stuff)
     *
     */

    // All Commands
    private final static List<PollCommand> commands;

    static {
        commands = new ArrayList<>();
    }

    private final String identifier, permission, description, usage;

    // Constructor
    public PollCommand(String identifier, String description, String usage, String permission) {
        this.identifier = identifier;
        this.permission = permission;
        this.description = description;
        this.usage = usage;

        commands.add(this);
    }

    public PollCommand(String identifier, String description, String usage) {
        this(identifier, description, usage, "");
    }

    public abstract void use(CommandSender sender, String[] args);

    // Getters
    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean hasPermission() {
        return !this.permission.trim().isEmpty();
    }

    public static PollCommand getCommandByName(String identifier) {
        return commands.stream().filter(command -> command.getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    public static List<PollCommand> getCommands() {
        return commands;
    }
}
