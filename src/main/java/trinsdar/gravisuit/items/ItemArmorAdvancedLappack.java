package trinsdar.gravisuit.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import ic2.core.item.armor.electric.ItemArmorElectricPack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import trinsdar.gravisuit.GravisuitClassic;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true)
public class ItemArmorAdvancedLappack extends ItemArmorElectricPack implements IBauble {
    public ItemArmorAdvancedLappack() {
        super(36, "ic2:textures/models/armor/lappack", 600000, 2, 500);
        this.setRegistryName("advanced_lappack");
        this.setUnlocalizedName(GravisuitClassic.MODID + ".advancedLappack");
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.BODY;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            this.onArmorTick(entity.getEntityWorld(), (EntityPlayer)entity, itemstack);
        }

    }


    @Override
    @Optional.Method(modid = "baubles")
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }
}
