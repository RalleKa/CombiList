import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * 
 * @author RalleKa
 *
 * This list holds an array with the values. 
 * When removing an element, instead of moving every element to fill the gap, the gap will keep existing. This will give major performance improvements over the ArrayList when it comes to removing elements.
 * However, there are some down sides as well:
 * For every gap inside the list the get-method will get a little slower. The optimize-method will close all gaps.
 * Furthermore, to close the gaps, new elements will be inserted to the position of the gaps. The user will not have the ability to decide the index of an element.
 *
 * @param <T>
 */
public class CombiList<T> implements RandomAccess, Cloneable, java.io.Serializable, Iterable<T>, List<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4488999531603269028L;
	private Object[] a;
	private int size;
	private ArrayList<Integer> gaps;

	public CombiList(int size) {
		a = new Object[size];
		gaps = new ArrayList<>(size / 2);
	}

	/**
	 * returns the Element at the given index
	 * @param index
	 * @return the Element
	 */
	@SuppressWarnings("unchecked")
	public T get(int index) {
		int gapCount = getGapIndex(index);
		Object t = a[Math.max(0, index + gapCount - 1)];
		return (T) t;
	}

	/**
	 * Adds the Element to the given index
	 * @param element to add
	 * @param index to set the element to
	 */
	public void add(int index, T element) {
		int gapCount = getGapIndex(index);
		int nextGapIndex = gapCount < gaps.size() ? gaps.get(gapCount + 1) : size + gaps.size();

		Object nextValue = element;
		Object store;
		for (int i = index + gapCount - 1; i < nextGapIndex; i++) {
			store = a[i];
			a[i] = nextValue;
			nextValue = store;
		}
		size++;
	}
	
	/**
	 * If there is a gap, the element will be set to the gap, moving every element with a higher index one further position up.
	 * If there is no gap, the element will get the index of getSize
	 * If the array is to small, a new array will be created with newLength = oldLength * 2
	 * @param element
	 * @return 
	 */
	@Override
	public boolean add(T element) {
		add(element, true);
		return true;
	}
	
	/**
	 * If gapClosing and there is a gap, the element will be set to the gap, moving every element with a higher index one further position up.
	 * If there is no gap, the element will get the index of getSize
	 * If the array is to small, a new array will be created with newLength = oldLength * 2
	 * @param element
	 * @param wheather gaps should be closed by adding this element
	 */
	public void add(T element, boolean gapClosing) {
		if (gapClosing && gaps.size() > 0) {
			a[gaps.remove(0)] = element;
			size++;
			return;
		}

		if (size >= a.length) {
			a = Arrays.copyOf(a, a.length * 2);
		}

		a[size] = element;
		size++;
	}
	
	/**
	 * Sets the Element to the given index
	 * @param element to set
	 * @param index to set the element to
	 * @return 
	 */
	@Override
	public T set(int index, T element) {
		index = index + getGapIndex(index) - 1;
		@SuppressWarnings("unchecked")
		T oldElement = (T) a[index];
		a[index] = element;
		return oldElement;
	}

	/**
	 * removes the element at the given index. Every element with a higher index will get newIndex = index - 1, however, in the ram there will be a gap
	 * @param index
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T remove(int index) {
		if (index >= size) {
			return null;
		}
		int gapCount = getGapIndex(index);
		index += gapCount;
		gaps.add(gapCount, index);
		size--;
		return (T) a[index];
	}

	private int getGapIndex(int index) {
		// TODO O(log(n)) Zugriff
		for (int i = 0; i < gaps.size(); i++) {
			if (gaps.get(i) + i + 1 < index) {
				return i;
			}
		}
		
		int upperBound = gaps.size();
		int lowerBound = 0;
		int i = gaps.size() / 2;
		while (i >= lowerBound && i < upperBound) {
			if (gaps.get(i) + i + 1 < index) {
				upperBound = i;
				i = (i + lowerBound) / 2;
			}else {
				lowerBound = i;
				i += (upperBound - i + 1) / 2;
			}
		}

		return gaps.size();
	}

	/**
	 * @return the total amount of elements stored
	 */
	public int size() {
		return size;
	}
	
	/**
	 * The method will close all gaps. Get-Methods will get a small performance improvement afterwards.
	 * @return weather optimizations were done
	 */
	public boolean optimize() {
		if (gaps.size() == 0) {
			return false;
		}
		
		int offset = 1;
		int gapIndex = 1;
		for (int i = gaps.get(0); i < size + gaps.size(); i++) {
			if (gaps.size() > gapIndex && gaps.get(gapIndex) == a[i + offset]) {
				gapIndex++;
				offset++;
				i--;
			}else {
				a[i] = a[i + offset];
				
				if (i + 1 == size) {
					break;
				}
			}
			
			
			for (Object a : this.a) {
				System.out.print(a + ", ");
			}
			System.out.println();
		}
		gaps = new ArrayList<>();
		
		return true;
	}
	
	/**
	 * The method will close all gaps. Get-Methods will get a small performance improvement afterwards. 
	 * This method is faster then optimize(), but, however, changes the indexes of the elements within the list.
	 */
	public void fastOptimize() {
		while (gaps.size() > 0) {
			a[gaps.get(0)] = a[size + gaps.size() - 1];
			gaps.remove(0);
		}
	}
	
	/**
	 * @return weather there are gaps to optimize
	 */
	public boolean optimizable() {
		return gaps.size() > 0;
	}
	
	/**
	 * trims the underlying array to the size of elements stored.
	 * @return
	 */
	public boolean trim() {
		if (size == a.length) {
			return false;
		}
		
		a = Arrays.copyOf(a, size + gaps.size());
		return true;
	}

	public static void main(String[] args) {
		int size = 10;
		CombiList<Integer> list = new CombiList<>(size);

		System.out.println("Erstellen:");
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		System.out.println(list.toArray());
		System.out.println("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]");

		System.out.println();
		System.out.println("Löschen");
		list.remove(5);
		System.out.println(list.toArray());
		System.out.println("[0, 1, 2, 3, 4, 6, 7, 8, 9]");
		
		System.out.println();
		System.out.println("Adden");
		list.add(10);
		list.add(11);
		System.out.println(list.toArray());
		System.out.println("[0, 1, 2, 3, 4, 10, 6, 7, 8, 9, 11]");
		
		System.out.println();
		System.out.println("löschen");
		for (int i = 0; i < 5; i++) {
			list.remove(0);
		}
		System.out.println(list.toArray());
		System.out.println("[10, 6, 7, 8, 9, 11]");
		
		System.out.println();
		System.out.println("Adden");
		list.add(12);
		System.out.println(list.get(2));
		System.out.println("10");
		for (Object a : list.a) {
			System.out.print(a + ", ");
		}
		System.out.println();
		System.out.println("12, 1, 2, 3, 4, 10, 6, 7, 8, 9, 11");
		
		System.out.println();
		System.out.println("set");
		list.set(2, 13);
		System.out.println(list.toArray());
		System.out.println("[12, 13, 6, 7, 8, 9, 11]");
		for (Object a : list.a) {
			System.out.print(a + ", ");
		}
		System.out.println();
		System.out.println("12, 1, 2, 3, 4, 13, 6, 7, 8, 9, 11");
		
//		System.out.println();
//		System.out.println("optimize");
//		list.optimize();
//		System.out.println(list.getArrayList());
//		System.out.println("[12, 13, 6, 7, 8, 9, 11]");
//		for (Object a : list.a) {
//			System.out.print(a + ", ");
//		}
//		System.out.println();
//		System.out.println("12, 13, 6, 7, 8, 9, 11");
		
		System.out.println();
		System.out.println("fastOptimize");
		list.fastOptimize();
		System.out.println(list.toArray());
		System.out.println("[12, 11, 9, 8, 7, 13, 6]");
		for (Object a : list.a) {
			System.out.print(a + ", ");
		}
		System.out.println();
		System.out.println("12, 11, 9, 8, 7, 13, 6");
		
		System.out.println();
		System.out.println("Trim");
		list.trim();
		for (Object a : list.a) {
			System.out.print(a + ", ");
		}
		System.out.println();
		System.out.println("12, 13, 6, 7, 8, 9, 11");
	}

	/**
	 * returns an iterator for the CombiList. If the CombiList is optimize()d, the iterator will be slower then iterating over get(), however, for every gap inside the array, the iterator will become lass slower then the index
	 */
	@Override
	public Iterator<T> iterator() {
		
		return new Itr<T>(this);
	}
	
	@SuppressWarnings("hiding")
	private class Itr<T> implements Iterator<T>, ListIterator<T>{
		int cursor;
		int gapIndex;
		int index;
		CombiList<T> list;
		
		public Itr(CombiList<T> list) {
			this.list = list;
		}

		@Override
		public boolean hasNext() {
			return cursor < size;
		}

		@Override
		public T next() {
			while (gaps.size() > gapIndex && gaps.get(gapIndex) == cursor) {
				cursor++;
				gapIndex++;
			}
			@SuppressWarnings("unchecked")
			T t = (T) a[cursor];
			cursor++;
			index++;
			
			return t;
		}

		@Override
		public boolean hasPrevious() {
			return index != 0;
		}

		@Override
		public T previous() {
			while (0 <= gapIndex && gaps.get(gapIndex) == cursor) {
				cursor--;
				gapIndex--;
			}
			@SuppressWarnings("unchecked")
			T t = (T) a[cursor];
			cursor--;
			index--;
			
			return t;
		}

		@Override
		public int nextIndex() {
			return index + 1;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}
		
		@Override
		public void remove() {
			gaps.add(cursor);
			size--;
		}

		@Override
		public void set(T e) {
			a[cursor] = e;
		}

		@Override
		public void add(T e) {
			list.add(index, e);
		}
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		for (T t : this) {
			if (t.equals(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return an array without gaps with every element. The indexes will be the same as in CombiList
	 */
	@Override
	public Object[] toArray() {
		Object[] l = new Object[size];

		for (int i = 0; i < l.length; i++) {
			if (!gaps.contains(i)) {
				l[i] = a[i];
			}
		}

		return l;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <T> T[] toArray(T[] a) {
		for (int i = 0; i < a.length; i++) {
			if (!gaps.contains(i)) {
				a[i] = (T) this.a[i];
			}
		}

		return a;
	}

	@Override
	public boolean remove(Object o) {
		return remove(indexOf(o)) != null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object t : c) {
			if (!contains(t)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (size + c.size() > a.length) {
			a = Arrays.copyOf(a, (a.length + c.size()) * 2);
		}
		
		for (T t : c) {
			add(t);
		}
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if (size + c.size() > a.length) {
			a = Arrays.copyOf(a, (a.length + c.size()) * 2);
		}
		
		for (T t : c) {
			add(index, t);
			index++;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean all = true;
		
		for (Object t : c) {
			if (!remove(t)) {
				all = false;
			}
		}
		
		return all;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		for (int i = size - 1; i >= 0; i--) {
			if (!c.contains(get(i))) {
				remove(i);
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public void clear() {
		size = 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int indexOf(Object o) {
		Iterator<T> iterator = this.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(o)) {
				return ((Itr) iterator).index;
			}
		}
		
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		for (int i = size - 1; i >= 0; i--) {
			if (get(i).equals(o)) {
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public ListIterator<T> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
