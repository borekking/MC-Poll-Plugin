package de.borekking.mcPolls.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final Material material;

    private String name;

    private final List<String> lore;

    // Items damage, default 0
    private short damage;

    // Item's amount, default = 1
    private int amount = 1;

    public ItemBuilder(Material material) {
        this.material = material;

        this.lore = new ArrayList<>();
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore.addAll(lore);
        return this;
    }

    public ItemBuilder clearLore() {
        this.lore.clear();
        return this;
    }

    public ItemBuilder damage(short damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack build() {
        // Create ItemStack; Material, Amount, Damage
        ItemStack item = new ItemStack(this.material, this.amount, this.damage);

        // --- ItemMeta ---
        ItemMeta im = item.getItemMeta();

        // Name
        if (this.name != null) im.setDisplayName(this.name);

        // Lore
        im.setLore(this.lore);

        // Set ItemMeta
        item.setItemMeta(im);
        // ------

        return item;
    }
}
