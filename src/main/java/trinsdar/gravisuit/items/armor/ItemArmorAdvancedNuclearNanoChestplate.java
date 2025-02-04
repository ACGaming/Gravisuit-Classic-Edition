package trinsdar.gravisuit.items.armor;

import ic2.core.IC2;
import ic2.core.inventory.base.IHandHeldInventory;
import ic2.core.inventory.base.IHasGui;
import ic2.core.item.inv.inventories.NuclearJetpackInventory;
import ic2.core.item.inv.logics.NuclearJetpackLogic;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import trinsdar.gravisuit.util.GravisuitConfig;
import trinsdar.gravisuit.util.GravisuitLang;
import trinsdar.ic2c_extras.util.INuclearJetpackLogic;

@Optional.Interface(iface = "trinsdar.ic2c_extras.util.INuclearJetpackLogic", modid = "ic2c_extras")
public class ItemArmorAdvancedNuclearNanoChestplate extends ItemArmorAdvancedNanoChestplate implements IHandHeldInventory, INuclearJetpackLogic {
    public ItemArmorAdvancedNuclearNanoChestplate() {
        super(new ItemArmorAdvancedNuclearJetpack(false), "advancedNuclearNanoChestplate", GravisuitLang.advancedNuclearNanoChestplate,"advanced_nuclear_nano_chestplate", 21);
        this.transferLimit = GravisuitConfig.powerValues.advancedNuclearNanoChestplateTransfer;
        this.maxCharge = GravisuitConfig.powerValues.advancedNuclearNanoChestplateStorage;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.isSneaking()) {
            return super.onItemRightClick(worldIn, playerIn, hand);
        } else {
            if (IC2.platform.isSimulating()) {
                IC2.platform.launchGui(playerIn, this.getInventory(playerIn, hand, playerIn.getHeldItem(hand)), hand);
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
        }
    }

    @Override
    public IHasGui getInventory(EntityPlayer player, EnumHand hand, ItemStack stack) {
        return new NuclearJetpackInventory(player, this, stack);
    }

    @Override
    public int getGuiId(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getNbtData(stack);
        return nbt.hasKey("GuiID") ? nbt.getInteger("GuiID") : -1;
    }

    @Override
    public void setGuiID(ItemStack stack, int id) {
        if (id == -1) {
            StackUtil.getOrCreateNbtData(stack).removeTag("GuiID");
        } else {
            StackUtil.getOrCreateNbtData(stack).setInteger("GuiID", id);
        }
    }

    @Optional.Method(modid = "ic2c_extras")
    @Override
    public NuclearJetpackLogic getLogic(EntityPlayer entityPlayer, ItemStack itemStack) {
        return ((ItemArmorAdvancedNuclearJetpack)this.jetpack).getLogic(entityPlayer, itemStack);
    }
}
