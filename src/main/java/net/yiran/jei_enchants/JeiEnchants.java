package net.yiran.jei_enchants;

import net.yiran.jei_enchants.jei.recipes.EnchantJeiRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(JeiEnchants.MODID)
public class JeiEnchants {
    public static final String MODID = "jei_enchants";

    public JeiEnchants(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(
                ModConfig.Type.CLIENT,
                Config.SPEC
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack stack = item.getDefaultInstance();
            if (item.isEnchantable(stack)) {
                EnchantJeiRecipe.CanEnchantStackList.add(stack);
            }
        }
    }

}
