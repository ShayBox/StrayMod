package com.shaybox.straymav;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.time.LocalDateTime;
import java.util.Timer;

@Mod(modid = Main.MOD_ID, name = Main.MOD_NAME, version = Main.VERSION, dependencies = "required-after:chancecubes;required-after:tickratechanger")
public class Main {
	static final String MOD_ID = "straymav";
	static final String MOD_NAME = "StrayMav";
	static final String VERSION = "1.0.5";

	@Mod.Instance(MOD_ID)
	static Main INSTANCE;

	private Timer timer;
	private LocalDateTime timerDateTime;
	private LocalDateTime pauseDateTime;
	private String state = "NOT_RUNNING";
	private EntityPlayer player;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		Keybindings.register();
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	Timer getTimer() {
		return timer;
	}

	void setTimer(Timer timer) {
		this.timer = timer;
	}

	LocalDateTime getTimerDateTime() {
		return timerDateTime;
	}

	void setTimerDateTime(LocalDateTime timerDateTime) {
		this.timerDateTime = timerDateTime;
	}

	LocalDateTime getPauseDateTime() {
		return pauseDateTime;
	}

	void setPauseDateTime(LocalDateTime pauseDateTime) {
		this.pauseDateTime = pauseDateTime;
	}

	String getState() {
		return state;
	}

	void setState(String state) {
		this.state = state;
	}

	EntityPlayer getPlayer() {
		return player;
	}

	void setPlayer(EntityPlayer player) {
		this.player = player;
	}
}
