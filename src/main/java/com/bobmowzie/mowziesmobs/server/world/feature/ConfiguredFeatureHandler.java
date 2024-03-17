package com.bobmowzie.mowziesmobs.server.world.feature;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.FrostmawStructure;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

public class ConfiguredFeatureHandler {
    public static Holder<Structure> CONFIGURED_FROSTMAW;
    public static Holder<StructureSet> FROSTMAWS;
    public static Holder<StructureSet> MONASTERIES;

    public static BiomeChecker FROSTMAW_BIOME_CHECKER;
    public static final Set<Holder<Biome>> FROSTMAW_BIOMES = new HashSet<>();

    private static ResourceKey<Structure> createStructureKey(String name) {
        return ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, name));
    }

    private static ResourceKey<StructureSet> createSetKey(String name) {
        return ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, new ResourceLocation(MowziesMobs.MODID, name));
    }

    private static Holder<Structure> register(ResourceKey<Structure> key, Structure feature) {
        return BuiltinRegistries.register(BuiltinRegistries.STRUCTURES, key, feature);
    }

    static Holder<StructureSet> register(ResourceKey<StructureSet> key, StructureSet set) {
        return BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, key, set);
    }

    static Holder<StructureSet> register(ResourceKey<StructureSet> key, Holder<Structure> configuredFeature, StructurePlacement placement) {
        return register(key, new StructureSet(configuredFeature, placement));
    }

    public static void registerConfiguredFeatures() {
    	CONFIGURED_FROSTMAW = register(createStructureKey("frostmaw_spawn"), new FrostmawStructure(structure(TagHandler.HAS_MOWZIE_STRUCTURE, TerrainAdjustment.BEARD_THIN)));

        FROSTMAWS = register(createSetKey("frostmaw_spawns"), CONFIGURED_FROSTMAW, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23317578));
    }
    
    private static HolderSet<Biome> biomes(TagKey<Biome> p_236537_) {
        return BuiltinRegistries.BIOME.getOrCreateTag(p_236537_);
    }
    
    private static Structure.StructureSettings structure(TagKey<Biome> p_236546_, Map<MobCategory, StructureSpawnOverride> p_236547_, GenerationStep.Decoration p_236548_, TerrainAdjustment p_236549_) {
        return new Structure.StructureSettings(biomes(p_236546_), p_236547_, p_236548_, p_236549_);
    }
    
    private static Structure.StructureSettings structure(TagKey<Biome> p_236543_, TerrainAdjustment p_236544_) {
        return structure(p_236543_, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, p_236544_);
    }

    public static void addBiomeSpawns(Holder<Biome> biomeKey) {
        if (FROSTMAW_BIOME_CHECKER == null) FROSTMAW_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get() >= 0 && FROSTMAW_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added frostmaw biome: " + biomeName.toString());
            FROSTMAW_BIOMES.add(biomeKey);
        }
    }
}
