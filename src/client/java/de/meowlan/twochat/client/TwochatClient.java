package de.meowlan.twochat.client;

import de.meowlan.twochat.Twochat;
import de.meowlan.twochat.networking.MessagePacket;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class TwochatClient implements ClientModInitializer {

    private TwochatHud twochatHud;

    @Override
    public void onInitializeClient() {
        Twochat.LOGGER.info("Initializing TwoChat client");

        // register config
        AutoConfig.register(TwochatConfig.class, Toml4jConfigSerializer::new);

        // initialize hud
        MinecraftClient client = MinecraftClient.getInstance();
        this.twochatHud = new TwochatHud(client);

        // register render callback
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> twochatHud.render());

        // register tick callback for updating the chat (for fadeout, etc.)
        ClientTickEvents.END_CLIENT_TICK.register(w -> twochatHud.tick());

        // register packet receiver
        ClientPlayNetworking.registerGlobalReceiver(MessagePacket.ID, (payload, context) -> client.execute(() -> twochatHud.addMessage(payload.text())));
    }
}