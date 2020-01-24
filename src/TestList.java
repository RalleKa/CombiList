import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class TestList {
	public static int sum;

	public static void main(String[] args) {
		int size = 1000000;
		int removeCalls = 1000;
		int addCalls = 1000;
		int getCalls = size * removeCalls + size * addCalls + addCalls * 3;

		// erstelle HashSet
		HashSet<Integer> s = new HashSet<>(size);
		for (int i = 0; i < size; i++) {
			s.add(i);
		}

		// add HashSet
		long start = System.currentTimeMillis();
		for (int i = 0; i < addCalls; i++) {
			s.add(i);
		}
		System.out.println("Time for add in Hashset: " + (System.currentTimeMillis() - start));

		sum = 0;
		// get Hashset
		start = System.currentTimeMillis();
		{
			Iterator<Integer> temp = s.iterator();
			while (temp.hasNext()) {
				sum += temp.next();
			}
		}
		System.out.println("Time for get in set: " + (System.currentTimeMillis() - start));

		// remove Hashset
		start = System.currentTimeMillis();
		for (int i = 0; i < removeCalls; i++) {
			s.remove(i);
		}
		System.out.println("Time for remove in set: " + (System.currentTimeMillis() - start));

		// erstelle ArrayList
		ArrayList<Integer> a = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			a.add(i);
		}
		System.out.println();

		// add ArrayList
		start = System.currentTimeMillis();
		for (int i = 0; i < addCalls; i++) {
			a.add(i);
		}
		System.out.println("Time for add in Arraylist: " + (System.currentTimeMillis() - start));

		// get ArrayList
		sum = 0;
		start = System.currentTimeMillis();
		for (int i : a) {
			sum += i;
		}
		System.out.println("Time for get in Arraylist: " + (System.currentTimeMillis() - start));

		// get ArrayList2
		sum = 0;
		start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			sum += a.get(i);
		}
		System.out.println("Time for get2 in Arraylist: " + (System.currentTimeMillis() - start));

		// remove ArrayList
		start = System.currentTimeMillis();
		for (int i = 0; i < removeCalls; i++) {
			a.remove(0);
		}
		System.out.println("Time for remove in Arraylist: " + (System.currentTimeMillis() - start));

		// erstelle CombiList
		CombiList<Integer> c = new CombiList<>(size);
		for (int i = 0; i < size; i++) {
			c.add(i);
		}
		System.out.println();

		// add CombiList
		start = System.currentTimeMillis();
		for (int i = 0; i < addCalls; i++) {
			c.add(i);
		}
		System.out.println("Time for add in CombiList: " + (System.currentTimeMillis() - start));

		// get ArrayList
		sum = 0;
		start = System.currentTimeMillis();
		for (int i : c) {
			sum += i;
		}
		System.out.println("Time for get in Arraylist: " + (System.currentTimeMillis() - start));

		// get CombiList2
		sum = 0;
		start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			sum += c.get(i);
		}
		System.out.println("Time for get2 in CombiList: " + (System.currentTimeMillis() - start));

		// remove CombiList
		start = System.currentTimeMillis();
		for (int i = 0; i < removeCalls; i++) {
			c.remove(0);
		}
		System.out.println("Time for remove in CombiList: " + (System.currentTimeMillis() - start));

		// get CombiList2.2
		sum = 0;
		start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			sum += c.get(i);
		}
		System.out.println("Time for get2.2 in CombiList: " + (System.currentTimeMillis() - start));

		// remove CombiList
		start = System.currentTimeMillis();
		for (int i = 0; i < removeCalls; i++) {
			c.remove(0);
		}
		System.out.println("Time for remove in CombiList: " + (System.currentTimeMillis() - start));

		// erstelle LinkedList
		LinkedList<Integer> l = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			l.add(i);
		}
		System.out.println();

		// add LinkedList
		start = System.currentTimeMillis();
		for (int i = 0; i < addCalls; i++) {
			l.add(i);
		}
		System.out.println("Time for add in LinkedList: " + (System.currentTimeMillis() - start));

		// get LinkedList
		sum = 0;
		start = System.currentTimeMillis();
		for (int i : l) {
			sum += i;
		}
		System.out.println("Time for get in LinkedList: " + (System.currentTimeMillis() - start));

//		// get LinkedList2
//		sum = 0;
//		start = System.currentTimeMillis();
//		for (int i = 0; i < size; i++) {
//			sum += l.get(i);
//		}
//		System.out.println("Time for get2 in LinkedList: " + (System.currentTimeMillis() - start));

		// remove LinkedList
		start = System.currentTimeMillis();
		for (int i = 0; i < removeCalls; i++) {
			l.remove(0);
		}
		System.out.println("Time for remove in LinkedList: " + (System.currentTimeMillis() - start));
	}
}
