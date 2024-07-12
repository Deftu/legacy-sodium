package net.caffeinemc.mods.sodium.client.data.fingerprint;

import net.minecraft.client.Minecraft;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class FingerprintMeasure {
    private static final int SALT_LENGTH = 64;

    @NotNull
    public final String uuid;

    @NotNull
    public final String path;

    public static @Nullable FingerprintMeasure create() {
        UUID uuid = Minecraft.getMinecraft().getSession().getProfile().getId();
        File path = Minecraft.getMinecraft().mcDataDir;

        if (uuid == null || path == null) {
            return null;
        }

        return new FingerprintMeasure(uuid.toString(), path.getAbsoluteFile().toString());
    }

    public FingerprintMeasure(@NotNull String uuid, @NotNull String path) {
        this.uuid = uuid;
        this.path = path;
    }

    public HashedFingerprint hashed() {
        Instant date = Instant.now();
        String salt = createSalt();

        String uuidHashHex = sha512(salt, this.uuid);
        String pathHashHex = sha512(salt, this.path);

        return new HashedFingerprint(HashedFingerprint.CURRENT_VERSION, salt, uuidHashHex, pathHashHex, date.getEpochSecond());
    }

    public boolean looselyMatches(HashedFingerprint hashed) {
        String uuidHashHex = sha512(hashed.saltHex, this.uuid);
        String pathHashHex = sha512(hashed.saltHex, this.path);

        return Objects.equals(uuidHashHex, hashed.uuidHashHex) || Objects.equals(pathHashHex, hashed.pathHashHex);
    }

    private static String sha512(@NotNull String salt, @NotNull String message) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(Hex.decodeHex(salt.toCharArray()));
            md.update(message.getBytes(StandardCharsets.UTF_8));
        } catch (Throwable t) {
            throw new RuntimeException("Failed to hash value", t);
        }

        return Hex.encodeHexString(md.digest());
    }

    private static String createSalt() {
        SecureRandom rng = new SecureRandom();

        byte[] salt = new byte[SALT_LENGTH];
        rng.nextBytes(salt);

        return Hex.encodeHexString(salt);
    }
}
