package com.ty;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<E> extends BinaryTree {

    private Comparator<E> comparator;   // 比较器

    public BinarySearchTree() {
        this.comparator = null;
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }


    /**
     * add 处理思路:
     * 1. 先处理根节点
     * 2. 处理完根节点, 根据根节点, 往下找新添加节点的父节点
     * 3. 找到父节点之后, 让父节点的 right 或者 left 指向它
     */

    public void add(E element) {
        elementNotNullCheck(element);

        // 根结点为空, 添加第一个节点.
        if (root == null) {
            root = createNode(element, null);
            size++;
//            System.out.println("[根]" +element);

            afterAdd(root);

            return;
        }

        // 其他添加不是第一个节点的情况
        Node<E> parent = root;
        Node<E> node = root;
        int cmp = 0;

        // 顺着根结点往下找要添加节点的父节点, 然后让父节点的 left 或者 right 指向它
        while (node != null) {
            // 传入节点和 node 节点做比较
            cmp = compare(element, node.element);
            // 存父节点
            parent = node;
            if (cmp > 0) {
                node = node.right;
//                System.out.println("[遍历-右]");
            } else if (cmp < 0) {
                node = node.left;
//                System.out.println("[遍历-左]");
            } else {
                // 相等情况, 先覆盖处理
//                System.out.print("[遍历-相等]");
                node.element = element;
                return;
            }
        }

//        System.out.println("找到的父节点" + parent.element);
        // 创建要添加的新节点
        Node<E> newNode = createNode(element, parent);
        // 根据方向插入
        if (cmp > 0) {
            // 添加到右子树
            parent.right = newNode;
//            System.out.println("[新-右子]" + newNode.element);
        } else {
            // 添加到左子树
            parent.left = newNode;
//            System.out.println("[新-左子]" + newNode.element);
        }

        // size 递增
        size++;

        afterAdd(newNode);

    }

    /**
     * 添加节点之后处理
     * @param node 新添加的节点
     */
    protected void afterAdd(Node<E> node) {
        // 二叉搜索树什么都不做
    }

    protected void afterRemove(Node<E> node, Node<E> replacement) {

    }

    /**
     * 删除节点
     * 1. 删除叶子节点, 度为 0
     * 2. 删除度为 1 的
     * 3. 删除度为 2 的
     */


    public void remove(E element) {
        remove(node(element));
    }

    private void remove(Node<E> node) {

        if (node == null) return;

        size--;

        // 节点度为 2 的情况, 找前驱或者后继几点替代
        if (node.hasTwoChildren()) {
            // 找后继节点
            Node<E> s = successor(node);
            // 后继节点的值覆盖当前节点
            node.element = s.element;
            // 节点指向后继节点, 后面删除节点时, 就会把后继节点同时删除
            node = s;
        }

        // 删除节点 node (度为 1 或者 0)
        // 如果度为 1, 那么 node.left == null 就剩右子树,反之左子树
        // 如果度为 0. 那么左右子树均为空
        Node<E> replacement = node.left != null ? node.left : node.right;

        if (replacement != null) {
            // 度为 1
            // 更改 parent
            replacement.parent = node.parent;
            // 更改 parent 的 left或者 right 的指向

            if (node.parent == null) {
                // 叶子节点并且时根结点的情况, 将 root 指向被替代节点即可
                root = replacement;
            } else if (node == node.parent.left) {
                // 在左边
                node.parent.left = replacement;
            } else {
                // 在右边
                node.parent.right = replacement;
            }

            // 删除节点之后的处理
            afterRemove(node, replacement);

        } else if (node.parent == null) {
            // 叶子节点并且时根节点
            root = null;

            // 删除节点之后的处理
            afterRemove(node, null);
        } else {
            // 叶子节点
            if (node == node.parent.left) {
                // 清空右子树
                node.parent.left = null;
            } else {
                // 反之清空左子树
                node.parent.right = null;
            }

            // 删除节点之后的处理
            afterRemove(node, null);
        }

    }

    // 根据传入元素, 找到对应节点
    private Node<E> node(E element) {
        Node<E> node = root;
        while (node != null) {
            int cmp = compare(element, node.element);
            // 二者相等
            if (cmp == 0) return node;
            if (cmp > 0) {
                // 在右子树
                node = node.right;
            } else {
                // 在左子树
                node = node.left;
            }
        }
        // 退出循环, node==null
        return null;
    }

    /**
     * @param element 传入元素
     * @return true 节点存在
     */
    public boolean contains(E element) {
        return node(element) != null;
    }

    /**
     * @param e1 元素 1
     * @param e2 元素 2
     * @return 比较两个元素, 返回值为0, 代表e1和e2相等, 返回值大于0,e1 大于 e2, 返回值小于 0, 则 e1 小于 e2
     */
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        // 如果没有传入comparator, 则强制转换为 Comparable,实现对应接口
        return ((Comparable<E>)e1).compareTo(e2);
    }

}
