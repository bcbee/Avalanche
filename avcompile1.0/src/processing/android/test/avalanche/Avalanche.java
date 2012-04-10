package processing.android.test.avalanche;

import processing.core.*; 
import processing.xml.*; 

import android.view.MotionEvent; 
import android.view.KeyEvent; 
import android.graphics.Bitmap; 
import java.io.*; 
import java.util.*; 

public class Avalanche extends PApplet {



//Avalanche By Brendan Boyle of DBZ Apps
//Visit dbztech.com for more info
//For help please email brendan@dbztech.com

//Sets up fonts
PFont font;

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

//Sets strings
String url;

//Setup void
//Run once on setup
public void setup() {
  orientation(LANDSCAPE);
 
  font = loadFont("Font.vlw"); 
  textFont(font);
  halfHeight = screenHeight/2;
  halfWidth = screenWidth/2;
  player[0] = halfWidth;
  player[1] = screenHeight - 75;
  leftBound = 30;
  rightBound = screenWidth-20;
  background(255,255,255);
  rectMode(CENTER);
  setupArrays();
  frameRate(50);
}

//Main draw loop
//Runs as fast as possible after setup
public void draw() {
  background(255,255,255);
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
    rect(iceX[i],iceY[i],25,25);
  }
}

public void drawPlayer() {
  finalXop = xop * playerMoveSpeed;
  player[0] = player[0] + finalXop;
  fill(255,0,0);
  rect(player[0],player[1],25,25);
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
      if (iceTimer[i] > 0) {
        iceState[i] = 0;
        iceTimer[i] = iceTimer[i] - 1;
      } else {
        iceState[i] = 1;
        iceTimer[i] = -1;
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
}

public void drawOther() {
  //println("FPS:"+frameRate);
  player[2] = player[2] + 1;
}

public void drawMenus() {
  if (menu == 1) {
    fill(150,150,150);
    rect(halfWidth,halfHeight, 150, 50);
    fill(0,0,0);
    text("Start",halfWidth-70,halfHeight+15);
    fill(150,150,150);
    rect(halfWidth,halfHeight-150, 175, 50);
    fill(0,0,0);
    text("Scores",halfWidth-83,halfHeight-135);
  }
  if (menu == 2) {
    fill(150,150,150);
    rect(halfWidth,halfHeight-150, 175, 50);
    fill(0,0,0);
    text("Submit",halfWidth-83,halfHeight-135);
    textAlign(CENTER);
    text(PApplet.parseInt(player[2]), halfWidth, halfHeight-50);
    textAlign(LEFT);
    text("You Won!",halfWidth-100,halfHeight+100);
    fill(150,150,150);
    rect(halfWidth,halfHeight, 150, 50);
    fill(0,0,0);
    text("Retry",halfWidth-70,halfHeight+15);
  }
  if (menu == 3) {
    fill(150,150,150);
    rect(halfWidth,halfHeight-150, 175, 50);
    fill(0,0,0);
    text("Submit",halfWidth-83,halfHeight-135);
    textAlign(CENTER);
    text(PApplet.parseInt(player[2]), halfWidth, halfHeight-50);
    textAlign(LEFT);
    text("You Lost",halfWidth-100,halfHeight+100);
    fill(150,150,150);
    rect(halfWidth,halfHeight, 150, 50);
    fill(0,0,0);
    text("Retry",halfWidth-70,halfHeight+15);
  }
}

public void mousePressed() {
  if (mouseX > halfWidth) {
    xop = 1;
  }
  if (mouseX < halfWidth) {
    xop = -1;
  }
  
  if (menu == 1) {
    if (mouseX > halfWidth-75) {
      if (mouseX < halfWidth+75) {
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
    if (mouseX > halfWidth-75) {
      if (mouseX < halfWidth+75) {
        if (mouseY < halfHeight + 25) {
          if (mouseY > halfHeight - 25) {
          setVars();
          }
        }
        if (mouseY < halfHeight - 125) {
          if (mouseY > halfHeight - 175) {
            url = "http://dbztech.com/Avalanche/redirect.php?input="+player[2];
            link(url);
          }
        }
      }
    }
  }
}

public void mouseReleased() {
  xop = 0;
}

  public int sketchWidth() { return screenWidth; }
  public int sketchHeight() { return screenHeight; }
  public String sketchRenderer() { return OPENGL; }
}
