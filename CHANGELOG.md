# Changelog — Druidcraft Regrown (NeoForge 1.21.1)

## 0.0.13-1.21.1

### Fixes
- **Woodcutter no longer crashes the game** when opened (infinite recursion in the screen's background rendering). Thanks to lunarrose for the report.
- **Ores drop their items again**: moonstone ore → moonstone, amber ore → amber, fiery glass ore → fiery glass, rockroot ore → rockroot. Silk touch still drops the ore block, and Fortune works.
- **Elder and darkwood leaves behave like vanilla leaves**: they only drop themselves when sheared or mined with silk touch. Decaying or broken leaves drop saplings and sticks as normal.
- **Composting works**: hemp, hemp seeds, elder/darkwood leaves and saplings, lavender, blueberries, elderberries, elderflower, blueberry muffin and apple elderberry crumble can all go in a composter.
- **Fiery glass torches are waterloggable in any water.** Placing one in flowing water, or letting water flow over one, waterlogs it instead of leaving an air bubble.
- **Fiery glass torch glow particle** no longer renders as a hard pink square. It's now a soft warm glow that also behaves with shader mods (Iris/Oculus).
- **Darkwood and elder doors** render correctly (they were missing their models).
- **Worldgen and spawns restored** in plains, coniferous and dense-vegetation biomes: hemp, elder trees, lavender, blueberries and beetles. These were pointing at biome tags that no longer exist in 1.21.
- **Elderflower cordial recipe** loads again.
- **Dreadfish loot table** loads again; Looting now increases its drops.
- Travel pack and beetle inventory screens no longer draw their background twice.
