package com.shaybox.straymod.item;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class CustomItemRecord extends ItemRecord {
	public CustomItemRecord(String recordName, SoundEvent soundIn) {
		super(recordName, soundIn);
		setRegistryName(recordName);
		setTranslationKey("record." + recordName);
	}
}
