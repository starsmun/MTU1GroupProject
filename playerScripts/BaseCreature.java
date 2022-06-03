package PlayerScripts;

import java.util.LinkedList;
import java.util.List;

public class BaseCreature { // Stats & Get functions
    int maxHealth, defense, attack, skillLevel,health;

    public int getSkillLevel(){
        return skillLevel;
    }

    public double getHealth(){
        return health;
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    public int getDefense(){
        return defense;
    }

    public int getAttack(){
        return attack;
    }

    public boolean takeDamage(int damage){  // Call when Creature takes damage
        health -= Math.max(damage,0);

        if(health < 0) health = 0;

        return health <= 0; // Returns True if creature is dead
    }

    public List<String> attackCreature(BaseCreature creature){ // Call when attacking another Creature
        List<String> events = new LinkedList<>();

        creature.takeDamage(attack - creature.defense);
        return events;
    }
}
