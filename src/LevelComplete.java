import com.raylib.java.core.Color;

public class LevelComplete
{
    ButtonUI gameOverButton = new ButtonUI(340, 290, 150, 50, "Go to Editor");
    ButtonUI menuButton = new ButtonUI(340, 350, 150, 50, "Main Menu");

    public LevelComplete()
    {

    }

    public void levelCompleteUpdate()
    {
        if(gameOverButton.click())
        {
            Main.gameState = 1;
        }
        if(menuButton.click())
        {
            Main.gameState = 0;
        }
    }

    public void levelCompleteRender()
    {
        Main.rlj.core.ClearBackground(Color.RAYWHITE);
        gameOverButton.buttonRender();
        menuButton.buttonRender();
        Main.rlj.text.DrawText("Level Complete!", 210, 15, 50, Color.GREEN);
    }

}