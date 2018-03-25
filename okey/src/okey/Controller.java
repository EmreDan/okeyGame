package okey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Controller {

	final static int tileNumberLimit = 13;
	final static int copySize = 2;
	final static int numberOfPlayers = 4;
	final static int tilesPerPlayer = 14;
	final static int minimumTileCountPerSet = 3;

	/**
	 * Goal of the OKEY game is minimizing remaining tile count after organizing the
	 * given tiles.
	 */
	public void startGame() {
		// All the tiles that will be used in the game.
		List<Tile> allTiles = initializeTiles();

		List<Player> players = new ArrayList<Player>();
		for (int i = 1; i <= numberOfPlayers; i++) {
			players.add(new Player(i));
		}

		players = distributeTilesToPlayers(players, allTiles);

		sortTiles(players);

		checkForDoubles(players);

		checkForSequences(players);

		checkForSameNumberDifferentColors(players);

		printPlayersTiles(players);

	}

	/**
	 * Checks each players tiles for the same number, different color sets. Such as
	 * Yellow 2, Black 2, Blue 2
	 * 
	 * @param players
	 */
	protected void checkForSameNumberDifferentColors(List<Player> players) {

		for (Player player : players) {

			// Map tiles with numbers as keys. It will be used to check colors with same
			// number.
			Map<Integer, List<Tile>> colorTileMap = player.getTiles().stream()
					.filter(tile -> !tile.isJoker() && !tile.isUsed()).collect(Collectors.groupingBy(Tile::getNumber));

			Set<Tile> tilesUsedInSet = new HashSet<>();

			Long jokerCount = player.getTiles().stream().filter(tile -> tile.isJoker() && !tile.isUsed()).count();

			for (List<Tile> sameNumberTiles : colorTileMap.values()) {

				if (sameNumberTiles.size() >= minimumTileCountPerSet) {
					tilesUsedInSet.addAll(sameNumberTiles);
				} else if (sameNumberTiles.size() == minimumTileCountPerSet - 1) {
					// if there is 1 tile missing to create a set. Check for Joker tile.

					if (jokerCount > 0) {
						jokerCount--;
						tilesUsedInSet.add(player.getTiles().stream().filter(tile -> tile.isJoker() && !tile.isUsed())
								.findFirst().get());
						tilesUsedInSet.addAll(sameNumberTiles);
					}
				}
			}

			if (player.getDoubleCount() == 0) {
				Set<Integer> usedIndexes = tilesUsedInSet.stream().map(Tile::getIndex).collect(Collectors.toSet());

				for (Tile tile : player.getTiles()) {
					if (usedIndexes.contains(tile.getIndex())) {
						tile.setUsed(true);
					}
				}

				player.setRemainingTileAmount(player.getRemainingTileAmount() - tilesUsedInSet.size());
			}

		}
	}

	/**
	 * Checks each players tiles for sequences. If, number of remaining tiles after
	 * sequence operation is less than current remaining tile count, updates the
	 * remaining tile count.
	 * 
	 * @param players
	 */
	protected void checkForSequences(List<Player> players) {
		for (Player player : players) {

			// Map tiles with colors as keys. It will be used to check sequences with same
			// color.
			// Joker tiles will be used later on.
			Map<Color, List<Tile>> colorTileMap = player.getTiles().stream().filter(tile -> !tile.isJoker())
					.collect(Collectors.groupingBy(Tile::getColor));

			Map<Color, List<Tile>> sortedColorTileMap = colorTileMap.entrySet().stream()
					.sorted(Comparator.comparingInt(e -> tilesPerPlayer - e.getValue().size()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
						throw new AssertionError();
					}, LinkedHashMap::new));

			Long jokerCount = player.getTiles().stream().filter(Tile::isJoker).count();

			List<Tile> usedTilesInSequence = new ArrayList<>();
			for (List<Tile> sameColorTiles : sortedColorTileMap.values()) {
				// Number of tiles in a sequence
				Set<Tile> sequencedTiles = new HashSet<>();
				for (int i = 0; i < sameColorTiles.size(); i++) {

					// If the sequence ended on the last element of same color. And if there is a a
					// sequence.
					if (i == sameColorTiles.size() - 1) {
						if (sequencedTiles.size() >= minimumTileCountPerSet) {
							usedTilesInSequence.addAll(sequencedTiles);
						} else if (sequencedTiles.size() == minimumTileCountPerSet - 1) {
							// if there is 1 tile missing to create a set. Check for Joker tile.
							Optional<Tile> joker = player.getTiles().stream().filter(tile -> tile.isJoker()).findAny();
							if (joker.isPresent() && jokerCount > 0) {
								sequencedTiles.add(joker.get());
								usedTilesInSequence.addAll(sequencedTiles);
								jokerCount--;
							}
						}
						break;
					}

					// if next number is 1 bigger.
					if (sameColorTiles.get(i + 1).getNumber().equals(sameColorTiles.get(i).getNumber() + 1)) {
						sequencedTiles.add(sameColorTiles.get(i));
						sequencedTiles.add(sameColorTiles.get(i + 1));
					} else {// next number is not sequenced. Sequence is broken.
						// If there is enough tiles to create a set.

						if (sequencedTiles.size() >= minimumTileCountPerSet) {
							usedTilesInSequence.addAll(sequencedTiles);
						} else if (sequencedTiles.size() == minimumTileCountPerSet - 1) {
							// if there is 1 tile missing to create a set. Check for Joker tile.
							Optional<Tile> joker = player.getTiles().stream().filter(tile -> tile.isJoker()).findAny();
							if (joker.isPresent() && jokerCount > 0) {
								sequencedTiles.add(joker.get());
								usedTilesInSequence.addAll(sequencedTiles);
								jokerCount--;
							}
						}
						sequencedTiles = new HashSet<>();
					}

				}
			}
			Integer remainingTileCountAfterSequences = player.getTiles().size() - usedTilesInSequence.size();

			if (remainingTileCountAfterSequences < player.getRemainingTileAmount()) {
				player.setDoubleCount(0);
				player.setRemainingTileAmount(remainingTileCountAfterSequences);
				Set<Integer> usedIndexes = usedTilesInSequence.stream().map(Tile::getIndex).collect(Collectors.toSet());
				for (Tile tile : player.getTiles()) {
					if (usedIndexes.contains(tile.getIndex())) {
						tile.setUsed(true);
					} else {
						tile.setUsed(false);
					}
				}
			}

		}

	}

	/**
	 * Sorts tiles for first number then color.
	 * @param players
	 */
	protected void sortTiles(List<Player> players) {
		players.forEach(player -> {
			{
				player.setTiles(player.getTiles().stream()
						.sorted(Comparator.comparing(Tile::getNumber).thenComparing(Tile::getColor))
						.collect(Collectors.toList()));
			}
		});
	}

	/**
	 * Checks each players tiles for doubles. If, number of remaining tiles after
	 * doubling operation is less than current remaining tile count, updates the
	 * remaining tile count.
	 * 
	 * @param players
	 */
	protected void checkForDoubles(List<Player> players) {
		for (Player player : players) {

			List<Tile> playerTiles = player.getTiles();
			Integer doubleCount = 0;

			for (Tile tile : playerTiles) {
				if (tile.isJoker()) {
					doubleCount++;
					tile.setUsed(true);
				}
			}

			for (int i = 0; i < playerTiles.size() - 1; i++) {
				Tile currentTile = playerTiles.get(i);
				if (!currentTile.isJoker() && currentTile.equals(playerTiles.get(i + 1))) {
					doubleCount++;
					currentTile.setUsed(true);
					playerTiles.get(i + 1).setUsed(true);
				}
			}

			player.setDoubleCount(doubleCount);
			if (player.getRemainingTileAmount() == null) {
				player.setRemainingTileAmount(player.getTiles().size());
			}

			Integer tilesRemained = player.getTiles().size() - 2 * doubleCount;

			if (tilesRemained < player.getRemainingTileAmount()) {
				player.setRemainingTileAmount(tilesRemained);
			}
		}

	}

	/**
	 * Returns a list of tiles consisting of 106 tiles. 4 colors of tiles each with
	 * 2 sets of numbers from 1 to 13 Plus 2 fake joker tiles.
	 * 
	 * @return
	 */
	protected List<Tile> initializeTiles() {
		List<Tile> allTiles = new ArrayList<>();

		// Colors in order.
		Color[] colors = new Color[] { Color.YELLOW, Color.BLUE, Color.BLACK, Color.RED };
		int index = 0;

		// Create 2 copies of 53 tiles. 4 Colors, each consisting of 13 tiles.
		for (int i = 0; i < copySize; i++) {
			for (Color color : colors) {
				List<Tile> tilesWithSameColor = getColorTiles(color, index);
				allTiles.addAll(tilesWithSameColor);
				index += tileNumberLimit;
			}
			Tile fakeJoker = new Tile(Color.CHANGEABLE, 0, index++);
			allTiles.add(fakeJoker);
		}

		shuffleAndSetJokers(allTiles);

		return allTiles;
	}

	/**
	 * Prints each players tiles.
	 * @param players
	 */
	protected void printPlayersTiles(List<Player> players) {

		List<Player> sortedPlayers = players.stream().sorted(Comparator.comparingInt(Player::getRemainingTileAmount))
				.collect(Collectors.toList());
		System.out.println("KAZANAN");
		for (Player player : sortedPlayers) {
			System.out.println("Oyuncu " + player.getNo() + " - Taş sayısı:" + player.getTiles().size());
			if (player.getDoubleCount() != 0) {
				System.out.println("Çifte gider, çift sayısı:" + player.getDoubleCount());
				System.out.println("Kullanılmamış kalan taş sayısı:" + player.getRemainingTileAmount());
				printTiles(player.getTiles());
			} else {
				System.out.println("Düze gider.");
				System.out.println("Kullanılmamış kalan taş sayısı:" + player.getRemainingTileAmount());
				printTiles(player.getTiles());
			}

		}
	}
