package com.shaybox.straymod;

import com.shaybox.straymod.proxy.ClientProxy;
import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber
public class KeyBindings {

	static KeyBinding action = new KeyBinding("Start, pause, and resume timer", Keyboard.KEY_HOME, Main.MOD_NAME + " Queue");
	static KeyBinding skip = new KeyBinding("Skip cycle", Keyboard.KEY_END, Main.MOD_NAME + " Queue");
	static KeyBinding add = new KeyBinding("Add to queue", Keyboard.KEY_INSERT, Main.MOD_NAME + " Queue");
	static KeyBinding remove = new KeyBinding("Remove from queue", Keyboard.KEY_DELETE, Main.MOD_NAME + " Queue");

	static KeyBinding give = new KeyBinding("Give chance cube", Keyboard.KEY_PRIOR, Main.MOD_NAME + " Manual");
	static KeyBinding bat = new KeyBinding("Spawn bat", Keyboard.KEY_NEXT, Main.MOD_NAME + " Manual");

	static KeyBinding tpsIncrease = new KeyBinding("Increases tickrate", Keyboard.KEY_UP, Main.MOD_NAME + " Tickrate");
	static KeyBinding tpsDefault = new KeyBinding("Set tickrate to default", Keyboard.KEY_DOWN, Main.MOD_NAME + " Tickrate");
	static KeyBinding rtsIncrease = new KeyBinding("Increases random tick speed", Keyboard.KEY_RIGHT, Main.MOD_NAME + " Tickrate");
	static KeyBinding rtsDefault = new KeyBinding("Sets random tick speed to default", Keyboard.KEY_LEFT, Main.MOD_NAME + " Tickrate");

	public static KeyBinding[] keys = new KeyBinding[]{
		KeyBindings.action,
		KeyBindings.skip,
		KeyBindings.add,
		KeyBindings.remove,
		KeyBindings.give,
		KeyBindings.bat,
		KeyBindings.tpsIncrease,
		KeyBindings.tpsDefault,
		KeyBindings.rtsIncrease,
		KeyBindings.rtsDefault
	};

	@SubscribeEvent
	public static void onKeyInput(InputEvent.KeyInputEvent event) {
		ClientProxy proxy = (ClientProxy) Main.PROXY;

		if (KeyBindings.action.isPressed()) {
			switch (proxy.timer.getState()) {
				case NOT_RUNNING: {
					if (proxy.getQueue().size() == 0) {
						proxy.getPlayer().sendMessage(new TextComponentString("Queue is empty"));
						return;
					}

					proxy.timer.start();
					break;
				}
				case RUNNING: {
					proxy.timer.pause();
					break;
				}
				case PAUSED: {
					proxy.timer.resume();
					break;
				}
			}
		}
		if (KeyBindings.skip.isPressed()) {
			if (proxy.getQueue().size() == 0) {
				proxy.getPlayer().sendMessage(new TextComponentString("Queue is empty"));
				return;
			}

			proxy.timer.skip();
		}
		if (KeyBindings.add.isPressed()) {
			proxy.getQueue().add(1);
			proxy.getPlayer().sendMessage(new TextComponentString("Added one to queue"));
		}
		if (KeyBindings.remove.isPressed()) {
			if (proxy.getQueue().size() == 0) {
				proxy.getPlayer().sendMessage(new TextComponentString("Queue is empty"));
				return;
			}

			proxy.getQueue().remove();
			proxy.getPlayer().sendMessage(new TextComponentString("removed one to queue"));
		}
		if (KeyBindings.give.isPressed()) {
			Utilities.giveChanceCube(proxy.getPlayer());
			proxy.getPlayer().sendMessage(new TextComponentString("Gave chancecube"));
		}
		if (KeyBindings.bat.isPressed()) {
			Utilities.spawnBat(proxy.getPlayer());
			proxy.getPlayer().sendMessage(new TextComponentString("Spawned bat"));
		}
		if (KeyBindings.tpsIncrease.isPressed()) {
			TickrateAPI.changeServerTickrate(Float.MAX_VALUE);
			proxy.getPlayer().sendMessage(new TextComponentString("Set tickrate to Infinity"));
		}
		if (KeyBindings.tpsDefault.isPressed()) {
			TickrateAPI.changeServerTickrate(20);
			proxy.getPlayer().sendMessage(new TextComponentString("Set tickrate to 20"));
		}
		if (KeyBindings.rtsIncrease.isPressed()) {
			int oldRTS = proxy.getPlayer().getEntityWorld().getGameRules().getInt("randomTickSpeed");
			int newRTS = (oldRTS + 1000) / 1000 * 1000;
			proxy.getPlayer().getEntityWorld().getGameRules().setOrCreateGameRule("randomTickSpeed", String.valueOf(newRTS));
			proxy.getPlayer().sendMessage(new TextComponentString("Set random tick speed to " + newRTS));
		}
		if (KeyBindings.rtsDefault.isPressed()) {
			proxy.getPlayer().getEntityWorld().getGameRules().setOrCreateGameRule("randomTickSpeed", "3");
			proxy.getPlayer().sendMessage(new TextComponentString("Set random tick speed to 3"));
		}
	}

	public static void register() {
		for (KeyBinding key : keys) ClientRegistry.registerKeyBinding(key);
	}
}
