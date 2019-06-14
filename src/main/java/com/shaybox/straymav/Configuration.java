package com.shaybox.straymav;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("WeakerAccess")
@Config(modid = Main.MOD_ID)
@Mod.EventBusSubscriber
class Configuration {

	@Config.Comment("How often to place a chance cube (minutes)")
	@Config.RangeInt(min = 1, max = 60)
	@Config.SlidingOption
	public static int timer = 5;

	@Config.Comment("How much health will the bat have")
	@Config.RangeInt(min = 1, max = 1000)
	@Config.SlidingOption
	public static int health = 6;

	@Config.Comment("How long will the bat live (Seconds)")
	@Config.RangeInt(min = 1, max = 300)
	public static int lifetime = 5;

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Main.MOD_ID)) ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);
	}
}
