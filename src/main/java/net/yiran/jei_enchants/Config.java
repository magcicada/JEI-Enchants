package net.yiran.jei_enchants;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.ConfigValue<Integer> enchantPageHeight;
    static {
        enchantPageHeight  = BUILDER.define("enchantPageHeight", 120);
        SPEC = BUILDER.build();
    }
}
