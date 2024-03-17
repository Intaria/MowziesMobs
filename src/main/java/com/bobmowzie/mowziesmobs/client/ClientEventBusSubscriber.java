package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.block.GongRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderDart;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderFallingBlock;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderFrostmaw;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderIceBall;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderLantern;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderNaga;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderNothing;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderPoisonBall;
import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityHandler.FROSTMAW.get(), RenderFrostmaw::new);
        EntityRenderers.register(EntityHandler.LANTERN.get(), RenderLantern::new);
        EntityRenderers.register(EntityHandler.NAGA.get(), RenderNaga::new);

        EntityRenderers.register(EntityHandler.DART.get(), RenderDart::new);
        EntityRenderers.register(EntityHandler.POISON_BALL.get(), RenderPoisonBall::new);
        EntityRenderers.register(EntityHandler.ICE_BALL.get(), RenderIceBall::new);
        EntityRenderers.register(EntityHandler.ICE_BREATH.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.FROZEN_CONTROLLER.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.FALLING_BLOCK.get(), RenderFallingBlock::new);
        EntityRenderers.register(EntityHandler.CAMERA_SHAKE.get(), RenderNothing::new);

        BlockEntityRenderers.register(BlockEntityHandler.GONG_BLOCK_ENTITY.get(), GongRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelEvent.RegisterAdditional modelRegistryEvent) {
        for (MaskType type : MaskType.values()) {
        	modelRegistryEvent.register(new ModelResourceLocation(MowziesMobs.MODID + ":umvuthana_mask_" + type.name + "_frame", "inventory"));
        }
    }
}