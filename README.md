# JEI Enchants

A small client-side **NeoForge** mod that adds an **Enchantment Details** category to
[Just Enough Items (JEI)](https://www.curseforge.com/minecraft/mc-mods/jei), showing
detailed information about every enchantment in the current datapack.

**In JEI, open the "Enchantment Details" category and click any enchanted book or
enchantment to see its details.**

[中文说明](#中文说明)

---

## Requirements

| Dependency | Version |
|---|---|
| Minecraft | **1.21.1** |
| NeoForge | **21.1.233** or newer |
| JEI (1.21.1 - NeoForge) | **19.27.0.340** or newer in the 1.21.1 line |

This mod is **client-side only** in practice: it only adds JEI UI. It does not declare a
hard dependency on JEI, so the game still loads without it (the JEI category simply
won't appear).

## Features

Each enchantment page shows:

- **Name + level range** (e.g. `Sharpness  1 - 5`).
- **Mod source** - the namespace of the enchantment (e.g. `minecraft`).
- **Weight** - enchanting table weight, with a tooltip listing the min/max level costs
  for every level (`getMinCost` / `getMaxCost`).
- **Enchanting table** - whether the enchantment can be obtained from an enchanting
  table (checks the `#minecraft:enchantment/in_enchanting_table` tag and whether any
  enchantable item is a *primary* item for it).
- **Discoverable** - whether the enchantment appears on random loot
  (checks `#minecraft:enchantment/on_random_loot`).
- **Villagers** - whether the enchantment is tradable
  (checks `#minecraft:enchantment/tradeable`).
- **Treasure** - checks `#minecraft:enchantment/treasure`.
- **Curse** - checks `#minecraft:enchantment/curse`.
- **Description** - shows `enchantment.<namespace>.<path>.desc` translation keys if
  present (e.g. `enchantment.minecraft.sharpness.desc`), otherwise "No description
  available".
- **Enchantable items** - all items this enchantment can be applied to.
- **Conflicting enchantments** - enchanted books of incompatible enchantments
  (uses `Enchantment.areCompatible` / exclusive sets).

All enchantment checks are **tag based**, so datapack-added enchantments and tags are
picked up automatically.

## Configuration

Client config file: `config/jei-enchants-client.toml`.

| Key | Default | Description |
|---|---|---|
| `enchantPageHeight` | `120` | Height of the enchantment page in JEI |
| `element.enableModId` | `true` | Show the mod source line |
| `element.enableWeight` | `true` | Show weight and level cost tooltip |
| `element.enableApplyAtEnchantingTable` | `true` | Show the enchanting table line |
| `element.enableIsDiscoverable` | `true` | Show the random loot line |
| `element.enableIsTradeable` | `true` | Show the villager trade line |
| `element.enableIsTreasure` | `true` | Show the treasure line |
| `element.enableIsCurse` | `true` | Show the curse line |
| `element.enableEnchantmentDesc` | `true` | Show the description line |

## Building from source

Requirements: JDK 21 and an internet connection (first build downloads
ModDevGradle / NeoForge / Minecraft / JEI artifacts).

```
gradlew build
```

The built jar is written to `build/libs/JEI Enchants-1.21.1-1.0.0.jar`.

Development settings (`gradle.properties`):

- `minecraft_version=1.21.1`, `neo_version=21.1.233`
- `parchment_mappings_version=2024.11.17`
- `jei_version=19.27.0.340`
- `mod_version=1.21.1-1.0.0`

Run the dev client with:

```
gradlew runClient
```

## How it works (technical notes)

- In 1.21.1 the enchantment registry is **datapack-driven** and lives in
  `Registries.ENCHANTMENT`; the plugin obtains it from the client connection's
  `RegistryAccess` (`registryOrThrow(Registries.ENCHANTMENT)`) and registers every
  `Holder<Enchantment>` as a JEI recipe.
- Info rows such as treasure/curse/tradeable/enchanting-table are derived from
  vanilla enchantment tags (`EnchantmentTags`), which also covers enchantments added
  by datapacks and other mods.
- Enchanted book previews are built with
  `EnchantedBookItem.createForEnchantment(new EnchantmentInstance(holder, level))`.

## License

AGPL v3.0. Author: **_yi_ran_**.

## Links

- [Just Enough Items](https://www.curseforge.com/minecraft/mc-mods/jei)
- [JEI Maven](https://maven.blamejared.com/mezz/jei/)
- [NeoForge Docs](https://docs.neoforged.net/)
- [ModDevGradle](https://github.com/neoforged/ModDevGradle)

---

## 中文说明

**JEI Enchants** 是一个面向 **NeoForge** 的客户端小模组：在 JEI 中新增「附魔详情」
分类，展示数据包中所有附魔的详细信息。

- 环境：Minecraft **1.21.1** + NeoForge **21.1.233+** + JEI（1.21.1 NeoForge）**19.27.0.340+**
- 页面信息：等级范围、模组来源、权重与各级花费、是否可附魔台获取
（`#minecraft:enchantment/in_enchanting_table`）、是否随机战利品可获得
（`#minecraft:enchantment/on_random_loot`）、是否可交易（`tradeable`）、是否宝藏
（`treasure`）、是否诅咒（`curse`）、可选描述文本
（`enchantment.<namespace>.<path>.desc`）、可附魔物品、冲突附魔书。
- 所有判断均基于 1.21.1 的附魔数据标签，数据包/其他模组新增的附魔自动生效。
- 客户端配置：`config/jei-enchants-client.toml`（键名见上表）。
- 构建：JDK 21，执行 `gradlew build`，产物在
  `build/libs/JEI Enchants-1.21.1-1.0.0.jar`；开发运行 `gradlew runClient`。
- 许可证：AGPL v3.0，作者 **_yi_ran_**。
