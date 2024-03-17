package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockHandler {

    public static final DeferredRegister<Block> REG = DeferredRegister.create(ForgeRegistries.BLOCKS, MowziesMobs.MODID);

    public static final RegistryObject<Block> GONG = REG.register("gong", () -> new GongBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.ANVIL)));
    public static final RegistryObject<Block> GONG_PART = REG.register("gong_part", () -> new GongBlock.GongPartBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.ANVIL)));
    public static final RegistryObject<RakedSandBlock> RAKED_SAND = REG.register("raked_sand", () -> new RakedSandBlock(14406560, BlockBehaviour.Properties.of(Material.SAND, MaterialColor.SAND).strength(0.5F).sound(SoundType.SAND), Blocks.SAND.defaultBlockState()));
    public static final RegistryObject<RakedSandBlock> RED_RAKED_SAND = REG.register("red_raked_sand", () -> new RakedSandBlock(11098145, BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_ORANGE).strength(0.5F).sound(SoundType.SAND), Blocks.RED_SAND.defaultBlockState()));
    
    public static void init() {
        FireBlock fireblock = (FireBlock)Blocks.FIRE;;
    }
}