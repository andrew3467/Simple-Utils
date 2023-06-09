package net.iirc.simpleutils.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.iirc.simpleutils.SimpleUtils;
import net.iirc.simpleutils.blocks.ModBlocks;
import net.iirc.simpleutils.recipe.OreAmplifierRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class OreAmplifierRecipeCategory implements IRecipeCategory<OreAmplifierRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(SimpleUtils.MOD_ID, "ore_amplifying");
    public final static ResourceLocation TEXTURE = new ResourceLocation(SimpleUtils.MOD_ID, "textures/gui/ore_amplifier.png");

    private final IDrawable background;
    private final IDrawable icon;



    public OreAmplifierRecipeCategory(IGuiHelper helper){
        this.background = helper.createDrawable(TEXTURE, 0, 0, 175, 79);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ORE_AMPLIFIER.get()));
    }

    @Override
    public RecipeType<OreAmplifierRecipe> getRecipeType() {
        return JEISimpleUtilsPlugin.AMPLIFIER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Ore Amplifier");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OreAmplifierRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 17, 35).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 94, 29).addItemStack(recipe.getResultItem());
    }
}
