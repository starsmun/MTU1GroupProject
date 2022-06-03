import PlayerScripts.*;
import GameScripts.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


/*/   Group Members   ///
 Lachlan W-S   21005784
 Brook Pugmire 15204583
 Floyd Smith   21010980
///                  /*/



public class DnDGame extends GameEngine{
    private static final List<CoolButton> buttons = new LinkedList<>();
    private static final List<CoolButton> monsterButtons = new LinkedList<>();
    private static int height = 600, width = 1000, nFrame, direction = 1;
    private static int[] monsterOffsetsY = new int[4], monsterDirectionY = {1,1,1,1};
    private static int[] monsterOffsetsX = new int[4], monsterDirectionX = {1,1,1,1};
    private static float state = 0;
    private static Image blankButton, background, player1, monster1, monster2, monster3, monster4;

    private static Player selectedPlayer;
    private static Monster selectedMonster = null;
    private static Monster previousMonster = null;
    private static Item selectedItem = null;

    private static AudioClip menuMusic, fightMusic, buttonSound, monsterDeath, playerDeath;

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

        //-------------------------------------  Loads Images  ------------------------------------------//

        background = loadImage("GameAssets/background.png");
        blankButton = loadImage("GameAssets/button.png");

        player1 = loadImage("PlayerScripts/warrior.png");
        monster1 = subImage(loadImage("MonsterAssets/Knight.png"),0,0,94,94);
        monster2 = subImage(loadImage("MonsterAssets/Lich.png"),0,0,94,94);
        monster3 = subImage(loadImage("MonsterAssets/Minotaurs.png"),0,0,94,94);
        monster4 = subImage(loadImage("MonsterAssets/Reptilian.png"),0,0,94,94);

        //---------------------------------------------------------------------------------------------//

        //       Load Music     //
        menuMusic = loadAudio("GameAssets/backgroundMusic.wav");
        fightMusic = loadAudio("GameAssets/fightMusic.wav");
        buttonSound = loadAudio("GameAssets/buttonClick.wav");
        monsterDeath = loadAudio("GameAssets/monsterDeath.wav");
        playerDeath = loadAudio("GameAssets/playerDeath.wav");


        selectedPlayer = ManageCreatures.playerByIndex(0); // This is the current player

        // Monsters that are alive //
        onScreenMonsters.add("4");
        onScreenMonsters.add("5");
        onScreenMonsters.add("6");
        onScreenMonsters.add("7");

        // Items that the player has //
        onScreenItems = selectedPlayer.getItems();

        //-------------------------------------  Loads Buttons  ------------------------------------------//

