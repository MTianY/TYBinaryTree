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

# AVL 树

平衡因子: 某节点的左右子树的高度差

#### AVL 树特点:

- 平衡因子只可能是 1, 0, -1 (绝对值 ≤ 1), 超过 1 则"失衡"
- 每个节点的左右子树高度差不超过 1
- 搜索,添加,删除的时间复杂度是 O(logn)

AVL 树在二叉搜索树基础上添加自平衡功能

#### 判断节点是否失衡

平衡因子绝对值小于等于 1, 则失衡

```java
public boolean isBanlance (Node<E> node) {
    return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;
}
```

#### AVLNode

AVL 树节点, 有些平衡操作, 二叉搜索树不需要, 这里继承二叉树节点, 单独处理逻辑

```java
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
```

#### 更新节点高度

```java
 /**
  * 更新节点高度
  * @param node 节点
  **/
private void updateHeight(Node<E> node) {
    // 放到 AVLNode 里更新节点高度
    ((AVLNode<E>)node).updateHeight();
}
```

#### 失衡处理

##### afterAdd, 添加节点之后导致失衡处理

```java
/**
 * 添加节点, 可能会导致所有祖先节点都失衡, 只序让高度最低的失衡节点恢复平衡, 整  棵树就会恢复平衡 (该操作 O(1))
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
```

##### 统一处理旋转操作

```java
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
```

##### afterRemove, 删除节点之后导致的失衡处理

```java
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
```

#### 平均时间复杂度

- 搜素: O(logn)
- 添加: O(logn)
- 删除: O(logn), 最多需要 O(logn) 次的旋转操作


# B 树

B 树是一种平衡的多路搜索树, 多用于文件系统,数据库的实现

#### B 树特点

- 1 个节点可以存储超过 2 个元素, 可以拥有超过 2 个节点
- 拥有二叉搜索树的一些性质
- 平衡, 每个节点的所有子树高度一致
- 比较矮

#### m 阶 B 树性质 (m >= 2)
    
1. 假设是 3 阶 B 树 (2,3 树, 2-3 树)

- 假设一个节点存储的元素个数 x
    - 根结点: 1 <= x <= m-1, 即: 1<= x <= 2;
    - 非根节点: m/2(向上取整) -1 <= x <= m-1, 即: 1 <= x <= 2;
    - 如果有子节点, 子节点个数 y=x+1
        - 根节点: 2 <=y <= m,    2<=y<=3
        - 非根节点: m/2(向上取整) <= y <= m  2<=y<=3


2. 二叉搜索树多代节点合并, 可以成为 B 树

3. 添加导致的 `上溢`

假设是 5 阶 B 树, 那么节点最多 4 个, 添加一个后导致上溢, 那么节点个数是 5 个.

- 上溢解决办法
    - 将节点向上移到父节点, 原位置左右两侧节点变成左右子树
    - 如果父节点还上溢, 依次按此解决

4. 删除

- 删除叶子节点则直接删除
- 删除非叶子节点
    - 先找到前驱或者后继节点, 覆盖所需删除元素的值
    - 再把前驱或者后继节点删除

5. 删除导致的 `下溢`

- 向兄弟节点借
- 兄弟节点个数不够向父节点借   



# 红黑树

自平衡二叉搜索树

#### 红黑树必须满足以下 5 条性质

- 节点是 RED 或者 BLACK
- 根结点是 BLACK
- 叶子节点 (外部节点, 空节点) 都是 BLACK
- RED 节点的子节点都是 BLACK
    - RED 节点的 parent 都是 BLACK
    - 从根节点到叶子节点所有路径上, 不能有两个连续的 RED 节点
- 从任意节点到叶子节点的所有路径都包含相同数目的 BLACK 节点 

#### 下面一棵就不是红黑树

红色虚线处, 不满足任意节点到叶子节点都包含相同数目的 BLACK 节点

![21674956717_.pi](media/16747811887578/21674956717_.pic.jpg)

#### 红黑树与 B 树的等价交换

![](media/16747811887578/16749584759722.jpg)

- 红黑树 与 4 阶 B 树(2-3-4 树) 具有等价性
- BLACK 节点与它的 RED 子节点融合在一起, 形成 1 个 B 树节点
- 红黑树的 BLACK 节点个数与 4 阶 B 树的节点总个数相等

#### 红黑树添加的所有情况

共 12 种情况

![](media/16747811887578/16751500636793.jpg)
##### 1. 有 4 种情况满足红黑树性质 4, parent 位 BLACK 的情况

- 添加新节点之后同样满足 4 阶 B 树的性质
- 不用做特殊处理

##### 2. 其他 8 种情况, 不满足红黑树性质 4, parent 位 RED 的情况

- 添加节点处的 uncle(叔父节点, 父节点的兄弟节点) 不是 RED, 有四种
- uncle 节点是 RED 的, B 树的上溢情况处理


#### 删除

在 B 树中, 最后真正被删除的节点, 都在`叶子节点`中

所以, 删除节点在最下面一层, 有两种情况, 删除红色节点, 删除黑色节点

##### 删除红色节点情况

- 删除红色节点, 不会影响红黑树性质, 所以不用处理

##### 删除黑色节点, 有 3 种情况

1. 删除拥有 2 个 RED 子节点的 BLACK 节点

- 删除拥有 2 个 RED子节点的 BLACK 节点, 这种不会直接删除 BLACK 节点, 而是会删除其前驱或者后继节点, 因此不用处理

2. 删除拥有 1 个 RED 子节点的 BLACK 节点

- 删除度为 1 的节点
- 如果用来替代被删除 BLACK 节点的子节点时 RED, 将替代的子节点染成 BLACK 即可保持红黑树性质

3. 删除 BLACK 叶子节点

- 删除 BLACK 叶子节点之后, 会导致 B 树节点下溢
- 删除节点它的兄弟节点是 BLACK 的情况
    - 被删除的 BLACK 节点, 兄弟节点有 RED 子节点的情况
        - 那么会从其兄弟节点去借一个, 此时其兄弟节点一定是黑色, 并且至少有一个红色子节点才能借 (只有兄弟节点是黑色, 才在一个层级, 才能借, 如果兄弟节点是红色, 那么会在其上面一个层级, 不能借用) 
        - 进行旋转操作
        - 旋转之后, 中心节点要继承 parent 的颜色
        - 旋转之后的左右节点染为 BLACK (独立节点必须黑色)
    - 被删除的 BLACK 节点, 其兄弟节点没有 RED 子节点的情况
        - 下溢
        - 父节点下来和其兄弟节点合并, 兄弟节点染成 RED, 父节点染成 BLACK
- 删除节点它的兄弟节点是 RED 的情况
    - 这种情况, 被删除的 BLACK 节点, 和其兄弟节点 RED 不在一个层级, 不能直接借
    - 此时需要将其兄弟节点 RED 的子节点, 变成自己的子节点, 然后再借
        - 将兄弟节点变成自己的子节点的操作, 执行旋转操作  



