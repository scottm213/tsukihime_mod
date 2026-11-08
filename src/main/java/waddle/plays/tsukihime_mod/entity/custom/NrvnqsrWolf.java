package waddle.plays.tsukihime_mod.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import waddle.plays.tsukihime_mod.entity.NrvnqsrSummons;


public class NrvnqsrWolf extends Wolf {
    public NrvnqsrWolf(EntityType<? extends Wolf> p_30369_, Level p_30370_) {
        super(p_30369_, p_30370_);
    }
    @Override
    public void setOrderedToSit(boolean orderedToSit) {
        super.setOrderedToSit(false);
    }

    @Override
    protected void registerGoals() {

        super.registerGoals();


        this.targetSelector.addGoal(
                5,
                new NearestAttackableTargetGoal<>(
                        this,
                        Mob.class,
                        true,
                        target -> this.isTame()
                                && !this.isOrderedToSit()
                                && target instanceof Enemy
                                && (target instanceof Creeper
                                || this.wantsToAttack(target, this.getOwner()))
                )
        );
    }


    private boolean robeSummon;

    public void initializeSummon(ServerPlayer player) {
        this.tame(player);
        this.setOrderedToSit(false);

        robeSummon = true;

        var server = player.serverLevel().getServer();
        var tracker = NrvnqsrSummons.get(server);

        tracker.register(
                this.getUUID(),
                player.getUUID(),
                NrvnqsrSummons.now(server)
        );

        tracker.updateHealth(player, NrvnqsrSummons.now(server));
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && robeSummon) {
            var server = this.getServer();
            var tracker = NrvnqsrSummons.get(server);

            if (!tracker.isActive(
                    this.getUUID(),
                    NrvnqsrSummons.now(server))) {
                this.discard();
                return;
            }
        }

        super.tick();

        if (!this.level().isClientSide
                && robeSummon
                && this.isDeadOrDying()) {
            NrvnqsrSummons.get(this.getServer())
                    .unregister(this.getUUID());
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide
                && robeSummon
                && reason.shouldDestroy()) {
            NrvnqsrSummons.get(this.getServer())
                    .unregister(this.getUUID());
        }

        super.remove(reason);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("RobeSummon", robeSummon);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        robeSummon = tag.getBoolean("RobeSummon");
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }


}
