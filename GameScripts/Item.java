package GameScripts;

import java.util.Random;

public class Item extends ManageItems{
    int id, count,effect,dmg;
    String title, description;


    public Item(String[] iDetails){
        Random randomNumberGenerator = new Random();
        new Random();
        count = randomNumberGenerator.nextInt(2); // Amount of this item currently held
        try{
            effect = Integer.parseInt(iDetails[2]); // An integer value that determines the effect when the useItem function is called
            dmg = Integer.parseInt(iDetails[3]); // Base damage the item will do (if a damaging item)
        }
        catch(NumberFormatException e){
            effect = 0;
            dmg = 5;
        }

        title = iDetails[1]; // The title - or name - of the item
        description = iDetails[4]; // Self Explanatory
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String useItem() { // ITEMS NOT CURRENTLY IMPLEMENTED
        String temp = "";
        if (count > 0) {
            switch(effect) {
                case 0:
                    // Lower all enemy defence
                    temp = "Lowered all enemy armor by " + dmg + " pts";
                    // Enemy has their turn
                    break;
                case 1:
                    // Hurt all enemies (SMALL)
                    count -= 1;
                    temp = ("Hurt all enemies a mild " + dmg + " hit points");
                    // Enemy has their turn
                    break;
                case 2:
                    // Hurt all enemies (LARGE)
                    count -= 1;
                    temp = ("Hurt all enemies a whopping " + dmg + " hit points");
                    // Enemy has their turn
            }
        } else {
            temp = "You don't have any of this item";
        }
        return temp;
    }

}
