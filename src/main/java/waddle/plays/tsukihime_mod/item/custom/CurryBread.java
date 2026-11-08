package waddle.plays.tsukihime_mod.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import waddle.plays.tsukihime_mod.effect.ModEffects;

public class CurryBread extends Item {
    public CurryBread(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(
            ItemStack stack, Level level, LivingEntity entity) {

        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0));
        }

        return result;
    }
}