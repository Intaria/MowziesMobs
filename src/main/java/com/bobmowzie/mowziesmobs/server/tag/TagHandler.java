package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class TagHandler {
    public static final TagKey<Biome> HAS_MOWZIE_STRUCTURE = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "has_structure/has_mowzie_structure"));
    public static final TagKey<Biome> IS_MAGICAL = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MowziesMobs.MODID, "is_magical"));
}
