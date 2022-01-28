package de.borekking.mcPolls.inventory.inventories;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.inventory.AbstractInventory;
import de.borekking.mcPolls.poll.types.EndedPoll;
import de.borekking.mcPolls.util.InventoryUtils;
import de.borekking.mcPolls.util.ItemBuilder;
import de.borekking.mcPolls.util.JavaUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

public class PollResultsInventory extends AbstractInventory {

    private final EndedPoll poll;

    // Poll's Date as String
    private final String time;

    public PollResultsInventory(Player player, EndedPoll poll) {
        super(player, 6 * 9, "Poll Results: " + poll.getName());

        this.poll = poll;
        this.time = this.getDateString();
    }

    @Override
    protected void fill() {
        // Set Question on middle of second line
        this.setItem(new ItemBuilder(Material.SIGN).name(Main.COLOR_CODE + "Question")
                .lore(JavaUtils.asList(this.poll.getQuestion(), "", "(Date: " + this.time + ")")).build(), 9 + 4);

        // Set Answers
        this.setAnswers();
    }

    private void setAnswers() {
        // Poll's answers
        String[] answers = this.poll.getAnswersArray();
        // Get Winner Answer's (Most votes) Answer
        String winner = this.poll.getWinner().getAnswer();

        // Get Slots starting at 2 * 9 + 1
        int[] slots = InventoryUtils.getCenteredSlots(3 * 9 + 1, answers.length);

        for (int i = 0; i < answers.length; i++) {
            int slot = slots[i];

            // If current answer is the winner answer
            boolean isWinner = winner.equals(answers[i]);

            // Get Damage: Green Dye if player choice this answer, blue one else
            // See @ DyeColor                Lime; Gray
            short damage = (short) (isWinner ? 10 : 8);
            // Create ItemStack
            ItemStack item = new ItemBuilder(Material.INK_SACK)
                    .name(Main.COLOR_CODE + "Answer " + i + ChatColor.GRAY + " (" + this.poll.getAnswer(i).getAmount() + ")")
                    .damage(damage)
                    .lore(Collections.singletonList(answers[i])).build();

            this.setItem(item, slot);
        }
    }

    private String getDateString() {
        // Get TimeStamp from Poll's End.
        Timestamp ts = new Timestamp(this.poll.getEnd());
        // Get Date from this TimeStamp
        Date date = new Date(ts.getTime());
        // Get date as String using a formatter
        return JavaUtils.getDateAsString(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
