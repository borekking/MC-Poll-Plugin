package de.borekking.mcPolls.listener;

import de.borekking.mcPolls.chatHook.ChatHook;
import de.borekking.mcPolls.chatHook.ChatHookManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final ChatHookManager chatHookManager;

    public ChatListener() {
        this.chatHookManager = ChatHookManager.getInstance();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        // Handle ChatHook
        Player player = event.getPlayer();
        String msg = event.getMessage();

        // Get ChatHook of Player
        ChatHook chatHook = this.chatHookManager.getChatHook(player);

        // Return if no ChatHook was found (-> player does not have one)
        if (chatHook == null) return;

        // Run the ChatHook (Consumer) with PlayerChatEven's Message
        boolean remove = chatHook.run(msg);

        // Remove the ChatHook in ChatHookManager if ChatHook#run returned true.
        if (remove) this.chatHookManager.removeChatHook(player);

        // Cancel the event thus the Message does not get sent.
        event.setCancelled(true);
        // ---
    }
}
