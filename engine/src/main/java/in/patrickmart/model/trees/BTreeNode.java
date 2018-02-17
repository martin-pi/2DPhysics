package in.patrickmart.model.trees;

import in.patrickmart.model.Entity;

public class BTreeNode {
    int id; //id of entity
    boolean leaf; //is the node a leaf
    Entity e; //the entity stored in this node
    BTreeNode[] children; //array of children id's

    public BTreeNode(int id,Entity e, boolean leaf)
    {
        this.id = id;
        this.leaf = leaf;
        this.e = e;
    }
}
