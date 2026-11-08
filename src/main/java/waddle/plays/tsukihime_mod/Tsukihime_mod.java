package waddle.plays.tsukihime_mod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import waddle.plays.tsukihime_mod.block.ModBlocks;
import waddle.plays.tsukihime_mod.effect.ModEffects;
import waddle.plays.tsukihime_mod.entity.ModEntities;
import waddle.plays.tsukihime_mod.entity.client.BlackKeyProjectileRenderer;
import waddle.plays.tsukihime_mod.entity.client.NrvnqsrWolfRenderer;
import waddle.plays.tsukihime_mod.item.ModItems;
import waddle.plays.tsukihime_mod.network.ModMessages;
import waddle.plays.tsukihime_mod.potion.ModPotions;
import waddle.plays.tsukihime_mod.sound.ModSounds;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Tsukihime_mod.MOD_ID)
public class Tsukihime_mod {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "tsukihime_mod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();




    public Tsukihime_mod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.BLACK_KEYS);
            event.accept(ModItems.SEVENTH_HOLY_SCRIPTURE_BASE);
            event.accept(ModItems.MASTER_BLACK_KEYS);
            event.accept(ModItems.DIVINE_IRON_SWORD);
            event.accept(ModItems.VLOV_LANCE);
            event.accept(ModItems.NEKO_ARC_EARS);
            event.accept(ModItems.NRVNQSR_ROBE);
            event.accept(ModItems.IKAITE_SWORD);
            event.accept(ModItems.SHIKI_KNIFE);
            event.accept(ModItems.SHIKI_GLASSES);

        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.BIBLICAL_VERSE);
            event.accept(ModItems.DIVINE_IRON);
            event.accept(ModItems.DIVINE_IRON_NUGGET);
            event.accept(ModItems.RAW_DIVINE_IRON);
            event.accept(ModItems.IKAITE);
            event.accept(ModItems.GAIA_ESSENCE);
            event.accept(ModItems.HOLY_BIBLE);
            event.accept(ModItems.LARGE_WEAPON_BASE);
        }
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.accept(ModItems.DIVINE_IRON_PICKAXE);
            event.accept(ModItems.IKAITE_AXE);
            event.accept(ModItems.IKAITE_PICKAXE);

        }
        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(ModBlocks.DIVINE_IRON_BLOCK);
            event.accept(ModBlocks.RAW_DIVINE_IRON_BLOCK);
            event.accept(ModBlocks.DIVINE_IRON_ORE);
            event.accept(ModBlocks.BLOCK_OF_IKAITE);
            event.accept(ModBlocks.IKAITE_ORE);
        }
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS){
            event.accept(ModItems.CURRY_BREAD);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerRenderers(
                EntityRenderersEvent.RegisterRenderers event
        ) {
            event.registerEntityRenderer(
                    ModEntities.BLACK_KEY_PROJECTILE.get(),
                    BlackKeyProjectileRenderer::new
            );
            event.registerEntityRenderer(
                    ModEntities.NRVNQSR_WOLF.get(),
                    NrvnqsrWolfRenderer::new
            );
        }


        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
