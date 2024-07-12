package net.caffeinemc.mods.sodium.client.platform.windows;

import net.caffeinemc.mods.sodium.client.platform.windows.api.version.VersionFixedFileInfoStruct;
import org.jetbrains.annotations.NotNull;

public class WindowsFileVersion {

    public final int
            x,
            y,
            z,
            w;

    public WindowsFileVersion(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public String toString() {
        return String.format("%s.%s.%s.%s", this.x, this.y, this.z, this.w);
    }

    public static @NotNull WindowsFileVersion fromFileVersion(VersionFixedFileInfoStruct fileVersion) {
        int x = (fileVersion.getFileVersionMostSignificantBits() >>> 16) & 0xffff;
        int y = (fileVersion.getFileVersionMostSignificantBits() >>>  0) & 0xffff;
        int z = (fileVersion.getFileVersionLeastSignificantBits() >>> 16) & 0xffff;
        int w = (fileVersion.getFileVersionLeastSignificantBits() >>>  0) & 0xffff;

        return new WindowsFileVersion(x, y, z, w);
    }

}
