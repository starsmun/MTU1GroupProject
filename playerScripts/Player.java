package PlayerScripts;

import java.util.LinkedList;
import java.util.List;

public class Player extends BaseCreature{
    int money, level;
    List<Integer> items = new LinkedList<>();

    public boolean attackCreature(Monster monster) {
        boolean killedCheck = super.attackCreature(monster);
        if(killedCheck){ // If creature killed
            System.out.println("Congrats you won " + monster.killPrize + " gold");
        }
        return killedCheck;
    }

    public Player(){
        maxHealth = 100;
        health = maxHealth;
        defense = 10;
        attack = 100;
        money = 100;
        level = 0;

    }

}
