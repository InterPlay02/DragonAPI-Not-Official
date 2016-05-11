/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2016
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ASM;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

import net.minecraft.launchwrapper.IClassTransformer;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;

/** Credit to KingLemming and Co. for this @Strippable annotation reader and ASM handler. */
public class APIStripper implements IClassTransformer {

	private static boolean scrapedData = false;
	private static ASMDataTable ASM_DATA = null;

	private static void scrapeData(ASMDataTable table) {
		AnnotationStripper.scrapeData(table);
		scrapedData = true;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {

		if (bytes == null) {
			return null;
		}

		if (scrapedData) {
			bytes = AnnotationStripper.parse(name, transformedName, bytes);
		}

		return bytes;
	}

	/**
	 * When used on a class, methods from referenced interfaces will not be removed <br>
	 * When using this annotation on methods, ensure you do not switch on an enum inside that method.
	 * JavaC implementation details means this will cause crashes.
	 * <p>
	 * Can also strip on modid using "mod:CoFHCore" as a value <br>
	 * Can also strip on API using "api:CoFHAPI|energy" as a value
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.TYPE })
	public static @interface Strippable {
		public String[] value();
		/**
		 * The side from which these interfaces will *always* be stripped.
		 */
		public AnnoSide side() default AnnoSide.NONE;
	}

	public static enum AnnoSide {
		NONE, CLIENT, SERVER;
	}

	public static class AnnotationDummyContainer extends DummyModContainer {

		public AnnotationDummyContainer() {
			super(new ModMetadata());
			ModMetadata md = this.getMetadata();
			md.autogenerated = true;
			md.authorList = Arrays.asList("Reika");
			md.modId = "<DragonAPI ASM>";
			md.name = md.description = "DragonAPI ASM Data Initialization";
			md.version = "0";
		}

		@Override
		public boolean registerBus(EventBus bus, LoadController controller) {
			bus.register(this);
			return true;
		}

		@Subscribe
		public void construction(FMLConstructionEvent evt) {
			ASM_DATA = evt.getASMHarvestedData();
			scrapeData(ASM_DATA);
		}
	}

}
