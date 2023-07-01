import com.raylib.java.core.Color;

public class MainMenu
{
    ButtonUI playButton = new ButtonUI(340, 290, 150, 50, "PLAY!");

    public MainMenu()
    {

    }

    public void mainMenuUpdate()
    {
        if(playButton.click())
        {
            Main.gameState = 1;
        }
    }

    public void mainMenuRender()
    {
        Main.rlj.core.ClearBackground(Color.RAYWHITE);
        playButton.buttonRender();
        Main.rlj.text.DrawText("Super\nRay\nMaker", 5, 15, 50, Color.YELLOW);
        Main.rlj.text.DrawText("Super\nRay\nMaker", 157, 15, 50, Color.ORANGE);
        Main.rlj.text.DrawText("Super\nRay\nMaker", 309, 15, 50, Color.RED);
        Main.rlj.text.DrawText("Super\nRay\nMaker", 461, 15, 50, Color.VIOLET);
        Main.rlj.text.DrawText("Super\nRay\nMaker", 613, 15, 50, Color.BLUE);
        Main.rlj.text.DrawText("Super\nRay\nMaker", 765, 15, 50, Color.LIME);
    }
}
