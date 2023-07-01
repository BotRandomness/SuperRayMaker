import com.raylib.java.core.Color;

public class GameOver
{
    private int score;
    private int coins;
    ButtonUI gameOverButton = new ButtonUI(340, 290, 150, 50, "Continue?");
    ButtonUI menuButton = new ButtonUI(340, 350, 150, 50, "Main Menu");

    public GameOver(int score, int coins)
    {
        this.coins = coins;
        this.score = score;
    }

    public GameOver()
    {

    }

    public void gameOverUpdate()
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

    public void gameOverRender()
    {
        Main.rlj.core.ClearBackground(Color.RAYWHITE);
        gameOverButton.buttonRender();
        menuButton.buttonRender();
        Main.rlj.text.DrawText("GAME OVER", 260, 15, 50, Color.RED);
    }

}
