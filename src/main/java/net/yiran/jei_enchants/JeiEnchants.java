package net.yiran.jei_enchants;

import net.yiran.jei_enchants.jei.recipes.EnchantJeiRecipe;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@SuppressWarnings("removal")
@Mod(JeiEnchants.MODID)
public class JeiEnchants {
    public static final String MODID = "jei_enchants";
    private static final Logger LOGGER = LogUtils.getLogger();

    public JeiEnchants() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        FMLJavaModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT,
                Config.SPEC
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        for (Item item : ForgeRegistries.ITEMS) {
            ItemStack stack = item.getDefaultInstance();
            if (item.isEnchantable(stack)) {
                EnchantJeiRecipe.CanEnchantStackList.add(stack);
            }
        }
    }

}
