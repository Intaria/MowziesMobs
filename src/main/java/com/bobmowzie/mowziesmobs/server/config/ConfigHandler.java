package com.bobmowzie.mowziesmobs.server.config;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ConfigHandler {
    private ConfigHandler() {}

    private static final String LANG_PREFIX = "config." + MowziesMobs.MODID + ".";

    public static final Common COMMON;
    public static final Client CLIENT;

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    private static final Predicate<Object> STRING_PREDICATE = s -> s instanceof String;
    private static final Predicate<Object> RESOURCE_LOCATION_PREDICATE = STRING_PREDICATE.and(s -> ResourceLocation.isValidResourceLocation((String) s));
    private static final Predicate<Object> BIOME_COMBO_PREDICATE = STRING_PREDICATE.and(s -> {
        String bigString = (String) s;
        String[] typeStrings = bigString.replace(" ", "").split("[,!]");
        for (String string : typeStrings) {
            if (!RESOURCE_LOCATION_PREDICATE.test(string)) {
                return false;
            }
        }
        return true;
    });
    private static final Predicate<Object> ITEM_NAME_PREDICATE = RESOURCE_LOCATION_PREDICATE.and(s -> ForgeRegistries.ITEMS.containsKey(new ResourceLocation((String) s)));

    static {
        COMMON = new Common(COMMON_BUILDER);
        CLIENT = new Client(CLIENT_BUILDER);

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    // Config templates
    public static class BiomeConfig {
        BiomeConfig(final ForgeConfigSpec.Builder builder, List<? extends String> biomeTags, List<? extends String> biomeWhitelist, List<? extends String> biomeBlacklist) {
            builder.push("biome_config");
            builder.comment("Mowzie's Mobs bosses cannot generate in modded or non-overworld biomes unless the biome is added to the 'has_structure/has_mowzie_structure' tag via a datapack!");
            this.biomeTags = builder.comment("Each entry is a combination of allowed biome tags or biome names.", "Separate types with commas to require biomes to have all tags in an entry", "Put a '!' before a biome tag to mean NOT that tag", "A blank entry means all biomes. No entries means no biomes.", "For example, 'minecraft:is_forest,forge:is_spooky,!forge:is_snowy' would mean all biomes that are spooky forests but not snowy forests", "'!minecraft:is_mountain' would mean all non-mountain biomes")
                    .translation(LANG_PREFIX + "biome_tags")
                    .defineList("biome_tags", biomeTags, BIOME_COMBO_PREDICATE);
            this.biomeWhitelist = builder.comment("Allow spawns in these biomes regardless of the biome tag settings")
                    .translation(LANG_PREFIX + "biome_whitelist")
                    .defineList("biome_whitelist", biomeWhitelist, BIOME_COMBO_PREDICATE);
            this.biomeBlacklist = builder.comment("Prevent spawns in these biomes regardless of the biome tag settings")
                    .translation(LANG_PREFIX + "biome_blacklist")
                    .defineList("biome_blacklist", biomeBlacklist, BIOME_COMBO_PREDICATE);
            builder.pop();
        }

        public final ConfigValue<List<? extends String>> biomeTags;

        public final ConfigValue<List<? extends String>> biomeWhitelist;

        public final ConfigValue<List<? extends String>> biomeBlacklist;
    }

    public static class SpawnConfig {
        SpawnConfig(final ForgeConfigSpec.Builder builder, int spawnRate, int minGroupSize, int maxGroupSize, double extraRarity, BiomeConfig biomeConfig, List<? extends String> allowedBlocks, List<? extends String> allowedBlockTags, int heightMax, int heightMin, boolean needsDarkness, boolean needsSeeSky, boolean needsCantSeeSky, List<String> avoidStructures) {
            builder.comment("Controls for vanilla-style mob spawning");
            builder.push("spawn_config");
            this.spawnRate = builder.comment("Smaller number causes less spawning, 0 to disable spawning")
                    .translation(LANG_PREFIX + "spawn_rate")
                    .defineInRange("spawn_rate", spawnRate, 0, Integer.MAX_VALUE);
            this.minGroupSize = builder.comment("Minimum number of mobs that appear in a spawn group")
                    .translation(LANG_PREFIX + "min_group_size")
                    .defineInRange("min_group_size", minGroupSize, 1, Integer.MAX_VALUE);
            this.maxGroupSize = builder.comment("Maximum number of mobs that appear in a spawn group")
                    .translation(LANG_PREFIX + "max_group_size")
                    .defineInRange("max_group_size", maxGroupSize, 1, Integer.MAX_VALUE);
            this.extraRarity = builder.comment("Probability of a spawn attempt succeeding. 1 for normal spawning, 0 will prevent spawning. Used to make mobs extra rare.")
                    .translation(LANG_PREFIX + "extra_rarity")
                    .defineInRange("extra_rarity", extraRarity, 0.0, 1.0);
            this.biomeConfig = biomeConfig;
            this.dimensions = builder.comment("Names of dimensions this mob can spawn in")
                    .translation(LANG_PREFIX + "dimensions")
                    .defineList("dimensions", Collections.singletonList("minecraft:overworld"), STRING_PREDICATE);
            this.allowedBlocks = builder.comment("Names of blocks this mob is allowed to spawn on. Leave blank to ignore block names.")
                    .translation(LANG_PREFIX + "allowed_blocks")
                    .defineList("allowed_blocks", allowedBlocks, STRING_PREDICATE);
            this.allowedBlockTags = builder.comment("Tags of blocks this mob is allowed to spawn on. Leave blank to ignore block tags.")
                    .translation(LANG_PREFIX + "allowed_block_tags")
                    .defineList("allowed_block_tags", allowedBlockTags, STRING_PREDICATE);
            this.heightMax = builder.comment("Maximum height for this spawn. -65 to ignore.")
                    .translation(LANG_PREFIX + "height_max")
                    .defineInRange("height_max", heightMax, -65, 256);
            this.heightMin = builder.comment("Minimum height for this spawn. -65 to ignore.")
                    .translation(LANG_PREFIX + "height_min")
                    .defineInRange("height_min", heightMin, -65, 256);
            this.needsDarkness = builder.comment("Set to true to only allow this mob to spawn in the dark, like zombies and skeletons.")
                    .translation(LANG_PREFIX + "needs_darkness")
                    .define("needs_darkness", needsDarkness);
            this.needsSeeSky = builder.comment("Set to true to only spawn mob if it can see the sky.")
                    .translation(LANG_PREFIX + "min_group_size")
                    .define("needs_see_sky", needsSeeSky);
            this.needsCantSeeSky = builder.comment("Set to true to only spawn mob if it can't see the sky.")
                    .translation(LANG_PREFIX + "min_group_size")
                    .define("needs_cant_see_sky", needsCantSeeSky);
            this.avoidStructures = builder.comment("Names of structures this mob will avoid spawning near.")
                    .translation(LANG_PREFIX + "avoid_structures")
                    .defineList("avoid_structures", avoidStructures, STRING_PREDICATE);
            builder.pop();
        }

        public final IntValue spawnRate;

        public final IntValue minGroupSize;

        public final IntValue maxGroupSize;

        public final DoubleValue extraRarity;

        public final BiomeConfig biomeConfig;

        public final ConfigValue<List<? extends String>> dimensions;

        public final IntValue heightMin;

        public final IntValue heightMax;

        public final BooleanValue needsDarkness;

        public final BooleanValue needsSeeSky;

        public final BooleanValue needsCantSeeSky;

        public final ConfigValue<List<? extends String>> allowedBlocks;

        public final ConfigValue<List<? extends String>> allowedBlockTags;

        public final ConfigValue<List<? extends String>> avoidStructures;
    }

    public static class GenerationConfig {
        GenerationConfig(final ForgeConfigSpec.Builder builder, int generationDistance, int generationSeparation, BiomeConfig biomeConfig, float heightMin, float heightMax, List<String> avoidStructures) {
            builder.comment("Controls for spawning structure/mob with world generation");
            builder.push("generation_config");
            this.generationDistance = builder.comment("Smaller number causes more generation, -1 to disable generation", "Maximum number of chunks between placements of this mob/structure")
                    .translation(LANG_PREFIX + "generation_distance")
                    .defineInRange("generation_distance", generationDistance, -1, Integer.MAX_VALUE);
            this.generationSeparation = builder.comment("Smaller number causes more generation, -1 to disable generation", "Minimum number of chunks between placements of this mob/structure")
                    .translation(LANG_PREFIX + "generation_separation")
                    .defineInRange("generation_separation", generationSeparation, -1, Integer.MAX_VALUE);
            this.biomeConfig = biomeConfig;
            this.heightMax = builder.comment("Maximum height for generation placement. -65 to ignore")
                    .translation(LANG_PREFIX + "height_max")
                    .defineInRange("height_max", heightMax, -65, 256);
            this.heightMin = builder.comment("Minimum height for generation placement. -65 to ignore")
                    .translation(LANG_PREFIX + "height_min")
                    .defineInRange("height_min", heightMin, -65, 256);
            this.avoidStructures = builder.comment("Names of structures this mob/structure will avoid when generating")
                    .translation(LANG_PREFIX + "avoid_structures")
                    .defineList("avoid_structures", avoidStructures, STRING_PREDICATE);
            builder.pop();
        }

        public final IntValue generationDistance;

        public final IntValue generationSeparation;

        public final BiomeConfig biomeConfig;

        public final DoubleValue heightMin;

        public final DoubleValue heightMax;

        public final ConfigValue<List<? extends String>> avoidStructures;
    }

    public static class CombatConfig {
        CombatConfig(final ForgeConfigSpec.Builder builder, float healthMultiplier, float attackMultiplier) {
            builder.push("combat_config");
            this.healthMultiplier = builder.comment("Scale mob health by this value")
                    .translation(LANG_PREFIX + "health_multiplier")
                    .defineInRange("health_multiplier", healthMultiplier, 0d, Double.MAX_VALUE);
            this.attackMultiplier = builder.comment("Scale mob attack damage by this value")
                    .translation(LANG_PREFIX + "attack_multiplier")
                    .defineInRange("attack_multiplier", attackMultiplier, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue healthMultiplier;

        public final DoubleValue attackMultiplier;
    }

    public static class ToolConfig {
        ToolConfig(final ForgeConfigSpec.Builder builder, float attackDamage, float attackSpeed) {
            builder.push("tool_config");
            this.attackDamage = builder.comment("Tool attack damage")
                    .translation(LANG_PREFIX + "attack_damage")
                    .defineInRange("attack_damage", attackDamage, 0d, Double.MAX_VALUE);
            this.attackSpeed = builder.comment("Tool attack speed")
                    .translation(LANG_PREFIX + "attack_speed")
                    .defineInRange("attack_speed", attackSpeed, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue attackDamage;
        
        public float attackDamageValue = 9;
        public float attackSpeedValue = 0.9F;

        public final DoubleValue attackSpeed;
    }

    public static class ArmorConfig {
        ArmorConfig(final ForgeConfigSpec.Builder builder, int damageReduction, float toughness) {
            builder.push("armor_config");
            this.damageReduction = builder.comment("See official Minecraft Wiki for an explanation of how armor damage reduction works.")
                    .translation(LANG_PREFIX + "damage_reduction")
                    .defineInRange("damage_reduction", damageReduction, 0, Integer.MAX_VALUE);
            this.toughness = builder.comment("See official Minecraft Wiki for an explanation of how armor toughness works.")
                    .translation(LANG_PREFIX + "toughness")
                    .defineInRange("toughness", toughness, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final IntValue damageReduction;
        
        public int damageReductionValue = ArmorMaterials.IRON.getDefenseForSlot(EquipmentSlot.HEAD);
        public float toughnessValue = ArmorMaterials.IRON.getToughness();

        public final DoubleValue toughness;
    }

    public static class Naga {
        Naga(final ForgeConfigSpec.Builder builder) {
            builder.push("naga");
            spawnConfig = new SpawnConfig(builder,
                    55, 2, 4, 1,
                    new BiomeConfig(builder, Arrays.asList("minecraft:is_beach,minecraft:is_mountain", "minecraft:is_beach,minecraft:is_hill"), Collections.singletonList("minecraft:stony_shore"), Collections.emptyList()),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    -65, 70, false, true, false,
                    Arrays.asList("minecraft:villages", "minecraft:pillager_outposts")
            );
            combatConfig = new CombatConfig(builder,1, 1);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final CombatConfig combatConfig;
    }

    public static class Lantern {
        Lantern(final ForgeConfigSpec.Builder builder) {
            builder.push("lantern");
            spawnConfig = new SpawnConfig(builder,
                    5, 2, 4, 1,
                    new BiomeConfig(builder, Collections.singletonList("minecraft:is_forest,mowziesmobs:is_magical,!forge:is_snowy"), Collections.emptyList(), Collections.emptyList()),
                    Collections.emptyList(),
                    Arrays.asList("minecraft:valid_spawn", "minecraft:leaves", "minecraft:logs"),
                    -65, 60, true, false, false,
                    Collections.emptyList()
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            builder.pop();
        }

        public final SpawnConfig spawnConfig;

        public final CombatConfig combatConfig;
    }

    public static class Frostmaw {
        Frostmaw(final ForgeConfigSpec.Builder builder) {
            builder.push("frostmaw");
            generationConfig = new GenerationConfig(builder, 35, 16,
                    new BiomeConfig(builder, Collections.singletonList("forge:is_snowy,!minecraft:is_ocean,!minecraft:is_river,!minecraft:is_beach,!minecraft:is_forest,!minecraft:is_taiga"), Collections.emptyList(), Collections.emptyList()),
                    50, 100,
                    Arrays.asList("minecraft:villages", "minecraft:pillager_outposts")
            );
            combatConfig = new CombatConfig(builder, 1, 1);
            this.hasBossBar = builder.comment("Disable/enable Frostmaw's boss health bar")
                    .translation(LANG_PREFIX + "has_boss_bar")
                    .define("has_boss_bar", true);
            this.healsOutOfBattle = builder.comment("Disable/enable frostmaws healing while asleep")
                    .translation(LANG_PREFIX + "heals_out_of_battle")
                    .define("heals_out_of_battle", true);
            this.stealableIceCrystal = builder.comment("Allow players to steal frostmaws' ice crystals (only using specific means!)")
                    .translation(LANG_PREFIX + "stealable_ice_crystal")
                    .define("stealable_ice_crystal", true);
            this.resetHealthWhenRespawn = builder.comment("Disable/enable frostmaws resetting health when a player respawns nearby. (Prevents respawn cheese!)")
                    .translation(LANG_PREFIX + "reset_health_when_respawn")
                    .define("reset_health_when_respawn", true);
            builder.pop();
        }

        public final GenerationConfig generationConfig;

        public final CombatConfig combatConfig;

        public final BooleanValue stealableIceCrystal;

        public final BooleanValue hasBossBar;

        public final BooleanValue healsOutOfBattle;

        public final BooleanValue resetHealthWhenRespawn;
    }

    public static class UmvuthanaMask {
        UmvuthanaMask(final ForgeConfigSpec.Builder builder) {
            builder.push("umvuthana_mask");
            armorConfig = new ArmorConfig(builder, ArmorMaterials.LEATHER.getDefenseForSlot(EquipmentSlot.HEAD), ArmorMaterials.LEATHER.getToughness());
            builder.pop();
        }

        public final ArmorConfig armorConfig;
    }

    public static class IceCrystal {
        IceCrystal(final ForgeConfigSpec.Builder builder) {
            builder.push("ice_crystal");
            attackMultiplier = builder.comment("Multiply all damage done with the ice crystal by this amount.")
                    .translation(LANG_PREFIX + "attack_multiplier")
                    .defineInRange("attack_multiplier", 1f, 0d, Double.MAX_VALUE);
            breakable = builder.comment("Set to true for the ice crystal to have limited durability.", "Prevents regeneration in inventory.")
                    .translation(LANG_PREFIX + "breakable")
                    .define("breakable", false);
            durability = builder.comment("Ice crystal durability")
                    .translation(LANG_PREFIX + "durability")
                    .defineInRange("durability", 600, 1, Integer.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue attackMultiplier;

        public final BooleanValue breakable;

        public final IntValue durability;
        public int durabilityValue;
    }

    public static class NagaFangDagger {
        NagaFangDagger(final ForgeConfigSpec.Builder builder) {
            builder.push("naga_fang_dagger");
            toolConfig = new ToolConfig(builder, 3, 2);
            poisonDuration = builder.comment("Duration in ticks of the poison effect (20 ticks = 1 second).")
                    .translation(LANG_PREFIX + "poison_duration")
                    .defineInRange("poison_duration", 40, 0, Integer.MAX_VALUE);
            backstabDamageMultiplier = builder.comment("Damage multiplier when attacking from behind")
                    .translation(LANG_PREFIX + "backstab_damage_mult")
                    .defineInRange("backstab_damage_mult", 2f, 0d, Double.MAX_VALUE);
            builder.pop();
        }

        public final ToolConfig toolConfig;

        public final IntValue poisonDuration;

        public final DoubleValue backstabDamageMultiplier;
    }

    public static class Blowgun {
        Blowgun(final ForgeConfigSpec.Builder builder) {
            builder.push("blowgun");
            poisonDuration = builder.comment("Duration in ticks of the poison effect (20 ticks = 1 second).")
                    .translation(LANG_PREFIX + "poison_duration")
                    .defineInRange("poison_duration", 40, 0, Integer.MAX_VALUE);
            attackDamage = builder.comment("Multiply all damage done with the blowgun/darts by this amount.")
                    .translation(LANG_PREFIX + "attack_damage")
                    .defineInRange("attack_damage", 1d, 0, Double.MAX_VALUE);
            builder.pop();
        }

        public final DoubleValue attackDamage;

        public final IntValue poisonDuration;
    }

    public static class Mobs {
        Mobs(final ForgeConfigSpec.Builder builder) {
            builder.push("mobs");
            FROSTMAW = new Frostmaw(builder);
            LANTERN = new Lantern(builder);
            NAGA = new Naga(builder);
            builder.pop();
        }

        public final Frostmaw FROSTMAW;

        public final Lantern LANTERN;

        public final Naga NAGA;

    }

    public static class ToolsAndAbilities {
        ToolsAndAbilities(final ForgeConfigSpec.Builder builder) {
            builder.push("tools_and_abilities");
            geomancyAttackMultiplier = builder.translation(LANG_PREFIX + "geomancy_attack_multiplier")
                    .defineInRange("geomancy_attack_multiplier", 1f, 0, Double.MAX_VALUE);
            ICE_CRYSTAL = new IceCrystal(builder);
            UMVUTHANA_MASK = new UmvuthanaMask(builder);
            NAGA_FANG_DAGGER = new NagaFangDagger(builder);
            BLOW_GUN = new Blowgun(builder);
            builder.pop();
        }

        public final DoubleValue geomancyAttackMultiplier;

        public final IceCrystal ICE_CRYSTAL;

        public final UmvuthanaMask UMVUTHANA_MASK;

        public final NagaFangDagger NAGA_FANG_DAGGER;

        public final Blowgun BLOW_GUN;
    }

    public static class Client {
        private Client(final ForgeConfigSpec.Builder builder) {
            builder.push("client");
            this.glowEffect = builder.comment("Toggles the lantern glow effect, which may look bad with certain shaders.")
                    .translation(LANG_PREFIX + "glow_effect")
                    .define("glow_effect", true);
            this.doCameraShakes = builder.comment("Enable camera shaking during certain mob attacks and abilities.")
                    .translation(LANG_PREFIX + "do_camera_shake")
                    .define("do_camera_shake", true);
            this.playBossMusic = builder.comment("Play boss battle themes during boss encounters.")
                    .translation(LANG_PREFIX + "play_boss_music")
                    .define("play_boss_music", true);
            this.customBossBars = builder.comment("Use custom boss health bar textures, if the boss has them.")
                    .translation(LANG_PREFIX + "custom_boss_bar")
                    .define("custom_boss_bar", true);
            this.customPlayerAnims = builder.comment("Use custom player animations.")
                    .translation(LANG_PREFIX + "custom_player_anims")
                    .define("custom_player_anims", true);
            builder.pop();
        }

        public final BooleanValue glowEffect;

        public final BooleanValue doCameraShakes;

        public final BooleanValue playBossMusic;

        public final BooleanValue customBossBars;

        public final BooleanValue customPlayerAnims;
    }

    public static class Common {
        private Common(final ForgeConfigSpec.Builder builder) {
            TOOLS_AND_ABILITIES = new ToolsAndAbilities(builder);
            MOBS = new Mobs(builder);
        }

        public final ToolsAndAbilities TOOLS_AND_ABILITIES;

        public final Mobs MOBS;
    }
}
