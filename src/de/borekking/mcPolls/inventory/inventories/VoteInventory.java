package de.borekking.mcPolls.inventory.inventories;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.inventory.InteractiveInventory;
import de.borekking.mcPolls.inventory.inventoryClick.InventoryClick;
import de.borekking.mcPolls.poll.PollManager;
import de.borekking.mcPolls.poll.types.ActivePoll;
import de.borekking.mcPolls.util.InventoryUtils;
import de.borekking.mcPolls.util.ItemBuilder;
import de.borekking.mcPolls.util.JavaUtils;
import de.borekking.mcPolls.util.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class VoteInventory extends InteractiveInventory {

    /*
     * Design:
     *
     *  1st line -> empty,
     *  2nd line -> question,
     *  3rd line -> empty
     *
     *  One lines per 5
     *
     *  Last line empty
     *
     */

    // This inventory's poll
    private final ActivePoll poll;

    public VoteInventory(Player player, ActivePoll poll, PollManager manager) {
        super(player, 6 * 9, "Poll Menu: " + poll.getName(), 10L);

        this.poll = poll;
    }

    @Override
    protected void fill() {
        // If the Poll already ended
        boolean ended = this.poll.hasEnded();

        // Get rest time as String
        String time = TimeUnit.toString(JavaUtils.millisToSecs(this.poll.getTimeLeft()));

        // Set Question on middle of second line
        this.setItem(new ItemBuilder(Material.SIGN).name(Main.COLOR_CODE + "Question")
                .lore(JavaUtils.asList(this.poll.getQuestion(), "", ended ? ChatColor.RED + "Poll Ended!" : "Time Left: " + time)).build(), 9 + 4);

        // Set Answers
        this.setAnswers();

        // Add info TODO
    }

    private void setAnswers() {
        // Player choice as index in answers, in this inventory's poll
        int playerChoice = this.poll.getPlayerChoose(this.player.getUniqueId());

        // Poll's answers
        String[] answers = this.poll.getAnswersArray();

        // Get Slots starting at 2 * 9 + 1
        int[] slots = InventoryUtils.getCenteredSlots(3 * 9 + 1, answers.length);

        for (int i = 0; i < answers.length; i++) {
            int slot = slots[i];

            // If the current answers is the player's choice
            boolean isChoice = i == playerChoice;

            // Get Damage: Green Dye if player choice this answer, blue one else
            // See @ DyeColor                Lime; Gray
            short damage = (short) (isChoice ? 10 : 8);
            // Create ItemStack
            ItemStack item = new ItemBuilder(Material.INK_SACK)
                    .name(Main.COLOR_CODE + "Answer " + i + ChatColor.GRAY + " (" + this.poll.getAmount(i) + ")")
                    .damage(damage)
                    .lore(Collections.singletonList(answers[i])).build();

            // Set the InventoryClick, which will change the players choice and in-/decrease the poll's values (implemented in PlayerPollAdapter)
            final int index = i;
            InventoryClick click = event -> {
                if (!this.poll.hasEnded()) // If poll didn't end yet you can update your answer.
                    this.poll.updateAnswer(this.player.getUniqueId(), index);
            };

            this.setItem(item, slot, click);
        }
    }
}
