package net.iirc.simpleutils.blocks;

import net.iirc.simpleutils.Fluid.ModFluids;
import net.iirc.simpleutils.SimpleUtils;
import net.iirc.simpleutils.blocks.custom.OreAmplifierBlock;
import net.iirc.simpleutils.items.ModCreativeModeTab;
import net.iirc.simpleutils.items.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleUtils.MOD_ID);


    public static final RegistryObject<Block> ORE_AMPLIFIER = registerBlock("ore_amplifier",
            () -> new OreAmplifierBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(6.0f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()),
            ModCreativeModeTab.SIMPLE_UTILS_TAB);

    public static final RegistryObject<LiquidBlock> SOAP_WATER_BLOCK = BLOCKS.register("soap_water_block",
            () -> new LiquidBlock(ModFluids.SOURCE_SOAP_WATER, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<Block> DEEPSLATE_AURORA_ORE = registerBlock("deepslate_aurora_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.SIMPLE_UTILS_TAB);
    public static final RegistryObject<Block> DEEPSLATE_WOLFRAMITE_ORE = registerBlock("deepslate_wolframite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(2f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.SIMPLE_UTILS_TAB);


    public static final RegistryObject<Block> DEEPSLATE_PNEUMONIA_ORE = registerBlock("deepslate_pneumonia_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.SIMPLE_UTILS_TAB);


    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
}
