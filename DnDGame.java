import PlayerScripts.*;
import GameScripts.*;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


public class DnDGame extends GameEngine{
    private static final List<CoolButton> buttons = new LinkedList<>();
    private static final List<CoolButton> monsterButtons = new LinkedList<>();
    private static int height = 600, width = 1000, nFrame, direction = 1;
    private static int[] monsterOffsetsY = new int[4], monsterDirectionY = {1,1,1,1};
    private static float state = 1;
    private static Image blankButton, background, player1, monster1, monster2, monster3, monster4;

    private static Player selectedPlayer;
    private static Monster selectedMonster = null;
    private static Monster previousMonster = null;
    private static Item selectedItem = null;


    private static List<String> onScreenMonsters = new LinkedList<>();
    private static List<String> onScreenItems = new LinkedList<>();

    private static List<String> pastEvents = new LinkedList<>();

    public static void main(String args[]) {
        DnDGame game = new DnDGame();
        createGame(game,60);
    }

    public void init(){
        setWindowSize(width,height);
        ManageCreatures.setupMonsters();
        ManageCreatures.setupPlayers(1);
        ManageItems.setupItems();

        background = loadImage("GameAssets/background.png");
        blankButton = loadImage("GameAssets/button.png");

        player1 = loadImage("PlayerScripts/player1.png");
        monster1 = loadImage("MonsterAssets/Fred.png");
        monster2 = loadImage("MonsterAssets/Mark.png");
        monster3 = loadImage("MonsterAssets/Trolle.png");
        monster4 = loadImage("MonsterAssets/UnPol.png");

        selectedPlayer = ManageCreatures.playerByIndex(0);

        onScreenMonsters.add("4");
        onScreenMonsters.add("5");
        onScreenMonsters.add("6");
        onScreenMonsters.add("7");
        onScreenItems = selectedPlayer.getItems();

        // A New type of button called CoolButton
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1);
        buttons.add(new CoolButton("Attack", (int) (x+w*0.3), (int) (y+h*0.25),150,70,1,0));
        buttons.add(new CoolButton("Item", (int) (x+w*0.55), (int) (y+h*0.25),150,70,1,1));
        buttons.add(new CoolButton("Heal", (int) (x+w*0.3), (int) (y+h*0.6),150,70,1,2));
        buttons.add(new CoolButton("Run", (int) (x+w*0.55), (int) (y+h*0.6),150,70,1,3));

        buttons.add(new CoolButton(monster1,0,height/9,150,150,1.1f,4));
        buttons.add(new CoolButton(monster2,0,height/9,150,150,1.1f,5));
        buttons.add(new CoolButton(monster3,0,height/9,150,150,1.1f,6));
        buttons.add(new CoolButton(monster4,0,height/9,150,150,1.1f,7));

        buttons.add(new CoolButton("Item 1",0,(int) (y+h*0.10),100,50,1.2f,8));
        buttons.add(new CoolButton("Item 2",0,(int) (y+h*0.10),100,50,1.2f,9));
        buttons.add(new CoolButton("Item 3",0,(int) (y+h*0.10),100,50,1.2f,10));

