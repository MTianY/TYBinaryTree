package com.ty;

public class Main {

    public static void main(String[] args) {

        Integer data[] = new Integer[] {
            7,4,9,2,5,8,11,3
        };

        BinarySearchTree<Integer> bst1 = new BinarySearchTree();
        for (int i=0; i<data.length; i++) {
            System.out.print("--------");
            bst1.add(data[i]);
        }
    }

}
