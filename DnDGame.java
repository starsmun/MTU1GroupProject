import PlayerScripts.ManageCreatures;
import PlayerScripts.Monster;
import PlayerScripts.Player;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;


public class DnDGame extends GameEngine{
    private static final List<CoolButton> buttons = new LinkedList<>();
    private static int height = 600, width = 1000, state = 1;
    private static Image player1;

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

        player1 = loadImage("PlayerScripts/player1.png");

        selectedPlayer = ManageCreatures.playerByIndex(0);
        selectedMonster = ManageCreatures.monsterByIndex(0);

        // A New type of button called CoolButton
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1);
        buttons.add(new CoolButton());
        buttons.get(0).setup("Attack", (int) (x+w*0.3), (int) (y+h*0.25),150,70,1,0);

        buttons.add(new CoolButton());
        buttons.get(1).setup("Item", (int) (x+w*0.55), (int) (y+h*0.25),150,70,1,1);

        buttons.add(new CoolButton());
        buttons.get(2).setup("Heal", (int) (x+w*0.3), (int) (y+h*0.6),150,70,1,2);

        buttons.add(new CoolButton());
        buttons.get(3).setup("Run", (int) (x+w*0.55), (int) (y+h*0.6),150,70,1,3);




    }

    public void update(double dt) {

    }


    public void paintComponent() {
        changeBackgroundColor(84,84,84);
        clearBackground(width,height);
        paintDefaultLayout();
        paintFightLayout();
        // For every button in list (Should add something to check for state)
        for(CoolButton button : buttons){
            if(button.stateR == state) {
                paintButton(button);
            }
        }
    }

    public void paintDefaultLayout(){
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1); //Dimensions of the box
        changeColor(237,191,149);
        drawSolidRectangle(x,y,w,h);
        changeColor(Color.white);
        drawSolidRectangle(x+w*0.27,y,5,h);
        drawSolidRectangle(x+w*0.73,y,5,h);
    }

    public void paintFightLayout(){
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1); //Dimensions of the box
        drawImage(player1,(x+w*0.27/2)-70,y+10,140,140);
        changeColor(black);
        drawText(new double[]{x,y+h/2,x+w*0.27,y+h,-1},"Health: " + selectedPlayer.getHealth() + "/" + selectedPlayer.getMaxHealth(),"Comic Sans MS",23,"Centre");
        drawText(new double[]{x,y+h/2,x+w*0.27,y+h,0},"Attack: " + selectedPlayer.getAttack(),"Comic Sans MS",23,"Centre");
        drawText(new double[]{x,y+h/2,x+w*0.27,y+h,1},"Defence: " + selectedPlayer.getDefense(),"Comic Sans MS",23,"Centre");
    }

    public void paintButton(CoolButton button){
         //Key is the text of the label
        changeColor(black);
        drawSolidRectangle(button.buttonPosX,button.buttonPosY,button.width,button.height);
        changeColor(white);
        //Custom Function to properly centre text
        drawBoldText(new double[]{button.buttonPosX,button.buttonPosY,button.buttonPosX+button.width,
                button.buttonPosY+button.height,0},button.label,"Comic Sans MS",+button.height/3,"Centre");
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

            case 5: // Selected Monster attacks Selected player
                selectedMonster.attackCreature(selectedPlayer);
                break;

        }
    }
}
