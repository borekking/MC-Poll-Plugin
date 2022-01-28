package de.borekking.mcPolls.inventory.inventoryClick;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

@FunctionalInterface
public interface InventoryClick extends Consumer<InventoryClickEvent> {

    /*
     * Interface to use an Inventory Click by using Consumer
     * 
     */

}
