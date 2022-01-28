package de.borekking.mcPolls;

import de.borekking.mcPolls.commands.PollCommandManager;
import de.borekking.mcPolls.commands.VoteCommand;
import de.borekking.mcPolls.data.DataManager;
import de.borekking.mcPolls.listener.ChatListener;
import de.borekking.mcPolls.listener.InventoryClickListener;
import de.borekking.mcPolls.listener.InventoryCloseListener;
import de.borekking.mcPolls.poll.types.ActivePoll;
import de.borekking.mcPolls.poll.PollManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class Main extends JavaPlugin {

    /*
     * Plugin to create ingame polls, in minecraft (spigot, 1.8)
     *
     *
     * TODO:
     *  - disabling
     *  - canceling
     *  - Set, Add, Decrease time, ...
     * => Poll Manager Menu (Inventory)
     *
     * TODO - next -
     *  Do PollResultsCommand
     *
     * TODO Add Multiply Choice Option
     *
     */

    // Version
    public static final String VERSION = "1.0", COLOR_CODE = ChatColor.BLUE.toString(), PREFIX = ChatColor.GRAY + "[" + COLOR_CODE + "MC-Polls" + ChatColor.GRAY + "] ",
    CONSOLE_PREFIX = "[MC-Polls] ";

    // Singleton
    private static Main plugin;

    // <Static-Methods>

    public static Main getPlugin() {
        return plugin;
    }

    public static void sendMessage(CommandSender sender, String msg) {
        if (sender instanceof Player) sender.sendMessage(PREFIX + msg);
        else sender.sendMessage(CONSOLE_PREFIX + msg);
    }

    public static void sendMessage(CommandSender sender, String... msg) {
        for (String s : msg) sendMessage(sender, s);
    }

    public static void broadcast(String msg) {
        Bukkit.broadcastMessage(PREFIX + msg);
    }

    // </Static-Methods

    private PollManager pollManager;

    private DataManager dataManager;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Create PollManager
        Consumer<ActivePoll> endingPollProcedure = poll -> Main.broadcast(ChatColor.GRAY + "The Poll \"" + COLOR_CODE + poll.getName() + ChatColor.GRAY + "\" ended! Check out the Result with: \"/poll results <name>\"");
        this.pollManager = new PollManager(endingPollProcedure);

        // Create DataManager
        this.dataManager = new DataManager(this.pollManager);

        // Register Commands and Listeners
        this.registerCommands(this.pollManager);
        this.registerListeners();

        // Load Active and Ended Polls from DataManager
        this.dataManager.load();

        System.out.println("Enabled MC Polls Plugin by borekking [" + VERSION + "]");
    }

    @Override
    public void onDisable() {
        // Save Active and Ended Polls with DataManager#
        this.dataManager.save();

        System.out.println("Disabled MC Polls Plugin by borekking [" + VERSION + "]");
    }


    private void registerCommand(String name, CommandExecutor command) {
        this.getCommand(name).setExecutor(command);
    }

    private void registerCommands(PollManager pm) {
        this.registerCommand("poll", new PollCommandManager(pm));
        this.registerCommand("vote", new VoteCommand(pm));
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void registerListeners() {
        // Inventory
        this.registerListener(new InventoryCloseListener());
        this.registerListener(new InventoryClickListener());

        // Chat
        this.registerListener(new ChatListener());
    }

    public PollManager getPollManager() {
        return pollManager;
    }
}
