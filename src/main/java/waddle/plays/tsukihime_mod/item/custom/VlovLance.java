package waddle.plays.tsukihime_mod.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
        import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;
import waddle.plays.tsukihime_mod.item.client.ShikiKnifeRenderer;
import waddle.plays.tsukihime_mod.item.client.VlovLanceRenderer;
import waddle.plays.tsukihime_mod.sound.ModSounds;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class VlovLance extends SwordItem implements GeoItem {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public VlovLance(Tier tier, int attackDamage, float attackSpeed,
                     Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }



    private static final UUID LANCE_REACH_UUID =
            UUID.fromString("9f762a93-a10a-45df-93bd-fc9417ef025b");

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {

        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        p_41423_.add(Component.literal("Right click to create a snowy blast."));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(
            EquipmentSlot slot) {

        Multimap<Attribute, AttributeModifier> defaults =
                super.getDefaultAttributeModifiers(slot);

        if (slot != EquipmentSlot.MAINHAND) {
            return defaults;
        }

        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .putAll(defaults)
                .put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(
                        LANCE_REACH_UUID,
                        "Lance reach bonus",
                        9.0D, //starts at 3 so its 3 + this number
                        AttributeModifier.Operation.ADDITION
                ))
                .build();
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack){
        return RenderUtils.getCurrentTick();
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private VlovLanceRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null){
                    renderer = new VlovLanceRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public boolean onLeftClickEntity(
            ItemStack stack, Player player, Entity target) {
        if (!player.level().isClientSide) {
            if (target instanceof LivingEntity livingTarget){
                livingTarget.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        120, //time
                        3,   // level of effect
                        false,
                        false,
                        false
                ));
            }
        }

        return false;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);



        if (level instanceof ServerLevel serverLevel) {

            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 9, false, false, false));

            double radius = 6.0;
            float damage = 2.0f;
            double knockback = 2.0f;

            for (LivingEntity mob : serverLevel.getEntitiesOfClass(
               LivingEntity.class,
               player.getBoundingBox().inflate(radius),
               mob -> mob.isAlive()
                       && mob != player
                       && !mob.isSpectator()
               && mob.distanceToSqr(player) <= radius * radius
               )){
                mob.hurt(player.damageSources().playerAttack(player), damage);
                double dx = mob.getX() - player.getX();
                double dz = mob.getZ() - player.getZ();

                if (dx * dx + dz < 0.0001){
                    dx = 1.0;
                    dz = 0.0;
                }



                mob.knockback(knockback, -dx,-dz);
                mob.hurtMarked = true;

                mob.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 100, 3
                ));
            }
            int particleCount = 72;

            for (int i = 0; i < particleCount; i++) {
                double angle = Math.PI * 2.0 * i / particleCount;
                double x = player.getX() + Math.cos(angle) * radius;
                double z = player.getZ() + Math.sin(angle) * radius;

                serverLevel.sendParticles(
                        ParticleTypes.SNOWFLAKE,
                        x, player.getY() + 0.5, z,
                        2,
                        0.1, 0.2, 0.1,
                        0.02
                );
            }

            serverLevel.playSound(
                    null,
                    player.blockPosition(),
                    ModSounds.VLOV_EXPLOSION.get(),
                    SoundSource.PLAYERS,
                    1.7F, // volume
                    1.0F // pitch
            );

            BlockState snow = Blocks.SNOW.defaultBlockState()
                    .setValue(SnowLayerBlock.LAYERS, 1);

            BlockPos min = BlockPos.containing(
                    player.getX() - radius,
                    player.getY() - radius,
                    player.getZ() - radius
            );

            BlockPos max = BlockPos.containing(
                    player.getX() + radius,
                    player.getY() + radius,
                    player.getZ() + radius
            );

            for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
                if (!serverLevel.isInWorldBounds(pos)
                        || !serverLevel.hasChunkAt(pos)) {
                    continue;
                }


                double dx = pos.getX() + 0.5 - player.getX();
                double dy = pos.getY() - player.getY();
                double dz = pos.getZ() + 0.5 - player.getZ();

                if (dx * dx + dy * dy + dz * dz > radius * radius) {
                    continue;
                }


                if (!serverLevel.isEmptyBlock(pos)) {
                    continue;
                }

                if (snow.canSurvive(serverLevel, pos)) {
                    serverLevel.setBlock(pos, snow, Block.UPDATE_ALL);
                }
            }


            // take durability away
            player.getMainHandItem().hurtAndBreak(4, player, entity ->
                    entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));


            player.getCooldowns().addCooldown(this, 100);

        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }


}
