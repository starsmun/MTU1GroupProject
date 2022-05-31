import PlayerScripts.ManageCreatures;
import PlayerScripts.Monster;
import PlayerScripts.Player;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;


public class DnDGame extends GameEngine{
    private static final List<CoolButton> buttons = new LinkedList<>();
    private static final List<CoolButton> monsterButtons = new LinkedList<>();
    private static int height = 600, width = 1000;
    private static float state = 1;
    private static Image player1, monster1, monster2, monster3, monster4;

    private static Player selectedPlayer;
    private static Monster selectedMonster = null;

    private static List<Integer> onScreenMonsters = new LinkedList<>();

    public static void main(String args[]) {
        DnDGame game = new DnDGame();
        createGame(game,30);
    }

    public void init(){
        setWindowSize(width,height);
        ManageCreatures.setupMonsters();
        ManageCreatures.setupPlayers(1);

        player1 = loadImage("PlayerScripts/player1.png");
        monster1 = loadImage("MonsterAssets/Fred.png");
        monster2 = loadImage("MonsterAssets/Mark.png");
        monster3 = loadImage("MonsterAssets/Trolle.png");
        monster4 = loadImage("MonsterAssets/UnPol.png");

        onScreenMonsters.add(0);
        onScreenMonsters.add(1);
        onScreenMonsters.add(2);
        onScreenMonsters.add(3);

        selectedPlayer = ManageCreatures.playerByIndex(0);

        // A New type of button called CoolButton
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1);
        buttons.add(new CoolButton("Attack", (int) (x+w*0.3), (int) (y+h*0.25),150,70,1,0));
        buttons.add(new CoolButton("Item", (int) (x+w*0.55), (int) (y+h*0.25),150,70,1,1));
        buttons.add(new CoolButton("Heal", (int) (x+w*0.3), (int) (y+h*0.6),150,70,1,2));
        buttons.add(new CoolButton("Run", (int) (x+w*0.55), (int) (y+h*0.6),150,70,1,3));

        monsterButtons.add(new CoolButton(monster1,0,height/6,150,150,1.1f,0));
        monsterButtons.add(new CoolButton(monster2,0,height/6,150,150,1.1f,1));
        monsterButtons.add(new CoolButton(monster3,0,height/6,150,150,1.1f,2));
        monsterButtons.add(new CoolButton(monster4,0,height/6,150,150,1.1f,3));

        buttons.add(new CoolButton("Back", (int) (x+w*0.60), (int) (y+h*0.8),100,50,1.1f,10));

        centreMonsterButtons(onScreenMonsters);

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

        for(CoolButton button : monsterButtons){
            if(button.stateR == state) {
                try{
                    onScreenMonsters.get(button.ID);
                    paintButton(button);
                }catch (IndexOutOfBoundsException ignored){

                }
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

        if(state == 1){
            CoolButton button;
            for(int id: onScreenMonsters){
                button = monsterButtons.get(id); drawImage(button.image, button.buttonPosX,button.buttonPosY,button.width,button.height);
            }
        }



        if(state == 1.1f && selectedMonster != null){
            drawBoldText(new double[]{x+w*0.27,y,x+w*0.73,y+h,-1.5},selectedMonster.getName(),"Comic Sans MS",30,"Centre");
            drawText(new double[]{x+w*0.27,y,x+w*0.73,y+h,-0.5},"Health: " + selectedMonster.getHealth() + "/" + selectedMonster.getMaxHealth(),"Comic Sans MS",23,"Centre");
            drawText(new double[]{x+w*0.27,y,x+w*0.73,y+h,0.5},"Attack: " + selectedMonster.getAttack(),"Comic Sans MS",23,"Centre");
            drawText(new double[]{x+w*0.27,y,x+w*0.73,y+h,1.5},"Defence: " + selectedMonster.getDefense(),"Comic Sans MS",23,"Centre");
        }else if (state == 1.1f){
            drawBoldText(new double[]{x+w*0.27,y,x+w*0.73,y+h,-0.5},"Select the Monster","Comic Sans MS",30,"Centre");
            drawBoldText(new double[]{x+w*0.27,y,x+w*0.73,y+h,0.5},"to attack","Comic Sans MS",30,"Centre");
        }
    }

    public void paintButton(CoolButton button){
         //Key is the text of the label
        changeColor(black);
        drawSolidRectangle(button.buttonPosX,button.buttonPosY,button.width,button.height);
        changeColor(white);
        //Custom Function to properly centre text
        if(button.label != null){
            drawBoldText(new double[]{button.buttonPosX,button.buttonPosY,button.buttonPosX+button.width,
                    button.buttonPosY+button.height,0},button.label,"Comic Sans MS",+button.height/3,"Centre");
        }else{
            drawImage(button.image, button.buttonPosX,button.buttonPosY,button.width,button.height);
        }
        if(button.selected == true){
            changeColor(blue);
            drawRectangle(button.buttonPosX,button.buttonPosY,button.width,button.height,5);
        }
    }

    public void centreMonsterButtons(List<Integer> ids){
        int nMonsters = ids.size();
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1); //Dimensions of the box
        float offset = (nMonsters - 1) / 2f;
        int monsterCount = 0;
        for(float i = -offset;i <= offset;i++){
            CoolButton monsterButton = monsterButtons.get(ids.get(monsterCount));
            float xPosRelative = i * monsterButton.width;
            monsterButton.setButtonPosX((int) (x + (w / 2) + xPosRelative * 1.3) - monsterButton.width/2);
            monsterCount++;
        }
    }


    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();

        //Checks if any buttons are pressed
        for(CoolButton button : buttons){
            if(button.stateR == state){
                if(mouseX >= button.buttonPosX && mouseX <= button.buttonPosX+button.width && mouseY >= button.buttonPosY && mouseY <= button.buttonPosY+button.height) {
                    callButtonMethod(button.ID,"press");
                }
            }
        }
        for(CoolButton button : monsterButtons){
            if(button.stateR == state){
                if(mouseX >= button.buttonPosX && mouseX <= button.buttonPosX+button.width && mouseY >= button.buttonPosY && mouseY <= button.buttonPosY+button.height) {
                    callButtonMethod(button.ID,"press");
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        selectedMonster = null;
        //Checks if any buttons are pressed
        for(CoolButton button : buttons){
            if(button.stateR == state){
                if(mouseX >= button.buttonPosX && mouseX <= button.buttonPosX+button.width && mouseY >= button.buttonPosY && mouseY <= button.buttonPosY+button.height) {
                    button.setSelected(true);
                    callButtonMethod(button.ID,"hover");
                }else{
                    button.setSelected(false);

                }
            }
        }
        for(CoolButton button : monsterButtons){
            if(button.stateR == state){
                if(mouseX >= button.buttonPosX && mouseX <= button.buttonPosX+button.width && mouseY >= button.buttonPosY && mouseY <= button.buttonPosY+button.height) {
                    button.setSelected(true);
                    callButtonMethod(button.ID,"hover");
                }else{
                    button.setSelected(false);

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

    public void callButtonMethod(int ID,String condition){
        if(state == 1){
            switch(ID) {
                case 0:
                    if(condition == "press") state = 1.1f;
                    break;

            }
        }
        else if(state == 1.1f){
            if(ID < 4) {
                selectedMonster = ManageCreatures.monsterByIndex(ID);
                if(condition == "press") selectedPlayer.attackCreature(selectedMonster);
            }else {
                if(condition == "press") {
                    selectedMonster = null;
                    state = 1;
                }
            }
        }
    }
}
