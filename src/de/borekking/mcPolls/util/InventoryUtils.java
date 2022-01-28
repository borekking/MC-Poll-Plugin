package de.borekking.mcPolls.util;

public class InventoryUtils {

    // Array of slots (column) which are nicely centered in one row of an inventory.
    public static int[] getCenteredSlots(int startSlot, int amount) {
        if (amount < 1) return new int[0];

        // Get slot's column
        int column = startSlot % 9;
        // Not valid starting points
        if (column > 7 || (column % 2 == 0 && column != 2 && column != 6))
            throw new IllegalArgumentException("StartSlot " + startSlot + " is not allowed!");

        int[] arr = new int[amount];
        arr[0] = startSlot;

        int slot = startSlot;

        for (int i = 1; i < amount; i++) {
            // Update slot and column
            slot+=1;
            column = slot % 9;

            // No slot on very right/left
            if (column == 0) slot += 1;
            else if (column == 8) slot+=2;
            else if (column == 4) slot += 1; // No slot in middle (because of symmetry)

            // Put slot in array
            arr[i] = slot;
        }

        return arr;
    }
}
