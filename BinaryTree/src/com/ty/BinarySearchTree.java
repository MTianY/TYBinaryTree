package com.ty;

import java.util.Comparator;

public class BinarySearchTree<E> {

    private int size;
    private Node<E> root;   // 根结点
    private Comparator<E> comparator;   // 比较器

    public BinarySearchTree() {
        this.comparator = null;
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {

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
            root = new Node<>(element, null);
            size++;
            System.out.print("根节点" + root.element + "\n");
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
            System.out.print(">>>>> 循环开始" + "\n");
            System.out.print("比较大小:"+"传入 element=" + element + " 当前 node.element=" + node.element + " cmp=" + cmp + "\n");
            // 存父节点
            parent = node;
            if (cmp > 0) {
                node = node.right;
                System.out.print("右" + "\n");
            } else if (cmp < 0) {
                node = node.left;
                System.out.print("左" + "\n");
            } else {
                // 相等情况, 先覆盖处理
                System.out.print("相等" + "\n");
                node.element = element;
                return;
            }
            System.out.print("node=" + node + "\n");
        }

        System.out.print(">>>>> 循环结束" + "\n");

        System.out.print("找到的父节点" + parent.element + "\n");
        // 创建要添加的新节点
        Node<E> newNode = new Node<>(element, parent);
        // 根据方向插入
        if (cmp > 0) {
            // 添加到右子树
            parent.right = newNode;
            System.out.print("新节点, 右子树" + newNode.element + "\n");
        } else {
            // 添加到左子树
            parent.left = newNode;
            System.out.print("新节点, 左子树" + newNode.element + "\n");
        }

        // size 递增
        size++;

    }

    public void remove(E element) {

    }

    public boolean contains(E element) {
        return false;
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

    public void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element 不能为空!");
        }
    }

    private static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }
    }
}
