package com.ty;

import java.util.Comparator;

public class RBTree<E> extends BalancedBinarySearchTree<E> {

    // 定义节点颜色
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBTree() {

    }

    public RBTree(Comparator<E> comparator) {
        super(comparator);
    }

    /**
     * 给节点染色
     * @param node 待染色节点
     * @param color 颜色, RED 或者 BLACK
     * @return 被染过色的节点
     */
    private Node<E> color(Node<E> node, boolean color) {
        if (node == null) return node;
        ((RBNode<E>)node).color = color;
        return node;
    }

    /**
     * 染红色
     * @param node 待染色节点
     * @return 红色节点
     */
    private Node<E> red(Node<E> node) {
        return color(node, RED);
    }

    /**
     * 染黑色
     * @param node 待染色节点
     * @return 黑色节点
     */
    private Node<E> black(Node<E> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>)node).color;
    }

    private boolean isBLACK(Node<E> node) {
        return colorOf(node) == BLACK;
    }

    private boolean isRED(Node<E> node) {
        return colorOf(node) == RED;
    }

    @Override
    protected void afterAdd(Node node) {

        // 父节点
        Node<E> parent = node.parent;

        // 1. 如果添加的是根节点, 直接染成黑色
        if (parent == null) {
            black(node);
            return;
        }

        // 2. 第一种四种情况中, 父节点是黑色的情况, 不用处理
        if (isBLACK(parent)) return;

        // 3. 其余 8 种父节点是红色的情况
        // 找到 uncle 节点
        Node<E> uncle = parent.sibling();
        // 祖父节点
        Node<E> grand = parent.parent;
        // 3.1 叔父节点是红色的情况
        if (isRED(uncle)) {
            // 父节点染成黑色
            black(parent);
            // 叔父节点染成黑色
            black(uncle);
            // 祖父节点当做新添加的节点, 染成红色, 递归
            afterAdd(red(grand));
            return;
        }

        // 4. 叔父节点不是红色的情况

        // 父节点是左子树情况
        if (parent.isLeftChild()) { // L
            // 新添加节点是左子树情况
            if (node.isLeftChild()) {
                // LL
                black(parent);
                red(grand);
                rotateRight(grand);
            } else {
                // LR
                black(node);
                red(grand);
                rotateLeft(parent);
                rotateRight(grand);
            }

        } else {    // R

            if (node.isLeftChild()) {
                // RL
                black(node);
                red(grand);
                rotateRight(parent);
                rotateLeft(grand);
            } else {
                // RR
                black(parent);
                red(grand);
                rotateLeft(grand);
            }
        }

    }

    /**
     * 删除节点之后处理
     * @param node 被删除节点
     * @param replacement  用以取代被删除的节点
     */
    @Override
    protected void afterRemove(Node<E> node, Node<E> replacement) {

        // 1. 如果删除的节点是红色, 不处理
        if (isRED(node)) return;

        // 2. 删除节点为黑色
        // 删除节点的子节点是红色
        if (isRED(replacement)) {
            // 染成黑色, 删除度为 1 的操作, 父类已经处理完成
            black(replacement);
            return;
        }

        Node<E> parent = node.parent;
        // 如果删除的节点 BLACK 是根节点, 不处理
        if (parent == null) return;

        // 删除的是黑色叶子节点
        // 判断被删除的节点 node 是左还是右
        boolean left = parent.left == null;
        // 找到其兄弟节点, 不能用 node.sibling(), 因为在此刻, 那个方法求出的兄弟节点不准确了
        Node<E> sibling = left ? parent.right : parent.left;
        if (left) {
            // 被删除的节点在左, 兄弟节点在右, 逻辑与下面对称

            // 如果兄弟节点是 RED 的情况
            if (isRED(sibling)) {
                black(sibling);
                red(parent);
                rotateLeft(parent);

                // 更换兄弟节点
                sibling = parent.right;
            }

            // 走到这里, 兄弟节点必然是 BLACK
            if (isBLACK(sibling.left) && isBLACK(sibling.right)) {
                // 兄弟节点都是 BLACK, 父节点要向下和兄弟节点合并

                boolean parentBlack = isBLACK(parent);
                black(parent);
                red(sibling);
                // 判断父节点是 BLACK 的情况
                if (parentBlack) {
                    afterRemove(parent, null);
                }

            } else {
                // 兄弟节点至少有一个 RED 子节点, 向兄弟节点借元素

                // 先处理左边是黑色的情况, 先左旋兄弟节点, 后面情况基本一致
                if (isBLACK(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }
                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }

        } else {
            // 被删除的节点在右, 兄弟节点在左

            // 如果兄弟节点是 RED 的情况
            if (isRED(sibling)) {
                black(sibling);
                red(parent);
                rotateRight(parent);

                // 更换兄弟节点
                sibling = parent.left;
            }

            // 走到这里, 兄弟节点必然是 BLACK
            if (isBLACK(sibling.left) && isBLACK(sibling.right)) {
                // 兄弟节点都是 BLACK, 父节点要向下和兄弟节点合并

                boolean parentBlack = isBLACK(parent);
                black(parent);
                red(sibling);
                // 判断父节点是 BLACK 的情况
                if (parentBlack) {
                    afterRemove(parent, null);
                }

            } else {
                // 兄弟节点至少有一个 RED 子节点, 向兄弟节点借元素

                // 先处理左边是黑色的情况, 先左旋兄弟节点, 后面情况基本一致
                if (isBLACK(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }

        }

    }

    private static class RBNode<E> extends Node<E> {

        // 默认节点红色
        boolean color = RED;

        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }
    }

}
