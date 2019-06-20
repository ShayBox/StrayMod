package com.shaybox.straymod;

import com.shaybox.straymod.item.CustomItemRecord;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(Main.MOD_ID)
public class Items {
	public static final Item TUCK_AND_ROLL = null;

	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		Sounds.initialize();
		event.getRegistry().register(new CustomItemRecord("tuck_and_roll", Sounds.getSoundEvent("tuck_and_roll")));
	}

	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(TUCK_AND_ROLL, 0, new ModelResourceLocation(TUCK_AND_ROLL.getRegistryName(), "inventory"));
	}
}
