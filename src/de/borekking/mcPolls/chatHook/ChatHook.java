package de.borekking.mcPolls.chatHook;

import org.bukkit.entity.Player;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ChatHook {

    /*
     * TODO
     *  Add canceling
     *
     */

    private final Player player;

    // Returns true if the ChatHook should be disabled, false otherwise.
    private final BiFunction<String, Player, Boolean> consumer;

    public ChatHook(Player player, BiFunction<String, Player, Boolean> consumer) {
        this.player = player;
        this.consumer = consumer;
    }

    public ChatHook(Player player, Function<String, Boolean> consumer) {
        this(player, (str, p) -> consumer.apply(str));
    }

    void sendMessage(String msg) {
        this.player.sendMessage(msg);
    }

    public boolean run(String message) {
        return this.consumer.apply(message, this.player);
    }

    public Player getPlayer() {
        return player;
    }
}
