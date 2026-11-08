package waddle.plays.tsukihime_mod.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import waddle.plays.tsukihime_mod.item.client.NrvnqsrRobeRenderer;

import java.util.List;
import java.util.function.Consumer;

public class NrvnqsrRobe extends ArmorItem implements GeoItem {
    public NrvnqsrRobe(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private NrvnqsrRobeRenderer renderer;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(
                    LivingEntity entity,
                    ItemStack stack,
                    EquipmentSlot slot,
                    HumanoidModel<?> original) {
                if (renderer == null) {
                    renderer = new NrvnqsrRobeRenderer();
                }

                renderer.prepForRender(entity, stack, slot, original);
                return renderer;
            }
        });
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(
            EquipmentSlot slot) {

        ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers =
                ImmutableMultimap.builder();


        modifiers.putAll(super.getDefaultAttributeModifiers(slot));

        if (slot == EquipmentSlot.CHEST) {
            int i = 0;
        }

        return modifiers.build();
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {

        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

        p_41423_.add(Component.literal("Right click to summon a wolf at the exchange of some health. Up to 5 at a time."));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
