package Reika.DragonAPI.Instantiable.Data;

import Reika.DragonAPI.Interfaces.BlockEnum;
import Reika.DragonAPI.Interfaces.ItemEnum;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ImmutableItemStack {

	private final ItemStack data;

	public ImmutableItemStack(Item i) {
		this(new ItemStack(i));
	}

	public ImmutableItemStack(Block b) {
		this(new ItemStack(b));
	}

	public ImmutableItemStack(ItemEnum i) {
		this(new ItemStack(i.getItemInstance()));
	}

	public ImmutableItemStack(BlockEnum b) {
		this(new ItemStack(b.getBlockInstance()));
	}

	public ImmutableItemStack(Item i, int num) {
		this(new ItemStack(i, num));
	}

	public ImmutableItemStack(Block b, int num) {
		this(new ItemStack(b, num));
	}

	public ImmutableItemStack(Item i, int num, int meta) {
		this(new ItemStack(i, num, meta));
	}

	public ImmutableItemStack(Block b, int num, int meta) {
		this(new ItemStack(b, num, meta));
	}

	public ImmutableItemStack(ItemStack is) {
		data = is.copy();
	}

	public int stackSize() {
		return data.stackSize;
	}

	public int getItemDamage() {
		return data.getItemDamage();
	}

	public Item getItem() {
		return data.getItem();
	}

	public ItemStack getItemStack() {
		return data.copy();
	}

}