        //  Dimensions of the main box  //
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1);


        // StareR = State the button appears in, ID = same as button ID, used instead if a button gets removed

        //  Main Fight Buttons  //
        buttons.add(new CoolButton("Attack", (int) (x+w*0.3), (int) (y+h*0.25),150,70,1,0));
        buttons.add(new CoolButton("Item", (int) (x+w*0.55), (int) (y+h*0.25),150,70,1,1));
        buttons.add(new CoolButton("Heal", (int) (x+w*0.3), (int) (y+h*0.6),150,70,1,2));
        buttons.add(new CoolButton("Run", (int) (x+w*0.55), (int) (y+h*0.6),150,70,1,3));

        //  Monster Buttons  //
        buttons.add(new CoolButton(monster1,0,height/9,150,150,1.1f,4));
        buttons.add(new CoolButton(monster2,0,height/9,150,150,1.1f,5));
        buttons.add(new CoolButton(monster3,0,height/9,150,150,1.1f,6));
        buttons.add(new CoolButton(monster4,0,height/9,150,150,1.1f,7));

        //  Item Buttons  //
        buttons.add(new CoolButton("Item 1",0,(int) (y+h*0.10),100,50,1.2f,8));
        buttons.add(new CoolButton("Item 2",0,(int) (y+h*0.10),100,50,1.2f,9));
        buttons.add(new CoolButton("Item 3",0,(int) (y+h*0.10),100,50,1.2f,10));

        // State Change Buttons //
        buttons.add(new CoolButton("Back", (int) (x+w*0.60), (int) (y+h*0.8),100,50,1.1f,11));
        buttons.add(new CoolButton("Back", (int) (x+w*0.60), (int) (y+h*0.8),100,50,1.2f,12));
        buttons.add(new CoolButton("Menu", width/2 - 75, height/2 + 130,150,70,10,13));
        buttons.add(new CoolButton("Menu", width/2 - 75, height/2 + 130,150,70,10.1f,14));

        //  Main Menu Buttons  //
        buttons.add(new CoolButton("Play", width/2 - 175, height/2,150,70,0,15));
        buttons.add(new CoolButton("Quit", width/2 + 25, height/2,150,70,0,16));
        buttons.add(new CoolButton("Menu", width/2 - 75, height/2 + 130,150,70,10.2f,17));

        //---------------------------------------------------------------------------------------------//


        centreItems(onScreenMonsters); // Centres Monsters
        centreItems(onScreenItems); // Centres Items

        // Plays first instance of Music based on state //
        if((state < 1) || (state >= 2)){
            startAudioLoop(menuMusic);
        }else{
            startAudioLoop(fightMusic);
        }

    }

    //
    public void update(double dt) {

        // If Player Dies, reset gold, change state, change music, and heal alittle
        if(selectedPlayer.getHealth() == 0){
            playAudio(playerDeath);
            state = 10.2f;
            stopAudioLoop(fightMusic);
            startAudioLoop(menuMusic);
            selectedPlayer.heal();
            selectedPlayer.resetGold();
        }

        //Counts frames per second
        nFrame++;
        if(nFrame > 30){
            nFrame = 0;
        }

        //For onScreenMonsters, moves the monster
        for(String id: onScreenMonsters) {
            if (nFrame % 2 == 0 || nFrame % 5 == 0) {
                int ID = Integer.parseInt(id);
                if (monsterOffsetsY[ID - 4] > 30) {
                    monsterDirectionY[ID - 4] = -1;
                } else if (monsterOffsetsY[ID - 4] < 0) {
                    monsterDirectionY[ID - 4] = 1;
                }
                // Makes Movement different using attack
                monsterOffsetsY[ID-4] += monsterDirectionY[ID-4] * ManageCreatures.monsterByIndex(ID-4).getAttack() / 2;
            }
            if (nFrame % 5 == 0) {
                int ID = Integer.parseInt(id);
                if (monsterOffsetsX[ID - 4] > 10) {
                    monsterDirectionX[ID - 4] = -1;
                } else if (monsterOffsetsX[ID - 4] < -10) {
                    monsterDirectionX[ID - 4] = 1;
                }
                // Makes Movement different using defense
                monsterOffsetsX[ID-4] += monsterDirectionX[ID-4] * ManageCreatures.monsterByIndex(ID-4).getDefense() / 4;
            }
        }
    }

