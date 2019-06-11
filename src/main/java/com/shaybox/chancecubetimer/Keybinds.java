package com.shaybox.chancecubetimer;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

class Keybinds {
	public static KeyBinding pause = new KeyBinding("Pause the timer", Keyboard.KEY_HOME, Main.MOD_NAME);
	public static KeyBinding reset = new KeyBinding("Reset the timer", Keyboard.KEY_END, Main.MOD_NAME);

	static void register() {
		ClientRegistry.registerKeyBinding(pause);
		ClientRegistry.registerKeyBinding(reset);
	}
}
