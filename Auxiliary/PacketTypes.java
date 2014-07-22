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

public enum PacketTypes {

	DATA(),
	SOUND(),
	STRING(),
	UPDATE(),
	FLOAT(),
	SYNC(),
	TANK(),
	RAW();

	private PacketTypes() {

	}

	public static PacketTypes getPacketType(int type) {
		return PacketTypes.values()[type];
	}

}
