package net.iirc.simpleutils.recipe;

import net.iirc.simpleutils.SimpleUtils;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleUtils.MOD_ID);


    public static final RegistryObject<RecipeSerializer<OreAmplifierRecipe>> ORE_AMPLIFIER_SERIALIZER =
            SERIALIZERS.register("ore_amplifying", () -> OreAmplifierRecipe.Serializer.INSTANCE);



    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
