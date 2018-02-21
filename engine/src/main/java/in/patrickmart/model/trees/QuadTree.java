package in.patrickmart.model.trees;

import in.patrickmart.model.AABB;
import in.patrickmart.model.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Quadtrees store contents by location, allowing for quick retrieval of objects near some point.
 * @param <T> Boundable objects can be placed in 2D space, and have some height and width
 */
public class QuadTree<T extends Boundable> {
    private AABB bounds; // The outer bounds of this QuadTree.
    private ArrayList<T> objects; // A list of objects that are located within the bounds of this QuadTree.
    private QuadTree<T>[] children; // An array of 4 smaller QuadTrees that together make up the same area as this one.
                                    //  1 | 0
                                    //  --+--
                                    //  2 | 3

    private static final int OBJECTS_PER_NODE = 32;

    /**
     * Constructor for objects of class QuadTree.
     * @param bounds The Axis-Aligned Bounding Box that defines this QuadTree's covered area
     */
    public QuadTree(AABB bounds) {
        this.bounds = bounds;
    }

    /**
     * Creates 4 children in order to store more objects.
     */
    private void split() {
        double halfWidth = bounds.getHalfWidth();
        double quarterWidth = halfWidth / 2;
        double halfHeight = bounds.getHalfHeight();
        double quarterHeight = halfHeight / 2;
        double x = bounds.getCenter().getX();
        double y = bounds.getCenter().getY();

        children = new QuadTree[4];
        children[0] = new QuadTree<>(new AABB(x + quarterWidth, y + quarterWidth, quarterWidth, quarterHeight));
        children[1] = new QuadTree<>(new AABB(x - quarterWidth, y + quarterWidth, quarterWidth, quarterHeight));
        children[2] = new QuadTree<>(new AABB(x - quarterWidth, y - quarterWidth, quarterWidth, quarterHeight));
        children[3] = new QuadTree<>(new AABB(x + quarterWidth, y - quarterWidth, quarterWidth, quarterHeight));
    }

    /**
     * Attempts to insert an object into this QuadTree node or one of its child nodes if it has them.
     * @param object The boundable object to be inserted
     * @return whether or not the insertion was successful
     */
    public boolean insert(T object) {
        // First see if there is a more specific node for this object to be in.
        if (children != null && insertToChild(object)) {
            return true;
        }

        // If this node is full, we have some work to do.
        if (objects != null && objects.size() == OBJECTS_PER_NODE) {
            if (children == null) {
                // Split this node and redistribute its objects to the children.
                split();
                for (int i = 0; i < objects.size(); i++) {
                    if (insertToChild(objects.get(i))) {
                        objects.remove(i);
                        i--;
                    }
                }
            }

            // Now try to throw the object to a child.
            if (!insertToChild(object)) {
                // Children are full? maybe there is room in this node after redistribution.
                if (objects.size() == OBJECTS_PER_NODE) {
                    return false;
                } else {
                    objects.add(object);
                }
            }
        } else {
            // Put the object in this node.
            if (objects == null) {
                objects = new ArrayList<T>();
            }
            objects.add(object);
        }
        return true;
    }

    /**
     * Tests whether an object belongs in one of this node's children, and passes it to that child.
     * @param object The object to be inserted
     * @return Whether or not the insertion was successful
     */
    private boolean insertToChild(T object) {
        AABB objectBounds = object.getBounds();
        boolean success = false;
       	double midX = bounds.getCenter().getX();
	double midY = bounds.getCenter().getY();
	double halfWidth = bounds.getHalfWidth();
	double halfHeight = bounds.getHalfHeight();

	double top = objectBounds.getCenter().getY() + objectBounds.getHalfHeight();
	double bottom = objectBounds.getCenter().getY() - objectBounds.getHalfHeight();
	double left = objectBounds.getCenter.getX() - objectBounds.getHalfWidth();
	double right = objectBounds.getCenter.getX() + objectBounds.getHalfWidth();

	if (left > midX && right <= midX + halfWidth) {
		if (bottom > midY && top <= midY + halfHeight) {
			success = children[0].insert(object);
		} else if (bottom > midY - halfHeight && top <= midY) {
			success = children[3].insert(object);
		}
	} else if (left > midX - halfWidth && right <= midX) {
		if ( bottom > midY && top <= midY + halfHeight) {
			success = children[1].insert(object);
		} else if (bottom > midY - halfHeight && top <= midY) {
			success = children[2].insert(object);
		}
	}
        return success;
    }

    public void query(AABB bounds, List<T> results) {

    }
}
