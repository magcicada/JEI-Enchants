package net.yiran.jei_enchants.jei.recipes;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
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
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.yiran.jei_enchants.Config;
import net.yiran.jei_enchants.JeiEnchants;
import net.yiran.jei_enchants.jei.JeiEnchantsPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("removal")
public class EnchantJeiRecipe extends AbstractRecipeCategory<Holder<Enchantment>> {
    @SuppressWarnings({"unchecked"})
    public static final RecipeType<Holder<Enchantment>> recipeType = RecipeType.create(JeiEnchants.MODID, "enchant", (Class<Holder<Enchantment>>) (Object) Holder.class);
    public static Map<Holder<Enchantment>, List<ItemStack>> EnchantBooksCache = new Object2ObjectOpenHashMap<>();
    public static Map<Holder<Enchantment>, List<ItemStack>> CanEnchantItemCache = new Object2ObjectOpenHashMap<>();
    public static Map<Holder<Enchantment>, List<ItemStack>> UnCompatibleEnchantBooksCache = new Object2ObjectOpenHashMap<>();
    public static Map<Holder<Enchantment>, Boolean> ApplyAtEnchantingTableCache = new Object2ObjectOpenHashMap<>();
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
    public void setRecipe(IRecipeLayoutBuilder builder, Holder<Enchantment> enchantment, IFocusGroup iFocusGroup) {
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
                        list.set(0, Component.translatable("jei_enchants.unCompatible.tooltip", enchantment.value().description()));
                    });
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, Holder<Enchantment> enchantment, IFocusGroup focuses) {
        Enchantment enchant = enchantment.value();
        MutableComponent name = enchant.description().copy();
        if (enchant.getMaxLevel() == 1) {
            addText(builder, 18, 0, name);
        } else {
            addText(builder, 18, 0, name.append(I18n.get("jei_enchants.level.title", enchant.getMaxLevel())));
        }
        int height = 0;
        if (Config.enableModId.get()) {
            addText(builder, 18, height += 10,
                    Component.literal(enchantment.unwrapKey().get().location().getNamespace()).withStyle(ChatFormatting.BLUE));
        }
        if (Config.enableWeight.get()) {
            addText(builder, 18, height += 10, Component.translatable("jei_enchants.weight.desc", enchant.getWeight()));

            List<FormattedText> weightTooltip = new ArrayList<>();
            weightTooltip.add(Component.translatable("jei_enchants.weight.tooltip", enchant.getWeight()));
            for (int i = 1; i <= enchant.getMaxLevel(); i++) {
                weightTooltip.add(Component.translatable("jei_enchants.level.cost.tooltip", i, enchant.getMinCost(i), enchant.getMaxCost(i)));
            }
            addTooltips(builder, 18, height, weightTooltip);
        }
        if (Config.enableIsTradeable.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.isTradeable.desc", getBooleanWrapper(enchantment.is(EnchantmentTags.TRADEABLE))));
        }
        if (Config.enableApplyAtEnchantingTable.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.applyAtEnchantingTable.desc", getBooleanWrapper(applyAtEnchantingTable(enchantment))));
        }
        if (Config.enableIsDiscoverable.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.isDiscoverable.desc", getBooleanWrapper(enchantment.is(EnchantmentTags.ON_RANDOM_LOOT))));
        }
        if (Config.enableIsTreasure.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.isTreasure.desc", getBooleanWrapper(enchantment.is(EnchantmentTags.TREASURE))));
        }
        if (Config.enableIsCurse.get()) {
            addText(builder, 18, height += 10,
                    Component.translatable("jei_enchants.isCurse.desc", getBooleanWrapper(enchantment.is(EnchantmentTags.CURSE))));
        }
        if (Config.enableEnchantmentDesc.get()) {
            height += 10;
            int xOffset = 0;
            if (height < 54) {
                xOffset = 18;
            }
            String descKey = enchantment.unwrapKey().map(ResourceKey::location).map(location -> location.toLanguageKey("enchantment") + ".desc").orElse("");
            if (!descKey.isEmpty() && I18n.exists(descKey)) {
                builder.addText(
                                Minecraft.getInstance().font.getSplitter().splitLines(
                                        Component.translatable(descKey).withStyle(ChatFormatting.ITALIC),
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

    public static List<ItemStack> getAllEnchantedBooks(Holder<Enchantment> enchantment) {
        if (EnchantBooksCache.containsKey(enchantment)) {
            return EnchantBooksCache.get(enchantment);
        }
        List<ItemStack> list = new ArrayList<>();
        for (int i = 1; i <= enchantment.value().getMaxLevel(); i++) {
            list.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i)));
        }
        EnchantBooksCache.put(enchantment, list);
        return list;
    }

    public static List<ItemStack> getCanEnchantItem(Holder<Enchantment> enchantment) {
        if (CanEnchantItemCache.containsKey(enchantment)) {
            return CanEnchantItemCache.get(enchantment);
        }
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack stack : CanEnchantStackList) {
            if (stack.supportsEnchantment(enchantment)) {
                list.add(stack);
            }
        }
        CanEnchantItemCache.put(enchantment, list);
        return list;
    }

    public static List<ItemStack> getUnCompatibleEnchantBook(Holder<Enchantment> enchantment) {
        if (UnCompatibleEnchantBooksCache.containsKey(enchantment)) {
            return UnCompatibleEnchantBooksCache.get(enchantment);
        }
        List<ItemStack> list = new ArrayList<>();
        for (Holder<Enchantment> other : JeiEnchantsPlugin.ENCHANTMENTSLIST) {
            if (!enchantment.equals(other) && !Enchantment.areCompatible(enchantment, other)) {
                list.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(other, other.value().getMaxLevel())));
            }
        }
        UnCompatibleEnchantBooksCache.put(enchantment, list);
        return list;
    }

    public static boolean applyAtEnchantingTable(Holder<Enchantment> enchantment) {
        if (ApplyAtEnchantingTableCache.containsKey(enchantment)) {
            return ApplyAtEnchantingTableCache.get(enchantment);
        }
        boolean result = enchantment.is(EnchantmentTags.IN_ENCHANTING_TABLE) && hasPrimaryEnchantableItem(enchantment);
        ApplyAtEnchantingTableCache.put(enchantment, result);
        return result;
    }

    private static boolean hasPrimaryEnchantableItem(Holder<Enchantment> enchantment) {
        for (ItemStack stack : CanEnchantStackList) {
            if (stack.isPrimaryItemFor(enchantment)) {
                return true;
            }
        }
        return false;
    }
}
