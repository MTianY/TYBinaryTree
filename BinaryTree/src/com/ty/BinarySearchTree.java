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

    // 前驱节点: [中序遍历]时当前节点的前一个节点
    private Node<E> predecessor(Node<E> node) {
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
    private Node<E> successor(Node<E> node) {
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

        // 是否是叶子节点
        public boolean isLeaf() {
            return left == null && right == null;
        }

        // 是否有 2 个节点
        public boolean hasTwoChildren() {
            return left != null && right != null;
        }
    }
}
