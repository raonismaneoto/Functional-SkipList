import SkipList;
import SkipListImpl;

public class main {
    public static void main(String[]args) {
        SkipList sl = new SkipListImpl(3);
        sl.insert(3, 5, 2);
        sl.insert(4, 3, 1);
        sl.insert(1, 7, 3);
        System.out.println(sl.toArray()[5]);
    }
}