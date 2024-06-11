package com.astieoce.divinewhisper.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AlchemyStationScreen extends HandledScreen<AlchemyStationScreenHandler> {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier("divinewhisper", "textures/gui/container/generic_ui.png");
    private static final Identifier ITEM_SLOT_TEXTURE = new Identifier("divinewhisper", "textures/gui/container/item_slot.png");

    private final int slotSize = 18;
    private final int slotOffset = 2;
    private final int gridSize = 5;
    private final int playerInvColumns = 9;
    private final int playerInvRows = 3;
    private final int hotbarRows = 1;
    private final int borderSize = 4; // Size of the border for 9-slice scaling

    public AlchemyStationScreen(AlchemyStationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        // Calculate the required width and height based on the grid and player inventory slots
        this.backgroundWidth = (gridSize * slotSize) + (slotOffset * 2) + (borderSize * 2);  // extra pixels for padding
        this.backgroundHeight = ((gridSize + playerInvRows + hotbarRows) * slotSize) + (slotOffset * 4) + (borderSize * 2) + 28;  // extra pixels for padding and titles

        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // Draw the 9-slice background
        draw9SliceBackground(context, x, y, this.backgroundWidth, this.backgroundHeight);

        // Draw item slots dynamically
        RenderSystem.setShaderTexture(0, ITEM_SLOT_TEXTURE);
        int startX = x + slotOffset + borderSize; // Starting X position for the 5x5 grid, adjusted for padding
        int startY = y + slotOffset + borderSize; // Starting Y position for the 5x5 grid, adjusted for padding

        for (int row = 0; row < gridSize; ++row) {
            for (int col = 0; col < gridSize; ++col) {
                int slotX = startX + col * slotSize;
                int slotY = startY + row * slotSize;
                context.drawTexture(ITEM_SLOT_TEXTURE, slotX, slotY, 0, 0, slotSize, slotSize, 18, 18);
            }
        }

        // Draw player inventory slots dynamically
        int playerInvY = startY + gridSize * slotSize + slotOffset;  // Adjust as needed
        for (int row = 0; row < playerInvRows; ++row) {
            for (int col = 0; col < playerInvColumns; ++col) {
                int slotX = startX - 36 + col * slotSize;
                int slotY = playerInvY + row * slotSize;
                context.drawTexture(ITEM_SLOT_TEXTURE, slotX, slotY, 0, 0, slotSize, slotSize, 18, 18);
            }
        }

        // Draw player hotbar slots dynamically
        int hotbarY = playerInvY + playerInvRows * slotSize + slotOffset;  // Adjust as needed
        for (int col = 0; col < playerInvColumns; ++col) {
            int slotX = startX - 36 + col * slotSize;
            int slotY = hotbarY;
            context.drawTexture(ITEM_SLOT_TEXTURE, slotX, slotY, 0, 0, slotSize, slotSize, 18, 18);
        }
    }

    private void draw9SliceBackground(DrawContext context, int x, int y, int width, int height) {
        int centerWidth = width - borderSize * 2;
        int centerHeight = height - borderSize * 2;

        // Corners
        context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, borderSize, borderSize, 18, 18);
        context.drawTexture(BACKGROUND_TEXTURE, x + width - borderSize, y, 14, 0, borderSize, borderSize, 18, 18);
        context.drawTexture(BACKGROUND_TEXTURE, x, y + height - borderSize, 0, 14, borderSize, borderSize, 18, 18);
        context.drawTexture(BACKGROUND_TEXTURE, x + width - borderSize, y + height - borderSize, 14, 14, borderSize, borderSize, 18, 18);

        // Edges
        context.drawTexture(BACKGROUND_TEXTURE, x + borderSize, y, borderSize, 0, centerWidth, borderSize, 18, 18);
        context.drawTexture(BACKGROUND_TEXTURE, x + borderSize, y + height - borderSize, borderSize, 14, centerWidth, borderSize, 18, 18);
        context.drawTexture(BACKGROUND_TEXTURE, x, y + borderSize, 0, borderSize, borderSize, centerHeight, 18, 18);
        context.drawTexture(BACKGROUND_TEXTURE, x + width - borderSize, y + borderSize, 14, borderSize, borderSize, centerHeight, 18, 18);

        // Center
        context.drawTexture(BACKGROUND_TEXTURE, x + borderSize, y + borderSize, borderSize, borderSize, centerWidth, centerHeight, 18, 18);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }
}
