package waddle.plays.tsukihime_mod.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import waddle.plays.tsukihime_mod.item.client.NekoArcEarsRenderer;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class NekoArcEars extends ArmorItem implements GeoItem {
    public NekoArcEars(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private NekoArcEarsRenderer renderer;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(
                    LivingEntity entity,
                    ItemStack stack,
                    EquipmentSlot slot,
                    HumanoidModel<?> original) {
                if (renderer == null) {
                    renderer = new NekoArcEarsRenderer();
                }

                renderer.prepForRender(entity, stack, slot, original);
                return renderer;
            }
        });
    }

    private static final UUID STEP_HEIGHT_UUID =
            UUID.fromString("bb3c7a26-a9f7-4db4-b730-8eca97849ed6");

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(
            EquipmentSlot slot) {

        ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers =
                ImmutableMultimap.builder();


        modifiers.putAll(super.getDefaultAttributeModifiers(slot));

        if (slot == EquipmentSlot.HEAD) {
            modifiers.put(
                    ForgeMod.STEP_HEIGHT_ADDITION.get(),
                    new AttributeModifier(
                            STEP_HEIGHT_UUID,
                            "Neko ears step height",
                            0.5D,
                            AttributeModifier.Operation.ADDITION
                    )
            );
        }

        return modifiers.build();
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {

        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        p_41423_.add(Component.literal("+2 Speed"));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity,int slotId, boolean isSelected){
        super.inventoryTick(stack, level, entity, slotId, isSelected);


    if (level.isClientSide || !(entity instanceof Player player)){
        return;
    }

    if (player.getItemBySlot(EquipmentSlot.HEAD) != stack){
        return;
    }



    player.addEffect(new MobEffectInstance(
            MobEffects.MOVEMENT_SPEED,
            40,
            2,
            true, //ambient effect
            false, // hide particles
            false //show icon
    ));

    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
