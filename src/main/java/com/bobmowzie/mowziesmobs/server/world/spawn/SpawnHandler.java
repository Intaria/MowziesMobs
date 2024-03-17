package com.bobmowzie.mowziesmobs.server.world.spawn;

import java.util.HashMap;
import java.util.Map;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class SpawnHandler {
    public static BiomeChecker LANTERN_BIOME_CHECKER;
    public static BiomeChecker NAGA_BIOME_CHECKER;

    public static final Map<EntityType<?>, ConfigHandler.SpawnConfig> spawnConfigs = new HashMap<>();
    static {
        spawnConfigs.put(EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig);
        spawnConfigs.put(EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig);
    }

    public static void registerSpawnPlacementTypes() {
        SpawnPlacements.Type.create("MMSPAWN", new TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>>() {
            @Override
            public boolean test(LevelReader t, BlockPos pos, EntityType<? extends Mob> entityType) {
                BlockState block = t.getBlockState(pos.below());
                if (block.getBlock() == Blocks.BEDROCK || block.getBlock() == Blocks.BARRIER || !block.getMaterial().blocksMotion())
                    return false;
                BlockState iblockstateUp = t.getBlockState(pos);
                BlockState iblockstateUp2 = t.getBlockState(pos.above());
                return NaturalSpawner.isValidEmptySpawnBlock(t, pos, iblockstateUp, iblockstateUp.getFluidState(), entityType) && NaturalSpawner.isValidEmptySpawnBlock(t, pos.above(), iblockstateUp2, iblockstateUp2.getFluidState(), entityType);
            }
        });

        SpawnPlacements.Type mmSpawn = SpawnPlacements.Type.valueOf("MMSPAWN");
        if (mmSpawn != null) {
            SpawnPlacements.register(EntityHandler.LANTERN.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.NAGA.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        }
    }

    public static void addBiomeSpawns(Holder<Biome> biomeKey, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (LANTERN_BIOME_CHECKER == null) LANTERN_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.spawnRate.get() > 0 && LANTERN_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//              System.out.println("Added lantern biome: " + biomeName.toString());
            registerEntityWorldSpawn(builder, EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig, MobCategory.AMBIENT);
        }

        if (NAGA_BIOME_CHECKER == null) NAGA_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.spawnRate.get() > 0 && NAGA_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//              System.out.println("Added naga biome: " + biomeName.toString());
            registerEntityWorldSpawn(builder, EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig, MobCategory.MONSTER);
        }
    }

    private static void registerEntityWorldSpawn(ModifiableBiomeInfo.BiomeInfo.Builder builder, EntityType<?> entity, ConfigHandler.SpawnConfig spawnConfig, MobCategory classification) {
    	builder.getMobSpawnSettings().getSpawner(classification).add(new MobSpawnSettings.SpawnerData(entity, spawnConfig.spawnRate.get(), spawnConfig.minGroupSize.get(), spawnConfig.maxGroupSize.get()));
    }
}