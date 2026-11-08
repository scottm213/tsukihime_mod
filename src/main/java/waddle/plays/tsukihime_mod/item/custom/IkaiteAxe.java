package waddle.plays.tsukihime_mod.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class IkaiteAxe extends AxeItem {
    public IkaiteAxe(Tier tier, int attackDamage, float attackSpeed,
                       Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }


    @Override
    public boolean onLeftClickEntity(
            ItemStack stack, Player player, Entity target) {
        if (!player.level().isClientSide) {
            if (target instanceof LivingEntity livingTarget){
                livingTarget.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        100, //time
                        1,   // level of effect
                        false,
                        true,
                        false
                ));
            }
        }

        return false;
    }
}
