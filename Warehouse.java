// Nikoo Sarraf
// 260767310

public class Warehouse {

	protected Shelf[] storage;
	protected int nbShelves;
	public Box toShip;
	public UrgentBox toShipUrgently;
	static String problem = "problem encountered while performing the operation";
	static String noProblem = "operation was successfully carried out";

	public Warehouse(int n, int[] heights, int[] lengths) {
		this.nbShelves = n;
		this.storage = new Shelf[n];
		for (int i = 0; i < n; i++) {
			this.storage[i] = new Shelf(heights[i], lengths[i]);
		}
		this.toShip = null;
		this.toShipUrgently = null;
	}

	public String printShipping() {
		Box b = toShip;
		String result = "not urgent : ";
		while (b != null) {
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n" + "should be already gone : ";
		b = toShipUrgently;
		while (b != null) {
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}

	public String print() {
		String result = "";
		for (int i = 0; i < nbShelves; i++) {
			result += i + "-th shelf " + storage[i].print();
		}
		return result;
	}

	public void clear() {
		toShip = null;
		toShipUrgently = null;
		for (int i = 0; i < nbShelves; i++) {
			storage[i].clear();
		}
	}

	/**
	 * initiate the merge sort algorithm
	 */
	public void sort() {
		mergeSort(0, nbShelves - 1);
	}

	/**
	 * performs the induction step of the merge sort algorithm
	 * 
	 * @param start
	 * @param end
	 */
	// this method works
	protected void mergeSort(int start, int end) {
		// ADD YOUR CODE HERE
		if (start < end) {
			int mid = (start + end) / 2;
			mergeSort(start, mid);
			mergeSort((mid + 1), end);
			merge(start, mid, end);
		}
	}

	/**
	 * performs the merge part of the merge sort algorithm
	 * 
	 * @param start
	 * @param mid
	 * @param end
	 */
	// this method works
	protected void merge(int start, int mid, int end) {
		// ADD YOUR CODE HERE
		Shelf[] helper = new Shelf[storage.length];
		for (int i = start; i <= end; i++) {
			helper[i] = storage[i];
		}
		int i = start;
		int j = mid + 1;
		int k = start;

		while (i <= mid && j <= end) {
			if (helper[i].height <= helper[j].height) {
				storage[k] = helper[i];
				i++;
			} else {
				storage[k] = helper[j];
				j++;
			}
			k++;
		}
		while (i <= mid) {
			storage[k] = helper[i];
			k++;
			i++;
		}
	}

	/**
	 * Adds a box is the smallest possible shelf where there is room available. Here
	 * we assume that there is at least one shelf (i.e. nbShelves >0)
	 * 
	 * @param b
	 * @return problem or noProblem
	 */
	public String addBox(Box b) {
		// ADD YOUR CODE HERE
		for (int i = 0; i < storage.length; i++) {
			if (storage[i].height >= b.height && storage[i].availableLength >= b.length) {
				storage[i].addBox(b);
				return noProblem;
			}
		}
		return problem;
	}

	/**
	 * Adds a box to its corresponding shipping list and updates all the fields
	 * 
	 * @param b
	 * @return problem or noProblem
	 */
	public String addToShip(Box b) {
		// ADD YOUR CODE HERE
		if (b == null) {
			return problem;
		}
		if (b instanceof UrgentBox) {
			if (this.toShipUrgently == null) {
				this.toShipUrgently = (UrgentBox) b;
				this.toShipUrgently.previous = null;
				this.toShipUrgently.next = null;
				return noProblem;
			} else {
				b.next = toShipUrgently;
				toShipUrgently.previous = b;
				b.previous = null;
				this.toShipUrgently = (UrgentBox) b;
				return noProblem;
			}
		} else if (b instanceof Box) {
			if (this.toShip == null) {
				this.toShip = b;
				this.toShip.previous = null;
				this.toShip.next = null;
				return noProblem;
			} else {
				b.next = toShip;
				toShip.previous = b;
				b.previous = null;
				this.toShip = b;
				return noProblem;
			}
		}
		return problem;
	}

	/**
	 * Find a box with the identifier (if it exists) Remove the box from its
	 * corresponding shelf Add it to its corresponding shipping list
	 * 
	 * @param identifier
	 * @return problem or noProblem
	 */
	public String shipBox(String identifier) {
		// ADD YOUR CODE HERE
		for (int i = 0; i < storage.length; i++) {
			Box b = storage[i].removeBox(identifier);
			if (b == null) {
				continue;
			}
			if (b.id == identifier) {
				addToShip(b);
				return noProblem;
			}
			if (b.id == null) {
				return problem;
			}
		}
		return problem;
	}

	/**
	 * if there is a better shelf for the box, moves the box to the optimal shelf.
	 * If there are none, do not do anything
	 * 
	 * @param b
	 * @param position
	 */
	public void moveOneBox(Box b, int position) {
		// ADD YOUR CODE HERE
		int currentHeight = this.storage[position].height;
		for (int i = 0; i < this.nbShelves; i++) {
			if (this.storage[i].height < currentHeight && this.storage[i].height >= b.height
					&& this.storage[i].availableLength >= b.length && position != i) {
				storage[position].removeBox(b.id);
				this.storage[i].addBox(b);
				return;
			}
		}
	}

	/**
	 * reorganize the entire warehouse : start with smaller shelves and first box on
	 * each shelf.
	 */
	public void reorganize() {
		// ADD YOUR CODE HERE
		Box nextBox;
		Box currentBox;
		for (int i = 0; i < nbShelves; i++) {
			currentBox = this.storage[i].firstBox;
			if (currentBox == null) {
				nextBox = null;
			} else {
				nextBox = this.storage[i].firstBox.next;
			}
			while (currentBox != null) {
				nextBox = currentBox.next;
				moveOneBox(currentBox, i);
				currentBox = nextBox;
			}
		}
	}
}