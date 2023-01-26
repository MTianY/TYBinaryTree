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

        preorderTraversal();
        inorderTraversal();
        postorderTraversal();
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
        bst.preorderTraversal(new BinarySearchTree.Visitor<Integer>() {
            @Override
            boolean visit(Integer element) {
                System.out.print("[前序]" + element + " ");
                return element == 2 ? true : false;
            }
        });
        System.out.println();
    }

    static void inorderTraversal() {
        Integer data[] = new Integer[] {
                7,4,9,2,1,3,5,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        bst.inorderTraversal(new BinarySearchTree.Visitor<Integer>() {
            @Override
            boolean visit(Integer element) {
                System.out.print("[中序]" + element + " ");
                return element == 3 ? true : false;
            }
        });
        System.out.println();
    }

    static void postorderTraversal() {
        Integer data[] = new Integer[] {
                7,4,9,2,1,3,5,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        bst.postorderTraversal(new BinarySearchTree.Visitor<Integer>() {
            @Override
            boolean visit(Integer element) {
                System.out.print("[后序]" + element + " ");
                return element == 2 ? true : false;
            }
        });
        System.out.println();
    }

    static void levelorderTraversal() {
        Integer data[] = new Integer[] {
                7,4,9,2,1,3,5,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        bst.levelorderTraversal(new BinarySearchTree.Visitor<Integer>() {
            @Override
            boolean visit(Integer element) {
                System.out.print("[层序]" + element + " ");
                return element == 12 ? true : false;
            }
        });
        System.out.println();
    }

}
