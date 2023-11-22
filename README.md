# FabriZoom

![modrinth badge](https://img.shields.io/modrinth/dt/pNFKDyna?label=Modrinth)

[Modrinth](https://modrinth.com/mod/fabrizoom/)

A simple, lightweight, configurable, cross-platform zoom mod for Minecraft.

## What?
Fork of OkZoomer v4, updated to modern Minecraft, rewritten in Kotlin, moved to different (actually maintained) GUI and
configuration libraries, and ported to (Neo)Forge using Architectury. 

This mod adds a zoom key (by default, `c`) that allows the user to zoom in their view, and is highly configurable.
If you don't like the default "feel" of the zoom, you can configure it to be however you want.

## Forge? NeoForge?
Despite the name, this mod ***does*** support both Forge and NeoForge since version `2.0.0`.
**However, it is not as well tested** and may have issues or bugs not present in the Fabric version,
so be sure to report any issues you come across.

Also, **FabriZoom is looking for a new name!**. Since the mod now supports Forge, it doesn't make sense to have Fabric in the name. 
If you have any ideas, please let me know!

## Requirements
- All Platforms
    - [Minecraft](https://minecraft.net) `1.20.2`
    - [YACL](https://github.com/isXander/yetanotherconfiglib) `3.*`
- Fabric Only
    - [Fabric Loader](https://fabricmc.net/) `0.14.*` (or the equivalent [Quilt](https://quiltmc.org/) version)
    - [ModMenu](https://github.com/TerraformersMC/ModMenu) `8.*`
    - [Fabric Language Kotlin](https://github.com/FabricMC/fabric-language-kotlin) `1.10.*` (or the
      equivalent [QKL](https://modrinth.com/mod/qkl) version)
    - [Fabric API](https://modrinth.com/mod/fabric-api) `0.90.*` (or the
      equivalent [QFAPI](https://modrinth.com/mod/qsl) version)
- (Neo)Forge Only
    - [Forge](https://files.minecraftforge.net/net/minecraftforge/forge/) `48.*`
      or [NeoForge](https://neoforged.net/) `20.*`
    - [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge) `4.6.*`

## Future Plans
- New name
- Scaling mouse sensitivity
- Add setting explanation gifs to config menu
- Pick a better default keybind, since it conflicts

Feel free to suggest new features or report bugs on [the GitHub Issues page](https://github.com/trainb0y/fabrizoom/issues).

## Contributing
All contributions are welcome, including translations. [The source code](https://github.com/trainb0y/fabrizoom) is
licensed under the MIT license.

Thanks
to [Configurate](https://github.com/SpongePowered/Configurate), [SpruceUI](https://github.com/LambdAurora/SpruceUI),
and [YACL](https://github.com/isXander/YetAnotherConfigLib) for being excellent libraries.
Also [Devin's Badges](https://github.com/intergrav/devins-badges) are great.
Some inspiration taken from the excellent [Zoomify](https://github.com/isXander/Zoomify) mod though I've never intentionally used its code.


## Links
[![~fabric~](https://raw.githubusercontent.com/intergrav/devins-badges/main/badges/fabric_64h.png)](https://modrinth.com/mod/fabrizoom/)
[![~forge~](https://raw.githubusercontent.com/intergrav/devins-badges/main/badges/forge_64h.png)](https://modrinth.com/mod/fabrizoom/)
[![~quilt~](https://raw.githubusercontent.com/intergrav/devins-badges/main/badges/quilt_64h.png)](https://modrinth.com/mod/fabrizoom/)
[![~modrinth~](https://raw.githubusercontent.com/intergrav/devins-badges/main/badges/modrinth_64h.png)](https://modrinth.com/mod/fabrizoom/)
[![~github~](https://raw.githubusercontent.com/intergrav/devins-badges/main/badges/github-repository_64h.png)](https://github.com/trainb0y/fabrizoom)
