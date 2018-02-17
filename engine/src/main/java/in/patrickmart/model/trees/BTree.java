package in.patrickmart.model.trees;

import in.patrickmart.model.Entity;

public class BTree {
    private BTreeNode root; //root of B-tree
    private int height;
    private int n; //items in tree

    /**
     * intialize the BTree with the first entity created.
     * @param first
     */
    public BTree(Entity first)
    {
        root = new BTreeNode(0,first,false);
        n = 1;
        height = 1;
    }

    /**
     * add a node at the same height if possible.
    */
    public void addNode(Entity e)
    {
    }

    /**
     * search by the entity to find id
     * todo
     */
     public int search(Entity e)
     {
     return root.id;
     }

     /**
      * search by id
     * todo
    */
    public Entity search(int id)
    {
        return root.e;
    }

    public String toString()
    {
        //todo
        return "todo";
    }


}
