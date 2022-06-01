package PlayerScripts;

import java.util.LinkedList;
import java.util.List;

public class Player extends BaseCreature{
    int money, level;
    List<Integer> items = new LinkedList<>();


    public List<String> attackCreature(Monster monster) {
        List<String> events = super.attackCreature(monster);

        boolean killedCheck = monster.health == 0;
        events.add("You attacked " + monster.name);

        int damage = attack - monster.defense;

        if (!killedCheck) { // If creature killed
            events.add("You hit! You deal " + damage + " damage");
        } else {
            events.add("You killed " + monster.name +",You get " + monster.killPrize + " gold");
        }
        return events;
    }

    public Player(){
        maxHealth = 100; health = maxHealth; defense = 10;
        attack = 30; money = 100; level = 0;

        items.add(8);
        items.add(9);
        items.add(10);
    }

    public int getItem(int index){
        return items.get(index);
    }

    public List<Integer> getItems(){
        return items;
    }

}
