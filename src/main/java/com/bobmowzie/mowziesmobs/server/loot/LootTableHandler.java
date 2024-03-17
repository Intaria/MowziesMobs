package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootTableHandler {
    // Mob drops
    public static final ResourceLocation LANTERN = register("entities/lantern");
    public static final ResourceLocation NAGA = register("entities/naga");
    public static final ResourceLocation FROSTMAW = register("entities/frostmaw");

    public static LootItemFunctionType CHECK_FROSTMAW_CRYSTAL;

    public static LootItemConditionType FROSTMAW_HAS_CRYSTAL;

    public static void init() {
//        CHECK_FROSTMAW_CRYSTAL = registerFunction("mowziesmobs:has_crystal", new LootFunctionCheckFrostmawCrystal.Serializer());
        FROSTMAW_HAS_CRYSTAL = registerCondition("mowziesmobs:has_crystal", new LootConditionFrostmawHasCrystal.ConditionSerializer());
    }

    private static ResourceLocation register(String id) {
        return new ResourceLocation(MowziesMobs.MODID, id);
    }

    private static LootItemFunctionType registerFunction(String name, Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(name), new LootItemFunctionType(serializer));
    }

    private static LootItemConditionType registerCondition(String registryName, Serializer<? extends LootItemCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(registryName), new LootItemConditionType(serializer));
    }
}
