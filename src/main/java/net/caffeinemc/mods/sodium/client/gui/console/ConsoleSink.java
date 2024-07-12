package net.caffeinemc.mods.sodium.client.gui.console;

import net.caffeinemc.mods.sodium.client.gui.console.message.MessageLevel;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public interface ConsoleSink {
    void logMessage(@NotNull MessageLevel level, @NotNull IChatComponent text, double duration);
}
