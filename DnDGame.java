import PlayerScripts.ManageCreatures;
import PlayerScripts.Monster;
import PlayerScripts.Player;


import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;


public class DnDGame extends GameEngine{
    private static final List<CoolButton> buttons = new LinkedList<>();
    private static int height = 640, width = 1080, state = 0;

    private static Player selectedPlayer;
    private static Monster selectedMonster;

    public static void main(String args[]) {
        DnDGame game = new DnDGame();
        createGame(game,30);
    }

    public void init(){
        setWindowSize(width,height);
        ManageCreatures.setupMonsters();
        ManageCreatures.setupPlayers(1);

        selectedPlayer = ManageCreatures.playerByIndex(0);
        selectedMonster = ManageCreatures.monsterByIndex(0);

        // A New type of button called CoolButton
        buttons.add(new CoolButton());
        buttons.get(0).setup("Attack Monster", 0.3f,0.5f,220,80,0,0);

        buttons.add(new CoolButton());
        buttons.get(1).setup("Attack Player", 0.6f,0.5f,220,80,0,1);
    }

    public void update(double dt) {

    }


    public void paintComponent() {
        // For every button in list (Should add something to check for state)
        for(CoolButton button : buttons){
            if(button.stateR == state) {
                paintButton(button);
            }
        }
    }

    public void paintButton(CoolButton button){
         //Key is the text of the label
        drawSolidRectangle(button.buttonPosX,button.buttonPosY,button.width,button.height);
        changeColor(white);
        //Custom Function to properly centre text
        drawBoldText(new double[]{button.buttonPosX,button.buttonPosY,button.buttonPosX+button.width,
                button.buttonPosY+button.height,button.stateR},button.label,"Comic Sans MS",+button.height/3,"Centre");
        changeColor(black);
    }


    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();

        //Checks if any buttons are pressed
        for(CoolButton button : buttons){
            if(button.stateR == state){
                if(mouseX >= button.buttonPosX && mouseX <= button.buttonPosX+button.width && mouseY >= button.buttonPosY && mouseY <= button.buttonPosY+button.height) {
                    callButtonMethod(button.ID);
                }
            }
        }
    }

    public static int getWidth(){
        return width;
    }

    public static int getHeight(){
        return height;
    }

    public void callButtonMethod(int ID){
        switch(ID) {
            case 0: // Selected Player attacks Selected Monster
                selectedPlayer.attackCreature(selectedMonster);
                break;

            case 1: // Selected Monster attacks Selected player (Not working currently since monsters don't have attack currently
                selectedMonster.attackCreature(selectedPlayer);
                break;

        }
    }
}
