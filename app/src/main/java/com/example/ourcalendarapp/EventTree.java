package com.example.ourcalendarapp;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.Comparator;


public class EventTree<Event extends Comparable<? super com.example.ourcalendarapp.Event>>
{
    // Default Constructor
    public EventTree( )
    {
        root = null;
    }

    // Insert into tree, duplicates ignored
    public void insert( com.example.ourcalendarapp.Event x )
    {
        root = insert( x, root );
    }

    // Remove from tree
    public void remove( com.example.ourcalendarapp.Event x )
    {
        root = remove( x, root );
    }


   // Private helper function that recursively removes the node from the tree
    private AvlNode<Event> remove( com.example.ourcalendarapp.Event x, AvlNode<Event> t )
    {
        if( t == null )
            return t;   // Item not found; do nothing

        int compareResult = x.compare( x, t.element );

        if( compareResult < 0 )
            t.left = remove( x, t.left );
        else if( compareResult > 0 )
            t.right = remove( x, t.right );
        else if( t.left != null && t.right != null ) // Two children
        {
            t.element = findMin( t.right ).element;
            t.right = remove( t.element, t.right );
        }
        else
            t = ( t.left != null ) ? t.left : t.right;
        return balance( t );
    }

    // Find the smallest item in the tree
    public com.example.ourcalendarapp.Event findMin( )
    {
        if( isEmpty( ) )
            throw new BufferUnderflowException( );
        return findMin( root ).element;
    }



   // Check if the tree is empty or not
    public boolean isEmpty( )
    {
        return root == null;
    }


    private static final int ALLOWED_IMBALANCE = 1; // This is maximum imbalance allowed in tree

    // Assume t is either balanced or within one of being balanced
    private AvlNode<Event> balance( AvlNode<Event> t )
    {
        if( t == null )
            return t;

        if( height( t.left ) - height( t.right ) > ALLOWED_IMBALANCE )
            if( height( t.left.left ) >= height( t.left.right ) )
                t = rotateWithLeftChild( t );
            else
                t = doubleWithLeftChild( t );
        else
        if( height( t.right ) - height( t.left ) > ALLOWED_IMBALANCE )
            if( height( t.right.right ) >= height( t.right.left ) )
                t = rotateWithRightChild( t );
            else
                t = doubleWithRightChild( t );

        t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
        return t;
    }

    public void checkBalance( )
    {
        checkBalance( root );
    }

    // Check the balance of the tree
    private int checkBalance( AvlNode<Event> t )
    {
        if( t == null )
            return -1;

        if( t != null )
        {
            int hl = checkBalance( t.left );
            int hr = checkBalance( t.right );
            if( Math.abs( height( t.left ) - height( t.right ) ) > 1 ||
                    height( t.left ) != hl || height( t.right ) != hr )
                System.out.println( "OOPS!!" );
        }

        return height( t );
    }


    // Private recursive helper function that inserts a node into our tree
    private AvlNode<Event> insert( com.example.ourcalendarapp.Event x, AvlNode<Event> t )
    {
        if( t == null )
            return new AvlNode<>( x, null, null );

        // Comapare method in Event compares ASCIII values of the event names.
        int compareResult = x.compare(x, t.element);

        if( compareResult < 0 )
            t.left = insert( x, t.left );
        else if( compareResult > 0 )
            t.right = insert( x, t.right );
        else
            ;  // Duplicate; do nothing
        return balance( t );
    }

    // Private recursive helper function to find the smallest value in tree
    private AvlNode<Event> findMin( AvlNode<Event> t )
    {
        if( t == null )
            return t;

        while( t.left != null )
            t = t.left;
        return t;
    }

   // Private helper method that find maximum event in the tree via recursion
    private AvlNode<Event> findMax( AvlNode<Event> t )
    {
        if( t == null )
            return t;

        while( t.right != null )
            t = t.right;
        return t;
    }

   // Private recursive helper function to see if our tree contains a particular event

    private boolean contains( com.example.ourcalendarapp.Event x, AvlNode<Event> t )
    {
        while( t != null )
        {
            int compareResult = x.compare(x, t.element);

            if( compareResult < 0 )
                t = t.left;
            else if( compareResult > 0 )
                t = t.right;
            else
                return true;    // Match
        }

        return false;   // No match
    }

    // Prints the tree in sorted order
    private void printTree( AvlNode<Event> t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.element );
            printTree( t.right );
        }
    }

    // Returns the height of the tree
    private int height( AvlNode<Event> t )
    {
        return t == null ? -1 : t.height;
    }

   // Rotate binary tree with left child
    // Should be a single rotation
    // Update height and return root eturn root
    private AvlNode<Event> rotateWithLeftChild( AvlNode<Event> k2 )
    {
        AvlNode<Event> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max( height( k2.left ), height( k2.right ) ) + 1;
        k1.height = Math.max( height( k1.left ), k2.height ) + 1;
        return k1;
    }

    // Rotate binary tree with right child
    // Should be a single rotation
    // Update height and return root
    private AvlNode<Event> rotateWithRightChild( AvlNode<Event> k1 )
    {
        AvlNode<Event> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max( height( k1.left ), height( k1.right ) ) + 1;
        k2.height = Math.max( height( k2.right ), k1.height ) + 1;
        return k2;
    }


     // Double rotate binary tree node: first left child
     // with its right child; then node k3 with new left child.
     // Should be a double rotation
     // Update heights, then return new root.

    private AvlNode<Event> doubleWithLeftChild( AvlNode<Event> k3 )
    {
        k3.left = rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }

    // Double rotate binary tree node: first right child
    // with its left child; then node k3 with new right child.
    // Should be a double rotation
    // Update heights, then return new root.
    private AvlNode<Event> doubleWithRightChild( AvlNode<Event> k1 )
    {
        k1.right = rotateWithLeftChild( k1.right );
        return rotateWithRightChild( k1 );
    }


    // Defines the structure for our AvlNode of type Event
    private static class AvlNode<Event>
    {
        // Constructors
        AvlNode( com.example.ourcalendarapp.Event theElement )
        {
            this( theElement, null, null );
        }

        AvlNode( com.example.ourcalendarapp.Event element, AvlNode<Event> left, AvlNode<Event> right )
        {
            this.element  = element;
            this.left     = left;
            this.right    = right;
            height   = 0;
        }

        com.example.ourcalendarapp.Event           element;      // The data in the node
        AvlNode<Event>  left;         // Left child
        AvlNode<Event>  right;        // Right child
        int               height;       // Height
    }

    /** The tree root. */
    private AvlNode<Event> root;
}
