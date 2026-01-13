package net.yiran.jei_enchants.jei;

import net.yiran.jei_enchants.JeiEnchants;
import net.yiran.jei_enchants.jei.recipes.EnchantJeiRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

@JeiPlugin
public class JeiEnchantsPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(JeiEnchants.MODID, JeiEnchants.MODID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EnchantJeiRecipe(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(EnchantJeiRecipe.recipeType, ForgeRegistries.ENCHANTMENTS.getValues().stream().toList());
    }
}
