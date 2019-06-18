package com.shaybox.straymav;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;

@Mod(modid = Main.MOD_ID, name = Main.MOD_NAME, version = Main.VERSION, dependencies = "required-after:chancecubes;required-after:tickratechanger;")
public class Main {

	// Mod References

	static final String MOD_ID = "straymav";
	static final String MOD_NAME = "StrayMav";
	static final String VERSION = "1.3.0";

	@Mod.Instance(MOD_ID)
	static Main INSTANCE;

	// Custom variables

	private Timer timer = new Timer();
	private LocalDateTime timerDateTime = LocalDateTime.now();
	private LocalDateTime pauseDateTime = LocalDateTime.now();
	private String state = "NOT_RUNNING";
	private EntityPlayer player = Minecraft.getMinecraft().player;
	private Queue<Integer> queue = new LinkedList<>();

	// Forge events

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		Display.setTitle("#SAVEPICKLES");
		Keybindings.register();
	}

	// Getters & Setters

	Timer getTimer() {
		return timer;
	}

	Timer restartTimer() {
		this.timer.cancel();
		this.timer = new Timer();
		return this.timer;
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

	Queue<Integer> getQueue() {
		return queue;
	}
}
