package net.caffeinemc.mods.sodium.client.data.fingerprint;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import net.caffeinemc.mods.sodium.client.util.FileUtil;
import net.minecraftforge.fml.common.Loader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class HashedFingerprint {
    public static final int CURRENT_VERSION = 1;

    @SerializedName("v")
    public final int version;

    @NotNull
    @SerializedName("s")
    public final String saltHex;

    @NotNull
    @SerializedName("u")
    public final String uuidHashHex;

    @NotNull
    @SerializedName("p")
    public final String pathHashHex;

    @SerializedName("t")
    public final long timestamp;

    public HashedFingerprint(int version, @NotNull String saltHex, @NotNull String uuidHashHex, @NotNull String pathHashHex, long timestamp) {
        this.version = version;
        this.saltHex = saltHex;
        this.uuidHashHex = uuidHashHex;
        this.pathHashHex = pathHashHex;
        this.timestamp = timestamp;
    }

    public static @Nullable HashedFingerprint loadFromDisk() {
        Path path = getFilePath();

        if (!Files.exists(path)) {
            return null;
        }

        HashedFingerprint data;

        try {
            byte[] bytes = Files.readAllBytes(path);
            String json = new String(bytes);
            data = new Gson().fromJson(json, HashedFingerprint.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data file", e);
        }

        if (data.version != CURRENT_VERSION) {
            return null;
        }

        return data;
    }

    public static void writeToDisk(@NotNull HashedFingerprint data) {
        Objects.requireNonNull(data);

        try {
            FileUtil.writeTextRobustly(new Gson()
                    .toJson(data), getFilePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data file", e);
        }
    }

    private static Path getFilePath() {
        return new File(Loader.instance().getConfigDir(), "sodium-fingerprint.json").toPath();
    }
}
