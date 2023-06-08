package net.iirc.simpleutils;

import com.mojang.logging.LogUtils;
import net.iirc.simpleutils.Fluid.ModFluidTypes;
import net.iirc.simpleutils.Fluid.ModFluids;
import net.iirc.simpleutils.blocks.ModBlocks;
import net.iirc.simpleutils.blocks.entity.ModBlockEntities;
import net.iirc.simpleutils.items.ModItems;
import net.iirc.simpleutils.networking.ModMessages;
import net.iirc.simpleutils.screen.ModMenuTypes;
import net.iirc.simpleutils.screen.OreAmplifierScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SimpleUtils.MOD_ID)
public class SimpleUtils
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "simpleutils";
    private static final Logger LOGGER = LogUtils.getLogger();
    public SimpleUtils()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        modEventBus.addListener(this::commonSetup);

        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            //Fluids
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SOAP_WATER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SOAP_WATER.get(), RenderType.translucent());



            //Block GUIs
            MenuScreens.register(ModMenuTypes.ORE_AMPLIFIER_MENU.get(), OreAmplifierScreen::new);
        }
    }
}
