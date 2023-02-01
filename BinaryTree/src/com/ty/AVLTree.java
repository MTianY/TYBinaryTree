package com.ty;

import java.util.Comparator;

public class AVLTree<E> extends BalancedBinarySearchTree<E> {
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

    @Override
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);

        // 父类处理完之后, 更新高度
        // 红黑树不需要更新高度
        updateHeight(grand);
        updateHeight(parent);

    }

    @Override
    protected void rotate(Node<E> childRoot, Node<E> a, Node<E> b, Node<E> c, Node<E> d, Node<E> e, Node<E> f, Node<E> g) {
        super.rotate(childRoot, a, b, c, d, e, f, g);

        // 更新高度
        // 更新 b 高度
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
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



    /**
     * 删除节点
     * 只会导致父节点或祖父节点失衡 (只有 1 个节点会失衡, 其他节点不可能失衡), 让父节点恢复平衡后, 可能会导致更高层的祖先节点失衡 (最多 O(logn) 次调整)
     * @param node 要删除节点
     */
    @Override
    protected void afterRemove(Node<E> node, Node<E> replacement) {
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