//--------------------------------------------------- Paint Functions ---------------------------------------------------//

    // Sets the game canvas up with a background - then decides what it should draw on top of it
    public void paintComponent() {
        changeBackgroundColor(84,84,84);
        clearBackground(width,height);
        drawImage(background, 0,0);

        if((state >= 1) && (state < 2)){ // For Battling
            paintDefaultLayout();
            paintFightLayout();
        }else if(state >= 10){ // End Battle State: If its Won, Lost or Forfeited
            changeColor(0,0,0,200);
            drawSolidRectangle(300,200,400,200);
            changeColor(Color.white);
            drawText(new double[]{0,0,(double) width, (double) height,0.5},"Your Current Gold is " + selectedPlayer.getMoney(),"Comic Sans MS",30,"Centre");
            if(state == 10){
                drawBoldText(new double[]{0,0,(double) width, (double) height,-0.5},"YOU WON!!","Comic Sans MS",40,"Centre");
            }
            else if(state == 10.1f){
                drawBoldText(new double[]{0,0,(double) width, (double) height,-0.5},"You ran away","Comic Sans MS",40,"Centre");
            }
            else if(state == 10.2f){
                drawBoldText(new double[]{0,0,(double) width, (double) height,-0.5},"You Died","Comic Sans MS",40,"Centre");
            }
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

    // Draws the basic HUD rectangles that hold the game information
    public void paintDefaultLayout(){
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1); //Dimensions of the box
        changeColor(237,191,149,190);
        drawSolidRectangle(x,y,w,h);
        changeColor(255,255,255,190);
        drawSolidRectangle(x+w*0.27,y,5,h);
        drawSolidRectangle(x+w*0.73,y,5,h);
    }

    // Draws the fight layout - including the player's stats, selected enemy's stats, selected item info and the event list
    public void paintFightLayout(){
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1); //Dimensions of the box

        // Draws player stats & status
        drawImage(player1,(x+w*0.23/2)-70,y+10,180,180);
        changeColor(black);
        drawBoldText(new double[]{x,y,x+w*0.27,y+h/5,0},"Your Stats:","Comic Sans MS",26,"Centre");
        drawText(new double[]{x,y+h/2,x+w*0.27,y+h,-1},"Health: " + selectedPlayer.getHealth() + "/" + selectedPlayer.getMaxHealth(),"Comic Sans MS",20,"Centre");
        drawText(new double[]{x,y+h/2,x+w*0.27,y+h,0},"Attack: " + selectedPlayer.getAttack(),"Comic Sans MS",20,"Centre");
        drawText(new double[]{x,y+h/2,x+w*0.27,y+h,1},"Defence: " + selectedPlayer.getDefense(),"Comic Sans MS",20,"Centre");
        drawText(new double[]{x,y+h/2,x+w*0.27,y+h,2},"Gold: " + selectedPlayer.getMoney(),"Comic Sans MS",20,"Centre");

        // Draws the event viewer
        drawBoldText(new double[]{x+w*0.75,y,x+w,y+h/5,0},"Recap:","Comic Sans MS",26,"Centre");
        int i = 0;
        for(String event : pastEvents){
            if(event.length() < 30){
                i++;
                drawText(x+w*0.74,y+(width/40*(i+1.50)),event,"Comic Sans MS",18);
            }else{
                i++;
                drawText(x+w*0.74,y+(width/40*(i+1.50)),event.split(",")[0],"Comic Sans MS",18);
                i++;
                drawText(x+w*0.74,y+(width/40*(i+1.50)),event.split(",")[1],"Comic Sans MS",18);
            }

        }

        // Draws the header in the fight menu
        if((state == 1) || (state == 1.2f)){
            CoolButton button;
            if(state==1){
                drawBoldText(new double[]{x+w*0.27,y,x+w*0.73,y+h/4,0},"What would you like to do?","Comic Sans MS",26,"Centre");

            } // Draws the monsters as images using their button ID's values
            for(String id: onScreenMonsters){
                button = buttons.get(Integer.parseInt(id)); drawImage(button.image, button.buttonPosX+monsterOffsetsX[Integer.parseInt(id)-4],button.buttonPosY+monsterOffsetsY[Integer.parseInt(id)-4],button.width,button.height);
            }
        }

        // Shows a monster's stats when it's selected in the fight game screen
        if(state == 1.1f && selectedMonster != null){
            drawBoldText(new double[]{x+w*0.27,y,x+w*0.73,y+h,-1.5},selectedMonster.getName(),"Comic Sans MS",30,"Centre");
            drawText(new double[]{x+w*0.27,y,x+w*0.73,y+h,-0.5},"Health: " + selectedMonster.getHealth() + "/" + selectedMonster.getMaxHealth(),"Comic Sans MS",23,"Centre");
            drawText(new double[]{x+w*0.27,y,x+w*0.73,y+h,0.5},"Attack: " + selectedMonster.getAttack(),"Comic Sans MS",23,"Centre");
            drawText(new double[]{x+w*0.27,y,x+w*0.73,y+h,1.5},"Defence: " + selectedMonster.getDefense(),"Comic Sans MS",23,"Centre");
        }else if (state == 1.1f){ // Default text telling the player how to attack
            drawBoldText(new double[]{x+w*0.27,y,x+w*0.73,y+h,-0.5},"Select the Monster","Comic Sans MS",30,"Centre");
            drawBoldText(new double[]{x+w*0.27,y,x+w*0.73,y+h,0.5},"to attack","Comic Sans MS",30,"Centre");
        } else if (state == 1.2f && selectedItem != null){ // Shows the item's title and description when selected in the item game screen
            drawBoldText(new double[]{x+w*0.27,y-h/6,x+w*0.73,y+h,-0.5}, selectedItem.getTitle(), "Comic Sans MS", 25, "Centre");
            if(selectedItem.getDescription().length() < 35){
                drawText(new double[]{x+w*0.27,y-h/6,x+w*0.73,y+h,0.5}, "This is a " + selectedItem.getDescription(), "Comic Sans MS", 18, "Centre");
            }else{
                drawText(new double[]{x+w*0.27,y-h/6,x+w*0.73,y+h,0.5}, "This is a " + selectedItem.getDescription().split("#")[0], "Comic Sans MS", 18, "Centre");
                drawText(new double[]{x+w*0.27,y-h/6,x+w*0.73,y+h,1.5}, selectedItem.getDescription().split("#")[1], "Comic Sans MS", 18, "Centre");
            }

            drawText(new double[]{x+w*0.27,y+h*0.70,x+w*0.73,y+h*0.77,0}, "Use Items coming soon", "Comic Sans MS", 20, "Centre");

        }
    }

    // Paints the buttons seen on the screen, as well as how they show when their selected
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

    // Used to centre the items in the item game screen
    public static void centreItems(List<String> ids){
        int nItems = ids.size();
        int x = width/40, y = (int) (height/2), w = (int) (width/1.05), h = (int) (height/2.1); //Dimensions of the box
        float offset = (nItems - 1) / 2f;
        int count = 0;
        for(float i = -offset;i <= offset;i++){
            CoolButton button = buttons.get(Integer.parseInt(ids.get(count)));
            float xPosRelative = i * button.width;
            button.setButtonPosX((int) (x + (w / 2) + xPosRelative * 1.3) - button.width/2);
            count++;
        }
    }

