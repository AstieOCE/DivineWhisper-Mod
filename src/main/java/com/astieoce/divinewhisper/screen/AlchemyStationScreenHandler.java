package com.astieoce.divinewhisper.screen;

import com.astieoce.divinewhisper.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class AlchemyStationScreenHandler extends ScreenHandler implements CustomStackLimit {
    private final Inventory inventory;

    public AlchemyStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.ALCHEMY_STATION_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        checkSize(inventory, 25);

        // Centered positions for a 5x5 grid
        int startX = 44;  // Adjust as needed
        int startY = 20;  // Adjust as needed
        int slotSizePlus2 = 18;

        // Add custom 5x5 grid slots
        for (int row = 0; row < 5; ++row) {
            for (int col = 0; col < 5; ++col) {
                int x = startX + col * slotSizePlus2;
                int y = startY + row * slotSizePlus2;
                this.addSlot(new Slot(inventory, col + row * 5, x, y) {
                    @Override
                    public int getMaxItemCount() {
                        return getMaxCountPerStack(); // Use the custom stack limit
                    }
                });
            }
        }

        // Add player inventory slots
        int playerInvY = startY + 5 * slotSizePlus2 + 4;  // Adjust as needed
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = startX - 36 + col * slotSizePlus2;
                int y = playerInvY + row * slotSizePlus2;
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Add player hotbar slots
        int hotbarY = playerInvY + 3 * slotSizePlus2 + 4;  // Adjust as needed
        for (int col = 0; col < 9; ++col) {
            int x = startX - 36 + col * slotSizePlus2;
            int y = hotbarY;
            this.addSlot(new Slot(playerInventory, col, x, y));
        }
    }

    public static AlchemyStationScreenHandler create(int syncId, PlayerInventory playerInventory) {
        return new AlchemyStationScreenHandler(syncId, playerInventory, new SimpleInventory(25));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public int getMaxCountPerStack() {
        return 300; // Custom stack limit for inventory
    }
}
