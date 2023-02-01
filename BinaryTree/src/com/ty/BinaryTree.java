package com.ty;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree<E> {

    protected int size;
    protected Node<E> root;   // 根结点

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    // 前序遍历: 根->左->右
    public void preorderTraversal(BinarySearchTree.Visitor<E> visitor) {
        if (visitor == null) return;
        // 传入根节点
        preorderTraversal(root, visitor);
    }

    private void preorderTraversal(Node<E> node, BinarySearchTree.Visitor<E> visitor) {

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
    public void inorderTraversal(BinarySearchTree.Visitor<E> visitor) {
        if (visitor == null) return;
        // 传入根节点
        inorderTraversal(root, visitor);
    }

    private void inorderTraversal(Node<E> node, BinarySearchTree.Visitor<E> visitor) {

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
    public void postorderTraversal(BinarySearchTree.Visitor<E> visitor) {
        if (visitor == null) return;
        postorderTraversal(root, visitor);
    }

    private void postorderTraversal(Node<E> node, BinarySearchTree.Visitor<E> visitor) {

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

    public void levelorderTraversal(BinarySearchTree.Visitor<E> visitor) {
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

    // 前驱节点: [中序遍历]时当前节点的前一个节点
    protected Node<E> predecessor(Node<E> node) {
        if (node == null) return node;

        // 假设前驱节点是其左子节点
        Node<E> p = node.left;
        // 1. 先找其左子树情况, 如果左子节点不为空, 遍历找其右子节点, 其右子节点中最右边的一个,就是其前驱节点
        if (p != null) {
            // 当右子节点为空的情况下, 结束循环, 说明已找到前驱节点
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }

        // 2. 来到这说明左子树为空, 那么从其父节点,祖父节点中找前驱节点
        // 如果一直是其父节点的左子树, 那么找到最上层的父节点终止, 其再父节点则为其前驱节点
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        // 第一种情况是 node.parent == null, 那么其前驱节点也是空, 返回 node.parent
        // 第二种情况是 node == node.parent.left, 父节点左子树找到头了, 那么也是返回 node.parent
        return node.parent;
    }

    // 后继节点: [中序遍历]时,当前节点的后一个节点
    // 和找前驱相反
    protected Node<E> successor(Node<E> node) {
        if (node == null) return node;
        Node<E> s = node.right;
        if (s != null) {
            while (s.left != null) {
                s = s.left;
            }
            return s;
        }

        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    protected Node<E> createNode (E element, Node<E> parent) {
        return new Node<>(element, parent);
    }

    protected static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        // 是否是叶子节点
        public boolean isLeaf() {
            return left == null && right == null;
        }

        // 是否有 2 个节点
        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        // 自己是左子树
        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        // 自己是右子树
        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        // 兄弟节点
        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }
            if (isRightChild()) {
                return parent.left;
            }
            return null;
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

    // 求二叉树高度
    public int height() {
        // 递归方法
//        return height(root);

        // 迭代
        return height2();
    }

    private int height(Node<E> node) {
        // 递归终止条件: 当传入节点为空的时候
        if (node == null) return 0;
        // 求当前节点的高度, 递归方法,当前节点高度等于其左子树或者右子树的最大高度加 1
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 迭代方法求高度: 使用层序遍历
    private int height2() {
        if (root == null) return 0;

        int height = 0;
        int level = 1;  // 层数
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            // 出队之后, level 层数减 1
            level--;

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }

            // 当 level==0 时, 表示当前层已全部出队, 那么此时队列长度就是下一层的长度
            if (level == 0) {
                level = queue.size();
                // 递增高度
                height++;
            }

        }
        return height;
    }

    /**
     * 判断二叉树是否是完全二叉树 (使用层序遍历, 遍历每个节点, 看其左右子树做判断)
     * 完全二叉树条件:
     * 1. 度为 1 的节点,只有左子树
     * 2. 度为 1 的节点,只有0 个或者 1 个
     */
    public boolean isComplete2() {

        if (root == null) return false;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        // 是否是叶子节点
        boolean isLeaf = false;

        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            // 如果不是叶子节点, 则不是完全二叉树
            if (isLeaf && !node.isLeaf()) return false;

            // 如果有 2 个子节点, 才满足完全二叉树条件, 则入队
            if (node.hasTwoChildren()) {
                queue.offer(node.left);
                queue.offer(node.right);
            } else if (node.left == null && node.right != null) {
                return false;
            } else {
                // 除了上面两种条件外, 到这里之后,必须是叶子节点,才满足完全二叉树条件
                isLeaf = true;
                if (node.left != null) {
                    queue.offer(node.left);
                }
            }
        }

        return true;
    }

    // 另一种判断完全二叉树写法
    public boolean isComplete() {

        // 先层序遍历,确保每个节点都可以遍历到
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        // 是否该节点后面节点都是叶子节点(当找到一个节点只有左子树时, 那么依照完全二叉树性质, 其后面节点应该都是叶子节点)
        boolean isLeaf = false;

        while (!queue.isEmpty()) {

            Node<E> node = queue.poll();

            // 如果检测到后面节点必须是叶子节点, 但是当前节点不是叶子节点, 那么就不是完全二叉树
            if (isLeaf && !node.isLeaf()) return false;

            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) {
                // 左子树为 null时, 右子树不为 null, 一定不是完全二叉树
                return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else {
                // 右子树为空时, 左子树可能为空,可能不为空
                // 那么依赖完全二叉树性质, 其后面要遍历的节点必须满足叶子节点才可以
                isLeaf = true;
            }
        }
        return true;
    }

    public void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element 不能为空!");
        }
    }


}
