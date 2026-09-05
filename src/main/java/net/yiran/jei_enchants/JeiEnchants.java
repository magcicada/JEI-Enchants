package net.yiran.jei_enchants;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(JeiEnchants.MODID)
public class JeiEnchants {
    public static final String MODID = "jei_enchants";

    public JeiEnchants(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(
                ModConfig.Type.CLIENT,
                Config.SPEC
        );
    }
}