//--------------------------------------------- Mouse Functions ---------------------------------------------//

    // Defines if a button is pressed or not when a mouse press is detected
    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();

        //Checks if any buttons are pressed
        for(CoolButton button : buttons){
            if(button.stateR == state){
                if(mouseX >= button.buttonPosX && mouseX <= button.buttonPosX+button.width && mouseY >= button.buttonPosY && mouseY <= button.buttonPosY+button.height) {
                    if(button.ID > 3 && button.ID < 8) {
                        if (onScreenMonsters.indexOf(String.valueOf(button.ID)) >= 0) {callButtonMethod(button.ID, "press");}
                    }
                    else callButtonMethod(button.ID, "press");

                }
            }
        }
    }

    // Defines if a button is hovering over something ie an enemy or button
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

    // Get functions for the game window
    public static int getWidth(){
        return width;
    }

    public static int getHeight(){
        return height;
    }

    // Adds an event to the past events string
    public static void addEvent(String event){
        pastEvents.add(event);
    }

    // Sound & screen management in response to button presses
    public void callButtonMethod(int ID,String condition){
        if (condition == "press"){
            playAudio(buttonSound); // Plays when the mouse is clicked on a button
        }
        if(state == 0){ // Main Menu State
            switch(ID) {
                case 15: // Stops the menu music and plays the fight music when starting the game
                    if (condition == "press"){
                        stopAudioLoop(menuMusic);
                        startAudioLoop(fightMusic);
                        state = 1;
                        break;
                    }

                case 16: // Quits the game
                    if (condition == "press"){
                        System.exit(420);
                    }
            }
        }
        else if(state == 1){ // Battle Screen
            switch(ID) {
                case 0:
                    if (condition == "press") state = 1.1f; // Fight Menu State
                    break;
                case 1:
                    if (condition == "press") state = 1.2f; // Item Menu State
                    break;

                case 2: // Heal Button
                    if (condition == "press") {
                        selectedPlayer.heal();
                        pastEvents.add("You healed, giving 40 health");
                        if(previousMonster != null) if(previousMonster.getHealth() != 0) pastEvents.addAll(previousMonster.attackCreature(selectedPlayer));
                    };
                    break;

                case 3: // Run Button
                    if (condition == "press"){
                        state = 10.1f;
                        stopAudioLoop(fightMusic);
                        startAudioLoop(menuMusic);
                    }

            }
        }

        else if(state == 1.1f){ // Fighting
            if(ID > 3 && ID < 8) {
                selectedMonster = ManageCreatures.monsterByIndex(ID-4); // Shows selected monster
                if(condition == "press") {
                    pastEvents = selectedPlayer.attackCreature(selectedMonster); // Attacks selected monster
                    if(selectedMonster.getHealth() == 0){ // Death cycle of a monster
                        int index = onScreenMonsters.indexOf(ID + "");
                        onScreenMonsters.remove(index);
                        centreItems(onScreenMonsters);
                        playAudio(monsterDeath);

                    }else{
                        pastEvents.addAll(selectedMonster.attackCreature(selectedPlayer)); // The last selected monster attacks back
                    }
                    if(onScreenMonsters.size() == 0) { // No monsters present in the battle menu
                        state = 10;
                        stopAudioLoop(fightMusic);
                        startAudioLoop(menuMusic);
                        pastEvents = new LinkedList<>();
                        pastEvents.add("Nobodys Here");
                        pastEvents.add("Go Home");
                    }
                    previousMonster = selectedMonster;
                }
            }else { // Head back to the main battle screen (from the fighting screen)
                if(condition == "press") {
                    selectedMonster = null;
                    state = 1;
                }
            }
        }

        // Item Screen
        else if(state == 1.2f){
            if(ID > 7 && ID < 11) {
                selectedItem = ManageItems.getItem(Integer.parseInt(selectedPlayer.getItem(ID-8))-8);
            }else {
                if (condition == "press") { // Head back to the main battle screen (from the item screen)
                    selectedItem = null;
                    state = 1;
                }
            }
        }

        // Quit to main menu buttons on battle victory, loss and forfeit
        else if(state == 10){
            if(ID == 13){
                if(condition == "press") state = 0;
            }
        }
        else if(state == 10.1f){
            if(ID == 14){
                if(condition == "press") state = 0;
            }
        }
        else if(state == 10.2f){
            if(ID == 17){
                if(condition == "press") state = 0;
            }
        }

    }
}
