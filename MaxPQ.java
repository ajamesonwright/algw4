import java.util.NoSuchElementException;

public class MaxPQ<Key extends Comparable<Key>>
{
    private Key[] pq;
    private int N;

    public MaxPQ(int capacity)
    {
        pq = (Key[]) new Comparable[capacity + 1];
    }

    public MaxPQ(Key[] a)
    {
        pq = a;
    }

    public void insert (Key v)
    {
        pq[++N] = v;
        swim(N);
    }

    public Key delMax()
    {
        if (N == 0)
        {
            throw new NoSuchElementException("Cannot delete an element from an empty queue.");
        }

        Key max = pq[1];
        exch(1, N--);
        sink(1);
        pq[N + 1] = null;
        return max;
    }

    boolean isEmpty()
    {
        return N == 0;
    }

    private void swim(int k)
    {
        while (k > 1 && less(k/2, k))
        {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k)
    {
        while (2 * k <= N)
        {
            int j = 2 * k;
            if(j < N && less(j, j + 1))     j++;
            if (!less(k, j))                break;
            exch(k, j);
            k = j;
        }
    }

    public Key max()
    {
        return pq[1];
    }

    public int size()
    {
        return N;
    }

    private boolean less(int a, int b)
    {
        return pq[a].compareTo(pq[b]) < 0;
    }

    private void exch(int a, int b)
    {
        Key swap = pq[a];
        pq[a] = pq[b];
        pq[b] = swap;
    }
}