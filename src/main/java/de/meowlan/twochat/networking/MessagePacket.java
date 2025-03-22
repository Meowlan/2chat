package de.meowlan.twochat.networking;

import de.meowlan.twochat.Twochat;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

public record MessagePacket(Text text) implements CustomPayload {
    public static final CustomPayload.Id<MessagePacket> ID = new Id<>(Identifier.of(Twochat.MOD_ID, "message"));
    public static final PacketCodec<RegistryByteBuf, MessagePacket> CODEC = PacketCodec.tuple(
        TextCodecs.PACKET_CODEC, MessagePacket::text, MessagePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}