package okey;

public enum Color {
	YELLOW, BLUE, BLACK, RED, CHANGEABLE;

	public String toString() {

		switch (this) {
		case YELLOW:
			return "Sarı";
		case BLUE:
			return "Mavi";
		case BLACK:
			return "Siyah";
		case RED:
			return "Kırmızı";
		case CHANGEABLE:
			return "Değişken";
		default:
			return "";
		}
	}
}
