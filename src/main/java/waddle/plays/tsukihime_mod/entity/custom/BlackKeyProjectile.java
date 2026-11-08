package waddle.plays.tsukihime_mod.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import waddle.plays.tsukihime_mod.item.ModItems;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


public class BlackKeyProjectile extends ThrowableItemProjectile {

    private static final double MAX_RANGE = 30.0;
    private static final double MASTER_MAX_RANGE = 35.0;
    private static final float NORMAL_DAMAGE = 7.0F;
    private static final float MASTER_DAMAGE = 9.0F;

    private double distanceTravelled = 0.0;

    public boolean isMaster() {
        return getItem().is(ModItems.MASTER_BLACK_KEYS.get());
    }

    private void discardWithDropChance(){
        if (level().isClientSide || isRemoved()){
            return;
        }
        if (!isMaster() && random.nextFloat() < 0.70F){
            spawnAtLocation(ModItems.BLACK_KEYS.get());
        }
        discard();
    }


    public BlackKeyProjectile(
            EntityType<? extends BlackKeyProjectile> entityType,
            Level level
    ) {
        super(entityType, level);
        setNoGravity(true);
    }
    @Override
    public void tick() {
        double maxRange = isMaster() ? MASTER_MAX_RANGE : MAX_RANGE;
        double remainingDistance = maxRange - distanceTravelled;

        if (remainingDistance <= 0.0) {
            discardWithDropChance();
            return;
        }

        Vec3 movement = getDeltaMovement();

        if (movement.length() > remainingDistance) {
            setDeltaMovement(
                    movement.normalize().scale(remainingDistance)
            );
        }

        Vec3 previousPosition = position();

        super.tick();

        distanceTravelled += position().distanceTo(previousPosition);

        if (!level().isClientSide && !isRemoved()) {
            if (distanceTravelled >= maxRange - 0.0001) {
                discardWithDropChance();
            } else if (tickCount >= 100) {
                discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (!level().isClientSide) {
            float damage = isMaster() ? MASTER_DAMAGE : NORMAL_DAMAGE;

            hitResult.getEntity().hurt(
                    damageSources().thrown(this, getOwner()),
                    damage
            );
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (!level().isClientSide) {
            discardWithDropChance();
        }
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.BLACK_KEYS.get();
    }
}