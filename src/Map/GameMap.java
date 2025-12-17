package Map;

import java.awt.Image;
<<<<<<< HEAD
import Entities.Block;
import Entities.Ghost;
import Entities.PacmanPlayer;
import Logic.GameState;
=======
import java.util.HashSet;
import java.util.Set;
import Entities.Block;
import Entities.Ghost;
import Entities.PacmanPlayer;
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407

public class GameMap {

    private final int tileSize;
    private final Image wallImage;
    private final Image redGhostImage;
    private final Image blueGhostImage;
    private final Image pinkGhostImage;
    private final Image orangeGhostImage;
<<<<<<< HEAD

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
=======
    
    // Thay vì 1 ảnh pacman, giờ ta giữ 4 ảnh
    private final Image pacmanUp, pacmanDown, pacmanLeft, pacmanRight;

    private PacmanPlayer pacman;
    private Set<Block> walls = new HashSet<>();
    private Set<Block> foods = new HashSet<>();
    private Set<Ghost> ghosts = new HashSet<>();

    public GameMap(int tileSize,
                   Image wallImage,
                   Image redGhostImage,
                   Image blueGhostImage,
                   Image pinkGhostImage,
                   Image orangeGhostImage,
                   // Nhận 4 ảnh vào constructor
                   Image pacmanUp, Image pacmanDown, Image pacmanLeft, Image pacmanRight) {
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
        this.tileSize = tileSize;
        this.wallImage = wallImage;
        this.redGhostImage = redGhostImage;
        this.blueGhostImage = blueGhostImage;
        this.pinkGhostImage = pinkGhostImage;
        this.orangeGhostImage = orangeGhostImage;
<<<<<<< HEAD

=======
        
        // Gán vào biến
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
        this.pacmanUp = pacmanUp;
        this.pacmanDown = pacmanDown;
        this.pacmanLeft = pacmanLeft;
        this.pacmanRight = pacmanRight;
    }

<<<<<<< HEAD
    public void loadMap(int level, GameState state) {

        // ⚠️ KHÔNG tạo collection mới
        state.getWalls().clear();
        state.getFoods().clear();
        state.getGhosts().clear();
        state.setPacman(null);

        String[] tileMap = MapData.getMap(level);

=======
    public void loadMap(int level) {
        walls.clear();
        foods.clear();
        ghosts.clear();
        pacman = null;

        String[] tileMap = MapData.getMap(level);
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[r].length(); c++) {
                char ch = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

<<<<<<< HEAD
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
=======
                if (ch == 'X') {
                    walls.add(new Block(wallImage, x, y, tileSize, tileSize));
                } else if (ch == ' ') {
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                } else if (ch == 'P') {
                    // CẬP NHẬT: Truyền đủ 4 ảnh vào PacmanPlayer
                    pacman = new PacmanPlayer(pacmanUp, pacmanDown, pacmanLeft, pacmanRight, x, y, tileSize, tileSize);
                } else if (ch == 'r') {
                    ghosts.add(new Ghost(redGhostImage, x, y, tileSize, tileSize));
                } else if (ch == 'b') {
                    ghosts.add(new Ghost(blueGhostImage, x, y, tileSize, tileSize));
                } else if (ch == 'p') {
                    ghosts.add(new Ghost(pinkGhostImage, x, y, tileSize, tileSize));
                } else if (ch == 'o') {
                    ghosts.add(new Ghost(orangeGhostImage, x, y, tileSize, tileSize));
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
                }
            }
        }
    }
<<<<<<< HEAD
}
=======

    public PacmanPlayer getPacman() { return pacman; }
    public Set<Block> getWalls()    { return walls; }
    public Set<Block> getFoods()    { return foods; }
    public Set<Ghost> getGhosts()   { return ghosts; }
}
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
