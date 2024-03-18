package trinsdar.gravisuit.util.render;

import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.item.armor.base.ItemArmorJetpackBase.HoverMode;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import ic2.core.util.obj.plugins.IBaublesPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import trinsdar.gravisuit.util.GravisuitConfig;
import trinsdar.gravisuit.util.GravisuitConfig.Client.Positions;
import trinsdar.gravisuit.util.Registry;
import trinsdar.gravisuit.util.baubles.BaublesLoader;

public class GUIHandler extends Gui {
    private static final int ENERGY_LEVEL_FULL_COLOR = 5635925;
    private static final int ENERGY_LEVEL_HIGH_COLOR = 16755200;
    private static final int ENERGY_LEVEL_LOW_COLOR = 16733525;
    private static final String ENERGY_LEVEL_NAME = I18n.format("panelInfo.energyLevel") + ": ";

    private static int energyTextColor;
    private static String energyLevelString;

    private final Minecraft mc;
    private final ScaledResolution scaledResolution;
    private final EntityPlayer player;
    private final ItemStack armorStack;
    private final Item itemArmor;
    private String statusString;

    public GUIHandler(Minecraft mc) {
        this.mc = mc;
        this.scaledResolution = new ScaledResolution(mc);
        this.player = mc.player;
        this.armorStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        this.itemArmor = armorStack.getItem();
        this.statusString = "";

        int yPos1 = 3;
        if (GravisuitConfig.client.positions == Positions.BOTTOMLEFT || GravisuitConfig.client.positions == Positions.BOTTOMRIGHT) {
            yPos1 = scaledResolution.getScaledHeight() - ((mc.fontRenderer.FONT_HEIGHT * 2) + 5);
        }
        int yPos2 = yPos1 + mc.fontRenderer.FONT_HEIGHT + 2;

        if (!armorStack.isEmpty() && isJetpackOrLappack(itemArmor)) {
            handleJetpackRendering(yPos1, yPos2);
        } else if (Loader.isModLoaded("baubles") && IC2.loader.getPlugin("baubles", IBaublesPlugin.class) != null) {
            handleBaublesRendering(yPos1, yPos2);
        }
    }

    private boolean isJetpackOrLappack(Item item) {
        return or(item, Registry.getAdvancedLappack(), Registry.getUltimateLappack(), Registry.getAdvancedElectricJetpack(),
                Registry.getAdvancedNuclearJetpack(), Registry.advancedNanoChestplate, Registry.advancedNuclearNanoChestplate,
                Registry.gravisuit, Registry.nuclearGravisuit, Ic2Items.compactedElectricJetpack.getItem(), Ic2Items.compactedNuclearJetpack.getItem(),
                Ic2Items.quantumJetplate.getItem(), Ic2Items.quantumNuclearJetplate.getItem(), Ic2Items.lapPack.getItem(), Ic2Items.quantumPack.getItem());
    }

    private void handleJetpackRendering(int yPos1, int yPos2) {
        int currCharge = getCharge(armorStack);
        int energyStatus = (int) (currCharge / ((IElectricItem) itemArmor).getMaxCharge(armorStack) * 100);
        if (player.ticksExisted % 20 == 0) {
            energyLevelString = ENERGY_LEVEL_NAME + energyStatus;
            energyTextColor = getEnergyTextColor(energyStatus);
        }
        int xPos = 3;
        if (GravisuitConfig.client.positions == Positions.TOPRIGHT || GravisuitConfig.client.positions == Positions.BOTTOMRIGHT) {
            xPos = scaledResolution.getScaledWidth() - 3 - mc.fontRenderer.getStringWidth(energyLevelString + "%");
        } else if (GravisuitConfig.client.positions == Positions.TOPMIDDLE) {
            xPos = (int) (scaledResolution.getScaledWidth() * 0.50F) - (mc.fontRenderer.getStringWidth(energyLevelString + "%") / 2);
        }
        drawString(mc.fontRenderer, energyLevelString + "%", xPos, yPos1, energyTextColor);

        if (itemArmor != Registry.getAdvancedLappack() && itemArmor != Registry.getUltimateLappack() && itemArmor != Ic2Items.lapPack.getItem() && itemArmor != Ic2Items.quantumPack.getItem()) {
            NBTTagCompound tag = StackUtil.getOrCreateNbtData(armorStack);
            if (tag.getBoolean("enabled")) {
                statusString = I18n.format("panelInfo.gravitationEngineOn");
            } else if (!tag.getBoolean("disabled") & !tag.getBoolean("enabled")) {
                String engineStatus = I18n.format("panelInfo.jetpackEngineOn") + " ";
                HoverMode hoverMode = HoverMode.values()[tag.getByte("HoverMode")];
                statusString = engineStatus + getHoverModeStatus(hoverMode);
            }
            xPos = 3;
            if (GravisuitConfig.client.positions == Positions.TOPRIGHT || GravisuitConfig.client.positions == Positions.BOTTOMRIGHT) {
                xPos = scaledResolution.getScaledWidth() - 3 - mc.fontRenderer.getStringWidth(statusString);
            } else if (GravisuitConfig.client.positions == Positions.TOPMIDDLE) {
                xPos = (int) (scaledResolution.getScaledWidth() * 0.50F) - (mc.fontRenderer.getStringWidth(statusString) / 2);
            }
            drawString(mc.fontRenderer, statusString, xPos, yPos2, 5635925);
        }
    }

    private String getHoverModeStatus(HoverMode hoverMode) {
        if (hoverMode == HoverMode.Basic) {
            return "(" + I18n.format("panelInfo.jetpackHoverMode") + ")";
        } else if (hoverMode == HoverMode.Adv) {
            return "(" + I18n.format("panelInfo.jetpackExtremeHoverMode") + ")";
        }
        return "";
    }

    private void handleBaublesRendering(int yPos1, int yPos2) {
        ItemStack baublesArmorStack = BaublesLoader.getBaublesChestSlot(player);
        Item baublesItemArmor = baublesArmorStack.getItem();
        if (!baublesArmorStack.isEmpty() && isJetpackOrLappack(baublesItemArmor)) {
            handleJetpackRendering(yPos1, yPos2);
        }
    }

    private boolean or(Item compare, Item... items) {
        for (Item item : items) {
            if (compare == item) {
                return true;
            }
        }
        return false;
    }

    private int getEnergyTextColor(int energyLevel) {
        if (energyLevel == 100) {
            return ENERGY_LEVEL_FULL_COLOR;
        } else if (energyLevel > 50) {
            return ENERGY_LEVEL_HIGH_COLOR;
        } else {
            return ENERGY_LEVEL_LOW_COLOR;
        }
    }

    private int getCharge(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return nbt.getInteger("charge");
    }
}
