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
	@Config.RangeInt(min = 1, max = 100)
	@Config.SlidingOption
	public static int health = 6;

	@Config.Comment("How long will the bat live (Seconds)")
	@Config.RangeInt(min = 1, max = 60)
	public static int lifetime = 5;

	@Config.Comment("Block reach range")
	@Config.RangeInt(min = 5, max = 100)
	@Config.SlidingOption
	@Config.RequiresWorldRestart
	public static int range = 10;

	@Config.Comment("Sounds?")
	public static boolean sound = true;

	@Config.Comment("Extend reach?")
	@Config.RequiresWorldRestart
	public static boolean reach = true;

	@Config.Comment("Show FPS?")
	public static boolean fps = true;

	@Config.Comment("Show Tickrate?")
	public static boolean tickrate = true;

	@Config.Comment("Show guide?")
	public static boolean guide = true;

	@Config.Comment("Show random tick speed?")
	public static boolean tickspeed = true;

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Main.MOD_ID)) ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);
	}
}
