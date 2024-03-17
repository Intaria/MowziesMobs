package com.bobmowzie.mowziesmobs.server.world.feature;

import java.util.function.Supplier;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.FrostmawPieces;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.FrostmawStructure;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FeatureHandler {
    public static final DeferredRegister<StructureType<?>> REG = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, MowziesMobs.MODID);

    public static RegistryObject<StructureType<FrostmawStructure>> FROSTMAW = registerStructure("frostmaw_spawn", () -> () -> FrostmawStructure.CODEC);
    public static StructurePieceType FROSTMAW_PIECE;

    private static <T extends Structure> RegistryObject<StructureType<T>> registerStructure(String name, Supplier<StructureType<T>> structure) {
        return REG.register(name, structure);
    }

    public static void registerStructurePieces() {
        FROSTMAW_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "frostmaw_template"), FrostmawPieces.FrostmawPiece::new);
    }
}
