package okey;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private List<Tile> tiles;
	private Integer no;
	private Integer remainingTileAmount;
	private Integer doubleCount;

	public Player(Integer no) {
		super();
		this.no = no;
		this.tiles = new ArrayList<>();
		this.doubleCount = 0;
		this.remainingTileAmount = Integer.MAX_VALUE;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Integer getRemainingTileAmount() {
		return remainingTileAmount;
	}

	public void setRemainingTileAmount(Integer remainingTileAmount) {
		this.remainingTileAmount = remainingTileAmount;
	}

	public Integer getDoubleCount() {
		return doubleCount;
	}

	public void setDoubleCount(Integer doubleCount) {
		this.doubleCount = doubleCount;
	}

}