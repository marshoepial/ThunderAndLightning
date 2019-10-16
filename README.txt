    __    _       __    __        _             ______           ______ 
   / /   (_)___ _/ /_  / /_____  (_)___  ____ _/ ____/________ _/ __/ /_
  / /   / / __ `/ __ \/ __/ __ \/ / __ \/ __ `/ /   / ___/ __ `/ /_/ __/
 / /___/ / /_/ / / / / /_/ / / / / / / / /_/ / /___/ /  / /_/ / __/ /_  
/_____/_/\__, /_/ /_/\__/_/ /_/_/_/ /_/\__, /\____/_/   \__,_/_/  \__/  
        /____/                        /____/                            

                              LightningCraft
                       ~~A Mod For Minecraft 1.14.4~~

Features:
   -Lightning Attractor Blocks - Attract lightning!
         =Power generation and crafting
         =Different tiers with seperate modifiers - plenty of progression!
   -Jei Integration - know what you can make and how!
   -Many config options - modify the mod to fit your pack!
Todo:
   -Storm Furnace - create thunderstorms at your own will!
   -Lightning Wand - Attack others with the power of storms!
   
The Basics:
   -Getting Lightning to Strike your Attractor
      =The chance of your lightning attractor block being struck is based on its tier and its modifier.
         +Tiers: Creative = 100%, Iron = 50%, Diamond = 70%, Wool = 50%
      =Modifiers are set by how you craft the Tempestuous Blend, and modify the tier-set chance value.
         +Blend Modifiers: With Glowstone = 100%, Without Glowstone = 50%, All Dyes = 25%
         +E.G. If you crafted an Iron Lightning Attractor with a Tempestuous Blend with a 50% modifier, its lightning strike chance will be 25%.
   -Lightning Attractor Transformation
      =Place the blocks or items you want to change on top of a lightning attractor block. When lightning strikes it, the blocks or items will transform into the other items specified by the base or added recipes.
      =If the recipe has a "Max Stack" option, only that number of items will be transformed on a lightning strike. Any extra items will be knocked off the attractor.
   -Lightning Attractor Power
      =If no items are present on top of the attractor, the attractor will instead internalize the power of the strike as Forge Energy (FE).
      =Depending on which tier attractor and tier Tempestuous Blend you use, different amounts of energy will be internalized.
      =Over a short period of time, if the energy is not used, it will dissipate into the air.

Configs:

There are two config files in this mod. The first, lightningcraft.conf, is used for modifying general values. Currently it is used for modifying the base values for each lightning attractor tier. The second, recipes.json, is used to modify the lightning attractor recipes. Its base structure is as such:
[
    {
      "initial": "minecraft:redstone",
      "final": "minecraft:glowstone_dust",
      "maxRepeat": 16
    }
]
where "initial" is contains the beginning item or block id, "final" contains the final item or block id, and "maxRepeat" is how many items that can be transformed. The initial item can be one that already exists in the base mod, and then the recipe would be overrided, and the final item can be "minecraft:air", meaning that the recipe would be removed.
