package de.borekking.mcPolls.inventory.inventories;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.inventory.InteractiveInventory;
import de.borekking.mcPolls.util.ItemBuilder;
import de.borekking.mcPolls.util.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class GetPollTimeInventory extends InteractiveInventory {

    private final Consumer<Integer> secsConsumer;

    // Map from TimeUnit to it's amount
    private final Map<TimeUnit, Integer> timeUnits;

    public GetPollTimeInventory(Player player, int secs, Consumer<Integer> secsConsumer) {
        super(player, 6 * 9, "Set Poll Length", 5L, false);

        this.secsConsumer = secsConsumer;
        this.timeUnits = TimeUnit.asTimeUnits(secs);
    }

    @Override
    protected void fill() {
        // Empty row

        // Current Time: 2nd row, 5th slot
        String time = TimeUnit.toString(this.timeUnits);
        this.setItem(new ItemBuilder(Material.EMERALD).name(ChatColor.GREEN + "Confirm").lore(Collections.singletonList(time)).build(), 9 + 4,
                e -> {
                    this.closePlayer();
                    this.secsConsumer.accept(TimeUnit.getSeconds(this.timeUnits));
                }
        );

        // Empty row

        // Add adding-time-signs
        assert TimeUnit.values().length == 5;

        int slot = 0, mainRow = 3;

        for (TimeUnit timeUnit : TimeUnit.values()) {
            int middleSlot = 9 * mainRow + slot;

            // Set increasing, above middleSlot
            this.setItem(new ItemBuilder(Material.STONE_BUTTON).name(ChatColor.GREEN + "Increase").build(), middleSlot - 9, e -> this.increase(timeUnit));

            // Set TimeUnit name, and it's current amount, at middleSlot
            this.setItem(new ItemBuilder(Material.SIGN).name(Main.COLOR_CODE + timeUnit.getName() + " (" + this.timeUnits.getOrDefault(timeUnit, 0) + ")").build(), middleSlot);

            // Set increasing, below middleSlot
            this.setItem(new ItemBuilder(Material.STONE_BUTTON).name(ChatColor.RED + "Decrease").build(), middleSlot + 9, e -> this.decrease(timeUnit));

            slot += 2;
        }
        // ---
    }

    private void increase(TimeUnit t) {
        this.timeUnits.put(t, this.timeUnits.getOrDefault(t, 0) + 1);
    }

    private void decrease(TimeUnit t) {
        int amount = this.timeUnits.getOrDefault(t, 0);
        if (amount <= 0) return;

        this.timeUnits.put(t, amount - 1);
    }
}
