package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ItemHandler {
    private ItemHandler() {}

    @ObjectHolder(value = "mowziesmobs:umvuthana_mask_fury", registryName = "minecraft:item")
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FURY = null;
    @ObjectHolder(value = "mowziesmobs:umvuthana_mask_fear", registryName = "minecraft:item")
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FEAR = null;
    @ObjectHolder(value = "mowziesmobs:umvuthana_mask_rage", registryName = "minecraft:item")
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_RAGE = null;
    @ObjectHolder(value = "mowziesmobs:umvuthana_mask_bliss", registryName = "minecraft:item")
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_BLISS = null;
    @ObjectHolder(value = "mowziesmobs:umvuthana_mask_misery", registryName = "minecraft:item")
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_MISERY = null;
    @ObjectHolder(value = "mowziesmobs:umvuthana_mask_faith", registryName = "minecraft:item")
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FAITH = null;
    @ObjectHolder(value = "mowziesmobs:dart", registryName = "minecraft:item")
    public static final ItemDart DART = null;
    @ObjectHolder(value = "mowziesmobs:blowgun", registryName = "minecraft:item")
    public static final ItemBlowgun BLOWGUN = null;
    @ObjectHolder(value = "mowziesmobs:ice_crystal", registryName = "minecraft:item")
    public static final ItemIceCrystal ICE_CRYSTAL = null;
    @ObjectHolder(value = "mowziesmobs:glowing_jelly", registryName = "minecraft:item")
    public static final ItemGlowingJelly GLOWING_JELLY = null;
    @ObjectHolder(value = "mowziesmobs:naga_fang", registryName = "minecraft:item")
    public static final ItemNagaFang NAGA_FANG = null;
    @ObjectHolder(value = "mowziesmobs:naga_fang_dagger", registryName = "minecraft:item")
    public static final ItemNagaFangDagger NAGA_FANG_DAGGER = null;
    @ObjectHolder(value = "mowziesmobs:logo", registryName = "minecraft:item")
    public static final Item LOGO = null;
    @ObjectHolder(value = "mowziesmobs:music_disc_petiole", registryName = "minecraft:item")
    public static final RecordItem PETIOLE_MUSIC_DISC = null;

    @ObjectHolder(value = "mowziesmobs:frostmaw_spawn_egg", registryName = "minecraft:item")
    public static final ForgeSpawnEggItem FROSTMAW_SPAWN_EGG = null;
    @ObjectHolder(value = "mowziesmobs:lantern_spawn_egg", registryName = "minecraft:item")
    public static final ForgeSpawnEggItem LANTERN_SPAWN_EGG = null;
    @ObjectHolder(value = "mowziesmobs:naga_spawn_egg", registryName = "minecraft:item")
    public static final ForgeSpawnEggItem NAGA_SPAWN_EGG = null;

    public static Style TOOLTIP_STYLE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    @SubscribeEvent
    public static void register(RegisterEvent event) {
    	event.register(ForgeRegistries.Keys.ITEMS,
    			helper -> {
    	            helper.register("umvuthana_mask_fury", new ItemUmvuthanaMask(MaskType.FURY, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_fear", new ItemUmvuthanaMask(MaskType.FEAR, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_rage", new ItemUmvuthanaMask(MaskType.RAGE, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_bliss", new ItemUmvuthanaMask(MaskType.BLISS, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_misery", new ItemUmvuthanaMask(MaskType.MISERY, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("umvuthana_mask_faith", new ItemUmvuthanaMask(MaskType.FAITH, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("dart", new ItemDart(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("blowgun", new ItemBlowgun(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).durability(300)));
    	            helper.register("ice_crystal", new ItemIceCrystal(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).defaultDurability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durabilityValue).rarity(Rarity.RARE)));
    	            helper.register("glowing_jelly", new ItemGlowingJelly( new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).food(ItemGlowingJelly.GLOWING_JELLY_FOOD)));
    	            helper.register("naga_fang", new ItemNagaFang(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("naga_fang_dagger", new ItemNagaFangDagger(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
//    	            helper.register("sand_rake", new ItemSandRake(new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).defaultDurability(64)));
    	            helper.register("logo", new Item(new Item.Properties()));
    	            helper.register("music_disc_petiole", new RecordItem(14, MMSounds.MUSIC_PETIOLE, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab).stacksTo(1).rarity(Rarity.RARE), 0));
    	    
    	            helper.register("frostmaw_spawn_egg", new ForgeSpawnEggItem(EntityHandler.FROSTMAW, 0xf7faff, 0xafcdff, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("lantern_spawn_egg", new ForgeSpawnEggItem(EntityHandler.LANTERN, 0x6dea00, 0x235a10, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    	            helper.register("naga_spawn_egg", new ForgeSpawnEggItem(EntityHandler.NAGA, 0x154850, 0x8dd759, new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));

//    	            helper.register("gong", new BlockItem(BlockHandler.GONG.get(), new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
//    	            helper.register("raked_sand", new BlockItem(BlockHandler.RAKED_SAND.get(), new Item.Properties().tab(CreativeTabHandler.INSTANCE.creativeTab)));
    			});
    }

    public static void initializeAttributes() {
        UMVUTHANA_MASK_FURY.getAttributesFromConfig();
        UMVUTHANA_MASK_FEAR.getAttributesFromConfig();
        UMVUTHANA_MASK_RAGE.getAttributesFromConfig();
        UMVUTHANA_MASK_BLISS.getAttributesFromConfig();
        UMVUTHANA_MASK_MISERY.getAttributesFromConfig();
        UMVUTHANA_MASK_FAITH.getAttributesFromConfig();
        NAGA_FANG_DAGGER.getAttributesFromConfig();
    }

    public static void initializeDispenserBehaviors() {
        DispenserBlock.registerBehavior(DART, new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityDart dartentity = new EntityDart(EntityHandler.DART.get(), worldIn, position.x(), position.y(), position.z());
                dartentity.pickup = AbstractArrow.Pickup.ALLOWED;
                return dartentity;
            }
        });
    }
}