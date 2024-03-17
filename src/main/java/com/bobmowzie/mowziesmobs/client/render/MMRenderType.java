package com.bobmowzie.mowziesmobs.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MMRenderType extends RenderType {
    public MMRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType getGlowingEffect(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard shard = new RenderStateShard.TextureStateShard(locationIn, false, false);
        RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setTextureState(shard).setShaderState(RENDERTYPE_BEACON_BEAM_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setOverlayState(OVERLAY).setWriteMaskState(COLOR_WRITE).createCompositeState(false);
        return create("glow_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$state);
    }


    public static ParticleRenderType PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH = new ParticleRenderType() {
        public void begin(BufferBuilder p_217600_1_, TextureManager p_217600_2_) {
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            p_217600_1_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        public void end(Tesselator p_217599_1_) {
            p_217599_1_.end();
        }

        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH";
        }
    };
}
