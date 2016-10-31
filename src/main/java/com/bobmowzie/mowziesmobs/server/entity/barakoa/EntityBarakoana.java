package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import java.util.ArrayList;
import java.util.List;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

import org.apache.commons.lang3.ArrayUtils;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationBlockAI;

public class EntityBarakoana extends EntityBarakoa {
    public static final Animation BLOCK_ANIMATION = Animation.create(10);

    private List<EntityBarakoanToBarakoana> pack = new ArrayList<>();

    private int packRadius = 3;

    public EntityBarakoana(World world) {
        super(world);
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityCow.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityPig.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntitySheep.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityChicken.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, 0, true, false, null));
        this.targetTasks.addTask(3, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, true));
        this.tasks.addTask(2, new AnimationBlockAI<>(this, BLOCK_ANIMATION));
        this.setMask(1);
        this.experienceValue = 12;
    }

    @Override
    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        for (int i = 0; i < pack.size(); i++) {
            pack.get(i).index = i;
        }

        if (!worldObj.isRemote && pack != null) {
            float theta = (2 * (float) Math.PI / pack.size());
            for (int i = 0; i < pack.size(); i++) {
                EntityBarakoanToBarakoana hunter = pack.get(i);
                if (hunter.getAttackTarget() == null) {
                    hunter.getNavigator().tryMoveToXYZ(posX + packRadius * MathHelper.cos(theta * i), posY, posZ + packRadius * MathHelper.sin(theta * i), 0.45);
                    if (getDistanceToEntity(hunter) > 20) {
                        hunter.setPosition(posX + packRadius * MathHelper.cos(theta * i), posY, posZ + packRadius * MathHelper.sin(theta * i));
                    }
                }
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        if (entity != null && entity instanceof EntityLivingBase) {
            if (!(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).capabilities.isCreativeMode)) {
                setAttackTarget((EntityLivingBase) entity);
            }
        }
        if (entity instanceof EntityLivingBase && (getAnimation() == IAnimatedEntity.NO_ANIMATION || getAnimation() == HURT_ANIMATION || getAnimation() == BLOCK_ANIMATION)) {
            blockingEntity = (EntityLivingBase) entity;
            playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 0.3f, 1.5f);
            AnimationHandler.INSTANCE.sendAnimationMessage(this, BLOCK_ANIMATION);
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }

    public void removePackMember(EntityBarakoanToBarakoana tribeHunter) {
        pack.remove(tribeHunter);
        sortPackMembers();
    }

    public void addPackMember(EntityBarakoanToBarakoana tribeHunter) {
        pack.add(tribeHunter);
        sortPackMembers();
    }

    private void sortPackMembers() {
        double theta = 2 * Math.PI / pack.size();
        for (int i = 0; i < pack.size(); i++) {
            int nearestIndex = -1;
            double smallestDiffSq = Double.MAX_VALUE;
            double targetTheta = theta * i;
            double x = posX + packRadius * Math.cos(targetTheta);
            double z = posZ + packRadius * Math.sin(targetTheta);
            for (int n = 0; n < pack.size(); n++) {
                EntityBarakoanToBarakoana tribeHunter = pack.get(n);
                double diffSq = (x - tribeHunter.posX) * (x - tribeHunter.posX) + (z - tribeHunter.posZ) * (z - tribeHunter.posZ);
                if (diffSq < smallestDiffSq) {
                    smallestDiffSq = diffSq;
                    nearestIndex = n;
                }
            }
            if (nearestIndex == -1) {
                throw new ArithmeticException("All pack members have NaN x and z?");
            }
            pack.add(i, pack.remove(nearestIndex));
        }
    }

    public int getPackSize() {
        return pack.size();
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
        int size = rand.nextInt(2) + 3;
        for (int i = 0; i <= size; i++) {
            EntityBarakoanToBarakoana tribeHunter = new EntityBarakoanToBarakoana(worldObj, this);
            pack.add(tribeHunter);
            tribeHunter.setLeaderUUID(getUniqueID());
            tribeHunter.setPosition(posX + 0.1 * i, posY, posZ);
            int weapon = rand.nextInt(3) == 0 ? 1 : 0;
            tribeHunter.setWeapon(weapon);
            worldObj.spawnEntityInWorld(tribeHunter);
        }
        return super.onInitialSpawn(difficulty, data);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        pack.forEach(EntityBarakoanToBarakoana::removeLeader);
    }

    @Override
    public boolean getCanSpawnHere() {
        List<EntityLivingBase> nearby = getEntityLivingBaseNearby(10, 4, 10, 10);
        for (EntityLivingBase nearbyEntity : nearby) {
            if (nearbyEntity instanceof EntityBarakoana) {
                return false;
            }
        }
        if (worldObj.checkNoEntityCollision(getEntityBoundingBox()) && worldObj.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !worldObj.containsAnyLiquid(getEntityBoundingBox())) {
            BlockPos ground = new BlockPos(
                MathHelper.floor_double(posX),
                MathHelper.floor_double(getEntityBoundingBox().minY) - 1,
                MathHelper.floor_double(posZ)
            );
            return ground.getY() >= 64 && worldObj.getBlockState(ground).getBlock() == Blocks.GRASS;
        }
        return false;
    }

    @Override
    protected void despawnEntity() {
        Event.Result result;
        if (isNoDespawnRequired()) {
            entityAge = 0;
        } else if ((entityAge & 0x1F) == 0x1F && (result = ForgeEventFactory.canEntityDespawn(this)) != Event.Result.DEFAULT) {
            if (result == Event.Result.DENY) {
                entityAge = 0;
            } else {
                pack.forEach(EntityBarakoanToBarakoana::setDead);
                setDead();
            }
        } else {
            EntityPlayer closestPlayer = worldObj.getClosestPlayerToEntity(this, -1);
            if (closestPlayer != null) {
                double dX = closestPlayer.posX - posX;
                double dY = closestPlayer.posY - posY;
                double dZ = closestPlayer.posZ - posZ;
                double distance = dX * dX + dY * dY + dZ * dZ;
                if (canDespawn() && distance > 16384) {
                    pack.forEach(EntityBarakoanToBarakoana::setDead);
                    setDead();
                }
                if (entityAge > 600 && rand.nextInt(800) == 0 && distance > 1024 && canDespawn()) {
                    pack.forEach(EntityBarakoanToBarakoana::setDead);
                    setDead();
                } else if (distance < 1024) {
                    entityAge = 0;
                }
            }
        }
    }

    @Override
    public Animation[] getAnimations() {
        return ArrayUtils.add(super.getAnimations(), BLOCK_ANIMATION);
    }
}