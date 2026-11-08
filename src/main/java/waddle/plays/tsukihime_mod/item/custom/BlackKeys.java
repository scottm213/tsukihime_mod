package waddle.plays.tsukihime_mod.item.custom;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import waddle.plays.tsukihime_mod.entity.ModEntities;
import waddle.plays.tsukihime_mod.entity.custom.BlackKeyProjectile;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import waddle.plays.tsukihime_mod.item.ModItems;
import waddle.plays.tsukihime_mod.item.client.BlackKeysRenderer;

import java.util.function.Consumer;

public class BlackKeys extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BlackKeys(Properties properties) {
        super(properties);
    }

    public static int getMasterVisibleKeys(ItemStack stack) {
        if (stack.getTag() == null
                || !stack.getTag().contains("MasterVisibleKeys")) {
            return 3;
        }

        int count = stack.getTag().getInt("MasterVisibleKeys");
        return Math.max(1, Math.min(3, count));
    }

    private static void cycleMasterVisibleKeys(ItemStack stack) {
        int current = getMasterVisibleKeys(stack);
        int next = current == 1 ? 3 : current - 1;

        stack.getOrCreateTag().putInt("MasterVisibleKeys", next);
    }

    @Override
    public boolean isValidRepairItem(
            ItemStack stack,
            ItemStack repairIngredient
    ) {
        if (stack.is(ModItems.MASTER_BLACK_KEYS.get())) {
            return repairIngredient.is(ModItems.BIBLICAL_VERSE.get());
        }

        return super.isValidRepairItem(stack, repairIngredient);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            BlackKeyProjectile projectile = new BlackKeyProjectile(
                    ModEntities.BLACK_KEY_PROJECTILE.get(),
                    level
            );

            projectile.setOwner(player);
            projectile.setItem(new ItemStack(stack.getItem(), 1));


            projectile.setPos(
                    player.getX(),
                    player.getEyeY() - 0.1,
                    player.getZ()
            );

            Vec3 direction = player.getLookAngle();

            projectile.shoot(
                    direction.x,
                    direction.y,
                    direction.z,
                    2.0F,
                    0.0F
            );

            if (!level.addFreshEntity(projectile)) {
                return InteractionResultHolder.fail(stack);
            }

            player.getCooldowns().addCooldown(this, 8);

            if (stack.is(ModItems.MASTER_BLACK_KEYS.get())) {
                cycleMasterVisibleKeys(stack);

                if (!player.getAbilities().instabuild) {
                    stack.hurtAndBreak(
                            1,
                            player,
                            entity -> entity.broadcastBreakEvent(hand)
                    );
                }
            } else if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(
                stack,
                level.isClientSide
        );
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // The rigid weapon follows the hand. PlayerAnimator animates the player bones.
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlackKeysRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) renderer = new BlackKeysRenderer();
                return renderer;
            }
        });
    }
}
