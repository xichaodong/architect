package LRU;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2020/9/21 10:54 上午
 */
public class LRU<K, V> {
    private final Map<K, CacheNode<K, V>> cache = new HashMap<>();
    private final int capacity;
    private CacheNode<K, V> head;
    private CacheNode<K, V> tail;

    public LRU(int capacity) {
        this.capacity = capacity;
        head = null;
        tail = null;
    }

    public V get(K key) {
        CacheNode<K, V> cacheNode = cache.get(key);

        if (Objects.isNull(cacheNode)) {
            return null;
        }

        move2First(cacheNode);

        return cacheNode.getValue();
    }

    public void put(K key, V value) {
        CacheNode<K, V> node = cache.get(key);
        if (Objects.isNull(node)) {
            if (cache.size() >= capacity) {
                cache.remove(tail.getKey());
                removeTail();
            }
            node = new CacheNode<>(key, value);
        } else {
            node.setValue(value);
        }

        move2First(node);

        cache.put(key, node);
    }

    private void move2First(CacheNode<K, V> node) {
        if (Objects.isNull(head) || Objects.isNull(tail)) {
            head = tail = node;
            return;
        }
        if (node == head) {
            return;
        }
        if (node == tail) {
            tail.setPre(node.getPre());
        }
        if (Objects.nonNull(node.getNext())) {
            node.getNext().setPre(node.getPre());
        }
        if (Objects.nonNull(node.getPre())) {
            node.getPre().setNext(node.getNext());
        }
        head.setPre(node);
        node.setNext(head);
        head = node;
        head.setPre(null);
    }

    private void removeTail() {
        if (Objects.isNull(head) || Objects.isNull(tail)) {
            return;
        }
        if (head == tail) {
            head = tail = null;
        }
        tail = tail.getPre();
        tail.setNext(null);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        CacheNode<K, V> node = head;

        while (node != null) {
            sb.append(String.format("%s:%s ", node.getKey(), node.getValue()));
            node = node.getNext();
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        LRU<Integer, String> lru = new LRU<Integer, String>(3);

        lru.put(1, "a");    // 1:a
        System.out.println(lru.toString());
        lru.put(2, "b");    // 2:b 1:a
        System.out.println(lru.toString());
        lru.put(3, "c");    // 3:c 2:b 1:a
        System.out.println(lru.toString());
        lru.put(4, "d");    // 4:d 3:c 2:b
        System.out.println(lru.toString());
        lru.put(1, "aa");   // 1:aa 4:d 3:c
        System.out.println(lru.toString());
        lru.put(2, "bb");   // 2:bb 1:aa 4:d
        System.out.println(lru.toString());
        lru.put(5, "e");    // 5:e 2:bb 1:aa
        System.out.println(lru.toString());
        lru.get(1);         // 1:aa 5:e 2:bb
        System.out.println(lru.toString());
        lru.put(1, "aaa");  //1:aaa 5:e 2:bb
        System.out.println(lru.toString());
    }
}

class CacheNode<K, V> {
    private CacheNode<K, V> pre;
    private CacheNode<K, V> next;
    private final K key;
    private V value;

    public CacheNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public CacheNode<K, V> getPre() {
        return pre;
    }

    public void setPre(CacheNode<K, V> pre) {
        this.pre = pre;
    }

    public CacheNode<K, V> getNext() {
        return next;
    }

    public void setNext(CacheNode<K, V> next) {
        this.next = next;
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
