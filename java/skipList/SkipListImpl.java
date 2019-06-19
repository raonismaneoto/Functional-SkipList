import java.util.Arrays;
import java.util.Comparator;

public class SkipListImpl<T> implements SkipList<T> {

	protected SkipListNode<T> root;
	protected SkipListNode<T> NIL;

	protected int height;
	protected int maxHeight;
	protected int size;

	protected boolean USE_MAX_HEIGHT_AS_HEIGHT = true;
	protected double PROBABILITY = 0.5;

	public SkipListImpl(int maxHeight) {
		if (USE_MAX_HEIGHT_AS_HEIGHT) {
			this.height = maxHeight;
		} else {
			this.height = 1;
		}

		this.maxHeight = maxHeight;
		this.size = 0;
		root = new SkipListNode<>(Integer.MIN_VALUE, maxHeight, null);
		NIL = new SkipListNode<>(Integer.MAX_VALUE, maxHeight, null);
		connectRootToNil();
	}

	/**
	 * Faz a ligacao inicial entre os apontadores forward do ROOT e o NIL Caso
	 * esteja-se usando o level do ROOT igual ao maxLevel esse metodo deve
	 * conectar todos os forward. Senao o ROOT eh inicializado com level=1 e o
	 * metodo deve conectar apenas o forward[0].
	 */
	private void connectRootToNil() {

		if (USE_MAX_HEIGHT_AS_HEIGHT) {

			for (int i = 0; i < maxHeight; i++) {

				root.forward[i] = NIL;
			}

		} else {

			root.forward[0] = NIL;
		}
	}

	/**
	 * Metodo que gera uma altura aleatoria para ser atribuida a um novo no no
	 * metodo insert(int,V)
	 */
	private int randomLevel() {
		int randomLevel = 1;
		double random = Math.random();
		while (Math.random() <= PROBABILITY && randomLevel < maxHeight) {
			randomLevel = randomLevel + 1;
		}
		return randomLevel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insert(int key, T newValue, int height) {

		SkipListNode<T>[] update = new SkipListNode[this.maxHeight];
		SkipListNode<T> aux = this.root;

		for (int i = this.height - 1; i >= 0; i--) {

			while (aux.getForward(i).getKey() < key) {

				aux = aux.getForward(i);
			}

			update[i] = aux;

		}

		aux = aux.getForward(0);

		if (aux.getKey() == key) {

			aux.setValue(newValue);

		} else {

			if (height > this.height) {

				for (int i = this.height; i < height; i++) {

					update[i] = this.root;

					if (this.root.getForward(i) == null) {
						this.root.getForward()[i] = NIL;
					}
				}

				this.height = height;
			}

			aux = new SkipListNode<>(key, height, newValue);

			for (int i = 0; i < height; i++) {

				aux.getForward()[i] = update[i].getForward(i);
				update[i].getForward()[i] = aux;
			}

			this.size += 1;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void remove(int key) {

		if (key == Integer.MAX_VALUE || key == Integer.MIN_VALUE)
			return;

		SkipListNode<T>[] update = new SkipListNode[this.maxHeight];
		SkipListNode<T> aux = this.root;

		for (int i = this.height - 1; i >= 0; i--) {

			while (aux.getForward(i).getKey() < key) {
				aux = aux.getForward(i);
			}

			update[i] = aux;
		}

		aux = aux.getForward(0);

		if (aux.key == key) {

			int i = 0;

			while (i < this.height && update[i].getForward(i).equals(aux)) {

				update[i].getForward()[i] = aux.getForward(i);
				i++;
			}

			if (! USE_MAX_HEIGHT_AS_HEIGHT) {

				while (this.height > 1 && this.root.getForward(this.height - 1).equals(NIL)) {

					this.root.getForward()[this.height - 1] = null;
					this.height--;
				}
			}

			this.size -= 1;
		}
	}

	@Override
	public int height() {
		SkipListNode[] slArray = this.toArray();

		return Arrays.stream(slArray)
		             .filter(node -> (node != this.root && node != NIL))
					 .map(node -> node.getHeight())
					 .max((a, b) -> a < b ? a : b).get();
	}

	public boolean isEmpty() {
		return this.root.getForward(0).equals(this.NIL);
	}

	@Override
	public SkipListNode<T> search(int key) {

		SkipListNode<T> aux = this.root;

		for (int i = this.height - 1; i >= 0; i--) {

			while (aux.getForward(i).key < key) {
				aux = aux.getForward(i);
				System.out.println(aux.height);
			}
		}
		
		aux = aux.getForward(0);

		if (aux.getKey() == key) {
			return aux;
		} else {
			return null;
		}
	}

	@Override
	public int size() {
		return this.size;
	}

	public SkipListNode<T>[] toArray() {

		SkipListNode<T>[] array = new SkipListNode[2 + size()];

		SkipListNode<T> aux = this.root;

		int i = 0;

		while (aux != null) {
			array[i] = aux;
			i++;
			aux = aux.getForward(0);
		}

		return array;
	}
}
