package okey;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class OkeyTest {

	Controller controller;

	@Before
	public void initialize() {
		controller = new Controller();
	}

	@Test
	public void testSequenceAndColorTogetherWithJoker() {

		Tile tile1 = new Tile(Color.CHANGEABLE, -1, 0);
		tile1.setJoker(true);
		Tile tile2 = new Tile(Color.BLACK, 2, 1);
		Tile tile3 = new Tile(Color.YELLOW, 5, 2);
		Tile tile4 = new Tile(Color.BLUE, 5, 3);
		Tile tile5 = new Tile(Color.RED, 5, 4);
		Tile tile6 = new Tile(Color.BLUE, 6, 5);
		Tile tile7 = new Tile(Color.BLACK, 6, 6);
		Tile tile8 = new Tile(Color.BLUE, 7, 7);
		Tile tile9 = new Tile(Color.RED, 7, 8);
		Tile tile10 = new Tile(Color.RED, 8, 9);
		Tile tile11 = new Tile(Color.RED, 9, 10);
		Tile tile12 = new Tile(Color.RED, 10, 11);
		Tile tile13 = new Tile(Color.RED, 11, 12);
		Tile tile14 = new Tile(Color.BLUE, 13, 13);

		List<Tile> tiles = Arrays.asList(new Tile[] { tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9,
				tile10, tile11, tile12, tile13, tile14 });

		Player player1 = new Player(1);
		player1.setTiles(tiles);
		List<Player> players = Arrays.asList(new Player[] { player1 });
		controller.sortTiles(players);
		controller.checkForSequences(players);
		controller.checkForSameNumberDifferentColors(players);

		assertEquals(3, players.get(0).getRemainingTileAmount().intValue());
	}

	@Test
	public void testDoublesWithJoker() {

		Tile tile1 = new Tile(Color.CHANGEABLE, -1, 0);
		tile1.setJoker(true);
		Tile tile2 = new Tile(Color.BLUE, 3, 1);
		Tile tile3 = new Tile(Color.YELLOW, 8, 2);
		Tile tile4 = new Tile(Color.RED, 10, 3);
		Tile tile5 = new Tile(Color.BLUE, 3, 4);
		Tile tile6 = new Tile(Color.BLACK, 2, 5);
		Tile tile7 = new Tile(Color.BLACK, 2, 6);
		Tile tile8 = new Tile(Color.BLUE, 12, 7);
		Tile tile9 = new Tile(Color.RED, 10, 8);
		Tile tile10 = new Tile(Color.BLUE, 9, 9);
		Tile tile11 = new Tile(Color.BLUE, 9, 10);
		Tile tile12 = new Tile(Color.BLACK, 6, 11);
		Tile tile13 = new Tile(Color.BLACK, 11, 12);
		Tile tile14 = new Tile(Color.BLUE, 11, 13);
		Tile tile15 = new Tile(Color.YELLOW, 5, 14);

		List<Tile> tiles = Arrays.asList(new Tile[] { tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9,
				tile10, tile11, tile12, tile13, tile14, tile15 });

		Player player1 = new Player(2);
		player1.setTiles(tiles);
		List<Player> players = Arrays.asList(new Player[] { player1 });
		controller.sortTiles(players);
		controller.checkForDoubles(players);

		controller.checkForSequences(players);
		controller.checkForSameNumberDifferentColors(players);

		assertEquals(5, players.get(0).getDoubleCount().intValue());
		assertEquals(5, players.get(0).getRemainingTileAmount().intValue());
	}

	@Test
	public void testDoublesWithoutJoker() {

		Tile tile1 = new Tile(Color.YELLOW, 3, 0);
		Tile tile2 = new Tile(Color.BLUE, 3, 1);
		Tile tile3 = new Tile(Color.YELLOW, 8, 2);
		Tile tile4 = new Tile(Color.RED, 10, 3);
		Tile tile5 = new Tile(Color.BLUE, 3, 4);
		Tile tile6 = new Tile(Color.BLACK, 2, 5);
		Tile tile7 = new Tile(Color.BLACK, 2, 6);
		Tile tile8 = new Tile(Color.BLUE, 12, 7);
		Tile tile9 = new Tile(Color.RED, 10, 8);
		Tile tile10 = new Tile(Color.BLUE, 9, 9);
		Tile tile11 = new Tile(Color.BLUE, 9, 10);
		Tile tile12 = new Tile(Color.BLACK, 6, 11);
		Tile tile13 = new Tile(Color.BLACK, 11, 12);
		Tile tile14 = new Tile(Color.BLUE, 11, 13);
		Tile tile15 = new Tile(Color.YELLOW, 5, 14);

		List<Tile> tiles = Arrays.asList(new Tile[] { tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9,
				tile10, tile11, tile12, tile13, tile14, tile15 });

		Player player1 = new Player(3);
		player1.setTiles(tiles);
		List<Player> players = Arrays.asList(new Player[] { player1 });
		controller.sortTiles(players);
		controller.checkForDoubles(players);
		assertEquals(4, players.get(0).getDoubleCount().intValue());
		assertEquals(7, players.get(0).getRemainingTileAmount().intValue());
	}

	@Test
	public void testSequenceAndColorTogetherWithoutJoker() {

		Tile tile1 = new Tile(Color.YELLOW, 6, 0);
		Tile tile2 = new Tile(Color.BLACK, 8, 1);
		Tile tile3 = new Tile(Color.YELLOW, 3, 2);
		Tile tile4 = new Tile(Color.BLUE, 8, 3);
		Tile tile5 = new Tile(Color.YELLOW, 4, 4);
		Tile tile6 = new Tile(Color.YELLOW, 5, 5);
		Tile tile7 = new Tile(Color.BLACK, 9, 6);
		Tile tile8 = new Tile(Color.BLUE, 9, 7);
		Tile tile9 = new Tile(Color.RED, 13, 8);
		Tile tile10 = new Tile(Color.RED, 2, 9);
		Tile tile11 = new Tile(Color.RED, 8, 10);
		Tile tile12 = new Tile(Color.RED, 1, 11);
		Tile tile13 = new Tile(Color.RED, 11, 12);
		Tile tile14 = new Tile(Color.BLUE, 13, 13);

		List<Tile> tiles = Arrays.asList(new Tile[] { tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9,
				tile10, tile11, tile12, tile13, tile14 });

		Player player1 = new Player(4);
		player1.setTiles(tiles);
		List<Player> players = Arrays.asList(new Player[] { player1 });
		controller.sortTiles(players);
		controller.checkForSequences(players);
		controller.checkForSameNumberDifferentColors(players);

		assertEquals(7, players.get(0).getRemainingTileAmount().intValue());
	}

}
