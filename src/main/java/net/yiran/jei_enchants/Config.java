package net.yiran.jei_enchants;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static  ForgeConfigSpec.ConfigValue<Integer> enchantPageHeight;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableModId;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableCategory;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableRarity;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableApplyAtEnchantingTable;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableIsDiscoverable;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableIsTradeable;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableIsTreasureOnly;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableIsCurse;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableEnchantmentDesc;

    static {
        enchantPageHeight  = BUILDER.define("enchantPageHeight", 120);
        BUILDER.push("element");
        enableModId = BUILDER.define("enableModId", true);
        enableCategory = BUILDER.define("enableCategory", true);
        enableRarity = BUILDER.define("enableRarity", true);
        enableApplyAtEnchantingTable = BUILDER.define("enableApplyAtEnchantingTable", true);
        enableIsDiscoverable = BUILDER.define("enableIsDiscoverable", true);
        enableIsTradeable = BUILDER.define("enableIsTradeable", true);
        enableIsTreasureOnly = BUILDER.define("enableIsTreasureOnly", true);
        enableIsCurse = BUILDER.define("enableIsCurse", true);
        enableEnchantmentDesc = BUILDER.define("enableEnchantmentDesc", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
