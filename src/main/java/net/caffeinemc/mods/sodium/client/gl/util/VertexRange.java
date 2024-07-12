package net.caffeinemc.mods.sodium.client.gl.util;

public class VertexRange {

    private final int vertexStart, vertexCount;

    public VertexRange(int vertexStart, int vertexCount) {
        this.vertexStart = vertexStart;
        this.vertexCount = vertexCount;
    }

    public int vertexStart() {
        return vertexStart;
    }

    public int vertexCount() {
        return vertexCount;
    }

}
