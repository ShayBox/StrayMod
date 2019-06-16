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

	@Config.Name("Cow Options")
	public static CowOptions cow = new CowOptions();

	public static class CowOptions {
		@Config.Name("Modify Breeding")
		@Config.Comment("Modify breeding?")
		public boolean breeding = true;

		@Config.Name("Breeding Cooldown")
		@Config.Comment("Breeding cooldown (Seconds)")
		@Config.RangeInt(min = 1, max = 60)
		@Config.SlidingOption
		public int breedingCooldown = 30;

		@Config.Name("Ageing")
		@Config.Comment("Modify ageing?")
		public boolean ageing = true;

		@Config.Name("Ageing Cooldown")
		@Config.Comment("Ageing cooldown (Seconds)")
		public int ageingCooldown = 30;
	}

	@Config.Name("Misc Options")
	public static MiscOptions misc = new MiscOptions();

	public static class MiscOptions {
		@Config.Name("Guide")
		@Config.Comment("Show guide?")
		public boolean guide = true;

		@Config.Name("Sound")
		@Config.Comment("Play sounds?")
		public boolean sound = true;
	}

	@Config.Name("Overlay Options")
	public static OverlayOptions overlay = new OverlayOptions();

	public static class OverlayOptions {
		@Config.Name("Framerate")
		@Config.Comment("Show framerate?")
		public boolean framerate = true;

		@Config.Name("Tickrate")
		@Config.Comment("Show Tickrate?")
		public boolean tickrate = true;

		@Config.Name("Random Tick Speed")
		@Config.Comment("Show random tick speed?")
		public boolean randomTickSpeed = true;
	}

	@Config.Name("Reach Options")
	public static ReachOptions reach = new ReachOptions();

	public static class ReachOptions {
		@Config.Name("Extend")
		@Config.Comment("Extend extend?")
		@Config.RequiresWorldRestart
		public boolean extend = true;

		@Config.Name("Reach Range")
		@Config.Comment("Reach range")
		@Config.RangeInt(min = 5, max = 100)
		@Config.SlidingOption
		@Config.RequiresWorldRestart
		public int range = 10;
	}


	@Config.Name("Timer Options")
	public static TimerOptions timer = new TimerOptions();

	public static class TimerOptions {
		@Config.Name("Minutes")
		@Config.Comment("How often to place a chance cube (minutes)")
		@Config.RangeInt(min = 1, max = 60)
		@Config.SlidingOption
		public int minutes = 5;

		@Config.Name("Bat Health")
		@Config.Comment("How much batHealth will the bat have")
		@Config.RangeInt(min = 1, max = 100)
		@Config.SlidingOption
		public int batHealth = 6;

		@Config.Name("Bat Lifetime")
		@Config.Comment("How long will the bat live (Seconds)")
		@Config.RangeInt(min = 1, max = 60)
		public int batLifetime = 5;
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Main.MOD_ID)) ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);
	}
}
