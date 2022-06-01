package GameScripts;

public class items extends ManageItems{

    public items(){
        count = 0; // Amount of this item currently held
        effect = 0; // An integer value that determines the effect when the useItem function is called
        dmg = 0; // Base damage the item will do (if a damaging item)
        title = "None"; // The title - or name - of the item
        description = "N/a"; // Self Explanatory
    }

    public String useItem() {
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
