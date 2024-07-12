package net.caffeinemc.mods.sodium.client.gl.attribute;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * An enumeration over the supported data types that can be used for vertex attributes.
 */
public class GlVertexAttributeFormat {

    public static final GlVertexAttributeFormat FLOAT = new GlVertexAttributeFormat(GL11.GL_FLOAT, 4);

    public static final GlVertexAttributeFormat BYTE = new GlVertexAttributeFormat(GL11.GL_BYTE, 1);
    public static final GlVertexAttributeFormat UNSIGNED_BYTE = new GlVertexAttributeFormat(GL11.GL_UNSIGNED_BYTE, 1);

    public static final GlVertexAttributeFormat SHORT = new GlVertexAttributeFormat(GL11.GL_SHORT, 2);
    public static final GlVertexAttributeFormat UNSIGNED_SHORT = new GlVertexAttributeFormat(GL11.GL_UNSIGNED_SHORT, 2);

    public static final GlVertexAttributeFormat INT = new GlVertexAttributeFormat(GL11.GL_INT, 4);
    public static final GlVertexAttributeFormat UNSIGNED_INT = new GlVertexAttributeFormat(GL11.GL_UNSIGNED_INT, 4);

    public static final GlVertexAttributeFormat UNSIGNED_2_10_10_10_REV = new GlVertexAttributeFormat(GL12.GL_UNSIGNED_INT_2_10_10_10_REV, 4);

    private final int typeId, size;

    private GlVertexAttributeFormat(int typeId, int size) {
        this.typeId = typeId;
        this.size = size;
    }

    public int typeId() {
        return typeId;
    }

    public int size() {
        return size;
    }

}
