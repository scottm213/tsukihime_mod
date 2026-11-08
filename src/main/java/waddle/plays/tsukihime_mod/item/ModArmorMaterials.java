package waddle.plays.tsukihime_mod.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;



public enum ModArmorMaterials implements ArmorMaterial {
    SHIKI_GLASSES,
    NEKO_ARC_EARS,
    NRVNQSR_ROBE;

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return 0;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        if (this == NEKO_ARC_EARS){
            return 4;
        }
        if (this == NRVNQSR_ROBE){
            return 8;
        }

        return type == ArmorItem.Type.HELMET ? 3 : 0;
    }

    @Override
    public float getToughness() {
        return 2.0F;
    }

    @Override
    public float getKnockbackResistance() {

        return switch (this){
            case SHIKI_GLASSES -> 0.0F;
            case NEKO_ARC_EARS -> 0.1F;
            case NRVNQSR_ROBE -> 0.0F;
        };

    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return switch (this){
            case SHIKI_GLASSES -> Ingredient.EMPTY;
            case NEKO_ARC_EARS -> Ingredient.of(ModItems.GAIA_ESSENCE.get());
            case NRVNQSR_ROBE -> Ingredient.of(ModItems.DIVINE_IRON.get());
        };
    }

    @Override
    public String getName() {
        return switch (this){
            case SHIKI_GLASSES -> "tsukihime_mod:shiki_glasses";
            case NEKO_ARC_EARS -> "tsukihime_mod:neko_arc_ears";
            case NRVNQSR_ROBE -> "tsukihime_mod:nrvnqsr_robe";
        };

    }


}