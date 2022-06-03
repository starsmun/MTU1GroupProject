package PlayerScripts;

import java.util.LinkedList;
import java.util.List;

public class Player extends BaseCreature{
    int money, level;
    List<String> items = new LinkedList<>();


    public List<String> attackCreature(Monster monster) { // Deals with calculating damage & updates the events list
        List<String> events = super.attackCreature(monster);

        boolean killedCheck = monster.health == 0;
        events.add("You attacked " + monster.name);

        int damage = attack - monster.defense;

        if (!killedCheck) { // If creature hit
            events.add("You hit! You deal " + damage + " damage");
        } else { // If creature killed
            events.add("You killed " + monster.name +",You get " + monster.killPrize + " gold");
            money += monster.killPrize;
        }
        return events;
    }

    public Player(){ // Player Stats & Items
        maxHealth = 100; health = maxHealth; defense = 10;
        attack = 30; money = 0; level = 0;

        items.add("8");
        items.add("9");
        items.add("10");
    }

    // Get functions
    public String getItem(int index){
        return items.get(index);
    }

    public List<String> getItems(){
        return items;
    }

    public int getMoney(){
        return money;
    }

    public void heal(){ // Heals the character a custom amount (40 currently)
        health += 40;
        if(health > 100) health = 100;
    }

    public void resetGold(){
        money = 0;
    } // Resets gold (for use on player death)

}
