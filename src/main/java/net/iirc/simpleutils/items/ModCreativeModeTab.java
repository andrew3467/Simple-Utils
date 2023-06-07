package net.iirc.simpleutils.items;

import net.iirc.simpleutils.blocks.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab SIMPLE_UTILS_TAB = new CreativeModeTab("simpleutilstab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.ORE_AMPLIFIER.get());
        }
    };
}
