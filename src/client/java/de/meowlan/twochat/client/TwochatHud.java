package de.meowlan.twochat.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

import static de.meowlan.twochat.client.TwochatConfig.getConfig;

public class TwochatHud {
    private final MinecraftClient client;
    private final List<ChatMessage> messages = new ArrayList<>();
    private final int maxMessages = 100;

    public TwochatHud(MinecraftClient client) {
        this.client = client;
    }

    static class ChatMessage {
        final Text text;
        final long addedTime;

        ChatMessage(Text text) {
            this.text = text;
            this.addedTime = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - addedTime > getConfig().getFadeTimeMs();
        }

        float getOpacity() {
            long age = System.currentTimeMillis() - addedTime;
            long fadeTime = getConfig().getFadeTimeMs();

            // Start fading out after 80% of the lifetime
            long fadeOutStart = (long)(fadeTime * 0.8);

            if (age < fadeOutStart) {
                return 1.0f;
            } else {
                // Calculate opacity during fade out
                float fadeOutProgress = (float)(age - fadeOutStart) / (fadeTime - fadeOutStart);
                return Math.max(0.05f, 1.0f - fadeOutProgress);
            }
        }
    }

    public void addMessage(Text message) {
        messages.addFirst(new ChatMessage(message));
        if (messages.size() > maxMessages) {
            messages.removeLast();
        }
    }

    public void tick() {
        // Remove expired messages
        messages.removeIf(ChatMessage::isExpired);
    }

    public void render() {
        if (!getConfig().getVisible() || messages.isEmpty()) {
            return;
        }

        DrawContext context = new DrawContext(client, client.getBufferBuilders().getEntityVertexConsumers());

        // Get the actual screen resolution
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Reference resolution
        final float referenceWidth = 1920.0f;
        final float referenceHeight = 1080.0f;

        // Scale factors
        float scaleX = screenWidth / referenceWidth;
        float scaleY = screenHeight / referenceHeight;

        // Scale all values accordingly
        int posX = (int) (getConfig().getPosX() * scaleX);
        int posY = (int) (getConfig().getPosY() * scaleY);
        int width = (int) (getConfig().getWidth() * scaleX);
        int height = (int) (getConfig().getHeight() * scaleY);

        // Draw background
        context.fill(
            posX,
            posY,
            posX + width,
            posY + height,
            (int)(getConfig().getBackgroundOpacity() * 255) << 24
        );

        // Calculate visible lines that can fit in the chat window
        int lineHeight = client.textRenderer.fontHeight + 2;
        int maxVisibleLines = height / lineHeight;

        // Draw messages
        int startY = getConfig().getChatflowDown() ? posY + 2 : posY + height - lineHeight;

        for (int i = 0; i < Math.min(maxVisibleLines, messages.size()); i++) {
            ChatMessage message = messages.get(i);

            if (message.isExpired())
                return;

            int alpha = (int) (message.getOpacity() * 255) << 24;

            // Get text width
            int textWidth = client.textRenderer.getWidth(message.text.getString());
            int xOffset = switch (getConfig().getAlign()) {
                case 1 -> (width - textWidth) / 2;  // Center align
                case 2 -> width - textWidth - 4;    // Right align
                default -> 4;                       // Left align
            };

            // Compute Y position based on direction
            int y = getConfig().getChatflowDown() ? (startY + (i * lineHeight)) : (startY - (i * lineHeight));

            context.drawText(
                client.textRenderer,
                message.text,
                posX + xOffset,  // Adjusted X position
                y,
                (0x00FFFFFF) | alpha, // Ensure correct alpha blending
                true
            );
        }
    }
}