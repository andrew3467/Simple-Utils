package net.iirc.simpleutils.blocks.entity;

import net.iirc.simpleutils.SimpleUtils;
import net.iirc.simpleutils.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SimpleUtils.MOD_ID);



    public static final RegistryObject<BlockEntityType<OreAmplifierBlockEntity>> ORE_AMPLIFIER =
            BLOCK_ENTITIES.register("gem_infusing_station", () ->
                    BlockEntityType.Builder.of(OreAmplifierBlockEntity::new,
                            ModBlocks.ORE_AMPLIFIER.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
