package net.iirc.simpleutils.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.iirc.simpleutils.SimpleUtils;
import net.iirc.simpleutils.recipe.OreAmplifierRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;


@JeiPlugin
public class JEISimpleUtilsPlugin implements IModPlugin {
    public static RecipeType<OreAmplifierRecipe> AMPLIFIER_TYPE =
            new RecipeType<>(OreAmplifierRecipeCategory.UID, OreAmplifierRecipe.class);



    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SimpleUtils.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new OreAmplifierRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<OreAmplifierRecipe> recipeInfusing = rm.getAllRecipesFor(OreAmplifierRecipe.Type.INSTANCE);
        registration.addRecipes(AMPLIFIER_TYPE, recipeInfusing);
    }
}
