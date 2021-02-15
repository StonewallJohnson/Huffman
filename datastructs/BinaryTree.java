package datastructs;
public class BinaryTree<E extends Comparable<E>>{
    private int size;
    private Node<E> root;

    /**
     * Creates a BinaryTree with size = 0 and 
     * a null root
     */
    public BinaryTree(){
        size = 0;
        root = null;
    }
    
    public BinaryTree(E val){
        add(val);
    }

    private static class Node<E>{
        private E value;
        private Node<E> leftChild;
        private Node<E> rightChild;

        /**
         * Creates a node with a left child, value, and right child
         * @param left the left child of this node
         * @param val the value of this node
         * @param right the right child of this node
         */
        private Node(Node<E> left, E val, Node<E> right){
            value = val;
            leftChild = left;
            rightChild = right;
        }

        /**
         * Creates a node with a given value and sets the 
         * children to null
         * @param val the value to be contained in the node
         */
        private Node(E val){
            this(null, val, null);
        }

        /**
         * Returns the value of this node
         */
        private E getValue(){
            return value;
        }

        /**
         * Sets the nodes value to the specified value
         */
        private void setValue(E newVal){
            value = newVal;
        }

        /**
         * Returns the left child of this node
         */
        private Node<E> getLeft(){
            return leftChild;
        }

        /**
         * Returns the right child of this node
         */
        private Node<E> getRight(){
            return rightChild;
        }

        /**
         * Sets this node's left child to left
         * @param left the new left child
         */
        private void setLeft(Node<E> left){
            leftChild = left;
        }
        
        /**
         * Sets this node's right child to right
         * @param right the new right child of this node
         */
        private void setRight(Node<E> right){
            rightChild = right;
        }

        private int numChildren(){
            final int ZERO_CHILDREN = 0;
            final int ONE_CHILD = 1;
            final int TWO_CHILDREN = 2;
            if(this.getRight() == null && this.getLeft() == null){
                //no children
                return ZERO_CHILDREN;
            }
            else if(this.getRight() == null ^ this.getLeft() == null){
                //one child
                return ONE_CHILD;
            }
            else{
                //two children
                return TWO_CHILDREN;
            }
        }
    }

    /**
     * 
     * @return the number of nodes in the tree
     */
    int size(){
        return size;
    }

    /**
     * creates a node and inserts it into the binary tree
     * @param val the value to be added to the tree
     */
    public void add(E val){
        checkValNull(val);
        root = insert(root, val);
        size++;
    }

    /**
     * A function which inserts the given value into the
     * tree
     * @param root begins as root of tree, becomes nodes along the path to val
     * @param val the value to be inserted into the tree
     * @return 
     */
    private Node<E> insert(Node<E> node, E val){
        if(node == null){
            //base case, make the new node
            node = new Node<E>(val);
        }
        else{
            //recursive
            int comp = node.getValue().compareTo(val);
            if(comp > 0){
                //val < root.val, go left
                node.setLeft(insert(node.getLeft(), val));
            }
            else{
                //val > root.val, go right
                node.setRight(insert(node.getRight(), val));
            }
            //node is duplicate
        }
        return node;
    }

    /**
     * Removes a given value from the tree
     * @param val the val to be removed from the tree
     */
    public void remove(E val){
        checkValNull(val);
        root = removeRecur(root, val);
        size--;
    }

    private Node<E> removeRecur(Node<E> current, E val){
        if(current == null){
            //root is null, tree is empty
            return current;
        }
        else{
            //recur down tree
            int comp = current.getValue().compareTo(val);
            if(comp > 0){
                //val < current.val, go left
                current.setLeft(removeRecur(current.getLeft(), val));
            }
            else if(comp < 0){
                //val > current.val, go right
                current.setRight(removeRecur(current.getRight(), val));
            }
            else{
                //val == current.val, remove node
                switch(current.numChildren()){
                    case 0:
                        //no children; make this node empty
                        current = null;
                        break;
                    case 1:
                        //one child; make this node the child node
                        current = current.getLeft() == null ? current.getRight() : current.getLeft();
                        break;
                    case 2:
                        //two children; change this node to next in order
                        Node<E> temp = current.getRight();
                        while(temp.getLeft() != null){
                            //until leftmost leaf is found
                            temp = temp.getLeft();
                        }
                        //set
                        current.setValue(temp.getValue());
                        //remove extraneous node
                        current.setRight(removeRecur(current.getRight(), temp.getValue()));
                        break;
                }
                
            }
            return current;
        }
    }
    
    /**
     * Finds the given node with the given val in the tree
     * @param val the value being searched for
     * @return the node with the given val or 
     * null if the that val is not in the tree
     */
    public E get(E val){
        checkValNull(val);
        Node<E> temp = root;
        while (temp != null && !temp.getValue().equals(val)){
            // until the proper child is found or fell out of tree
            int comp = temp.getValue().compareTo(val);
            if(comp > 0){
                // temp.val > val, go left
                temp = temp.getLeft();
            }
            else if(comp < 0){
                // temp.val < val, go right
                temp = temp.getRight();
            }
        }
        return temp.getValue();
    }
    
 
    /**
     * Creates a preOrder string of the elements in the tree
     */
    public String preOrder(){
        StringBuilder str = new StringBuilder();
        recurPre(str, root);
        return str.toString();
    }

    private void recurPre(StringBuilder str, Node<E> current){
        if(current == null){
            //base case, do not add anything to string
        }
        else{
            //recursive
            str.append(current.getValue().toString());
            str.append(", ");
            recurPre(str, current.getLeft());
            recurPre(str, current.getRight());
        }
    }
    
    /**
     * 
     * @return a string of the elements in the tree inorder
     */
    public String inOrder(){
        StringBuilder str = new StringBuilder();
        recurIn(str, root);
        return str.toString();
    }
    
    private void recurIn(StringBuilder str, Node<E> current){
        if(current == null){
            //base case, do not add anything
        }
        else{
            //recursive
            recurIn(str, current.getLeft());
            str.append(current.getValue().toString());
            str.append(", ");
            recurIn(str, current.getRight());
        }
    }

    /**
     * 
     * @return a string of the values in this tree in postorder
     */
    public String postOrder(){
        StringBuilder str = new StringBuilder();
        recurPost(str, root);
        return str.toString();
    }

    private void recurPost(StringBuilder str, Node<E> current){
        if(current == null){
            //base case, do not add anything
        }
        else{
            //recursive
            recurPost(str, current.getLeft());
            recurPost(str, current.getRight());
            str.append(current.getValue().toString());
            str.append(", ");
        }
    }

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("Pre: " + preOrder() + "\n");
        ret.append("In: " + inOrder() + "\n");
        ret.append("Post: " + postOrder());
        return ret.toString();
    }

    /**
     * Checks to see if the given val is null and throws
     * IAE if it is
     * @param val the value to be checked
     */
    private void checkValNull(E val){
        if(val == null){
            throw new IllegalArgumentException("This tree can not store null values!");
        }
    }
}