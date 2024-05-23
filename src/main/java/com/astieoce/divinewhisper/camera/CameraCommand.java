package com.astieoce.divinewhisper.camera;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import java.util.Date;

import static com.astieoce.divinewhisper.DivineWhisper.client;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CameraCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal("camera")
                .then(literal("record")
                        .executes(context -> startRecordingCommand()))
                .then(literal("stop")
                        .executes(context -> stopRecordingCommand()))
                .then(literal("playback")
                        .then(argument("filename", StringArgumentType.string())
                                .suggests((context, builder) -> CameraSaving.suggestRecordings(builder))
                                .executes(context -> {
                                    CameraPlayback.playback(context.getSource(), StringArgumentType.getString(context, "filename"));
                                    return 1;
                                })))
                .then(literal("list")
                        .executes(context -> CameraSaving.listRecordings(context.getSource()))));
    }

    private static int startRecordingCommand() {
        CameraControl.startRecording();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("Started recording"));
        }
        return 1;
    }

    private static int stopRecordingCommand() {
        CameraControl.stopRecording();
        String filename = "recording_" + CameraSaving.DATE_FORMAT.format(new Date()) + ".json";
        CameraSaving.saveRecording(filename);
        if (client.player != null) {
            client.player.sendMessage(Text.literal("Stopped recording and saved to " + filename));
        }
        return 1;
    }
}
