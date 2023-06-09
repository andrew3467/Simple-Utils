package net.iirc.simpleutils.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.iirc.simpleutils.SimpleUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class OreAmplifierRecipe implements Recipe<SimpleContainer> {
    public final ResourceLocation id;
    private final ItemStack output;
    private int outputCount;
    private final NonNullList<Ingredient> recipeItems;


    public OreAmplifierRecipe(ResourceLocation id, ItemStack output, int outputCount, NonNullList<Ingredient> recipeItems){
        this.id = id;
        this.output = output;
        this.outputCount = outputCount;
        this.recipeItems = recipeItems;
    }


    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()){
            return false;
        }

        return recipeItems.get(0).test(pContainer.getItem(0));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    private int getOutputCount() {
        return this.outputCount;
    }

    public static class Type implements RecipeType<OreAmplifierRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "ore_amplifying";
    }


    public static class Serializer implements RecipeSerializer<OreAmplifierRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(SimpleUtils.MOD_ID, "ore_amplifying");

        @Override
        public OreAmplifierRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new OreAmplifierRecipe(pRecipeId, output, 2, inputs);
        }

        @Override
        public @Nullable OreAmplifierRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            int count = buf.readInt();
            return new OreAmplifierRecipe(id, output, count, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, OreAmplifierRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
            buf.writeInt(recipe.getOutputCount());
        }
    }
}
