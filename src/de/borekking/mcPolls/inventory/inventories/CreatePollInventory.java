package de.borekking.mcPolls.inventory.inventories;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.chatHook.ChatHook;
import de.borekking.mcPolls.chatHook.ChatHookManager;
import de.borekking.mcPolls.inventory.InteractiveInventory;
import de.borekking.mcPolls.poll.types.ActivePoll;
import de.borekking.mcPolls.poll.PollBuilder;
import de.borekking.mcPolls.poll.PollManager;
import de.borekking.mcPolls.util.InventoryUtils;
import de.borekking.mcPolls.util.ItemBuilder;
import de.borekking.mcPolls.util.JavaUtils;
import de.borekking.mcPolls.util.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CreatePollInventory extends InteractiveInventory {

    /*
     * Inventory for creating a new Poll
     *
     *
     * TODO
     *  Add feedback after chat hooks (e.g. After Wrong name, etc)
     *
     * TODO
     *  Add preventing from two equal answers.
     *
     */

    private final ChatHookManager chatHookManager;

    private final PollBuilder builder;

    private final PollManager pollManager;

    public CreatePollInventory(Player player) {
        super(player, 6 * 9, Main.COLOR_CODE + "Create a Poll", 5L);

        this.builder = new PollBuilder();
        this.chatHookManager = ChatHookManager.getInstance();
        this.pollManager = Main.getPlugin().getPollManager();
    }

    @Override
    protected void fill() {
        // Get answers
        List<String> answers = this.builder.getAnswers();

        // First row empty

        // Create: Second row, middle
        boolean validPoll = this.builder.isValid();
        if (validPoll) {
            this.setItem(new ItemBuilder(Material.EMERALD).name(Main.COLOR_CODE + "Create").lore(JavaUtils.asList("Start the Poll")).build(),
                    4, e -> this.create());
        } else {
            this.setItem(new ItemBuilder(Material.EMERALD).name(ChatColor.RED + "Create").lore(JavaUtils.asList("Invalid Poll! Cant not create Poll.")).build(),
                    4);
        }

        // SetName: 3rd Row, 3rd slot
        this.setItem(new ItemBuilder(Material.SIGN).name(Main.COLOR_CODE + "Set Name").lore(Collections.singletonList("-> " + this.builder.getName())).build(),
                9 + 2, e -> this.setName());

        // SetQuestion: 3rd Row, 7th slot
        this.setItem(new ItemBuilder(Material.SIGN).name(Main.COLOR_CODE + "Set Question").lore(Collections.singletonList("-> " + this.builder.getQuestion())).build(),
                9 + 6, e -> this.setQuestion());

        // 4th row empty

        // Max amount of answers
        int maxAnswers = 6;

        // Fill Answers (max 4)
        // Get Slots starting at 4 * 9 + 1
        int startSlot = 4 * 9 + 1;
        int[] slots = InventoryUtils.getCenteredSlots(startSlot, answers.size());

        for (int i = 0; i < answers.size(); i++) {
            int slot = slots[i]; // Current slot
            String answer = answers.get(i);

            // Break on invalid slot (only between 5th and 8th row)
            if (i >= maxAnswers) break;

            // Set Answer
            this.setItem(new ItemBuilder(Material.REDSTONE).name(Main.COLOR_CODE + "Answer " + i).lore(Collections.singletonList(answer)).build(), slot);

            // Set remove button above
            this.setItem(new ItemBuilder(Material.STONE_BUTTON).name(ChatColor.RED + "Remove Answer").build(), slot - 9, e -> this.builder.removeAnswer(answer));
        }

        // Set add-answer-button (only if maxAnswerSlot isn't reached) to last answers right
        if (answers.size() < maxAnswers) {
            int index = slots.length == 0 ? startSlot + 1 : slots[slots.length - 1] + 1;
            this.setItem(new ItemBuilder(Material.LEVER).name(Main.COLOR_CODE + "Add Answer").build(), index, e -> this.addAnswer());
        }

        // Set Poll's Length: Last slot, bottom right corner
        String currentTime = TimeUnit.toString(JavaUtils.millisToSecs(this.builder.getPollLength()));
        this.setItem(new ItemBuilder(Material.COMPASS).name(Main.COLOR_CODE + "Set Poll's Length").lore(Collections.singletonList(currentTime)).build(),
                this.size - 1, e -> this.setPollLength());
    }

    private void setPollLength() {
        this.closePlayer();

        int currentSeconds = JavaUtils.millisToSecs(this.builder.getPollLength());
        new GetPollTimeInventory(this.player, currentSeconds, secs -> {
            this.builder.pollLength(JavaUtils.secsToMillis(secs));
            this.open();
        });
    }

    private void addAnswer() {
        this.useInput(Main.PREFIX + ChatColor.GRAY + "Please enter an answer option.", new ChatHook(this.player, str -> {
            this.builder.addAnswer(str);
            this.open();
            return true;
        }));
    }

    private void setName() {
        this.useInput(Main.PREFIX + ChatColor.GRAY + "Please enter a new name.", new ChatHook(this.player, (str, p) -> {
            // Name might not be already used (other active or ended Polls) and not contain spaces.
            if (this.pollManager.containsActivePoll(str) || this.pollManager.containsEndedPoll(str) || str.contains(" ")) {
                Main.sendMessage(p, ChatColor.RED + "Name \"" + str + "\" might already exist or contains spaces.");
                return false;
            } else {
                this.builder.name(str);
                this.open();
                return true;
            }
        }));
    }

    private void setQuestion() {
        this.useInput(Main.PREFIX + ChatColor.GRAY + "Please enter a new question.", new ChatHook(this.player, str -> {
            this.builder.question(str);
            this.open();
            return true;
        }));
    }

    private void create() {
        ActivePoll poll = this.builder.build(); // TODO Add Confirmation
        this.pollManager.startPoll(poll);

        Main.sendMessage(this.player, "Successfully started new Poll: " + Main.COLOR_CODE + poll.getName() + ChatColor.GRAY + "."); // TODO Help msg

        // TODO Add broadcast option

        this.closePlayer();
    }

    // Method to get a user input via chat
    private void useInput(String msg, ChatHook chatHook) {
        this.closePlayer();

        this.chatHookManager.addChatHook(msg, chatHook);
    }
}
