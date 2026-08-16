# Druidcraft Regrown

A Minecraft mod adding hemp farming, crates, bedrolls, rope climbing, beams/platforms,
darkwood forest worldgen, and (on newer branches) tameable creatures, shields, and more —
built around a cozy, druidic-fantasy aesthetic.

This repository is a continuation/port of **Druidcraft Regrown**, originally created by
**VulpTheHorseDog** (later renamed VulpTheKangaFox). The original repository and CurseForge
page are no longer available and no forks were found, so this project was reconstructed by
decompiling the last published jar (`druidcraftrg-0.0.12`, 1.18.2) and porting it forward to
newer Minecraft versions.

## License

LGPL 2.1, inherited from the original mod. See [LICENSE](LICENSE). All credit for the original
design, content, and code goes to VulpTheHorseDog.

## Branches

| Branch | Minecraft | Loader | Status |
|---|---|---|---|
| `main` | 1.20.1 | Forge | Complete — all milestones (M1–M7) ported and deployed |
| `1.21.1-neoforge` | 1.21.1 | NeoForge | In progress |

## Building

Requires the Java version matching the branch's Minecraft version (Java 17 for 1.20.1 Forge,
Java 21 for 1.21.1 NeoForge — see `gradle.properties`).

```bash
./gradlew build
```

### Third-party jars (`1.21.1-neoforge` branch only)

The NeoForge branch's `libs/` folder holds local copies of TerraBlender and JEI used as an
offline dependency workaround during porting; those jars aren't redistributed in this repo.
Download them yourself and drop them in `libs/` before building:

- TerraBlender (NeoForge, 1.21.1): `com.github.glitchfiend:TerraBlender-neoforge:1.21.1-4.1.0.8`
- JEI (NeoForge, 1.21.1): `mezz.jei:jei-1.21.1-neoforge:19.27.0.336`

`build.gradle` has a comment marking where to swap these for proper Maven coordinates once a
suitable repository is confirmed available.
