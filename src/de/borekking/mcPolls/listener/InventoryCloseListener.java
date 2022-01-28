package de.borekking.mcPolls.listener;

import de.borekking.mcPolls.inventory.AbstractInventory;
import de.borekking.mcPolls.inventory.inventoryClick.InventoryManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    private final InventoryManager inventoryManager;

    public InventoryCloseListener() {
        this.inventoryManager = InventoryManager.getInstance();
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        // Get AbstractInventory
        AbstractInventory inventory = this.inventoryManager.getAbstractInventory(event);

        // Not null check (if inventory does not belong to any AbstractInventory)
        if (inventory == null) return;

        // Close the inventory;
        inventory.closeInventoryOnly();
    }
}
