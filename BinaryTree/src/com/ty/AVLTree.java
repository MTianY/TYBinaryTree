package com.ty;

import java.util.Comparator;

public class AVLTree<E> extends BinarySearchTree {
    public AVLTree() {
        this(null);
    }

    public AVLTree(Comparator<E> comparator) {

    }

    /**
     * 判断节点是否平衡
     * @param node 节点
     * @return 是否平衡
     */
    public boolean isBanlance (Node<E> node) {
        // 平衡因子绝对值小于等于 1,则平衡
        return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;
    }

    private static class AVLNode<E> extends Node<E> {

        // 算高度时, 传进来的节点一定是叶子节点, 所以默认高度 1
        int height = 1;

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }


        // 平衡因子: 左右子树高度差
        public int balanceFactor() {
            int leftHeight = left != null ? ((AVLNode<E>)left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>)right).height : 0;
            return leftHeight - rightHeight;
        }

        // 更新节点自己的高度
        public void updateHeight() {
            int leftHeight = left != null ? ((AVLNode<E>)left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>)right).height : 0;
            // 左右子树最大高度+1
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        /**
         * @return 返回左右子树中高度比较高的节点
         */
        public Node<E> tallerChild() {
            int leftHeight = left != null ? ((AVLNode<E>)left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>)right).height : 0;
            // 左边高
            if (leftHeight > rightHeight) {
                return left;
            }
            // 左边高
            if (leftHeight < rightHeight) {
                return right;
            }
            // 左右高度相等, 取同个方向的
            return isLeftChild() ? left : right;

        }

    }

    /**
     * 更新节点高度
     * @param node 节点
     */
    private void updateHeight(Node<E> node) {
        // 放到 AVLNode 里更新节点高度
        ((AVLNode<E>)node).updateHeight();
    }

    /**
     * 添加节点, 可能会导致所有祖先节点都失衡, 只序让高度最低的失衡节点恢复平衡, 整棵树就会恢复平衡 (该操作 O(1))
     * @param node 新添加的节点
     */

    @Override
    protected void afterAdd(Node node) {
        // 一路找父节点,找到失衡节点
        while ((node = node.parent) != null) {
            if (isBanlance(node)) {
                // 平衡, 更新高度
                updateHeight(node);
            } else {
                // 不平衡, 恢复平衡
                rebanlance(node);
                // 整棵树恢复平衡, 结束循环
                break;
            }
        }

    }

    /**
     * 恢复平衡
     * @param grand 高度最低的那个不平衡节点
     */
    private void rebanlance2(Node<E> grand) {

        /**
         *         grand
         *
         *       parent
         *
         *     node
         *
         *  newNode (新添加节点后, 会导致 grand 节点失衡)
         *
         */

        // 找到 grand 下面的两个节点
        // 传入节点是高度 最低的那个 不平衡 节点, 取其高度最大的子节点
        Node<E> parent = ((AVLNode<E>)grand).tallerChild();
        Node<E> node = ((AVLNode<E>)parent).tallerChild();

        if (parent.isLeftChild()) {
            if (node.isLeftChild()) {
                // LL
                rotateRight(grand);
            } else {
                // LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        } else {
            if (node.isLeftChild()) {
                // RL
                rotateRight(parent);
                rotateLeft(grand);
            } else {
                // RR
                rotateLeft(grand);
            }
        }
    }

    /**
     * 左旋转
     * @param grand 节点
     */
    private void rotateLeft(Node<E> grand) {
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
    private void rotateRight(Node<E> grand) {

        Node<E> parent = grand.left;
        Node<E> child = parent.right;

        grand.left = child;
        parent.right = grand;

        afterRotate(grand, parent, child);

    }

    private void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
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

        // 更新高度
        updateHeight(grand);
        updateHeight(parent);
    }

    private void rebanlance(Node<E> grand) {

        // 找到 grand 下面的两个节点
        // 传入节点是高度 最低的那个 不平衡 节点, 取其高度最大的子节点
        Node<E> parent = ((AVLNode<E>)grand).tallerChild();
        Node<E> node = ((AVLNode<E>)parent).tallerChild();

        if (parent.isLeftChild()) {
            if (node.isLeftChild()) {
                // LL
                rotate(grand, node.left, node, node.right, parent, parent.right, grand, grand.right);
            } else {
                // LR
                rotate(grand, parent.left, parent, node.left, node, node.right, grand, grand.right);
            }
        } else {
            if (node.isLeftChild()) {
                // RL
                rotate(grand, grand.left, grand, node.left, node, node.right, parent, parent.right);
            } else {
                // RR
                rotate(grand, grand.left, grand, parent.left, parent, node.left, node.right, node);
            }
        }
    }

    // 统一旋转操作
    private void rotate (
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
        // 更新 b 高度
        updateHeight(b);

        // 3. 处理 e, f, g
        f.left = g;
        if (e != null) {
            e.parent =g;
        }
        f.right = g;
        if (g != null) {
            g.parent = f;
        }
        updateHeight(f);

        // 4. b, d, f 串起来
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
        updateHeight(d);

    }

    /**
     * 删除节点
     * 只会导致父节点或祖父节点失衡 (只有 1 个节点会失衡, 其他节点不可能失衡), 让父节点恢复平衡后, 可能会导致更高层的祖先节点失衡 (最多 O(logn) 次调整)
     * @param node 要删除节点
     */
    @Override
    protected void afterRemove(Node node) {
        while ((node = node.parent) != null) {
            if (isBanlance(node)) {
                // 更新高度
                updateHeight(node);
            } else {
                // 恢复平衡
                rebanlance(node);
            }
        }
    }

    /**
     * 平均时间复杂度
     * 搜素 : O(logn)
     * 添加 : O(logn)
     * 删除 : O(logn), 最多需要 O(logn) 次的旋转操作
     */

    @Override
    protected Node createNode(Object element, Node parent) {
        return new AVLNode<>(element, parent);
    }
}
