package de.borekking.mcPolls.chatHook;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ChatHookManager {

    // Singleton
    private static ChatHookManager instance;

    public static ChatHookManager getInstance() {
        if (instance == null) instance = new ChatHookManager();
        return instance;
    }
    // ---

    // Active ChatHooks
    private final Map<Player, ChatHook> chatHooks;

    private ChatHookManager() {
        this.chatHooks = new HashMap<>();
    }

    public void addChatHook(String message, ChatHook chatHook) {
        chatHook.sendMessage(message);
        this.chatHooks.put(chatHook.getPlayer(), chatHook);
    }

    public void removeChatHook(Player p) {
        this.chatHooks.remove(p);
    }

    public ChatHook getChatHook(Player p) {
        return this.chatHooks.get(p);
    }
}
