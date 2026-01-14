package net.yiran.jei_enchants.jei.recipes;

import net.yiran.jei_enchants.Config;
import net.yiran.jei_enchants.JeiEnchants;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("removal")
public class EnchantJeiRecipe extends AbstractRecipeCategory<Enchantment> {
    public static RecipeType<Enchantment> recipeType = RecipeType.create(JeiEnchants.MODID, "enchant", Enchantment.class);
    public static Map<Enchantment, List<ItemStack>> EnchantBooksCache = new HashMap<>();
    public static Map<Enchantment, List<ItemStack>> CanEnchantItemCache = new HashMap<>();
    public static Map<Enchantment, List<ItemStack>> UnCompatibleEnchantBooksCache = new HashMap<>();
    public static Map<Enchantment, Boolean> ApplyAtEnchantingTableCache = new HashMap<>();
    public static List<ItemStack> CanEnchantStackList = new ArrayList<>();
    public static Component TRUE = Component.translatable("jei_enchants.wrapper.true");
    public static Component FALSE = Component.translatable("jei_enchants.wrapper.false");

    public EnchantJeiRecipe(IGuiHelper guiHelper) {
        super(
                recipeType,
                Component.translatable("jei.category.jei_enchants.enchant"),
                guiHelper.createDrawableItemLike(Items.ENCHANTED_BOOK),
                160,
                Config.enchantPageHeight.get()
        );
        EnchantBooksCache.clear();
        CanEnchantItemCache.clear();
        UnCompatibleEnchantBooksCache.clear();
        ApplyAtEnchantingTableCache.clear();
    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Enchantment enchantment, IFocusGroup iFocusGroup) {
        List<ItemStack> books = getAllEnchantedBooks(enchantment);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 0, 0)
                .setStandardSlotBackground()
                .addItemStacks(books);
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addItemStacks(books);
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 18)
                .setStandardSlotBackground()
                .addItemStacks(getCanEnchantItem(enchantment));
        if (!getUnCompatibleEnchantBook(enchantment).isEmpty())
            builder.addSlot(RecipeIngredientRole.INPUT, 0, 18 * 2)
                    .setStandardSlotBackground()
                    .addItemStacks(getUnCompatibleEnchantBook(enchantment))
                    .addTooltipCallback((iRecipeSlotView, list) -> {
                        list.set(0, Component.translatable("jei_enchants.unCompatible.tooltip", Component.translatable(enchantment.getDescriptionId())));
                    });
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, Enchantment enchantment, IFocusGroup focuses) {
        if (enchantment.getMaxLevel() == 1) {
            addText(builder, 18, 0,
                    Component.translatable(enchantment.getDescriptionId()));
        } else {
            addText(builder, 18, 0,
                    Component.translatable(enchantment.getDescriptionId()).append(I18n.get("jei_enchants.level.title", enchantment.getMaxLevel())));
        }
        int height = 0;
        if (Config.enableModId.get()) {
            addText(builder, 18, height += 10,
                    Component.literal(ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getNamespace()).withStyle(ChatFormatting.BLUE));
        }
        if (Config.enableCategory.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.category.desc", Component.translatable("enchantment.category." + enchantment.category.name().toLowerCase())));
        }
        if (Config.enableRarity.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.rarity.desc", Component.translatable("enchantment.rarity." + enchantment.getRarity().name().toLowerCase())));

            List<FormattedText> rarityTooltip = new ArrayList<>();
            rarityTooltip.add(Component.translatable("jei_enchants.weight.tooltip", enchantment.getRarity().getWeight()));
            for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                rarityTooltip.add(Component.translatable("jei_enchants.level.cost.tooltip", i, enchantment.getMinCost(i), enchantment.getMaxCost(i)));
            }
            addTooltips(builder, 18, height, rarityTooltip);
        }
        if (Config.enableApplyAtEnchantingTable.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.applyAtEnchantingTable.desc", getBooleanWrapper(applyAtEnchantingTable(enchantment))));
        }
        if (Config.enableIsDiscoverable.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.isDiscoverable.desc", getBooleanWrapper(enchantment.isDiscoverable())));
        }
        if (Config.enableIsTradeable.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.isTradeable.desc", getBooleanWrapper(enchantment.isTradeable())));
        }
        if (Config.enableIsTreasureOnly.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.isTreasureOnly.desc", getBooleanWrapper(enchantment.isTreasureOnly())));
        }
        if (Config.enableIsCurse.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.isCurse.desc", getBooleanWrapper(enchantment.isCurse())));
        }
        if (Config.enableEnchantmentDesc.get()) {
            height += 10;
            int xOffset = 0;
            if (height < 54) {
                xOffset = 18;
            }
            if (I18n.exists(enchantment.getDescriptionId() + ".desc")) {
                builder.addText(
                                Minecraft.getInstance().font.getSplitter().splitLines(
                                        Component.translatable(enchantment.getDescriptionId() + ".desc").withStyle(ChatFormatting.ITALIC),
                                        getWidth(),
                                        Style.EMPTY
                                )
                                , getWidth() - xOffset, getHeight() - 90)
                        .setPosition(xOffset, height);
            } else {
                builder.addText(Component.translatable("jei_enchants.none.desc"), getWidth() - xOffset, getHeight() - 90)
                        .setPosition(xOffset, height);
            }
        }
    }

    public Component getBooleanWrapper(boolean bool) {
        return bool ? TRUE : FALSE;
    }

    public void addText(IRecipeExtrasBuilder builder, int x, int y, FormattedText... texts) {
        builder.addText(List.of(texts), getWidth() - x, 10)
                .setPosition(x, y);
    }

    public void addTooltips(IRecipeExtrasBuilder builder, int x, int y, FormattedText... texts) {
        builder.addText(List.of(texts), getWidth() - x, 8)
                .setPosition(x, y + 1);
    }

    public void addTooltips(IRecipeExtrasBuilder builder, int x, int y, List<FormattedText> texts) {
        builder.addText(texts, getWidth() - x, 8)
                .setPosition(x, y + 1);
    }

    public static List<ItemStack> getAllEnchantedBooks(Enchantment enchantment) {
        if (EnchantBooksCache.containsKey(enchantment)) {
            return EnchantBooksCache.get(enchantment);
        }
        List<ItemStack> list = new ArrayList<>();
        for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
            list.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i)));
        }
        EnchantBooksCache.put(enchantment, list);
        return list;
    }

    public static List<ItemStack> getCanEnchantItem(Enchantment enchantment) {
        if (CanEnchantItemCache.containsKey(enchantment)) {
            return CanEnchantItemCache.get(enchantment);
        }
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack stack : CanEnchantStackList) {
            if (enchantment.canEnchant(stack)) {
                list.add(stack);
            }
        }
        CanEnchantItemCache.put(enchantment, list);
        return list;
    }

    public static List<ItemStack> getUnCompatibleEnchantBook(Enchantment enchantment) {
        if (UnCompatibleEnchantBooksCache.containsKey(enchantment)) {
            return UnCompatibleEnchantBooksCache.get(enchantment);
        }
        List<ItemStack> list = new ArrayList<>();
        for (Enchantment other : ForgeRegistries.ENCHANTMENTS) {
            if (!enchantment.equals(other) && !enchantment.isCompatibleWith(other)) {
                list.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(other, other.getMaxLevel())));
            }
        }
        UnCompatibleEnchantBooksCache.put(enchantment, list);
        return list;
    }

    public static boolean applyAtEnchantingTable(Enchantment enchantment) {
        if (ApplyAtEnchantingTableCache.containsKey(enchantment)) {
            return ApplyAtEnchantingTableCache.get(enchantment);
        }
        for (ItemStack stack : CanEnchantStackList) {
            if (enchantment.canApplyAtEnchantingTable(stack)) {
                ApplyAtEnchantingTableCache.put(enchantment, true);
                return true;
            }
        }
        ApplyAtEnchantingTableCache.put(enchantment, false);
        return false;
    }
}
