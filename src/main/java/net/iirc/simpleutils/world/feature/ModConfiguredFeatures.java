package net.iirc.simpleutils.world.feature;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.iirc.simpleutils.SimpleUtils;
import net.iirc.simpleutils.blocks.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, SimpleUtils.MOD_ID);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_DEEPSLATE_AURORA = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_AURORA_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_DEEPSLATE_WOLFRAMITE = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_WOLFRAMITE_ORE.get().defaultBlockState())));

    public static final RegistryObject<ConfiguredFeature<?, ?>> DEEPSLATE_AURORA = CONFIGURED_FEATURES.register("deepslate_aurora",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_DEEPSLATE_AURORA.get(),10)));

    public static final RegistryObject<ConfiguredFeature<?, ?>> DEEPSLATE_WOLFRAMITE = CONFIGURED_FEATURES.register("deepslate_wolframite",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_DEEPSLATE_WOLFRAMITE.get(),6)));



    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}