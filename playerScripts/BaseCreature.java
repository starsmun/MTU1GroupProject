package PlayerScripts;

public class BaseCreature {
    int maxHealth, defense, attack, skillLevel;
    double health;

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
        health -= damage;
        return health <= 0; // Returns True if creature is dead
    }

    public boolean attackCreature(BaseCreature creature){ // Call when attacking another Creature
        return creature.takeDamage(attack); // Returns True if creature is dead
    }
}
