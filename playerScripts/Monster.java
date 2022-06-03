package PlayerScripts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Monster extends BaseCreature{ // Monster Stats
    int killPrize, maxLevel;
    String name, ID;

    public Monster(String name) {
        this.name = name;
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader("PlayerScripts/MonsterStats.txt"))); // // Scans the Monster stats file and allocates the data to in-engine variables

            String line;
            String[] mDetails;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                mDetails = line.split(",");
                if(Objects.equals(mDetails[1], this.name)){
                    ID = mDetails[0];
                    try{
                        killPrize = Integer.parseInt(mDetails[2]);
                        maxHealth = Integer.parseInt(mDetails[3]);
                        defense = Integer.parseInt(mDetails[4]);
                        attack = Integer.parseInt(mDetails[5]);
                        maxLevel = Integer.parseInt(mDetails[6]);
                    }
                    catch(NumberFormatException e){ // Default Monster Stats
                        killPrize = 0;
                        maxHealth = 10;
                        defense = 10;
                        attack = 10;
                        maxLevel = 10;
                    }
                    health = maxHealth;
                    break;
                }

            }
        } catch (
            FileNotFoundException e) { // Catches when the file is not found
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    // Get functions
    public String getName(){
        return name;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public String getID(){
        return ID;
    }

    public int getKillPrize(){
        return killPrize;
    }

    // Attacking function -- for Monster -> Player attacks. Also updates event list
    public List<String> attackCreature(Player player) {
        super.attackCreature(player);

        boolean killedCheck = player.health == 0;
        List<String> events = new LinkedList<>();

        int damage = attack-player.defense;

        if(!killedCheck){ // If creature killed
            events.add(name + " attacked!,Dealing " + damage + " damage");
        }else{
            events.add("You died");
        }
        return events;
    }
}
