package de.borekking.mcPolls.inventory;

import de.borekking.mcPolls.inventory.inventoryClick.InventoryClick;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class InteractiveInventory extends AbstractInventory {

    /*
     * Inventory with option to give Slots a usage.
     *
     */

    // Map: Slot to Consumer
    private final Map<Integer, InventoryClick> inventoryClicks = new HashMap<>();

    public InteractiveInventory(Player player, int size, String title, long refreshDelay, boolean closable) {
        super(player, size, title, refreshDelay, closable);
    }

    public InteractiveInventory(Player player, int size, String title, long refreshDelay) {
        super(player, size, title, refreshDelay);
    }

    public InteractiveInventory(Player player, int size, String title) {
        super(player, size, title);
    }

    public void setItem(ItemStack item, int index, InventoryClick click) {
        this.setItem(item, index);

        this.inventoryClicks.put(index, click);
    }

    @Override
    public void clear() {
        super.clear();
        this.inventoryClicks.clear();
    }

    public InventoryClick getInventoryClick(int slot) {
        return this.inventoryClicks.get(slot);
    }
}
