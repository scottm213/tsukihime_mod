package waddle.plays.tsukihime_mod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class AnemiaEffect extends MobEffect {

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    @Override
    public java.util.List<net.minecraft.world.item.ItemStack> getCurativeItems() {
        return java.util.Collections.emptyList();
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return;
        }

        // Anemia II and higher: 35% chance of 5 seconds of nausea.
        if (amplifier == 1) {
            if (entity.getRandom().nextFloat() < 0.55F){
                entity.addEffect(
                        new MobEffectInstance(MobEffects.CONFUSION, 180, 0)
                );
            }
            if (entity.getRandom().nextFloat() < 0.40F) {
                entity.addEffect(
                        new MobEffectInstance(MobEffects.BLINDNESS, 60, 0)
                );
            }
            if (entity.getRandom().nextFloat() < 0.35F){
                entity.addEffect(
                        new MobEffectInstance(MobEffects.WITHER, 60, 1)
                );
            }

        } else if (amplifier >= 2){
            if (entity.getRandom().nextFloat() < 0.65F) {
                entity.addEffect(
                        new MobEffectInstance(MobEffects.BLINDNESS, 80, 0)
                );
            }
            if (entity.getRandom().nextFloat() < 0.40F) {
                entity.addEffect(
                        new MobEffectInstance(MobEffects.CONFUSION, 180, 0)
                );
            }
            if (entity.getRandom().nextFloat() < 0.90F) {
                entity.addEffect(
                        new MobEffectInstance(MobEffects.WITHER, 80, 2)
                );
            }
        }



    }

    public AnemiaEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B3030);

        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "8ed760ab-21a2-4ca8-9ef4-e62fdbb22e51",
                -0.05,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );

    }
}
