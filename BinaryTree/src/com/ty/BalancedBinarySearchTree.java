package com.ty;

import java.util.Comparator;

// 平衡二叉搜索树, 处理平衡逻辑
public class BalancedBinarySearchTree<E> extends BinarySearchTree<E> {

    public BalancedBinarySearchTree() {
        this(null);
    }

    public BalancedBinarySearchTree(Comparator<E> comparator) {
        super(comparator);
    }

    /**
     * 左旋转
     * @param grand 节点
     */
    protected void rotateLeft(Node<E> grand) {
        // 找到 parent 节点, 能来到这, 说明 parent 是 grand 的左子树
        Node<E> parent = grand.left;
        Node<E> child = parent.left;

        // 旋转
        grand.right = child;
        parent.left = grand;

        afterRotate(grand, parent, child);

    }

    /**
     * 右旋转
     * @param grand 节点
     */
    protected void rotateRight(Node<E> grand) {

        Node<E> parent = grand.left;
        Node<E> child = parent.right;

        grand.left = child;
        parent.right = grand;

        afterRotate(grand, parent, child);

    }

    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        // 让 parent 成为子树的根节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            // grand 是其父节点的左子树
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            // grand 是其父节点的右子树
            grand.parent.right = parent;
        } else {
            // grand 是根节点
            root = parent;
        }

        // 更新 child 的 parent
        if (child != null) {
            child.parent = grand;
        }

        // 更新 grand 的 parent
        grand.parent = parent;
    }

    // 统一旋转操作
    protected void rotate (
            Node<E> childRoot,// 子树的根结点
            Node<E> a, Node<E> b, Node<E> c,
            Node<E> d,
            Node<E> e, Node<E> f, Node<E> g) {

        // 1. 让 d 成为这棵子树的根节点
        d.parent = childRoot.parent;
        if (childRoot.isLeftChild()) {
            childRoot.parent.left = d;
        } else if (childRoot.isRightChild()) {
            childRoot.parent.right = d;
        } else {
            root = d;
        }

        // 2.处理 a, b, c
        b.left = c;
        if (a != null) {
            a.parent = b;
        }
        b.right = c;
        if (c != null) {
            c.parent = b;
        }

        // 3. 处理 e, f, g
        f.left = g;
        if (e != null) {
            e.parent =g;
        }
        f.right = g;
        if (g != null) {
            g.parent = f;
        }

        // 4. b, d, f 串起来
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;

    }

}
