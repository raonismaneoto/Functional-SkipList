import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SkipListImplTest {

    SkipListImpl<Integer> skip;
    SkipListNode<Integer>[] array;

    @Before
    public void setUp(){
        skip = new SkipListImpl<Integer>(5);
    }

    @Test
    public void testInsert() {
        skip.insert(10, 1, 2);
        skip.insert(20, 2, 1);
        skip.insert(0, 3, 1);
        skip.insert(15, 4, 3);
        skip.insert(5, 5, 3);

        assertEquals(5, skip.size());

        array = skip.toArray();
        assertEquals("[<ROOT,4,4>, <0,1>, <5,3>, <10,2>, <15,3>, <20,1>, <NIL,4>]", Arrays.toString(array));
        assertEquals(0, array[0].getForward(0).getKey());
        assertEquals(5, array[0].getForward(1).getKey());
        assertEquals(5, array[0].getForward(2).getKey());
        assertEquals(5, array[1].getForward(0).getKey());
        assertEquals(10, array[2].getForward(0).getKey());
        assertEquals(10, array[2].getForward(1).getKey());
        assertEquals(15, array[2].getForward(2).getKey());
        assertEquals(15, array[3].getForward(0).getKey());
        assertEquals(15, array[3].getForward(1).getKey());
        assertEquals(20, array[4].getForward(0).getKey());
        assertEquals(Integer.MAX_VALUE, array[4].getForward(1).getKey());
        assertEquals(Integer.MAX_VALUE, array[4].getForward(2).getKey());
        assertEquals(Integer.MAX_VALUE, array[5].getForward(0).getKey());
    }

    @Test
    void remove() {
        skip.insert(10, 1, 1);
        skip.insert(20, 2, 2);
        skip.insert(0, 3, 2);
        skip.insert(15, 4, 3);
        skip.insert(5, 5, 1);

        skip.insert(-10, 6, 1);
        skip.insert(30, 7, 3);
        skip.insert(9, 8, 2);
        skip.insert(17, 9, 2);
        skip.insert(-2, 10, 1);

        assertEquals(10, skip.size());

        skip.remove(10);
        skip.remove(20);
        skip.remove(0);
        skip.remove(15);
        skip.remove(5);

        assertEquals(5, skip.size());
    }

    @Test
    void search() {
        skip.insert(10, 1, 2);
        skip.insert(20, 2, 1);
        skip.insert(0, 3, 1);
        skip.insert(15, 4, 3);
        skip.insert(5, 5, 2);

        assertEquals(1, skip.search(10).getValue());
        assertEquals(2, skip.search(20).getValue());
        assertEquals(3, skip.search(0).getValue());
        assertEquals(4, skip.search(15).getValue());
        assertEquals(5, skip.search(5).getValue());

        assertEquals(null, skip.search(-10));
        assertEquals(null, skip.search(30));
        assertEquals(null, skip.search(9));
    }
}