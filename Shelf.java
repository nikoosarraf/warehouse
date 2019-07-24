//Nikoo Sarraf
//260767310

public class Shelf {
	protected int height;
	protected int availableLength;
	protected int totalLength;
	protected Box firstBox;
	protected Box lastBox;

	public Shelf(int height, int totalLength) {
		this.height = height;
		this.availableLength = totalLength;
		this.totalLength = totalLength;
		this.firstBox = null;
		this.lastBox = null;
	}

	protected void clear() {
		availableLength = totalLength;
		firstBox = null;
		lastBox = null;
	}

	public String print() {
		String result = "( " + height + " - " + availableLength + " ) : ";
		Box b = firstBox;
		while (b != null) {
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}

	/**
	 * Adds a box on the shelf. Here we assume that the box fits in height and
	 * length on the shelf.
	 * 
	 * @param b
	 */
	public void addBox(Box b) {
		// ADD YOUR CODE HERE
		if (lastBox == null && firstBox == null) {
			this.firstBox = b;
			this.lastBox = b;
			availableLength = availableLength - b.length;
		} else {
			b.previous = lastBox;
			b.next = null;
			lastBox.next = b;
			lastBox = b;
			availableLength = availableLength - b.length;
		}
	}

	/**
	 * If the box with the identifier is on the shelf, remove the box from the shelf
	 * and return that box. If not, do not do anything to the Shelf and return null.
	 * 
	 * @param identifier
	 * @return
	 */

	public Box removeBox(String identifier) {
		// ADD YOUR CODE HERE
		if (this.firstBox == null) {
			return null;
		}
		Box currentBox = this.firstBox;
		while (currentBox != null) {
			// when u find the box
			if (currentBox.id.equals(identifier)) {
				// if the box u want is the first one on the shelf
				if (currentBox == firstBox) {
					if (firstBox == lastBox) {
						lastBox = null;
					}
					this.firstBox = currentBox.next;
					if (this.firstBox != null) {
						this.firstBox.previous = null;
					}
				}
				// any box on the shelf besides the first one
				else {
					// not the last box
					if (currentBox.next != null) {
						currentBox.previous.next = currentBox.next;
						currentBox.next.previous = currentBox.previous;
					}
					// last box case
					else {
						this.lastBox = currentBox.previous;
						this.lastBox.next = null;
					}
				}
				availableLength = availableLength + currentBox.length;
				currentBox.next = null;
				currentBox.previous = null;
				return currentBox;
			}
			currentBox = currentBox.next;
		}
		return null;
	}
}