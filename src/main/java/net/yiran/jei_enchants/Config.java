package net.yiran.jei_enchants;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static  ModConfigSpec.ConfigValue<Integer> enchantPageHeight;
    public static  ModConfigSpec.ConfigValue<Boolean> enableModId;
    public static  ModConfigSpec.ConfigValue<Boolean> enableWeight;
    public static  ModConfigSpec.ConfigValue<Boolean> enableApplyAtEnchantingTable;
    public static  ModConfigSpec.ConfigValue<Boolean> enableIsDiscoverable;
    public static  ModConfigSpec.ConfigValue<Boolean> enableIsTradeable;
    public static  ModConfigSpec.ConfigValue<Boolean> enableIsTreasure;
    public static  ModConfigSpec.ConfigValue<Boolean> enableIsCurse;
    public static  ModConfigSpec.ConfigValue<Boolean> enableEnchantmentDesc;

    static {
        enchantPageHeight  = BUILDER.define("enchantPageHeight", 120);
        BUILDER.push("element");
        enableModId = BUILDER.define("enableModId", true);
        enableWeight = BUILDER.define("enableWeight", true);
        enableApplyAtEnchantingTable = BUILDER.define("enableApplyAtEnchantingTable", true);
        enableIsDiscoverable = BUILDER.define("enableIsDiscoverable", true);
        enableIsTradeable = BUILDER.define("enableIsTradeable", true);
        enableIsTreasure = BUILDER.define("enableIsTreasure", true);
        enableIsCurse = BUILDER.define("enableIsCurse", true);
        enableEnchantmentDesc = BUILDER.define("enableEnchantmentDesc", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
