package in.patrickmart.model.trees;

import in.patrickmart.model.Entity;
import in.patrickmart.model.trees.BTree;

public class BTreeNode {
    private final static int M = 4; //max children
    int id;         //id of entity. could later change to be significant info about entity stored.
    int n;          // number of children
    Entity e;       //the entity stored in this node
    BTreeNode[] children = new BTreeNode[M]; //array of children id's
    BTreeNode next; //to traverse nodes.

    public BTreeNode(int id,Entity e, boolean leaf)
    {
        this.id = id;
        this.e = e;
        this.next = null;
        n = 0;
    }
}
