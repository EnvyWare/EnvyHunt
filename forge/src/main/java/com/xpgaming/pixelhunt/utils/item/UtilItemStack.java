package com.xpgaming.pixelhunt.utils.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.List;

public class UtilItemStack {

    public static void setLore(ItemStack itemStack, List<String> lore) {
        NBTTagCompound display = itemStack.getOrCreateSubCompound("display");
        NBTTagList newLore = new NBTTagList();

        lore.forEach(s -> newLore.appendTag(new NBTTagString(s)));

        display.setTag("Lore", newLore);
        itemStack.setTagInfo("display", display);
    }
}
