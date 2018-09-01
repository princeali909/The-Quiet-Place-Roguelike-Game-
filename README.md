# The-Quiet-Place-Roguelike-Game-

![main game gif](https://user-images.githubusercontent.com/38504747/44948793-03ea5f00-add9-11e8-989f-0c7af5b04f46.gif)


The Quiet Place is a procedurally generated dungeon game where the objective is to collect all the tokens (in this case apples), without letting the monster get to you. This game is themed on the popular thriller "A Quiet Place" in that the enemy NPC only moves when you move. This was completely intentional and not because threading with this tile engine was so glitchy *wink*. I am using a tile engine from UC Berkeley, which uses Princeton’s STDraw library.

The program starts in the main menu with a "Lore" option (press 'L' key) people can check out before they play to brush up on the fictional backstory of the game.  

![menu gif](https://user-images.githubusercontent.com/38504747/44948890-0f3e8a00-addb-11e8-90eb-b62d0ceb8b02.gif)

```
Lore : You are a scavenger in a Dungeon looking for food for your family. A blind monster who tracks their prey purely on sound (including footsteps) is in the dungeon with you. You need to collect all the food before your leave the dungeon to feed your family.

Tip: Don't bang on the walls, make every step count!
```

You can press any key to leave the lore menu and head straight to playing the game by pressing the 'P' key.

This game is procedurally generated, generating random dungeons of various sizes, randomly placed tokens and token amounts as well as randomly placed player and enemy positions.

![random dungeon](https://user-images.githubusercontent.com/38504747/44948913-a1469280-addb-11e8-9b82-c9e68ed3ffc7.gif)


## How to Play:
1. Install Java 8
2. Clone Repository to IDE of choice.
3. Import the provided library (Code will not work without the library).
4. Run code in Main.java
5. 'w','s','a', & 'd' keys to move.
6. 'q' to quit game


![lose game](https://user-images.githubusercontent.com/38504747/44949175-326c3800-ade1-11e8-9b62-724cf4d7096b.gif)


# Credits

[Princeton STDraw library](https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html)

[Procedural Dungeon Generation Connect Rooms With Passageways](https://gamedev.stackexchange.com/questions/114767/procedural-dungeon-generation-connect-rooms-with-passageways)

[Procedurally Generating Dungeons](http://www.codingcookies.com/2012/08/07/procedurally-generating-dungeons/)

Tile Engine & Library from University of California, Berkeley












