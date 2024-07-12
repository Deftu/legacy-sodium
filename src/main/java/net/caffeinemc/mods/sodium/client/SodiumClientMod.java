package net.caffeinemc.mods.sodium.client;

import net.caffeinemc.mods.sodium.client.data.fingerprint.FingerprintMeasure;
import net.caffeinemc.mods.sodium.client.data.fingerprint.HashedFingerprint;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptions;
import net.caffeinemc.mods.sodium.client.gui.console.Console;
import net.caffeinemc.mods.sodium.client.gui.console.message.MessageLevel;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(modid = "legacy-sodium")
public class SodiumClientMod {

    private static Logger LOGGER = LogManager.getLogger("Sodium");

    private static SodiumGameOptions CONFIG;
    private static String MOD_VERSION;

    @Mod.EventHandler
    public void onInitializeClient(FMLInitializationEvent event) {
        CONFIG = loadConfig();
        MOD_VERSION = Loader.instance().activeModContainer().getVersion();

        try {
            updateFingerprint();
        } catch (Throwable t) {
            LOGGER.error("Failed to update fingerprint", t);
        }
    }

    public static SodiumGameOptions options() {
        if (CONFIG == null) {
            throw new IllegalStateException("Config not yet available");
        }

        return CONFIG;
    }

    public static Logger logger() {
        if (LOGGER == null) {
            throw new IllegalStateException("Logger not yet available");
        }

        return LOGGER;
    }

    public static void restoreDefaultOptions() {
        CONFIG = SodiumGameOptions.defaults();

        try {
            SodiumGameOptions.writeToDisk(CONFIG);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write config file", e);
        }
    }

    public static String getVersion() {
        if (MOD_VERSION == null) {
            throw new NullPointerException("Mod version hasn't been populated yet");
        }

        return MOD_VERSION;
    }

    private static SodiumGameOptions loadConfig() {
        try {
            return SodiumGameOptions.loadFromDisk();
        } catch (Exception e) {
            LOGGER.error("Failed to load configuration file", e);
            LOGGER.error("Using default configuration file in read-only mode");

            Console.instance().logMessage(MessageLevel.SEVERE, new ChatComponentText("sodium.console.config_not_loaded"), 12.5);

            SodiumGameOptions config = SodiumGameOptions.defaults();
            config.setReadOnly();

            return config;
        }
    }

    private static void updateFingerprint() {
        FingerprintMeasure current = FingerprintMeasure.create();

        if (current == null) {
            return;
        }

        HashedFingerprint saved = null;

        try {
            saved = HashedFingerprint.loadFromDisk();
        } catch (Throwable t) {
            LOGGER.error("Failed to load existing fingerprint",  t);
        }

        if (saved == null || !current.looselyMatches(saved)) {
            HashedFingerprint.writeToDisk(current.hashed());

            CONFIG.notifications.hasSeenDonationPrompt = false;
            CONFIG.notifications.hasClearedDonationButton = false;

            try {
                SodiumGameOptions.writeToDisk(CONFIG);
            } catch (IOException e) {
                LOGGER.error("Failed to update config file", e);
            }
        }
    }

}
