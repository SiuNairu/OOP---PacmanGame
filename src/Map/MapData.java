package Map;

// File: MapData.java
import java.util.ArrayList;
import java.util.List;

public class MapData {

    // Danh sách lưu trữ tất cả các bản đồ
    private static final List<String[]> ALL_MAPS = new ArrayList<>();

    // X = wall, O = skip, P = pac man, ' ' = food
    // Ghost: b = blue, o = orange, p = pink, r = red

    // MAP LEVEL 0
    private static final String[] MAP_LEVEL_0 = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "X    X       X    X",
        "XXXX X XXrXX X XXXX",
        "X       bpo       X",
        "XXXX X XXXXX X XXXX",
        "X    X       X    X",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };


    private static final String[] MAP_LEVEL_1 = {
        "XXXXXXXXXXXXXXXXXXX",
        "X   XX       XX   X",
        "X X XX XXXXX XX X X",
        "X X             X X",
        "X XXXX X XXX X XXXX",
        "X      X XXX X    X",
        "X XXXX X XXX X XXXX",
        "X X    X     X    X",
        "X X XX XXXXX XX X X",
        "X X X   bpor  X X X",
        "X X XX XXXXX XX X X",
        "X X    X     X    X",
        "X XXXX X XXX X XXXX",
        "X      X XXX X    X",
        "X XXXX X XXX X XXXX",
        "X  X           X  X",
        "XX X X XXXXXXX X XX",
        "X    X   P     X  X",
        "X XXXXXX X XXXXXX X",
        "X        O        X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    static {
        ALL_MAPS.add(MAP_LEVEL_0); 
        ALL_MAPS.add(MAP_LEVEL_1); 
    }

    public static String[] getMap(int level) {
        if (level >= 0 && level < ALL_MAPS.size()) {
            return ALL_MAPS.get(level);
        }
        return ALL_MAPS.get(0);
    }

    public static int getTotalLevels() {
        return ALL_MAPS.size();
    }
}
