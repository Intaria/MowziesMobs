package com.bobmowzie.mowziesmobs.server.item;

import java.util.List;

import javax.annotation.Nullable;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemIceCrystal extends Item {
    public ItemIceCrystal(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {
            Ability iceBreathAbility = AbilityHandler.INSTANCE.getAbility(entityLiving, AbilityHandler.ICE_BREATH_ABILITY);
            if (iceBreathAbility != null && iceBreathAbility.isUsing()) {
                iceBreathAbility.end();
            }
        }
        super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability.get();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get()) {
            tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        }
    }
}
