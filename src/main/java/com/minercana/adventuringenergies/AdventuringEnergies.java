package com.minercana.adventuringenergies;

import com.minercana.adventuringenergies.api.NBTCapStorage;
import com.minercana.adventuringenergies.api.energytracker.EnergyTracker;
import com.minercana.adventuringenergies.api.energytracker.IEnergyTracker;
import com.minercana.adventuringenergies.blocks.AdventuringEnergiesBlocks;
import com.minercana.adventuringenergies.data.AdventuringEnergiesBlockstateProvider;
import com.minercana.adventuringenergies.data.AdventuringEnergiesLootTableProvider;
import com.minercana.adventuringenergies.data.AdventuringEnergiesRecipeProvider;
import com.minercana.adventuringenergies.energytypes.AEEnergyTypes;
import com.minercana.adventuringenergies.energytypes.EnergyType;
import com.minercana.adventuringenergies.items.AdventuringEnergiesItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AdventuringEnergies.MOD_ID)
public class AdventuringEnergies {
    public static final String MOD_ID = "adventuringenergies";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public AdventuringEnergies() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register custom energy orb registry
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::createRegisters);
        // Register data generators
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onGatherData);

        // Register things in deferred registers
        AdventuringEnergiesBlocks.register(FMLJavaModLoadingContext.get().getModEventBus());
        AdventuringEnergiesItems.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static <T extends IForgeRegistryEntry<T>> void makeRegistry(String name, Class<T> type) {
        new RegistryBuilder<T>()
                .setName(new ResourceLocation(AdventuringEnergies.MOD_ID, name))
                .setType(type)
                .create();
    }

    private void createRegisters(RegistryEvent.NewRegistry event) {
        makeRegistry("energy_types", EnergyType.class);
        AEEnergyTypes.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IEnergyTracker.class, new NBTCapStorage<>(), EnergyTracker::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(MOD_ID, "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    public void onGatherData(GatherDataEvent event){
        final DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(new AdventuringEnergiesRecipeProvider(generator));
        generator.addProvider(new AdventuringEnergiesLootTableProvider(generator));
        generator.addProvider(new AdventuringEnergiesBlockstateProvider(generator, existingFileHelper));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
