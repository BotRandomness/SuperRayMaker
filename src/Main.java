import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.core.rCore;
import com.raylib.java.raymath.Vector2;
import com.raylib.java.textures.rTextures;
import com.raylib.java.textures.Image;
import com.raylib.java.gui.Raygui;

import java.io.FileNotFoundException;

public class Main
{
    public static Raylib rlj;
    public static Raygui rgui;
    public static int gameState = 0;

    public static void main(String[] args) throws FileNotFoundException
    {
        //System.out.println("Hello world!");

        // Initialization
        //--------------------------------------------------------------------------------------
        int screenWidth = 800;
        int screenHeight = 450;
        rlj = new Raylib();

        rlj.core.InitWindow(screenWidth, screenHeight, "Super Ray Maker");

        Image icon = rTextures.LoadImage("resources/superraymaker_logo.png");

        rlj.core.SetWindowIcon(icon);

        rlj.audio.InitAudioDevice();

        rgui = new Raygui(rlj);
        rgui.GuiEnable();

        //creating objects for each "screen"
	MainMenu mainMenu = new MainMenu();
        Editor editor = new Editor(new Player(new Vector2((float) screenWidth / 2, (float) screenHeight / 2), new Vector2(45.0f, 45.0f)), screenWidth, screenHeight);
        GameOver gameOver = new GameOver();
        LevelComplete levelComplete = new LevelComplete();

        rlj.core.SetTargetFPS(60);
        //--------------------------------------------------------------------------------------

        while (!rlj.core.WindowShouldClose()) //main game loop
        {
            // Update
            //----------------------------------------------------------------------------------

            //depeding on game state, updates the background calls  
	        if (gameState == 0)
            {
                mainMenu.mainMenuUpdate();
            }
            else if(gameState == 1)
            {
                editor.editorUpdate();
            }
            else if (gameState == 2)
            {
                gameOver.gameOverUpdate();
            }
            else if (gameState == 3)
            {
                levelComplete.levelCompleteUpdate();
            }

            //----------------------------------------------------------------------------------

            // Draw
            //----------------------------------------------------------------------------------
            rlj.core.BeginDrawing();

            //depeding on game state, renders the screen
	        if (gameState == 0)
            {
                mainMenu.mainMenuRender();
            }
            else if (gameState == 1)
            {
                editor.editorRender();
            }
            else if(gameState == 2)
            {
                gameOver.gameOverRender();
            }
            else if (gameState == 3)
            {
                levelComplete.levelCompleteRender();
            }

            rlj.text.DrawText("FPS: " + Integer.toString(rCore.GetFPS()), 3, 0, 10, Color.GREEN);

            rlj.core.EndDrawing();
        }
        rlj.audio.CloseAudioDevice();
        rgui.GuiDisable();
    }
}