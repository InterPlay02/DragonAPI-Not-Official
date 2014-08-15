/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ModInteract;

import Reika.DragonAPI.ModList;
import Reika.DragonAPI.Base.ModHandlerBase;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;

import java.lang.reflect.Field;

import net.minecraft.block.Block;

public class MFRHandler extends ModHandlerBase {

	private static final MFRHandler instance = new MFRHandler();

	public final Block cableID;

	private MFRHandler() {
		super();
		Block idcable = null;

		if (this.hasMod()) {
			try {
				Class blocks = this.getMod().getBlockClass();
				Field wire = blocks.getField("rednetCableBlock");
				idcable = ((Block)wire.get(null));
			}
			catch (NoSuchFieldException e) {
				ReikaJavaLibrary.pConsole("DRAGONAPI: "+this.getMod()+" field not found! "+e.getMessage());
				e.printStackTrace();
			}
			catch (SecurityException e) {
				ReikaJavaLibrary.pConsole("DRAGONAPI: Cannot read "+this.getMod()+" (Security Exception)! "+e.getMessage());
				e.printStackTrace();
			}
			catch (IllegalArgumentException e) {
				ReikaJavaLibrary.pConsole("DRAGONAPI: Illegal argument for reading "+this.getMod()+"!");
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				ReikaJavaLibrary.pConsole("DRAGONAPI: Illegal access exception for reading "+this.getMod()+"!");
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				ReikaJavaLibrary.pConsole("DRAGONAPI: Null pointer exception for reading "+this.getMod()+"! Was the class loaded?");
				e.printStackTrace();
			}
		}
		else {
			this.noMod();
		}

		cableID = idcable;
	}

	public static MFRHandler getInstance() {
		return instance;
	}

	@Override
	public boolean initializedProperly() {
		return cableID != null;
	}

	@Override
	public ModList getMod() {
		return ModList.MINEFACTORY;
	}

}