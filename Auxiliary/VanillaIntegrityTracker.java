/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Auxiliary;

import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Exception.MisuseException;
import Reika.DragonAPI.Exception.VanillaIntegrityException;
import Reika.DragonAPI.Exception.WTFException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class VanillaIntegrityTracker {

	private final HashMap<DragonAPIMod, ArrayList<Field>> blockList = new HashMap();
	private final HashMap<DragonAPIMod, ArrayList<Field>> itemList = new HashMap();

	private static final Class blockClass = Blocks.class;
	private static final Class itemClass = Item.class;

	private static final HashMap<Block, Field> blockFields = new HashMap();
	private static final HashMap<Item, Field> itemFields = new HashMap();

	public static final VanillaIntegrityTracker instance = new VanillaIntegrityTracker();

	private VanillaIntegrityTracker() {
		MinecraftForge.EVENT_BUS.register(this);

		Field[] blocks = blockClass.getFields();
		for (int i = 0; i < blocks.length; i++) {
			Field f = blocks[i];
			if (Modifier.isStatic(f.getModifiers())) {
				try {
					Object o = f.get(null);
					if (o == null) {
						throw new WTFException("Some mod is deleting the value of the block field "+f, true);
					}
					else if (o instanceof Block) {
						blockFields.put((Block)o, f);
					}
				}
				catch (Exception e) {}
			}
		}

		Field[] items = itemClass.getFields();
		for (int i = 0; i < items.length; i++) {
			Field f = items[i];
			if (Modifier.isStatic(f.getModifiers())) {
				try {
					Object o = f.get(null);
					if (o == null) {
						throw new WTFException("Some mod is deleting the value of the item field "+f, true);
					}
					else if (o instanceof Item) {
						itemFields.put((Item)o, f);
					}
				}
				catch (Exception e) {}
			}
		}
	}

	public void addWatchedBlock(DragonAPIMod mod, Block b) {
		ArrayList<Field> blocks = blockList.get(mod);
		if (blocks == null) {
			blocks = new ArrayList();
			blockList.put(mod, blocks);
		}
		Field f = blockFields.get(b);
		if (f == null)
			throw new MisuseException("Invalid block specified! No vanilla block has ID "+b+" and class "+b.getClass().getSimpleName());
		blocks.add(f);
	}

	public void addWatchedItem(DragonAPIMod mod, Item i) {
		ArrayList<Field> items = itemList.get(mod);
		if (items == null) {
			items = new ArrayList();
			itemList.put(mod, items);
		}
		Field f = itemFields.get(i);
		if (f == null)
			throw new MisuseException("Invalid item specified! No vanilla item has ID "+i+" and class "+i.getClass().getSimpleName());
		items.add(f);
	}

	public final void check() {
		for (DragonAPIMod mod : blockList.keySet()) {
			ArrayList<Field> blocks = blockList.get(mod);
			for (int i = 0; i < blocks.size(); i++) {
				Field f = blocks.get(i);
				try {
					Object o = f.get(null);
					if (!(o instanceof Block)) { //if null or some crap value
						throw new VanillaIntegrityException(mod, f.getName());
					}
				}
				catch (Exception e) {
				}
			}
		}
		for (DragonAPIMod mod : itemList.keySet()) {
			ArrayList<Field> items = itemList.get(mod);
			for (int i = 0; i < items.size(); i++) {
				Field f = items.get(i);
				try {
					Object o = f.get(null);
					if (!(o instanceof Item)) { //if null or some crap value
						throw new VanillaIntegrityException(mod, f.getName());
					}
				}
				catch (Exception e) {
				}
			}
		}
	}

}