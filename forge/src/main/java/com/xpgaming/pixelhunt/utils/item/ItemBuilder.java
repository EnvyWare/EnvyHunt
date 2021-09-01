package com.xpgaming.pixelhunt.utils.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private Item type = Items.AIR;
    private int amount = 1;
    private int damage = 0;
    private String name = "";
    private List<String> lore = Lists.newArrayList();
    private Map<String, NBTTagCompound> nbtData = Maps.newHashMap();

    public ItemBuilder() {}

    public ItemBuilder copyItem(ItemBuilder builder) {
        this.type(builder.type);
        this.name(builder.name);
        this.amount(builder.amount);
        this.damage(builder.damage);
        this.lore(builder.lore);
        return this;
    }

    public ItemBuilder type(Item type) {
        this.type = type;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder damage(int damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder lore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public ItemBuilder nbt(String key, NBTTagCompound compound) {
        this.nbtData.put(key, compound);
        return this;
    }

    public NBTTagCompound getCompound(String key) {
        return this.nbtData.computeIfAbsent(key, ___ -> new NBTTagCompound());
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(this.type, this.amount, this.damage);

        if (this.name != null && !this.name.isEmpty()) {
            itemStack.setStackDisplayName(this.name);
        }

        if (this.lore != null && !this.lore.isEmpty()) {
            NBTTagCompound display = itemStack.getOrCreateSubCompound("display");
            NBTTagList lore = new NBTTagList();

            this.lore.forEach(s -> lore.appendTag(new NBTTagString(s)));

            display.setTag("Lore", lore);
            itemStack.setTagInfo("display", display);
        }

        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();

        for (Map.Entry<String, NBTTagCompound> entry : nbtData.entrySet()) {
            compound.setTag(entry.getKey(), entry.getValue());
        }

        itemStack.setTagCompound(compound);

        return itemStack;
    }
}
