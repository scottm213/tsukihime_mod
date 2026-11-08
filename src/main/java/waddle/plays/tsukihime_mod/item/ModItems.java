package waddle.plays.tsukihime_mod.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.custom.*;

import java.sql.Ref;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Tsukihime_mod.MOD_ID);



    public static final RegistryObject<Item> SHIKI_KNIFE = ITEMS.register("shiki_knife", () -> new ShikiKnife(new Item.Properties().durability(512).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> BLACK_KEYS = ITEMS.register("black_keys",
            () -> new BlackKeys(new Item.Properties().rarity(Rarity.RARE).stacksTo(63)));

    public static final RegistryObject<Item> SEVENTH_HOLY_SCRIPTURE_BASE = ITEMS.register("seventh_holy_scripture_base",
            () -> new SeventhHolyScriptureBase(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MASTER_BLACK_KEYS = ITEMS.register("master_black_keys",
    () -> new BlackKeys(new Item.Properties().durability(2048).fireResistant().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> BIBLICAL_VERSE = ITEMS.register("biblical_verse", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIVINE_IRON = ITEMS.register("divine_iron", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIVINE_IRON_NUGGET = ITEMS.register("divine_iron_nugget", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_DIVINE_IRON = ITEMS.register("raw_divine_iron", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HOLY_BIBLE = ITEMS.register("holy_bible", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LARGE_WEAPON_BASE = ITEMS.register("large_weapon_base", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IKAITE = ITEMS.register("ikaite", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> VLOV_LANCE =
            ITEMS.register("vlov_lance", () -> new VlovLance(
                    ModToolTiers.VLOV_LANCE,
                    2,
                    -2.4f,  new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> IKAITE_SWORD =
            ITEMS.register("ikaite_sword", () -> new IkaiteSword(
                    ModToolTiers.IKAITE,
                    5,
                    -2.4f, new Item.Properties()
            ));

    public static final RegistryObject<Item> IKAITE_PICKAXE =
            ITEMS.register("ikaite_pickaxe", () -> new PickaxeItem(
                    ModToolTiers.IKAITE,
                    1,
                    -2.8F, new Item.Properties()));

    public static final RegistryObject<Item> IKAITE_AXE =
            ITEMS.register("ikaite_axe", () -> new IkaiteAxe(
                    ModToolTiers.IKAITE,
                    6,
                    -3.0f, new Item.Properties()
            ));

    public static final RegistryObject<Item> NEKO_ARC_EARS = ITEMS.register("neko_arc_ears", () -> new NekoArcEars(
        ModArmorMaterials.NEKO_ARC_EARS,
        ArmorItem.Type.HELMET,
        new Item.Properties().durability(1864)
    ));


    public static final RegistryObject<Item> NRVNQSR_ROBE = ITEMS.register("nrvnqsr_robe", () -> new NrvnqsrRobe(
            ModArmorMaterials.NRVNQSR_ROBE,
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties().durability(2048).rarity(Rarity.EPIC)
    ));


    public static final RegistryObject<Item> GAIA_ESSENCE = ITEMS.register("gaia_essence", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SHIKI_GLASSES = ITEMS.register("shiki_glasses", () -> new ArmorItem(ModArmorMaterials.SHIKI_GLASSES, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> DIVINE_IRON_PICKAXE = ITEMS.register("divine_iron_pickaxe", () -> new PickaxeItem(
            ModToolTiers.DIVINE_IRON,
            1,
            -2.8F, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> DIVINE_IRON_SWORD = ITEMS.register("divine_iron_sword", () -> new SwordItem(
            ModToolTiers.DIVINE_IRON,
            11,
            -2.6f, new Item.Properties().fireResistant()
    ));


    public static final RegistryObject<Item> CURRY_BREAD = ITEMS.register("curry_bread", () -> new CurryBread(new Item.Properties().food(
            new FoodProperties.Builder()
                    .nutrition(9)
                    .saturationMod(0.9f)
                    .build()
    )));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
