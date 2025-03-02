package com.crepsman.hextechmod.item.weapons;

import com.crepsman.hextechmod.item.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class HextechHammerBlasterMode extends CrossbowItem {
    private static final float DEFAULT_PULL_TIME = 1.25F;
    public static final int RANGE = 8;
    private boolean charged = false;
    private boolean loaded = false;
    private static final float CHARGE_PROGRESS = 0.2F;
    private static final float LOAD_PROGRESS = 0.5F;
    private static final float DEFAULT_SPEED = 3.15F;
    private static final float FIREWORK_ROCKET_SPEED = 1.6F;
    public static final float field_49258 = 1.6F;
    private static final net.minecraft.item.CrossbowItem.LoadingSounds DEFAULT_LOADING_SOUNDS;

    public HextechHammerBlasterMode(Settings settings) {
        super(settings);
    }

    public Predicate<ItemStack> getHeldProjectiles() {
        return CROSSBOW_HELD_PROJECTILES;
    }

    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ChargedProjectilesComponent chargedProjectilesComponent = (ChargedProjectilesComponent) itemStack.get(DataComponentTypes.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            this.shootAll(world, user, hand, itemStack, getSpeed(chargedProjectilesComponent), 1.0F, null);
            return ActionResult.CONSUME;
        } else if (!user.getProjectileType(itemStack).isEmpty()) {
            this.charged = false;
            this.loaded = false;
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        } else {
            return ActionResult.FAIL;
        }
    }

    private static float getSpeed(ChargedProjectilesComponent stack) {
        return stack.contains(Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
        float f = getPullProgress(i, stack, user);
        if (f >= 1.0F && !isCharged(stack) && loadProjectiles(user, stack)) {
            net.minecraft.item.CrossbowItem.LoadingSounds loadingSounds = this.getLoadingSounds(stack);
            loadingSounds.end().ifPresent((sound) -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound, user.getSoundCategory(), 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F));
            return true;
        } else {
            return false;
        }
    }

    private static boolean loadProjectiles(LivingEntity shooter, ItemStack crossbow) {
        List<ItemStack> list = load(crossbow, shooter.getProjectileType(crossbow), shooter);
        if (!list.isEmpty()) {
            crossbow.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(list));
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCharged(ItemStack stack) {
        ChargedProjectilesComponent chargedProjectilesComponent = (ChargedProjectilesComponent) stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        return !chargedProjectilesComponent.isEmpty();
    }

    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d = target.getX() - shooter.getX();
            double e = target.getZ() - shooter.getZ();
            double f = Math.sqrt(d * d + e * e);
            double g = target.getBodyY(0.3333333333333333) - projectile.getY() + f * 0.2F;
            vector3f = calcVelocity(shooter, new Vec3d(d, g, e), yaw);
        } else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(yaw * ((float) Math.PI / 180F), vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = shooter.getRotationVec(1.0F);
            vector3f = vec3d2.toVector3f().rotate(quaternionf);
        }

        projectile.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
        float h = getSoundPitch(shooter.getRandom(), index);
        shooter.getWorld().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, shooter.getSoundCategory(), 1.0F, h);
    }

    private static Vector3f calcVelocity(LivingEntity shooter, Vec3d direction, float yaw) {
        Vector3f vector3f = direction.toVector3f().normalize();
        Vector3f vector3f2 = (new Vector3f(vector3f)).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if (vector3f2.lengthSquared() <= 1.0E-7) {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
            vector3f2 = (new Vector3f(vector3f)).cross(vec3d.toVector3f());
        }

        Vector3f vector3f3 = (new Vector3f(vector3f)).rotateAxis(((float) Math.PI / 2F), vector3f2.x, vector3f2.y, vector3f2.z);
        return (new Vector3f(vector3f)).rotateAxis(yaw * ((float) Math.PI / 180F), vector3f3.x, vector3f3.y, vector3f3.z);
    }

    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        if (projectileStack.isOf(Items.FIREWORK_ROCKET)) {
            return new FireworkRocketEntity(world, projectileStack, shooter, shooter.getX(), shooter.getEyeY() - 0.15F, shooter.getZ(), true);
        } else {
            ProjectileEntity projectileEntity = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
            if (projectileEntity instanceof PersistentProjectileEntity) {
                PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity) projectileEntity;
                persistentProjectileEntity.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
            }

            return projectileEntity;
        }
    }

    protected int getWeaponStackDamage(ItemStack projectile) {
        return projectile.isOf(Items.FIREWORK_ROCKET) ? 3 : 1;
    }

    public void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, @Nullable LivingEntity target) {
        if (world instanceof ServerWorld serverWorld) {
            ChargedProjectilesComponent chargedProjectilesComponent = (ChargedProjectilesComponent) stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
                this.shootAll(serverWorld, shooter, hand, stack, chargedProjectilesComponent.getProjectiles(), speed, divergence, shooter instanceof PlayerEntity, target);
                if (shooter instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) shooter;
                    Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
                    serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                }
            }
        }
    }

    private static float getSoundPitch(Random random, int index) {
        return index == 0 ? 1.0F : getSoundPitch((index & 1) == 1, random);
    }

    private static float getSoundPitch(boolean flag, Random random) {
        float f = flag ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            net.minecraft.item.CrossbowItem.LoadingSounds loadingSounds = this.getLoadingSounds(stack);
            float f = (float) (stack.getMaxUseTime(user) - remainingUseTicks) / (float) getPullTime(stack, user);
            if (f < 0.2F) {
                this.charged = false;
                this.loaded = false;
            }

            if (f >= 0.2F && !this.charged) {
                this.charged = true;
                loadingSounds.start().ifPresent((sound) -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound, SoundCategory.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 0.5F && !this.loaded) {
                this.loaded = true;
                loadingSounds.mid().ifPresent((sound) -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound, SoundCategory.PLAYERS, 0.5F, 1.0F));
            }
        }
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return getPullTime(stack, user) + 3;
    }

    public static int getPullTime(ItemStack stack, LivingEntity user) {
        float f = EnchantmentHelper.getCrossbowChargeTime(stack, user, 1.25F);
        return MathHelper.floor(f * 20.0F);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    net.minecraft.item.CrossbowItem.LoadingSounds getLoadingSounds(ItemStack stack) {
        return (net.minecraft.item.CrossbowItem.LoadingSounds) EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.CROSSBOW_CHARGING_SOUNDS).orElse(DEFAULT_LOADING_SOUNDS);
    }

    private static float getPullProgress(int useTicks, ItemStack stack, LivingEntity user) {
        float f = (float) useTicks / (float) getPullTime(stack, user);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        ChargedProjectilesComponent chargedProjectilesComponent = (ChargedProjectilesComponent) stack.get(DataComponentTypes.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            ItemStack itemStack = (ItemStack) chargedProjectilesComponent.getProjectiles().get(0);
            tooltip.add(Text.translatable("item.minecraft.crossbow.projectile").append(ScreenTexts.SPACE).append(itemStack.toHoverableText()));
            if (type.isAdvanced() && itemStack.isOf(Items.FIREWORK_ROCKET)) {
                List<Text> list = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendTooltip(itemStack, context, list, type);
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); ++i) {
                        list.set(i, Text.literal("  ").append(list.get(i)).formatted(Formatting.GRAY));
                    }

                    tooltip.addAll(list);
                }
            }
        }
    }

    public boolean isUsedOnRelease(ItemStack stack) {
        return stack.isOf(this);
    }

    public int getRange() {
        return 8;
    }

    static {
        DEFAULT_LOADING_SOUNDS = new net.minecraft.item.CrossbowItem.LoadingSounds(Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_START), Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE), Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END));
    }

    public void toggleHammerMode(PlayerEntity player) {
        player.sendMessage(Text.literal("Hammer mode enabled"), true);
        player.setStackInHand(Hand.MAIN_HAND, new ItemStack(ModItems.HEXTECH_HAMMER));
    }
}