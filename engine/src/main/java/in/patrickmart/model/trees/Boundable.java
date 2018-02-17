package in.patrickmart.model.trees;

import in.patrickmart.model.AABB;

/**
 * Defines anything that has bounds, and therefore anything that can be placed in a Quadtree.
 */
public abstract interface Boundable {
    AABB getBounds();
}
