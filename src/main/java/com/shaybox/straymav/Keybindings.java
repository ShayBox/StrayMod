package com.shaybox.straymav;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

class Keybindings {
	static KeyBinding pause = new KeyBinding("Pause/Resume Timer", Keyboard.KEY_HOME, "StrayMav");
	static KeyBinding reset = new KeyBinding("Reset the timer", Keyboard.KEY_END, Main.MOD_NAME);
	static KeyBinding give = new KeyBinding("Give you a chance cube", Keyboard.KEY_INSERT, Main.MOD_NAME);
	static KeyBinding bat = new KeyBinding("Spawn a bat", Keyboard.KEY_DELETE, Main.MOD_NAME);
	static KeyBinding tickrateUp = new KeyBinding("Set tickrate to max", Keyboard.KEY_PRIOR, Main.MOD_NAME);
	static KeyBinding tickrateDown = new KeyBinding("Set tickrate to default", Keyboard.KEY_NEXT, Main.MOD_NAME);

	static void register() {
		ClientRegistry.registerKeyBinding(pause);
		ClientRegistry.registerKeyBinding(reset);
		ClientRegistry.registerKeyBinding(give);
		ClientRegistry.registerKeyBinding(bat);
		ClientRegistry.registerKeyBinding(tickrateUp);
		ClientRegistry.registerKeyBinding(tickrateDown);
	}
}
