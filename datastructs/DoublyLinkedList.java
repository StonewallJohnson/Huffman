package datastructs;

public class DoublyLinkedList<E extends Comparable<E>>{
    private int size;
    Node<E> head;
    Node<E> tail;

    private class Node<T>{
        private T con;
        private Node<T> next;
        private Node<T> previous;

        public Node(Node<T> prev, T val, Node<T> link){
            con = val;
            next = link;
            previous = prev;
        }

        public Node(T val){
            this(null, val, null);
        }
    }

    public DoublyLinkedList(){
        size = 0;
        head = null;
        tail = null;
    }

    /**
     * Adds the given value to the linked list
     * @param val value to add
     */
    public void add(E val){
        checkValNull(val);
        
        if(size == 0){
            //adding head
            addFirstNodeOfList(val);
        }
        else{
            //adding other
            Node<E> temp = head;
            while(temp.next != null){
                //until last node
                temp = temp.next;
            }
            temp.next = new Node<E>(temp, val, null);
            tail = temp.next;
        }
        size++;
    }

    /**
     * Inserts the val into the proper index of the list
     * given the ordering imposed by compareTo().
     * Head is least val
     * Tail is greatest val
     * @param val val to be inserted
     */
    public void insert(E val){
        checkValNull(val);
        if(size() == 0){
            //first node
            addFirstNodeOfList(val);
        }
        else if(val.compareTo(head.con) < 0){
            //val < smallest val, this node belongs at the front
            addFront(val);
        }
        else if(val.compareTo(tail.con) > 0){
            //val > largest val, this node belongs at end
            addBack(val);
        }
        else{
            //this val goes into middle
            Node<E> temp = tail;
            while(val.compareTo(temp.con) < 0){
                //while val < temp.con, move up list
                temp = temp.previous; 
            }
            //insert val after temp
            Node<E> oldNext = temp.next;
            temp.next = new Node<E>(temp, val, oldNext);
            oldNext.previous = temp.next;
            size++;
        }
    }

    /**
     * Makes this value the head node
     * @param val value to be added
     */
    public void addFront(E val){
        if(size() == 0){
            //adding first node
            addFirstNodeOfList(val);
        }
        else{
            //non first node
            Node<E> temp = new Node<E>(null, val, head);
            head.previous = temp;
            head = temp;
            size++;
        }
    }

    /**
     * Adds a value to the end of the linked list
     * @param val value to add
     */
    public void addBack(E val){
        if(size() == 0){
            //adding first node
            addFirstNodeOfList(val);
        }
        else{
            //adding non first node
            Node<E> temp = new Node<E>(tail, val, null);
            tail.next = temp;
            tail = temp;
            size++;
        }
    }

    private void addFirstNodeOfList(E val){
        Node<E> temp = new Node<E>(val);
        head = temp;
        tail = temp;
        size++;
    }

    /**
     * Finds the given value in the linked list
     * @param val the value searched for
     * @return the val if found, null if not
     */
    public E get(E val){
        return getRecur(head, val);
    }

    /**
     * 
     * @return the head of the linked list
     */
    public E getHead(){
        return head.con;
    }

    private E getRecur(Node<E> current, E val){
        if(current == null){
            //base case: val not in list
            return null;
        }
        else if(current.con.equals(val)){
            //base case: found val
            return current.con;
        }
        else{
            //recurse
            return getRecur(current.next, val);
        }
    }

    /**
     * @return the value of the tail node
     */
    public E getTail(){
        return tail.con;
    }
    
    /**
     * Removes the head node from the list
     */
    public E removeHead(){
        E temp = head.con;
        if(size() == 1){
            //only node in list
            empty();
        }
        else{
            //removing head that is not last node
            head = head.next;
            head.previous = null;
            size--;
        }
        return temp;
    }

    public E removeTail(){
        E temp = tail.con;
        if(size() == 1){
            //removing last node
            empty();
        }
        else{
            //removing tail that is not only node
            tail = tail.previous;
            tail.next = null;
            size--;
        }
        return temp;
    }

    /**
     * Removes the given value from the linked list
     * @param val the value to be removed
     */
    public void remove(E val){
        Node<E> temp = head;
        if(temp.con.equals(val)){
            removeHead();
        }
        else if(tail.con.equals(val)){
            //removing tail
            removeTail();
        }
        else{
            //removing internal node
            while(temp.next != null && !temp.next.con.equals(val)){
                //temp.next is not val and not null
                temp = temp.next;
            }
            if(temp.next != null){
                //val is found
                temp.next = temp.next.next;
                temp.next.previous = temp;
            }
            size--;
        }
    }

    /**
     * Makes the linked list empty
     */
    public void empty(){
        head = null;
        tail = null;
        size = 0;
    }

    public int size(){
        return size;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        Node<E> temp = head;
        while(temp != null){
            //for every element in the list
            str.append(temp.con.toString() + ", ");
            temp = temp.next;
        }

        return str.toString();
    }

    private void checkValNull(E val){
        if(val == null){
            throw new IllegalArgumentException("Can't store null!");
        }
    }
}