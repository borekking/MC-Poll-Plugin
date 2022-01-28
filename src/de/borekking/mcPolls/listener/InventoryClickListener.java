package de.borekking.mcPolls.listener;

import de.borekking.mcPolls.inventory.AbstractInventory;
import de.borekking.mcPolls.inventory.InteractiveInventory;
import de.borekking.mcPolls.inventory.inventoryClick.InventoryClick;
import de.borekking.mcPolls.inventory.inventoryClick.InventoryManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final InventoryManager inventoryManager;

    public InventoryClickListener() {
        this.inventoryManager = InventoryManager.getInstance();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Get clicked slot
        int slot = event.getRawSlot();

        // Get AbstractInventory from Manager if existing; If it's null, return
        AbstractInventory inventory = this.inventoryManager.getAbstractInventory(event);
        if (inventory == null) return;

        event.setCancelled(true);

        // If the closeSlot was pressed, close the inventory;
        if (inventory.isCloseSlot(slot)) {
            inventory.closePlayer();
            return;
        }

        // Else check if the AbstractInventory is an InteractiveInventory
        if (inventory instanceof InteractiveInventory) {
            // Get the Consumer for this slot in the inventory
            InventoryClick clickEvent = ((InteractiveInventory) inventory).getInventoryClick(slot);

            // If it exists, use it
            if (clickEvent != null) clickEvent.accept(event);
        }
    }
}
