package com.astieoce.divinewhisper.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;

public class GlowingGlassPaneBlock extends PaneBlock {
    //TODO:
    // Aster you twit. You forgot to make a glowing version of the bloody NORMAL/CLEAR Glass Pane -_-

    public GlowingGlassPaneBlock(Settings settings) {
        super(settings);
    }

    public int getLuminance(BlockState state) {
        return 15;
    }
}
