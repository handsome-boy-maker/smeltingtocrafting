package com.example.smeltingtocrafting;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SmeltingToCrafting.MODID)
public class SmeltingToCrafting {
    public static final String MODID = "smeltingtocrafting";

    public SmeltingToCrafting() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(RecipeGenerator::generateAllRecipes);
    }
}