package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.*;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageInterruptAbility;
import com.bobmowzie.mowziesmobs.server.message.MessageJumpToAbilitySection;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerUseAbility;
import com.bobmowzie.mowziesmobs.server.message.MessageUseAbility;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;

public enum AbilityHandler {
    INSTANCE;

    public static final AbilityType<Player, FireballAbility> FIREBALL_ABILITY = new AbilityType<>("fireball", FireballAbility::new);
    public static final AbilityType<Player, IceBreathAbility> ICE_BREATH_ABILITY = new AbilityType<>("ice_breath", IceBreathAbility::new);

    public static final AbilityType<Player, SimplePlayerAnimationAbility> BACKSTAB_ABILITY = new AbilityType<>("backstab", (type, player) ->
            new SimplePlayerAnimationAbility(type, (Player) player, "backstab", 12, true, true)
    );

    public static final AbilityType<Player, ? extends PlayerAbility>[] PLAYER_ABILITIES = new AbilityType[] {
            ICE_BREATH_ABILITY,
            BACKSTAB_ABILITY,
    };

    @Nullable
    public AbilityCapability.IAbilityCapability getAbilityCapability(LivingEntity entity) {
        return CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
    }

    @Nullable
    public Ability getAbility(LivingEntity entity, AbilityType<?, ?> abilityType) {
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            return abilityCapability.getAbilityMap().get(abilityType);
        }
        return null;
    }

    public <T extends LivingEntity> void sendAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.level.isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance != null && instance.canUse()) {
                abilityCapability.activateAbility(entity, abilityType);
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageUseAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)));
            }
        }
    }

    public <T extends LivingEntity> void sendInterruptAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.level.isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.interrupt();
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageInterruptAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)));
            }
        }
    }

    public <T extends Player> void sendPlayerTryAbilityMessage(T entity, AbilityType<?, ?> ability) {
        if (!(entity.level.isClientSide && entity instanceof LocalPlayer)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            MowziesMobs.NETWORK.sendToServer(new MessagePlayerUseAbility(ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), ability)));
        }
    }


    public <T extends LivingEntity> void sendJumpToSectionMessage(T entity, AbilityType<?, ?> abilityType, int sectionIndex) {
        if (entity.level.isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.jumpToSection(sectionIndex);
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageJumpToAbilitySection(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType), sectionIndex));
            }
        }
    }
}