        buttons.add(new CoolButton("Back", (int) (x+w*0.60), (int) (y+h*0.8),100,50,1.1f,11));
        buttons.add(new CoolButton("Back", (int) (x+w*0.60), (int) (y+h*0.8),100,50,1.2f,12));
        centreItems(onScreenMonsters);
        centreItems(onScreenItems);

    }

    public void update(double dt) {
        nFrame++;
        if(nFrame > 30){
            nFrame = 0;
        }
        for(String id: onScreenMonsters){
            if(nFrame % 2 == 0 || nFrame % 5 == 0){
                int ID = Integer.parseInt(id);
                if(monsterOffsetsY[ID-4] > 30){
                    monsterDirectionY[ID-4] = -1;
                }
                else if (monsterOffsetsY[ID-4] < 0){
                    monsterDirectionY[ID-4] = 1;
                }

                monsterOffsetsY[ID-4] += monsterDirectionY[ID-4] * ManageCreatures.monsterByIndex(ID-4).getAttack() / 2;
            }

        }
    }


    public void paintComponent() {
        changeBackgroundColor(84,84,84);
        clearBackground(width,height);
        drawImage(background, 0,0);

        if((state >= 1) && (state < 2)){
            paintDefaultLayout();
            paintFightLayout();
        }
        // For every button in list (Should add something to check for state)
        for(CoolButton button : buttons){
            if(button.stateR == state) {
                if(button.ID > 3 && button.ID < 8) {
                    try {
                        if(onScreenMonsters.indexOf(String.valueOf(button.ID)) >= 0) paintButton(button);
                    } catch (IndexOutOfBoundsException ignored) {

                    }
                }else if(button.ID > 7 && button.ID < 11){
                    try{
                        if(onScreenItems.indexOf(String.valueOf(button.ID)) >= 0) paintButton(button);
                    }catch (IndexOutOfBoundsException ignored){

                    }
                }else{
                    paintButton(button);
                }

            }
        }
    }

    public void paintDefaultLayout(){
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1); //Dimensions of the box
        changeColor(237,191,149,190);
        drawSolidRectangle(x,y,w,h);
        changeColor(255,255,255,190);
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

        int i = 0;
        for(String event : pastEvents){
            if(event.length() < 30){
                i++;
                drawText(x+w*0.75,y+(width/40*(i+1)),event,"Comic Sans MS",18);
            }else{
                i++;
                drawText(x+w*0.75,y+(width/40*(i+1)),event.split(",")[0],"Comic Sans MS",18);
                i++;
                drawText(x+w*0.75,y+(width/40*(i+1)),event.split(",")[1],"Comic Sans MS",18);
            }

        }

        if((state == 1) || (state == 1.2f)){
            CoolButton button;
            for(String id: onScreenMonsters){
                button = buttons.get(Integer.parseInt(id)); drawImage(button.image, button.buttonPosX,button.buttonPosY+monsterOffsetsY[Integer.parseInt(id)-4],button.width,button.height);
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
        } else if (state == 1.2f && selectedItem != null){
            drawBoldText(new double[]{x+w*0.27,y-h/6,x+w*0.73,y+h,-0.5}, selectedItem.getTitle(), "Comic Sans MS", 25, "Centre");
            if(selectedItem.getDescription().length() < 35){
                drawText(new double[]{x+w*0.27,y-h/6,x+w*0.73,y+h,0.5}, "This is a " + selectedItem.getDescription(), "Comic Sans MS", 18, "Centre");
            }else{
                drawText(new double[]{x+w*0.27,y-h/6,x+w*0.73,y+h,0.5}, "This is a " + selectedItem.getDescription().split("#")[0], "Comic Sans MS", 18, "Centre");
                drawText(new double[]{x+w*0.27,y-h/6,x+w*0.73,y+h,1.5}, selectedItem.getDescription().split("#")[1], "Comic Sans MS", 18, "Centre");
            }

        }
    }

    public void paintButton(CoolButton button){
         //Key is the text of the label
        changeColor(black);
        // drawSolidRectangle(button.buttonPosX,button.buttonPosY,button.width,button.height);
        drawImage(blankButton, button.buttonPosX,button.buttonPosY,button.width,button.height);
        changeColor(white);
        //Custom Function to properly centre text
        if(button.label != null){
            drawBoldText(new double[]{button.buttonPosX,button.buttonPosY,button.buttonPosX+button.width,
                    button.buttonPosY+button.height,0},button.label,"Comic Sans MS",+button.height/3,"Centre");
        }else{
            drawImage(button.image, button.buttonPosX,button.buttonPosY,button.width,button.height);
        }
        if(button.selected){
            changeColor(blue);
            drawRectangle(button.buttonPosX,button.buttonPosY,button.width,button.height,5);
        }
    }

    public static void centreItems(List<String> ids){
        int nItems = ids.size();
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1); //Dimensions of the box
        float offset = (nItems - 1) / 2f;
        int count = Integer.parseInt(ids.get(0));
        for(float i = -offset;i <= offset;i++){
            CoolButton button = buttons.get(count);
            float xPosRelative = i * button.width;
            button.setButtonPosX((int) (x + (w / 2) + xPosRelative * 1.3) - button.width/2);
            count++;
        }
    }


    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();

        //Checks if any buttons are pressed
        for(CoolButton button : buttons){
            if(button.stateR == state){
                if(mouseX >= button.buttonPosX && mouseX <= button.buttonPosX+button.width && mouseY >= button.buttonPosY && mouseY <= button.buttonPosY+button.height) {
                    if(button.ID > 3 && button.ID < 8) {
                        if (onScreenMonsters.indexOf(String.valueOf(button.ID)) >= 0) callButtonMethod(button.ID, "press");
                    }
                    else callButtonMethod(button.ID, "press");

                }
            }
        }
    }

    public void mouseMoved(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        selectedMonster = null;
        selectedItem = null;
        //Checks if any buttons are pressed
        for(CoolButton button : buttons){
            if(button.stateR == state){
                if(mouseX >= button.buttonPosX && mouseX <= button.buttonPosX+button.width && mouseY >= button.buttonPosY && mouseY <= button.buttonPosY+button.height) {
                    if(button.ID > 3 && button.ID < 8) {
                        if (onScreenMonsters.indexOf(String.valueOf(button.ID)) >= 0){
                            button.setSelected(true);
                            callButtonMethod(button.ID,"hover");
                        }
                    }else{
                        button.setSelected(true);
                        callButtonMethod(button.ID,"hover");
                    }
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

    public static void addEvent(String event){
        pastEvents.add(event);
    }

    public static void callButtonMethod(int ID,String condition){
        if(state == 1){
            switch(ID) {
                case 0:
                    if (condition == "press") state = 1.1f; // Fight Menu State
                    break;
                case 1:
                    if (condition == "press") state = 1.2f; // Item Menu State

                case 2:
                    if (condition == "press") {
                        selectedPlayer.heal();
                        pastEvents.add("You healed, giving 40 health");
                        if(previousMonster != null) if(previousMonster.getHealth() != 0) pastEvents.addAll(previousMonster.attackCreature(selectedPlayer));
                    }; // Item Menu State
            }
        }

        else if(state == 1.1f){
            if(ID > 3 && ID < 8) {
                selectedMonster = ManageCreatures.monsterByIndex(ID-4);
                if(condition == "press") {
                    pastEvents = selectedPlayer.attackCreature(selectedMonster);
                    if(selectedMonster.getHealth() == 0){
                        int index = onScreenMonsters.indexOf(ID + "");
                        onScreenMonsters.remove(index);

                    }else{
                        pastEvents.addAll(selectedMonster.attackCreature(selectedPlayer));
                    }
                    if(onScreenMonsters.size() == 0) state = 10;
                    else centreItems(onScreenMonsters);
                    previousMonster = selectedMonster;
                }
            }else {
                if(condition == "press") {
                    selectedMonster = null;
                    state = 1;
                }
            }
        }

        else if(state == 1.2f){
            if(ID > 7 && ID < 11) {
                selectedItem = ManageItems.getItem(Integer.parseInt(selectedPlayer.getItem(ID-8))-8);
            }else {
                if (condition == "press") {
                    selectedItem = null;
                    state = 1;
                }
            }
        }

    }
}
