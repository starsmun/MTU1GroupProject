package PlayerScripts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

public class Monster extends BaseCreature{
    int killPrize, maxLevel;
    String name;

    public Monster(String name) {
        this.name = name;
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader("PlayerScripts/MonsterStats.txt")));

            String line;
            String[] mDetails;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                mDetails = line.split(",");
                if(Objects.equals(mDetails[0], this.name)){
                    try{
                        killPrize = Integer.parseInt(mDetails[1]);
                        maxHealth = Integer.parseInt(mDetails[2]);
                        defense = Integer.parseInt(mDetails[3]);
                        attack = Integer.parseInt(mDetails[4]);
                        maxLevel = Integer.parseInt(mDetails[5]);
                    }
                    catch(NumberFormatException e){
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

    public String getName(){
        return name;
    }

    public int getKillPrize(){
        return killPrize;
    }

    public boolean attackCreature(Player player) {
        boolean killedCheck = super.attackCreature(player);
        if(killedCheck){ // If creature killed
            System.out.println("Player is dead");

        }
        return killedCheck;
    }
}
