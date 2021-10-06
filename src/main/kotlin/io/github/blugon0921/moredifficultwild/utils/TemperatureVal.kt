package io.github.blugon0921.moredifficultwild.utils

import org.bukkit.Material
import org.bukkit.block.Biome

class TemperatureVal {

    companion object {
        val extremecold_biom =
            arrayListOf(
                Biome.SNOWY_TAIGA,
                Biome.SNOWY_TAIGA_HILLS,
                Biome.SNOWY_TAIGA_MOUNTAINS,
                Biome.FROZEN_RIVER,
                Biome.SNOWY_TUNDRA,
                Biome.SNOWY_MOUNTAINS,
                Biome.ICE_SPIKES,
                Biome.SNOWY_BEACH,
                Biome.FROZEN_OCEAN,
                Biome.DEEP_FROZEN_OCEAN
            )

        val cold_biom =
            arrayListOf(
                Biome.MOUNTAINS,
                Biome.WOODED_MOUNTAINS,
                Biome.GRAVELLY_MOUNTAINS,
                Biome.MODIFIED_GRAVELLY_MOUNTAINS,
                Biome.STONE_SHORE,
                Biome.TAIGA,
                Biome.TAIGA_HILLS,
                Biome.TAIGA_MOUNTAINS,
                Biome.GIANT_TREE_TAIGA,
                Biome.GIANT_TREE_TAIGA_HILLS,
                Biome.GIANT_SPRUCE_TAIGA,
                Biome.GIANT_SPRUCE_TAIGA_HILLS,
                Biome.COLD_OCEAN,
                Biome.DEEP_COLD_OCEAN
            )

        val temperate_biom =
            arrayListOf(
                Biome.RIVER,
                Biome.FOREST,
                Biome.WOODED_HILLS,
                Biome.FLOWER_FOREST,
                Biome.BIRCH_FOREST,
                Biome.BIRCH_FOREST_HILLS,
                Biome.TALL_BIRCH_FOREST,
                Biome.TALL_BIRCH_HILLS,
                Biome.DARK_FOREST,
                Biome.DARK_FOREST_HILLS,
                Biome.PLAINS,
                Biome.SUNFLOWER_PLAINS,
                Biome.SWAMP,
                Biome.SWAMP_HILLS,
                Biome.BEACH,
                Biome.MUSHROOM_FIELDS,
                Biome.MUSHROOM_FIELD_SHORE,
                Biome.OCEAN,
                Biome.DEEP_OCEAN
            )

        val tropical_biom =
            arrayListOf(
                Biome.JUNGLE,
                Biome.JUNGLE_EDGE,
                Biome.JUNGLE_HILLS,
                Biome.MODIFIED_JUNGLE,
                Biome.MODIFIED_JUNGLE_EDGE,
                Biome.BAMBOO_JUNGLE,
                Biome.BAMBOO_JUNGLE_HILLS,
                Biome.LUKEWARM_OCEAN,
                Biome.DEEP_LUKEWARM_OCEAN
            )

        val dry_biom =
            arrayListOf(
                Biome.SAVANNA,
                Biome.SAVANNA_PLATEAU,
                Biome.SHATTERED_SAVANNA,
                Biome.SHATTERED_SAVANNA_PLATEAU,
                Biome.DESERT,
                Biome.DESERT_HILLS,
                Biome.DESERT_LAKES,
                Biome.BADLANDS,
                Biome.BADLANDS_PLATEAU,
                Biome.MODIFIED_BADLANDS_PLATEAU,
                Biome.WOODED_BADLANDS_PLATEAU,
                Biome.MODIFIED_WOODED_BADLANDS_PLATEAU,
                Biome.ERODED_BADLANDS,
                Biome.WARM_OCEAN,
                Biome.DEEP_WARM_OCEAN
            )



        val temperatureUpBlocks =
            arrayListOf(
                Material.CAMPFIRE,
                Material.LAVA,
                Material.FIRE,
                Material.TORCH,
                Material.REDSTONE_TORCH,
                Material.LANTERN,
                Material.JACK_O_LANTERN,
                Material.GLOWSTONE,
                Material.SEA_LANTERN
            )
    }
}