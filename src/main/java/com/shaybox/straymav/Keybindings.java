package com.shaybox.straymav;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

class Keybindings {
	static KeyBinding pause = new KeyBinding("Pause and Resume queue", Keyboard.KEY_HOME, Main.MOD_NAME + " Queue");
	static KeyBinding skip = new KeyBinding("Skip cycle", Keyboard.KEY_END, Main.MOD_NAME + "Queue");
	static KeyBinding add = new KeyBinding("Add to queue", Keyboard.KEY_INSERT, Main.MOD_NAME + "Queue");
	static KeyBinding remove = new KeyBinding("Remove from queue", Keyboard.KEY_DELETE, Main.MOD_NAME + "Queue");
	static KeyBinding give = new KeyBinding("Give chance cube", Keyboard.KEY_PRIOR, Main.MOD_NAME + "Manual");
	static KeyBinding bat = new KeyBinding("Spawn bat", Keyboard.KEY_NEXT, Main.MOD_NAME + "Manual");
	static KeyBinding tickrateMax = new KeyBinding("Set tickrate to max", Keyboard.KEY_UP, Main.MOD_NAME + "Tickrate");
	static KeyBinding tickrateNormal = new KeyBinding("Set tickrate to normal", Keyboard.KEY_DOWN, Main.MOD_NAME + "Tickrate");
	static KeyBinding randomDouble = new KeyBinding("Doubles random tick speed", Keyboard.KEY_RIGHT, Main.MOD_NAME + "Tickrate");
	static KeyBinding randomNormal = new KeyBinding("Sets random tick speed to normal", Keyboard.KEY_LEFT, Main.MOD_NAME + "Tickrate");

	static void register() {
		ClientRegistry.registerKeyBinding(pause);
		ClientRegistry.registerKeyBinding(skip);
		ClientRegistry.registerKeyBinding(add);
		ClientRegistry.registerKeyBinding(remove);
		ClientRegistry.registerKeyBinding(give);
		ClientRegistry.registerKeyBinding(bat);
		ClientRegistry.registerKeyBinding(tickrateMax);
		ClientRegistry.registerKeyBinding(tickrateNormal);
		ClientRegistry.registerKeyBinding(randomDouble);
		ClientRegistry.registerKeyBinding(randomNormal);
	}
}
