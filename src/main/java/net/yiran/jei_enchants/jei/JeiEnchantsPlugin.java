package net.yiran.jei_enchants.jei;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.yiran.jei_enchants.JeiEnchants;
import net.yiran.jei_enchants.jei.recipes.EnchantJeiRecipe;

import java.util.List;

@JeiPlugin
public class JeiEnchantsPlugin implements IModPlugin {
    public static Registry<Enchantment> ENCHANTMENTS;
    public static List<Holder<Enchantment>> ENCHANTMENTSLIST;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(JeiEnchants.MODID, JeiEnchants.MODID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EnchantJeiRecipe(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ENCHANTMENTS = Minecraft.getInstance().getConnection().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        registration.addRecipes(EnchantJeiRecipe.recipeType, ENCHANTMENTSLIST = ENCHANTMENTS.holders().collect(ObjectArrayList.toList()));
    }
}
