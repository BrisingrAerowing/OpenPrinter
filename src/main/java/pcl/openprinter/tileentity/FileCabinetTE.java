/**
 * 
 */
package pcl.openprinter.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import pcl.openprinter.items.PrintedPage;
import net.minecraft.util.NonNullList;

/**
 * @author Caitlyn
 *
 */
public class FileCabinetTE extends TileEntity implements IInventory {
	public NonNullList<ItemStack> fileCabinetItemStacks = NonNullList.withSize(30, ItemStack.EMPTY);

	public String name = "";

	private static final int[] slots_top = new int[] {0};
	private static final int[] slots_bottom = new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};
	private static final int[] slots_sides = new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items",par1NBTTagCompound.getId());
		this.fileCabinetItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.getCompoundTagAt(var3);
			byte var5 = var4.getByte("Slot");
			if (var5 >= 0 && var5 < this.fileCabinetItemStacks.size())
			{
				this.fileCabinetItemStacks.set(var5, new ItemStack(var4));
			}
		}
		this.name = par1NBTTagCompound.getString("name");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList var2 = new NBTTagList();
		for (int var3 = 0; var3 < this.fileCabinetItemStacks.size(); ++var3)
		{
			if (!this.fileCabinetItemStacks.get(var3).isEmpty())
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.fileCabinetItemStacks.get(var3).writeToNBT(var4);
				var2.appendTag(var4);
			}
		}
		par1NBTTagCompound.setTag("Items", var2);
		
		par1NBTTagCompound.setString("name", this.name);
		return par1NBTTagCompound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return tag;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}


	public FileCabinetTE() { }

	@Override
	public int getSizeInventory() {
		return this.fileCabinetItemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		return this.fileCabinetItemStacks.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return this.fileCabinetItemStacks.get(i);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.getCount() <= amt) {
				setInventorySlotContents(slot, ItemStack.EMPTY);
			} else {
				stack = stack.splitStack(amt);
				if (stack.getCount() == 0) {
					setInventorySlotContents(slot, ItemStack.EMPTY);
				}
			}
		}
		return stack;
	}

	public void incStackSize(int i, int amt) {

		if(fileCabinetItemStacks.get(i).isEmpty())
			return;
		else if(fileCabinetItemStacks.get(i).getCount() + amt > fileCabinetItemStacks.get(i).getMaxStackSize())
			fileCabinetItemStacks.get(i).setCount(fileCabinetItemStacks.get(i).getMaxStackSize());
		else
			fileCabinetItemStacks.get(i).setCount(fileCabinetItemStacks.get(i).getCount() + amt);
	}

	@Override
	public ItemStack removeStackFromSlot(int i) {
		if (!getStackInSlot(i).isEmpty())
		{
			ItemStack var2 = getStackInSlot(i);
			setInventorySlotContents(i,ItemStack.EMPTY);
			return var2;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.fileCabinetItemStacks.set(i, itemstack);
		if (itemstack.getCount() > this.getInventoryStackLimit())
		{
			itemstack.setCount(this.getInventoryStackLimit());
		}
	}


	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		return getWorld().getTileEntity(pos) == this &&
				entityplayer.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
	}


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if (i == 0 && (itemstack.getItem() instanceof PrintedPage || itemstack.getItem().equals(Items.BOOK))) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "fileCabinet";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public boolean hasDisplayName() {
		return name.length() > 0;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}
}
