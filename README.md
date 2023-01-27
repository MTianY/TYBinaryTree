# 二叉搜索树

又叫二叉查找树, 二叉排序树.

#### 二叉搜索树特点:

- 任意一个节点的值, 都 `大于` 其 `左子树` 所有节点的值
- 任意一个节点的值, 都 `小于` 其 `右子树` 所有节点的值
- 其左右子树也是一棵二叉搜索树
- 二叉搜素树存储的元素必须具备`可比较性`, 不允许为 null

#### 二叉搜索树添加节点思路:

- 先处理根节点
- 处理完根节点, 根据根节点往下找新添加节点的`父节点`
- 找到父节点之后, 让父节点的 right 或者 left 指向它

```java
public void add(E element) {
    elementNotNullCheck(element);
    
    // 先处理根结点
    if (root == null) {
        root = new Node<>(element, parent:null);
        size++;
        return;
    }
    
    // 处理其它节点
    
    Node<E> parent = root;
    Node<E> node = root;
    int cmp = 0;
    while (node != null) {
        // 比较节点与传入节点大小
        cmp = compare(element, node.element);
        // 存父节点
        parent = node;
        
        if (cmp > 0) {
            // 右子树
            node = node.right;
        } else if (cmp < 0) {
            node = node.left;
        } else {
            // 相等情况
            node.element = element;
            return;
        }    
    }
    
    // 创建新节点
    Node<E> newNode = new Node<>(element, parent);
    
    // 根据方向插入新节点
    if (cmp > 0) {
        // 添加到右子树
        parent.right = newNode;
    } else {
        // 添加到左子树
        parent.left = newNode;
    }
    
    size++;
    
}                                                                  
```

#### 二叉树四种遍历方式

- 前序遍历
    - 根左右
- 中序遍历
    - 左根右 
- 后序遍历
    - 左右根 
- 层序遍历
    - 从上到下, 从左到右

##### 前序遍历

使用递归方式

```java
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
``` 

##### 中序遍历

使用递归方式

```java
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
```

##### 后序遍历

使用递归方式

```java
private void postorderTraversal(Node<E> node, Visitor<E> visitor) {

    if (node == null || visitor.stop) return;

    postorderTraversal(node.left, visitor);
    postorderTraversal(node.right, visitor);
    if (visitor.stop) return;
    visitor.stop = visitor.visit(node.element);
}
```

##### 层序遍历

思路:

- 创建一个队列
- 先将根节点入队
- 下面开始循环处理
- 将头结点出队, 访问
- 如果此根节点有左子树,则入队,如果有右子树,则入队

```java
public void levelorderTraversal(Visitor<E> visitor) {
    if (root == null || visitor == null) return;
    Queue<Node<E>> queue = new LinkedList<>();
    // 根节点入队
    queue.offer(root);
    // 队列为空时结束循环
    while(!queue.isEmpty()) {
        // 取出头结点
        Node<E> node = queue.poll();
        // 有左子树, 入队
        if (node.left != null) {
            queue.offer(node.left);
        }
        // 有右子树, 入队
        if (node.right != null) {
            queue.offer(node.right);
        }
    }
}
```

#### 求二叉树高度

二叉树高度, 等于其左子树或者右子树高度加 1.

递归方式:

```java
private int height(Node<E> node) {
    // 递归终止条件: 当传入节点为空的时候
    if (node == null) return 0;
    // 取左右子树节点的最大高度加 1
    return 1 + Math.max(height(node.left), height(node.right));
}
```

迭代方式: 使用层序遍历

```java
private int height() {
    if (root == null) return 0;
    
    int height = 0;
    int level = 1; // 层数
    Queue<Node<E>> queue = new LinkedList<>();
    queue.offer(root);
    while(!queue.isEmpty) {
        Node<E> node = queue.poll();
        // 出队之后, level 层数递减
        level--;
        
        if (node.left != null) {
            queue.offer(node.left);
        }
        if (node.right != null) {
            queue.offer(node.right);
        }
        
        // 当 level==0 时, 表示当前层已全部出队, 那么此时队列长度就是下一层的节点个数
        if (level == 0) {
            level = queue.size();
            height++;
        }
    }
    return height;
    
}
```

#### 判断完全二叉树

完全二叉树条件:

- 度为 1 的节点, 只有左子树
- 度为 1 的节点, 只有0 个或者 1 个

判断完全二叉树, 使用层序遍历, 遍历每个节点, 看其左右子树做判断

```java
public boolean isComplete() {
    Queue<Node<E>> queue = new LinkedList<>();
    queue.offer(root);
    
    // 记录节点后面是否都是叶子节点情况 (当找到一个节点只有左子树时, 依完全二叉树性质, 其后面节点必须都是叶子节点才可以) 
    boolean isLeaf = false;
    
    while(!queue.isEmpty) {
        Node<E> node = queue.poll();
        
        // 如果检测出应该是叶子节点, 但是当前节点不是叶子节点, 那么就不是完全二叉树
        if (isLeaf && !node.isLeaf()) return false;
        
        if (node.left != null) {
            queue.offer(node.left);
        } else if (node.right != null) {
            // 左子树为 null 时, 右子树不为 null, 一定不是完全二叉树
            return false;
        }
        
        if(node.right != null) {
            queue.offer(node.right);
        } else {
            // 右子树为空, 左子树有两种可能, 一种为空或者不为空
            // 所以其后面节点必须是叶子节点才满足完全二叉树性质
            isLeaf = true;
        }
    }
    return true;
}
```

#### 前驱节点, 后继节点

`中序遍历` 时,当前节点的前一个节点, 后一个节点

前驱节点思路:

- 前驱节点是中序遍历时的前一个节点
- 所以如果左子树存在,那么就在其左子树找, 找最右侧最大的节点就是前驱节点
- 左子树为空的话, 从其父节点、祖父节点找, 如果一直是父节点的左子树, 找到最上层父节点终止,其父节点则为其前驱节点

```java
private Node<E> predecessor(Node<E> node) {
    if (node == null) return node;
    
    // 假设前驱节点是其左子节点
    Node<E> p = node.left;
    
    // 如果左子树不为空, 一直遍历找, 最右就是其前驱节点
    if (p != null) {
        while (p.right != null) {
            p = p.right;
        }
        return p;
    }
    
    // 左子树为空的时候, 找父节点
    while (node.parent != null && node == node.parent.left) {
        node = node.parent;
    }
    
    // 第一种情况是 node.parent == null, 那么其前驱节点也是空, 返回 node.parent
    // 第二种情况是 node == node.parent.left, 父节点左子树最左, 那么也是返回 node.parent
    return node.parent; 
    
}
```

后继节点: [中序遍历]时, 当前节点的后一个节点
逻辑与找前驱节点相反

```java
private Node<E> successor(Node<E> node) {
    if (node == null) return node;
    Node<E> s = node.right;
    if (s != null) {
        while(s.left != null) {
            s = s.left;
        }
        return s;
    }
    while (node.parent != null && node == node.parent.right) {
        node = node.parent;
    }
    return node.parent;
}
```



