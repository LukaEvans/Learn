public class HashMap<K,V> {

    //默认容量2^4=2*2*2*2=16
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    //最大容量2^30
    static final int MAXIMUM_CAPACITY = 1 << 30;

    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    static class Node<K,V> {

        K key;
        V value;
        Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        public Node<K,V> getNext() {
            return this.next;
        }

        public Node<K,V> setNext(Node<K,V> next) {
            return this.next = next;
        }

    }

    //主表
    private Node<K,V>[] table;
    //最大容量
    private int capacity;
    //负载因子
    private float loadFactor;
    //真实容量
    private int size = 0;

    public HashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int capacity, float loadFactor) {
        //容量在2的幂次方之间
        this.capacity = tableSizeFor(capacity);
        this.loadFactor = loadFactor;
    }

    public int size() {
        return size;
    }

    public V get(K key) {
        int index = getIndex(key);
        Node<K,V> currentNode = table[index];
        if (null == currentNode) {
            return null;
        }
        //遍历链表
        while (null != currentNode) {
            if (currentNode.getKey().equals(key)) {
                return currentNode.getValue();
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    public V put(K key, V value){
        //初始化数组
        if (null == table) {
            table = new Node[capacity];
        }
        //判断扩容
        if (size > loadFactor * capacity) {
            // TODO resize();
        }
        //取hash值对应的下标
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        //如果没有发生hash冲突
        if (null == currentNode) {
            table[index] = newNode;
            size++;
            return value;
        //如果发生hash冲突
        } else {
            while (null != currentNode) {
                //key相同 修改val
                if (currentNode.getKey().equals(key)) {
                    return currentNode.setValue(value);
                //key不同 添加到链表尾部
                } else if (null == currentNode.getNext()) {
                    //1.7头插法(可能会造成死链) 1.8尾插法
                    currentNode.setNext(newNode);
                    size++;
                }
                currentNode = currentNode.getNext();
            }
        }
        return null;
    }

    public V remove(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        Node<K, V> lastNode = null;
        while (currentNode != null) {
            if (currentNode.getKey().equals(key)) {
                Node<K, V> newNode = currentNode.getNext();
                //如果是头节点 直接将其子节点作为root挂到table[i]
                if (null == lastNode) {
                    table[index] = newNode;
                //如果是尾节点 将父节点的next赋为空
                } else if (null == newNode) {
                    lastNode.setNext(null);
                }
                size--;
                return currentNode.getValue();
            }
            lastNode = currentNode;
            currentNode = currentNode.getNext();
        }
        return null;
    }

    private int getIndex(K key) {
        //只有当数组长度为2的幂次方时，h&(length-1)才等价于h%lengt;提高性能
        //hash(key) % capacity;
        return hash(key) & (capacity - 1);
    }

}
