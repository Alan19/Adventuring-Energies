package com.minercana.adventuringenergies.energytypes;

import com.minercana.adventuringenergies.AdventuringEnergies;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class AEEnergyTypes {
    private static final DeferredRegister<EnergyType> ENERGY_TYPES = DeferredRegister.create(EnergyType.class, AdventuringEnergies.MOD_ID);

    public static final RegistryObject<YellowEnergy> YELLOW = ENERGY_TYPES.register("yellow", YellowEnergy::new);
    public static final RegistryObject<RedEnergy> RED = ENERGY_TYPES.register("red", RedEnergy::new);
    public static final RegistryObject<AzureEnergy> AZURE = ENERGY_TYPES.register("azure", AzureEnergy::new);
    public static final RegistryObject<VerdantEnergy> VERDANT = ENERGY_TYPES.register("verdant", VerdantEnergy::new);
}
