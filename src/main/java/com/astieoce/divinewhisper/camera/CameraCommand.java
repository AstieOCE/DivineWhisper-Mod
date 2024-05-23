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
    private static final RecordingSettings settings = new RecordingSettings();

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
                        .executes(context -> CameraSaving.listRecordings(context.getSource())))
                .then(literal("settings")
                        .then(argument("setting", StringArgumentType.string())
                                .then(argument("value", StringArgumentType.string())
                                        .executes(context -> {
                                            String setting = StringArgumentType.getString(context, "setting");
                                            String value = StringArgumentType.getString(context, "value");
                                            setSetting(setting, value);
                                            if (client.player != null) {
                                                client.player.sendMessage(Text.literal("Setting " + setting + " updated to " + value));
                                            }
                                            return 1;
                                        }))))
        );
    }

    private static void setSetting(String setting, String value) {
        switch (setting) {
            case "recordInteracts":
            case "enableGravity":
            case "recordTime":
            case "relativePosition":
            case "recordInventory":
            case "recordGamemode":
            case "recordState":
            case "recordSelfSkin":
            case "recordEntities":
                settings.setSetting(setting, Boolean.parseBoolean(value));
                break;
            case "playbackSpeed":
                settings.setSetting(setting, Float.parseFloat(value));
                break;
            case "gamemode":
                settings.setSetting(setting, value);
                break;
            default:
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("Unknown setting: " + setting));
                }
                break;
        }
    }

    private static int startRecordingCommand() {
        CameraControl.startRecording(settings);
        if (client.player != null) {
            client.player.sendMessage(Text.literal("Started recording"));
        }
        return 1;
    }

    private static int stopRecordingCommand() {
        CameraControl.stopRecording();
        String filename = "recording_" + CameraSaving.DATE_FORMAT.format(new Date()) + ".json";
        CameraSaving.saveRecording(filename, settings.getAllSettings());
        if (client.player != null) {
            client.player.sendMessage(Text.literal("Stopped recording and saved to " + filename));
        }
        return 1;
    }
}
