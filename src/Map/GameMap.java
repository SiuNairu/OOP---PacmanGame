package Map;

import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import Entities.Block;
import Entities.Ghost;
import Entities.PacmanPlayer;

public class GameMap {

    private final int tileSize;
    private final Image wallImage;
    private final Image redGhostImage;
    private final Image blueGhostImage;
    private final Image pinkGhostImage;
    private final Image orangeGhostImage;
    
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
        this.tileSize = tileSize;
        this.wallImage = wallImage;
        this.redGhostImage = redGhostImage;
        this.blueGhostImage = blueGhostImage;
        this.pinkGhostImage = pinkGhostImage;
        this.orangeGhostImage = orangeGhostImage;
        
        // Gán vào biến
        this.pacmanUp = pacmanUp;
        this.pacmanDown = pacmanDown;
        this.pacmanLeft = pacmanLeft;
        this.pacmanRight = pacmanRight;
    }

    public void loadMap(int level) {
        walls.clear();
        foods.clear();
        ghosts.clear();
        pacman = null;

        String[] tileMap = MapData.getMap(level);
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[r].length(); c++) {
                char ch = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

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
                }
            }
        }
    }

    public PacmanPlayer getPacman() { return pacman; }
    public Set<Block> getWalls()    { return walls; }
    public Set<Block> getFoods()    { return foods; }
    public Set<Ghost> getGhosts()   { return ghosts; }
}