/**
 * Distributes all tiles to players.
 * 3 players gets 14 tiles. 1 Random player gets 15 tiles. 
 * @param players
 * @param allTiles
 * @return
 */
	protected List<Player> distributeTilesToPlayers(List<Player> players, List<Tile> allTiles) {
		int count = 0;
		// Distribute 14 tiles per player.
		for (Player player : players) {
			player.getTiles().addAll(allTiles.subList(count * tilesPerPlayer, (count + 1) * tilesPerPlayer));
			count++;
		}

		// Select 1 player randomly to give the next tile.
		Random random = new Random();
		players.get(random.nextInt(numberOfPlayers)).getTiles().add(allTiles.get(tilesPerPlayer * numberOfPlayers));

		return players;
	}

	/**
	 * Shuffles the given List<Tile> tiles. Picks a random indicator. Selects the
	 * next tile with the same color as joker. (If the indicator number is 13, joker
	 * number is will be 1) Sets the fake jokers color and number as same as the
	 * actual joker.
	 * 
	 * @param tiles
	 */
	protected void shuffleAndSetJokers(List<Tile> tiles) {

		Collections.shuffle(tiles);

		// Get indicator tile to determine joker tile. (Filtering changeable/fake joker
		// tiles)
		List<Tile> tilesWithNumbers = tiles.stream().filter(tile -> !tile.getColor().equals(Color.CHANGEABLE))
				.collect(Collectors.toList());
		Random random = new Random();
		// Select a random tile as indicator.
		Tile indicator = tilesWithNumbers.get(random.nextInt(tilesWithNumbers.size()));

		Color jokerColor = indicator.getColor();
		Integer jokerNumber = (indicator.getNumber() % tileNumberLimit) + 1;
		System.out.println("Okey taşı=" + jokerColor + " " + jokerNumber);

		// Set joker tiles boolean joker value to true, number to -1 and color to
		// CHANGEABLE
		tiles.stream().filter(tile -> tile.getColor().equals(jokerColor) && tile.getNumber().equals(jokerNumber))
				.forEach(tile -> {
					{
						tile.setNumber(-1);
						tile.setColor(Color.CHANGEABLE);
						tile.setJoker(true);
					}
				});

		// Set fake joker tiles values
		tiles.stream().filter(tile -> tile.getNumber().equals(0)).forEach(tile -> {
			tile.setColor(jokerColor);
			tile.setFakeJoker(true);
			tile.setNumber(jokerNumber);
		});
	}
	
	/**
	 * Creates 13 tiles for the given color. Indexes starting from starting index.
	 * @param color
	 * @param startingIndex
	 * @return
	 */
	List<Tile> getColorTiles(Color color, int startingIndex) {
		List<Tile> tiles = new ArrayList<Tile>();

		for (int i = 0; i < tileNumberLimit; i++) {
			Tile stone = new Tile(color, i + 1, startingIndex + i);
			tiles.add(stone);
		}

		return tiles;
	}
	
	/**
	 * Prints the given tiles.
	 * @param tiles
	 */
	protected void printTiles(List<Tile> tiles) {
		for (Tile tile : tiles) {
			String isUsed = tile.isUsed() ? " kullanıldı" : " ";
			String isJoker = tile.isJoker() ? " OKEY" : tile.isFakeJoker() ? " SAHTE OKEY " : "";
			System.out.println(tile + isJoker + isUsed);
		}
		System.out.println();
	}

}
