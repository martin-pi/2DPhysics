package in.patrickmart.model.trees;

import in.patrickmart.model.Entity;

public class BTree {
    private final static int M = 4; //must be greater than 2 and even
    private BTreeNode root; //root of B-tree
    private int height;
    private int n; //items in tree

    /**
     * intialize the BTree with the first entity created.
     * @param first
     */
    public BTree(Entity first)
    {
        root = new BTreeNode(0,first); //create root node with no children
        n = 1;
        height = 0;
    }

    /**
     * add a node at the same height if possible.
     * @param id new node's id
     * @param e entity to be stored
    */
    public void addNode(int id, Entity e)
    {
        BTreeNode t = new BTreeNode(n,e);
        n++;
        insert(root, t);

    }

    /**
     * insert node and rebalance tree.
     * @param c node check for insert
     * @param t node to be inserted
     */
    public void insert(BTreeNode c, BTreeNode t)
    {
        if(c.id > t.id)
        {
            for (int i = 0; i < c.children.length; i++)
            {
                if(c.children[i].id >= t.id)
                {
                    //add to that childs children if possible.
                }
                else if(c.children[i+1] == null) //if next child is empty
                {
                    c.children[i + 1] = t;
                }
                else if(c.children.length == i + 1) //if all children are smaller
                {
                    //need to re-balance the tree at this point.
                }
            }
        }
    }

    /**
     * search by the entity to find id.
     *
     * @param b node to search from.
     * @param id id to search for.
     * @param h current node height.
     * @return found node or null.
     */
     public BTreeNode search(BTreeNode b, int id, int h)
     {
         if (h == 0) //external node (children are leaves)
         {
             for (int i = 0; i < b.n; i++)
             {
                 if(b.children[i].id == id){
                     return b.children[i];
                 }
             }
         }
         else{ //internal node
             for (int i = 0; i < b.n; i++)
             {
                 if (b.children[i].id == id){
                     return b.children[i];
                 }
                 if (b.children[i].id > id) {
                     return search(b.children[i], id, h - 1);
                 }
             }
         }
         return null;
     }

    /**
     * converts tree and nodes to strings.
     * @return string representation of the tree.
     */
     public String toString()
     {
        //travers every node calling their to string method
        return "todo";
     }
}
