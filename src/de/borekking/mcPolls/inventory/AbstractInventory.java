package de.borekking.mcPolls.inventory;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.inventory.inventoryClick.InventoryManager;
import de.borekking.mcPolls.util.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractInventory {

    // Final close button name
    private final static String closeButtonName = ChatColor.RED + "close";

    // Inventory's size
    protected final int size;

    // Close Buttons Slot ( < 0 if unused)
    private int closeSlot;

    // The Inventory's player
    protected final Player player;

    // The actual inventory
    private final Inventory inventory;

    // If the Inventory should have a close-button
    private final boolean closable;

    // This inventory's update Runnable
    private BukkitRunnable updateRunnable;

    // refreshDelay
    private final long refreshDelay;

    public AbstractInventory(Player player, int size, String title, long refreshDelay, boolean closeable) {
        this.closable = closeable;
        this.player = player;
        this.refreshDelay = refreshDelay;

        // Set size to a valid size
        this.size = this.validSize(size);

        // Create inventory
        this.inventory = Bukkit.createInventory(player, size, title);

        // Start Tasks (load inventory ones)
        this.startTasks();

        // Open the inventory to the player
        this.open();
    }

    public AbstractInventory(Player player, int size, String title, long refreshDelay) {
        this(player, size, title, refreshDelay, true);
    }

    public AbstractInventory(Player player, int size, String title) {
        this(player, size, title, -1L);
    }

    // Abstract methode used to fill the inventory
    protected abstract void fill();

    // Methode to add an item to the inventory
    protected void setItem(ItemStack item, int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException("Index " + index + " is not valid for inventory size " + this.size);
        }

        this.inventory.setItem(index, item);
    }

    // Opens the inventory for the player
    public void open() {
        this.player.openInventory(this.inventory);

        // Start refreshing; Smaller zero value are disabled
        this.startRefreshing(this.refreshDelay);

        // Add Inventory to InventoryManager
        InventoryManager.getInstance().addInventory(this);
    }

    // Closes the inventory for the player (if the players current inventory is equal to this)
    public void close(boolean closePlayer) {
        // Close player's inventory
        if (closePlayer) this.player.closeInventory();

        // Stop refreshing
        if (this.updateRunnable != null) { // TODO FIX
            this.updateRunnable.cancel();
            this.updateRunnable = null;
        }

        // Remove Inventory from InventoryManager
        InventoryManager.getInstance().removeInventory(this);
    }

    public void closePlayer() {
        this.close(true);
    }

    public void closeInventoryOnly() {
        this.close(false);
    }

    public boolean isCloseSlot(int slot) {
        return this.closeSlot >= 0 && this.closeSlot == slot;
    }

    public void clear() {
        this.inventory.clear();
    }

    private void startTasks() {
        // Run Task (updating inventory) "later"
        new BukkitRunnable() {
            @Override
            public void run() {
                AbstractInventory.this.updateInventory();
            }
        }.runTaskLater(Main.getPlugin(), 0L);
    }

    private void startRefreshing(long delay) {
        // Cancel if delay <0 (disabled)
        if (delay < 0) return;

        (this.updateRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                AbstractInventory.this.updateInventory();
            }
        }).runTaskTimer(Main.getPlugin(), delay, delay);
    }

    // Updates the inventories
    private void updateInventory() {
        // Clear the current inventory
        this.clear();

        // Set provided items
        this.fill();

        // Fill background (Black Stained Glass Pane) new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
        this.fillBackground(new ItemBuilder(Material.STONE_BUTTON).name(closeButtonName).build(), new ItemBuilder(Material.STAINED_GLASS_PANE).name(ChatColor.GRAY.toString()).damage((short) 15).build());
    }

    // Set background
    private void fillBackground(ItemStack closeButton, ItemStack background) {
        // Set the closeButtonSlot to -1 if the slot is already used, otherwise set the closeButton on the slot.
        if (this.closable) {
            this.closeSlot = this.getCloseSlot();
            if (this.closeSlot >= 0) this.setItem(closeButton, this.closeSlot);
        }

        // Fill empty slots with background ItemStack
        for (int i = 0; i < this.size; i++) {
            ItemStack current = this.inventory.getItem(i);
            if (current == null) this.setItem(background, i);
        }
    }

    private int getCloseSlot() {
        int slot = this.size - 5;

        if (this.inventory.getItem(slot) != null) slot = -1;

        return slot;
    }

    // Turns any size into a valid inventory size by round up by steps of nine.
    private int validSize(int size) {
        return (size % 9 != 0) ? (size - (size % 9) + 9) : size;
    }

    public Inventory getInventory() {
        return inventory;
    }
}

