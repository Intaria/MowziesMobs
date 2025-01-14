package com.bobmowzie.mowziesmobs.client.model;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.block.GongRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LayerHandler {
    public static final ModelLayerLocation GONG_LAYER = register("gong", "main");

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GONG_LAYER, GongRenderer::createBodyLayer);
    }

    private static ModelLayerLocation register(String model, String layer) {
        return new ModelLayerLocation(new ResourceLocation(MowziesMobs.MODID, model), layer);
    }
}