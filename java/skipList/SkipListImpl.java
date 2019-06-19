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
		this.root.forward = Arrays.stream(this.root.forward).map(element -> element = NIL).toArray(SkipListNode[]::new);
	}

	/**
	 * Metodo que gera uma altura aleatoria para ser atribuida a um novo no no
	 * metodo insert(int,V)
	 */
	public int randomLevel(int level) {
		return (Math.random() <= PROBABILITY && level < maxHeight) ? randomLevel(level+1) : level;
	}

	private SkipListNode<T> findAux(int key, int height, SkipListNode<T> aux) {
		if (aux.getForward(height).getKey() < key) return findAux(key, height, aux.getForward(height));
		else if (aux.getForward(height).getKey() > key && height > 0) return findAux(key, height-1, aux);
		else if (aux.getForward(height).getKey() > key) return aux;
		else return aux.getForward(height); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insert(int key, T newValue, int height) {

		SkipListNode<T>[] update = new SkipListNode[this.maxHeight];
		SkipListNode<T> aux = this.root;
		// for (int i = this.height - 1; i >= 0; i--) update[i] = aux;

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
				// updateReferences(path, path.getHeight()-1, this.height-1);
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

	public int heightAux(int height) {
		if (this.root.getForward(height).getValue() != null) return height+1;
		else return heightAux(height-1);
	}

	@Override
	public int height() {
		return heightAux(this.maxHeight-1);
	}

	public boolean isEmpty() {
		return this.root.getForward(0).equals(this.NIL);
	}

	private SkipListNode<T> searchAux(int key, SkipListNode<T> aux, int height) {
		if (aux.getForward(height).getKey() == key || height == 0) return aux.getForward(0);
		else if (aux.getForward(height).getKey() > key) return searchAux(key, aux, height-1);
		else return searchAux(key, aux.getForward(height), height);
	}

	@Override
	public SkipListNode<T> search(int key) {
		SkipListNode<T> aux = searchAux(key, this.root, this.height-1);
		return aux.getKey() == key ? aux : null;
	}

	@Override
	public int size() {
		return this.size;
	}

	private SkipListNode<T>[] create(SkipListNode<T>[] array, SkipListNode<T> aux, int idx) {
		if (idx < size()+2) {
			array[idx] = aux;
			return create(array, aux.getForward(0), idx+1);
		} else return array;
	}

	public SkipListNode<T>[] toArray() {
		SkipListNode<T>[] array = new SkipListNode[2 + size()];
		return create(array, this.root, 0);
	}
}
