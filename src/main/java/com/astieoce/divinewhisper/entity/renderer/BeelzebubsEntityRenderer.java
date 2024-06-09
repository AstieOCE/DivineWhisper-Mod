package com.astieoce.divinewhisper.entity.renderer;

import com.astieoce.divinewhisper.DivineWhisper;
import com.astieoce.divinewhisper.entity.BeelzebubsEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import com.astieoce.divinewhisper.entity.renderer.BeelzebubsEntityRenderer;
import net.minecraft.util.Identifier;

public class BeelzebubsEntityRenderer extends MobEntityRenderer<BeelzebubsEntity, BeelzebubsEntityModel> {

    private static final Identifier TEXTURE = new Identifier(DivineWhisper.MOD_ID, "textures/entity/beelzebubs.png");

    public BeelzebubsEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BeelzebubsEntityModel(context.getPart(BeelzebubsEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public Identifier getTexture(BeelzebubsEntity entity) {
        return TEXTURE;
    }
}
