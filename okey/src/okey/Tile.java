package okey;

public class Tile {
	private Color color;
	private Integer number;
	private Integer index;
	private boolean joker = false;
	private boolean fakeJoker = false;
	private boolean isUsed = false;

	public Tile(Color color, Integer number, Integer index) {
		super();
		this.color = color;
		this.number = number;
		this.index = index;
	}

	@Override
	public String toString() {
		return index + " [" + color + " " + number + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (color != other.color)
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isJoker() {
		return joker;
	}

	public void setJoker(boolean joker) {
		this.joker = joker;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public boolean isFakeJoker() {
		return fakeJoker;
	}

	public void setFakeJoker(boolean fakeJoker) {
		this.fakeJoker = fakeJoker;
	}

}
