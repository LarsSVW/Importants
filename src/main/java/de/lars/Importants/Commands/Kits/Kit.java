package de.lars.Importants.Commands.Kits;

import org.bukkit.inventory.ItemStack;

public class Kit {
    private String name;
    private ItemStack[] items;
    private String command;

    public Kit(String name) {
        this(name, null, null);
    }

    public Kit(String name, ItemStack[] items, String command) {
        this.name = name;
        this.items = items;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public String getCommand() {
        return command;
    }

    public void addItems(ItemStack[] items) {
        this.items = items;
    }

    public void addItem(ItemStack item, int slot) {
        if (items == null) {
            items = new ItemStack[36];
        }

        items[slot] = item;
    }
}
