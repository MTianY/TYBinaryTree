package com.ty;

public class Main {

    public static void main(String[] args) {

//        Integer data[] = new Integer[] {
//            7,4,9,2,5,8,11,3
//        };
//
//        BinarySearchTree<Integer> bst1 = new BinarySearchTree();
//        for (int i=0; i<data.length; i++) {
//            System.out.print("--------");
//            bst1.add(data[i]);
//        }

//        preorderTraversal();
//        inorderTraversal();
//        postorderTraversal();
        levelorderTraversal();

    }

    static void preorderTraversal() {
        Integer data[] = new Integer[] {
                7,4,9,2,1,3,5,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        bst.preorderTraversal();
    }

    static void inorderTraversal() {
        Integer data[] = new Integer[] {
                7,4,9,2,1,3,5,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        bst.inorderTraversal();
    }

    static void postorderTraversal() {
        Integer data[] = new Integer[] {
                7,4,9,2,1,3,5,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        bst.postorderTraversal();
    }

    static void levelorderTraversal() {
        Integer data[] = new Integer[] {
                7,4,9,2,1,3,5,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        bst.levelorderTraversal();
    }

}
