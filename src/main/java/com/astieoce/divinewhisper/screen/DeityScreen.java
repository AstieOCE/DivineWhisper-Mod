package com.astieoce.divinewhisper.screen;

import com.astieoce.divinewhisper.DivineWhisper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DeityScreen extends Screen {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier("minecraft", "textures/block/stone.png");
    private ButtonWidget nextDeityButton;
    private ButtonWidget previousDeityButton;
    private ButtonWidget loreButton;
    private TextFieldWidget recentEventsTextBox;
    private int currentDeityIndex = 0;

    private final String[] deities = {"The Librarian", "Beelzebub", "The Observer"};
    private final int[] deityFavor = {300, -500, 100}; // Placeholder values

    public DeityScreen() {
        super(Text.literal("Deities Screen"));
    }

    @Override
    protected void init() {
        int x = (width - (int) (width * 0.8)) / 2;
        int y = (height - (int) (height * 0.8)) / 2;
        int elementWidth = (int) (width * 0.8);
        int elementHeight = (int) (height * 0.8);

        // Lore button
        loreButton = ButtonWidget.builder(Text.translatable("screen.divinewhisper.lore"), button -> {
                    System.out.println("LORE BUTTON PRESSED");
                })
                .dimensions(x + elementWidth - 100, y + 20, 80, 20)
                .tooltip(Tooltip.of(Text.translatable("screen.divinewhisper.lore.tooltip")))
                .build();

        // Navigation buttons
        previousDeityButton = ButtonWidget.builder(Text.literal("<"), button -> {
                    cycleDeity(-1);
                })
                .dimensions(x + 20, y + 20, 20, 20)
                .build();
        nextDeityButton = ButtonWidget.builder(Text.literal(">"), button -> {
                    cycleDeity(1);
                })
                .dimensions(x + 50, y + 20, 20, 20)
                .build();

        // Favor events label
        addDrawableChild(ButtonWidget.builder(Text.literal("Favor Events"), button -> {})
                .dimensions(x + 20, y + elementHeight - 120, elementWidth - 40, 20)
                .build()).active = false;

        // Recent events text box
        recentEventsTextBox = new TextFieldWidget(textRenderer, x + 20, y + elementHeight - 100, elementWidth - 40, 80, Text.literal(""));
        recentEventsTextBox.setText("Recent events affecting favor will be shown here.");
        recentEventsTextBox.setEditable(false);

        // Add elements to the screen
        addDrawableChild(loreButton);
        addDrawableChild(previousDeityButton);
        addDrawableChild(nextDeityButton);
        addDrawableChild(recentEventsTextBox);

        // Update the screen with the current deity info
        updateDeityInfo();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (width - (int) (width * 0.8)) / 2;
        int y = (height - (int) (height * 0.8)) / 2;
        context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, (int) (width * 0.8), (int) (height * 0.8));

        super.render(context, mouseX, mouseY, delta);

        // Draw favor bar
        drawFavorBar(context, x + 20, y + 60, deityFavor[currentDeityIndex]);

        // Draw deity name
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(deities[currentDeityIndex]), width / 2, y + 40, 0xFFFFFF);
    }

    private void drawFavorBar(DrawContext context, int x, int y, int favor) {
        int barWidth = 200;
        int barHeight = 20;
        int favorWidth = (int) ((favor + 1000) / 2000.0 * barWidth); // Convert favor to bar width

        context.fill(x, y, x + barWidth, y + barHeight, 0xFF000000); // Black background
        context.fill(x, y, x + favorWidth, y + barHeight, 0xFF00FF00); // Green for positive favor, adjust color as needed
    }

    private void cycleDeity(int direction) {
        currentDeityIndex = (currentDeityIndex + direction + deities.length) % deities.length;
        updateDeityInfo();
    }

    private void updateDeityInfo() {
        // Update recent events text box with placeholder text for each deity
        switch (currentDeityIndex) {
            case 0:
                recentEventsTextBox.setText("Recent events affecting The Librarian's favor.");
                break;
            case 1:
                recentEventsTextBox.setText("Recent events affecting Beelzebub's favor.");
                break;
            case 2:
                recentEventsTextBox.setText("Recent events affecting The Observer's favor.");
                break;
        }
    }
}
