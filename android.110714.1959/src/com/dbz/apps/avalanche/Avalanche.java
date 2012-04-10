package com.dbz.apps.avalanche;

import processing.core.*; 
import processing.xml.*; 

import android.view.MotionEvent; 
import android.view.KeyEvent; 
import android.graphics.Bitmap; 
import java.io.*; 
import java.util.*; 

public class Avalanche extends PApplet {

//

//Avalanche By Brendan Boyle of DBZ Apps
//Visit dbztech.com for more info
//For help please email brendan@dbztech.com

//Sets up fonts
PFont font;

//Sets up images
PImage iceImage;
PImage playerImage;

//Starts Variables
//Starts intagers
//Number of icecicles to spawn - 1
int iceNumber = 25;
//Left padding for icecicles
int currentX = 10;
//Temp var used in spawn loop to save random timer
int iceTime;
//Half width
int halfWidth;
//Half height
int halfHeight;
//Tells the game what menu to draw
//1 = Start
//2 = Win
//3 = Loss
int menu = 1;
//Left Screen Boundry
int leftBound;
//Right Screen Boundry
int rightBound;
//Counter for cube holding
int holdCounter;
//Level Ticker
int levelTicker;
//Level Limit
int levelLimit = 1000;
//X opperation to be applied every loop to player x
float xop = 0;
//Xop after speed multiplier
float finalXop = 0;
//Player speed
float playerMoveSpeed = 4;
//Icecilce speed
float iceSpeed = 8;

//Starts arrays
float[] emptyArray = {-50};
float[] emptyPlayerArray = {0, 0, 0};
//X of icecicels
int[] iceX = {-50};
//Y of icecicles
float[] iceY = {-50};
//State of iceciles
//0 = Resting but Visible
//1 = Moving
//2 = Off screen
int[] iceState = {-50};
//Time to spawn
//-1 = Not wating to spawn
int[] iceTimer = {-50};
//Player x,y,score
float[] player = {0, 0, 0};

//Booliean Variables
boolean alive = false;
boolean onHold = false; 
boolean move = false;

//Sets strings
String url;

//Setup void
//Run once on setup
public void setup() {
  iceImage = loadImage("ice.png");
  playerImage = loadImage("player.png");
  orientation(LANDSCAPE);
 
  font = loadFont("NewFont.vlw"); 
  textFont(font);
  halfHeight = screenHeight/2;
  halfWidth = screenWidth/2;
  player[0] = halfWidth;
  player[1] = screenHeight - 75;
  leftBound = 30;
  rightBound = screenWidth-20;
  background(56,88,127);
  rectMode(CENTER);
  setupArrays();
  frameRate(50);
  textAlign(CENTER);
}

//Main draw loop
//Runs as fast as possible after setup
public void draw() {
  background(54,112,184);
  if (alive) {
    drawIce();
    drawPlayer();
    conditionals();
    drawOther();
  } else {
    drawMenus();
  }
}

public void setVars() {
  levelLimit = 1000;
  levelTicker = 0;
  onHold = false;
  iceNumber = 25;
  currentX = 10;
  menu = 1;
  xop = 0;
  finalXop = 0;
  iceX = PApplet.parseInt(emptyArray);
  iceY = emptyArray;
  iceState = PApplet.parseInt(emptyArray);
  iceTimer = PApplet.parseInt(emptyArray);
  //Player x,y,score
  player = emptyPlayerArray;
  player[2] = 0;
  alive = true;
  setup();
}

public void setupArrays() {
  for (int i = 0; i < iceNumber; i = i+1) {
    currentX = currentX + 40;
    iceX = append(iceX, currentX);
    iceY = append(iceY, 0);
    iceState = append(iceState, 0);
    iceTime = PApplet.parseInt(random(0,100));
    iceTimer = append(iceTimer, iceTime);
  }
}

public void drawIce() {
  for (int i = 0; i < iceNumber; i = i+1) {
    //rect(iceX[i],iceY[i],25,25);
    image(iceImage, iceX[i], iceY[i]);
  }
}

public void drawPlayer() {
  finalXop = xop * playerMoveSpeed;
  player[0] = player[0] + finalXop;
  fill(255,0,0);
  image(playerImage, player[0], player[1]);
  fill(0,0,0);
}

public void conditionals() {
  
    //Left and Right Limits
  if (player[0] < leftBound) {
    xop = 0;
    player[0] = leftBound;
  }
  if (player[0] > rightBound) {
    xop = 0;
    player[0] = rightBound;
  }
  
  
  //Cube Loop
  for (int i = 0; i < iceNumber; i = i+1) {
    
    //Countdown to spawn
    if (iceState[i] == 0) {
      if (!onHold) {
      if (iceTimer[i] > 0) {
        iceState[i] = 0;
        iceTimer[i] = iceTimer[i] - 1;
      } else {
        iceState[i] = 1;
        iceTimer[i] = -1;
      }
      } else {
        iceState[i] = 0;
      }
      
    }
    
    //Fall
    if (iceState[i] == 1) {
      iceY[i] = iceY[i] + (1*iceSpeed);
    }
    
    //Fall
    if (iceState[i] == 2) {
      iceY[i] = iceY[i] + 1;
      if (iceY[i] == 0) {
        iceState[i] = 0;
        iceTimer[i] = PApplet.parseInt(random(0,100));
      }
    }
    
    //Respawn
    if (iceY[i] > (screenHeight+25)) {
      iceY[i] = -50;
      iceState[i] = 2;
    }
    //Collision Voloumes
    if (iceY[i] > player[1] - 25) {
       if (iceY[i] < player[1] + 25) {
         if (iceX[i] > player[0] - 25) {
            if (iceX[i] < player[0] + 25) {
              menu = 3;
              alive = false;
          }
        }
      }
    }
  }
  //End Cube Loop
  levelTicker = levelTicker + 1;
  //Cube Holder
  if (levelTicker > levelLimit) {
    holdCubes();
  }
  
  //Move Code
  if (move) {
  if (mouseX > halfWidth) {
    xop = 1;
  }
  if (mouseX < halfWidth) {
    xop = -1;
  }
  } else {
    xop = 0;
  }
}

public void holdCubes() {
  //iceTimer[i] = int(random(0,100));
  holdCounter = 0;
  for (int i = 1; i < iceNumber; i = i+1) {
    if (iceState[i] == 0) {
      holdCounter = holdCounter+1;
    }
  }
  if(holdCounter > iceNumber-2) {
    onHold = false;
    levelTicker = 0;
  } else {
    onHold = true;
  }
}

public void drawOther() {
  //println("FPS:"+frameRate);
  player[2] = player[2] + 1;
}

public void drawMenus() {
  if (menu == 1) {
    fill(150,150,150);
    rect(halfWidth,halfHeight, 250, 50);
    rect(halfWidth,halfHeight-150, 250, 50);
    fill(0,0,0);
    text("Start",halfWidth+8,halfHeight+15);
    text("Scores",halfWidth+5,halfHeight-135);
  }
  if (menu == 2) {
    fill(150,150,150);
    rect(halfWidth,halfHeight-150, 175, 50);
    fill(0,0,0);
    text("Submit",halfWidth,halfHeight-135);
    text(PApplet.parseInt(player[2]), halfWidth, halfHeight-50);
    text("You Won!",halfWidth,halfHeight+100);
    fill(150,150,150);
    rect(halfWidth,halfHeight, 150, 50);
    fill(0,0,0);
    text("Retry",halfWidth,halfHeight+15);
  }
  if (menu == 3) {
    fill(150,150,150);
    rect(halfWidth,halfHeight-150, 250, 50);
    rect(halfWidth,halfHeight, 250, 50);
    fill(0,0,0);
    text("Submit",halfWidth+8,halfHeight-135);
    text(PApplet.parseInt(player[2]), halfWidth, halfHeight-50);
    text("You Lost",halfWidth+5,halfHeight+100);
    text("Retry",halfWidth+5,halfHeight+15);
  }
}

public void mousePressed() {
  move = true;
  if (menu == 1) {
    if (mouseX > halfWidth-125) {
      if (mouseX < halfWidth+125) {
        if (mouseY < halfHeight + 25) {
          if (mouseY > halfHeight - 25) {
            alive = true;
            menu = 0;
          }
        }
        if (mouseY < halfHeight - 125) {
          if (mouseY > halfHeight - 175) {
            link("http://dbztech.com/Avalanche");
          }
        }
        
      }
    }
  }
  if (menu > 1) {
    if (mouseX > halfWidth-125) {
      if (mouseX < halfWidth+125) {
        if (mouseY < halfHeight + 25) {
          if (mouseY > halfHeight - 25) {
            setVars();
          }
        }
        if (mouseY < halfHeight - 125) {
          if (mouseY > halfHeight - 175) {
            url = "http://dbztech.com/Avalanche/redirect.php?input="+PApplet.parseInt(player[2]);
            link(url);
          }
        }
      }
    }
  }
}

public void mouseReleased() {
  move = false;
}

  public int sketchWidth() { return screenWidth; }
  public int sketchHeight() { return screenHeight; }
}
