package Map;

import java.awt.Image;
import Entities.Block;
import Entities.Ghost;
import Entities.PacmanPlayer;
import Logic.GameState;

public class GameMap {

    private final int tileSize;
    private final Image wallImage;
    private final Image redGhostImage;
    private final Image blueGhostImage;
    private final Image pinkGhostImage;
    private final Image orangeGhostImage;

    private final Image pacmanUp, pacmanDown, pacmanLeft, pacmanRight;

    public GameMap(
            int tileSize,
            Image wallImage,
            Image redGhostImage,
            Image blueGhostImage,
            Image pinkGhostImage,
            Image orangeGhostImage,
            Image pacmanUp,
            Image pacmanDown,
            Image pacmanLeft,
            Image pacmanRight
    ) {
        this.tileSize = tileSize;
        this.wallImage = wallImage;
        this.redGhostImage = redGhostImage;
        this.blueGhostImage = blueGhostImage;
        this.pinkGhostImage = pinkGhostImage;
        this.orangeGhostImage = orangeGhostImage;

        this.pacmanUp = pacmanUp;
        this.pacmanDown = pacmanDown;
        this.pacmanLeft = pacmanLeft;
        this.pacmanRight = pacmanRight;
    }

    public void loadMap(int level, GameState state) {

        // ⚠️ KHÔNG tạo collection mới
        state.getWalls().clear();
        state.getFoods().clear();
        state.getGhosts().clear();
        state.setPacman(null);

        String[] tileMap = MapData.getMap(level);
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[r].length(); c++) {
                char ch = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                switch (ch) {
                    case 'X':
                        state.getWalls().add(
                                new Block(wallImage, x, y, tileSize, tileSize)
                        );
                        break;

                    case ' ':
                        state.getFoods().add(
                                new Block(null, x + 14, y + 14, 4, 4)
                        );
                        break;

                    case 'P':
                        state.setPacman(
                                new PacmanPlayer(
                                        pacmanUp, pacmanDown,
                                        pacmanLeft, pacmanRight,
                                        x, y, tileSize, tileSize
                                )
                        );
                        break;

                    case 'r':
                        state.getGhosts().add(
                                new Ghost(redGhostImage, x, y, tileSize, tileSize)
                        );
                        break;

                    case 'b':
                        state.getGhosts().add(
                                new Ghost(blueGhostImage, x, y, tileSize, tileSize)
                        );
                        break;

                    case 'p':
                        state.getGhosts().add(
                                new Ghost(pinkGhostImage, x, y, tileSize, tileSize)
                        );
                        break;

                    case 'o':
                        state.getGhosts().add(
                                new Ghost(orangeGhostImage, x, y, tileSize, tileSize)
                        );
                        break;
                }
            }
        }
    }
}
