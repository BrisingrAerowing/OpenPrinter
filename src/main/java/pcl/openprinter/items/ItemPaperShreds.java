/**
 * 
 */
package pcl.openprinter.items;

import pcl.openprinter.OpenPrinter;
import net.minecraft.item.Item;

/**
 * @author Caitlyn
 *
 */
public class ItemPaperShreds extends Item {
	public ItemPaperShreds() {
		super();
		maxStackSize = 64;
		setCreativeTab(OpenPrinter.CreativeTab);
		setUnlocalizedName("shreddedPaper");
		setTextureName(OpenPrinter.MODID + ":shreddedPaper");
	}

}
