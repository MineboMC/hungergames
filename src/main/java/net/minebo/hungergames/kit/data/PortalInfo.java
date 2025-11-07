package net.minebo.hungergames.kit.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PortalInfo {
    public final Location bottomLoc;
    public final Material prevBottomMat;
    public final BlockData prevBottomData;
    public final Material prevTopMat;
    public final BlockData prevTopData;
    public final ItemStack savedRod;
    public BukkitRunnable restoreTask;

    public PortalInfo(Location bottomLoc,
                      Material prevBottomMat, BlockData prevBottomData,
                      Material prevTopMat, BlockData prevTopData,
                      ItemStack savedRod) {
        this.bottomLoc = bottomLoc;
        this.prevBottomMat = prevBottomMat;
        this.prevBottomData = prevBottomData;
        this.prevTopMat = prevTopMat;
        this.prevTopData = prevTopData;
        this.savedRod = savedRod;
    }
}