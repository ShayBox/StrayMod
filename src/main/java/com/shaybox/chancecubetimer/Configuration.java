package com.shaybox.chancecubetimer;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("ALL")
@Config(modid = Main.MOD_ID)
@Mod.EventBusSubscriber
public class Configuration {
	@Config.Comment("Timer interval (Minutes) (Reset timer to apply change)")
	public static int timer = 5;

	@Config.Comment("How long does the bat have to live? (Seconds)")
	public static int bat = 5;

	@Config.Comment("This should be clear enough")
	public static boolean sound = true;

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Main.MOD_ID)) ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);
	}
}