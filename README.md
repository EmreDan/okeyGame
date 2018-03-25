# okeyGame
Basic okey game in Java. Purpose of the game is to order tiles by the game rules and minimize the remaining tile amount.

Classic game details : https://en.wikipedia.org/wiki/Okey

106 Tiles are created. 2 copies of 4 Colors each with 13 numbers plus 2 fake jokey tiles. 

4 Players are created, tiles are shuffled and distributed to players randomly. 
1 random player gets 15 tiles and others get 14 tiles each.

After the distribution of tiles, each players tiles are checked by the game rules.
First, the algorithm checks for doubles. If there are doubles (also using the jokey) calculates the remaining tile amount.
Sets the remaining tile amount of player. 
Then checks the sequenced tiles and same number different colored tiles.
If the remaining tile amount is less than (doubled) remaining amount, then sets the new remaining tile amount of player. 

After the operations are completed. Prints out the players tiles, starting from the closest to the winning position.
