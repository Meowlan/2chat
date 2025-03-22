package de.meowlan.twochat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.meowlan.twochat.networking.MessagePacket;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.command.CommandRegistryAccess;

import java.util.Collection;

public class TwochatCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerSecondChatCommand(dispatcher, registryAccess));
    }

    private static void registerSecondChatCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal(Twochat.MOD_ID)
                .requires(source -> source.hasPermissionLevel(2)) // Require permission level 2 (ops)
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                    .then(CommandManager.argument("message", TextArgumentType.text(registryAccess))
                        .executes(context -> {
                            Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                            Text message = TextArgumentType.getTextArgument(context, "message");
                            MessagePacket packet = new MessagePacket(message);

                            for (ServerPlayerEntity player : players) {
                                ServerPlayNetworking.send(player, packet);
                            }

                            context.getSource().sendFeedback(() -> Text.literal("Message sent to second chat"), true);
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
        );
    }
}