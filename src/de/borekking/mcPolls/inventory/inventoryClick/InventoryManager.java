package de.borekking.mcPolls.inventory.inventoryClick;

import de.borekking.mcPolls.inventory.AbstractInventory;

import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    // All AbstractInventories' Manager -> using singleton
    private static InventoryManager singleton;

    private final List<AbstractInventory> inventories;

    private InventoryManager() {
        this.inventories = new ArrayList<>();
    }

    // Singleton
    public static InventoryManager getInstance() {
        if (singleton ==  null) singleton = new InventoryManager();
        return singleton;
    }

    public void addInventory(AbstractInventory inv) {
        this.inventories.add(inv);
    }

    public void removeInventory(AbstractInventory inv) {
        this.inventories.remove(inv);
    }

    public AbstractInventory getAbstractInventory(Inventory clickedInventory) {
        return this.inventories.stream().filter(inv -> inv.getInventory().equals(clickedInventory)).findFirst().orElse(null);
    }

    public AbstractInventory getAbstractInventory(InventoryEvent e) {
        // Get clicked Inventory and the belonging slot
        Inventory clickedInventory = e.getInventory();

        // Not null check
        if (clickedInventory == null) return null;

        // Get AbstractInventory from clicked Inventory
        return this.getAbstractInventory(clickedInventory);
    }
}
