package com.astieoce.divinewhisper.camera;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.astieoce.divinewhisper.DivineWhisper.client;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CameraCommand {
    private static final RecordingSettings settings = new RecordingSettings();
    private static final List<String> booleanSettings = Arrays.asList(
            "recordInteracts", "enableGravity", "recordTime", "relativePosition",
            "recordInventory", "recordGamemode", "recordState", "recordSelfSkin", "recordEntities"
    );
    private static final List<String> gamemodes = Arrays.asList("Survival", "Creative", "Spectator");

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess) {
        // registryAccess never ends up being used. Don't need it.
        dispatcher.register(literal("camera")
                .then(literal("record")
                        .executes(context -> startRecordingCommand()))
                .then(literal("stop")
                        .executes(context -> stopRecordingCommand()))
                .then(literal("playback")
                        .then(argument("filename", StringArgumentType.string())
                                .suggests(CameraCommand::suggestRecordings)
                                .executes(context -> {
                                    CameraPlayback.playback(context.getSource(), StringArgumentType.getString(context, "filename"));
                                    return 1;
                                })))
                .then(literal("list")
                        .executes(context -> CameraSaving.listRecordings(context.getSource())))
                .then(literal("settings")
                        .then(argument("setting", StringArgumentType.string())
                                .suggests(CameraCommand::suggestSettings)
                                .then(argument("value", StringArgumentType.string())
                                        .suggests(CameraCommand::suggestSettingValues)
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
        if (booleanSettings.contains(setting)) {
            settings.setSetting(setting, Boolean.parseBoolean(value));
        } else if ("playbackSpeed".equals(setting)) {
            settings.setSetting(setting, Float.parseFloat(value));
        } else if ("gamemode".equals(setting)) {
            settings.setSetting(setting, value);
        } else {
            if (client.player != null) {
                client.player.sendMessage(Text.literal("Unknown setting: " + setting));
            }
        }
    }

    private static CompletableFuture<Suggestions> suggestSettings(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        booleanSettings.stream()
                .filter(setting -> setting.toLowerCase().startsWith(input))
                .forEach(builder::suggest);
        if ("playbackSpeed".startsWith(input)) {
            builder.suggest("playbackSpeed");
        }
        if ("gamemode".startsWith(input)) {
            builder.suggest("gamemode");
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestSettingValues(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        String setting = StringArgumentType.getString(context, "setting").toLowerCase();
        String input = builder.getRemaining().toLowerCase();

        if (booleanSettings.contains(setting)) {
            if ("true".startsWith(input)) {
                builder.suggest("true");
            }
            if ("false".startsWith(input)) {
                builder.suggest("false");
            }
        } else if ("playbackSpeed".equals(setting)) {
            if ("0.5".startsWith(input)) {
                builder.suggest("0.5");
            }
            if ("1.0".startsWith(input)) {
                builder.suggest("1.0");
            }
            if ("2.0".startsWith(input)) {
                builder.suggest("2.0");
            }
        } else if ("gamemode".equals(setting)) {
            gamemodes.stream()
                    .filter(gamemode -> gamemode.toLowerCase().startsWith(input))
                    .forEach(builder::suggest);
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestRecordings(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        return CameraSaving.suggestRecordings(builder, input);
    }

    private static int startRecordingCommand() {
        CameraControl.startRecording(settings);
        if (client.player != null) {
            client.player.sendMessage(Text.literal("Started recording"), false);
        }
        return 1;
    }

    private static int stopRecordingCommand() {
        CameraControl.stopRecording();
        String filename = "recording_" + CameraSaving.DATE_FORMAT.format(new Date()) + ".json";
        CameraSaving.saveRecording(filename, settings.getAllSettings());
        if (client.player != null) {
            client.player.sendMessage(Text.literal("Stopped recording and saved to " + filename), true);
        }
        return 1;
    }
}
