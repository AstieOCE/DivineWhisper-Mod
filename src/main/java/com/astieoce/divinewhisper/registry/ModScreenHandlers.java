package com.astieoce.divinewhisper.registry;

import com.astieoce.divinewhisper.screen.AlchemyStationScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<AlchemyStationScreenHandler> ALCHEMY_STATION_SCREEN_HANDLER;

    static {
        ALCHEMY_STATION_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(
                new Identifier("divinewhisper", "alchemy_station"),
                AlchemyStationScreenHandler::create
        );
    }

    public static void registerAllScreenHandlers() {
        // This method ensures the static initializer is run, and the handler is registered
    }
}
