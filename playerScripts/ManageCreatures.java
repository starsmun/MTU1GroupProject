package PlayerScripts;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ManageCreatures {
    private static final List<Monster> monsters = new LinkedList<>();
    private static final List<Player> players = new LinkedList<>();


    public static ArrayList<Monster> cMonsterOptions(int nMonsters) {
        ArrayList<Monster> tempList = new ArrayList<>();
        for (int i = 0; i < nMonsters; i++) {
            //Choose Random Monster based on player level
        }

        return tempList;
    }

    //Creates monsters by using the file names in monster assets (Should change to check from a text file or something)
    public static void setupMonsters() {
        //List of Monsters from MonsterAsset Folder
        List<String> monstersList = Stream.of(new File("MonsterAssets").listFiles())
                .map(File::getName)
                .collect(Collectors.toList());
        int i = 0;
        for (String m : monstersList) {
            monsters.add(new Monster(m.split("\\.")[0]));
            i++;
        }
    }

    //Sets up default players, default values need to be changed, because the default is very overpowered
    public static void setupPlayers(int nPlayers) {
        for (int i = 0; i < nPlayers; i++) {
            players.add(new Player());
        }
    }

    //Returns monster from an index
    public static Monster monsterByIndex(int index) {
        return monsters.get(index);
    }

    //Returns player from an index
    public static Player playerByIndex(int index) {
        return players.get(index);
    }
}
