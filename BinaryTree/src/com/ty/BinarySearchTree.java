package com.ty;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

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
//            System.out.println("[根]" +element);
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
        Node<E> newNode = new Node<>(element, parent);
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

    }

    public void remove(E element) {

    }

    public boolean contains(E element) {
        return false;
    }

    // 前序遍历: 根->左->右
    public void preorderTraversal(Visitor<E> visitor) {
        if (visitor == null) return;
        // 传入根节点
        preorderTraversal(root, visitor);
    }

    private void preorderTraversal(Node<E> node, Visitor<E> visitor) {

        // 当传入节点为空时, 结束递归
        if (node == null || visitor.stop) return;

        // 先访问节点
        // 将访问节点传到外部自己调用
        visitor.stop = visitor.visit(node.element);
        // 再传入左子树
        preorderTraversal(node.left, visitor);
        // 再传入右子树
        preorderTraversal(node.right,visitor);
    }

    // 中序遍历: 左->根->右 或者 右->根->左
    // 如果时二叉搜索树, 则有升序或者降序
    public void inorderTraversal(Visitor<E> visitor) {
        if (visitor == null) return;
        // 传入根节点
        inorderTraversal(root, visitor);
    }

    private void inorderTraversal(Node<E> node, Visitor<E> visitor) {

        if (node == null || visitor.stop) return;

        // 二叉搜索树升序
        inorderTraversal(node.left, visitor);
        if (visitor.stop) return;
        visitor.stop = visitor.visit(node.element);
        inorderTraversal(node.right, visitor);

        // 二叉搜索树降序
//        inorderTraversal(node.right);
//        System.out.print("中序:" + node.element);
//        inorderTraversal(node.left);

    }

    // 后序遍历: 左->右->根
    public void postorderTraversal(Visitor<E> visitor) {
        if (visitor == null) return;
        postorderTraversal(root, visitor);
    }

    private void postorderTraversal(Node<E> node, Visitor<E> visitor) {

        if (node == null || visitor.stop) return;

        postorderTraversal(node.left, visitor);
        postorderTraversal(node.right, visitor);
        if (visitor.stop) return;
        visitor.stop = visitor.visit(node.element);
    }

    // 层序遍历: 从上到下, 从左到右

    /**
     *              7
     *         4          9
     *      2    5     8    11
     *    1  3          10    12
     */

    /**
     * 层序遍历思路:
     * 1. 创建一个队列
     * 2. 先将根节点入队
     * 3. 将头节点出队, 访问
     * 4. 如果此根节点有左子树, 则入队, 如果有右子树,则入队
     */

    public void levelorderTraversal(Visitor<E> visitor) {
        if (root == null || visitor == null) return;
        // 队列
        Queue<Node<E>> queue = new LinkedList<>();
        // 将根节点入队
        queue.offer(root);
        // 如果队列不为空
        while (!queue.isEmpty()) {
            // 取出根节点
            Node<E> node = queue.poll();
            // 访问
            if (visitor.stop) return;
            visitor.stop = visitor.visit(node.element);
            // 看其是否有左右子树
            if (node.left != null) {
                // 有左子树, 则入队
                queue.offer(node.left);
            }
            if (node.right != null) {
                // 有右子树, 则入队
                queue.offer(node.right);
            }
        }
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

    // 抽象类, 定义好方法, 继承他的类必须实现这个方法, 类似接口
    public static abstract class Visitor<E> {
        boolean stop;

        /**
         * @param element 元素
         * @return 返回 true, 表示停止遍历
         */
        abstract boolean visit(E element);
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
