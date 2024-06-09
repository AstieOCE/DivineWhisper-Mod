package com.astieoce.divinewhisper.screen;

public interface CustomStackLimit {
    /**
     * Returns the maximum number of items a stack can contain when placed inside this inventory.
     * No slots may have more than this number of items. It is effectively the
     * stacking limit for this inventory's slots.
     *
     * @return the max {@link net.minecraft.item.ItemStack#getCount() count} of item stacks in this inventory
     */
    int getMaxCountPerStack();
}
