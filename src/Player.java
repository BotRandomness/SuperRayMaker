import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import com.raylib.java.core.rCore;
import com.raylib.java.raymath.Vector2;
import com.raylib.java.shapes.Rectangle;
import com.raylib.java.textures.rTextures;
import com.raylib.java.textures.*;
import com.raylib.java.raudioal.Sound;
import java.util.*;

public class Player
{
    public Vector2 playerPosition;

    public Vector2 playerSize;
    public float gravity = 9.0f;
    public float jumpForce = 400.0f;
    public float velocityY = 0f;
    public float speed = 200.0f;
    public boolean onGround = false;
    public boolean canJump = false;
    ArrayList<Boolean> onBlocks = new ArrayList<>();

    public int groundPointX;
    int groundPointY;
    int leftPointX;
    int leftPointY;
    int rightPointX;
    int rightPointY;
    int topPointX;
    int topPointY;

    Texture2D spriteSheet = rTextures.LoadTexture("resources/playerstrip.png");
    int frameWidth = spriteSheet.width/2;
    int frameHeight = spriteSheet.height;
    int currentFrame = 0;
    int framesCounter = 0;
    int framesSpeed = 6;

    public int coins = 0;

    Sound jump = Main.rlj.audio.LoadSound("resources/jump.wav");
    Sound coinSound = Main.rlj.audio.LoadSound("resources/coin.wav");
    Sound enemyDeath = Main.rlj.audio.LoadSound("resources/enemyDeath.wav");

    public Player(Vector2 playerPosition, Vector2 playerSize , float gravity, float jumpForce, float velocityY, float speed)
    {
        this.playerPosition = playerPosition;
        this.playerSize = playerSize;
        this.gravity = gravity;
        this.jumpForce = jumpForce;
        this.velocityY = velocityY;
        this.speed = speed;
    }

    public Player(Vector2 playerPosition, Vector2 playerSize)
    {
        this.playerPosition = playerPosition;
        this.playerSize = playerSize;
    }

    //player movement
    private void playerMovement(float deltaTime)
    {
        if (rCore.IsKeyDown(Keyboard.KEY_RIGHT)){
            playerPosition.x += (speed*deltaTime);
            //velocityX += speed;
        }
        else if (rCore.IsKeyDown(Keyboard.KEY_LEFT)){
            playerPosition.x -= speed*deltaTime;
            //velocityX += -speed;
        }
        if (canJump == true)
        {
            if (rCore.IsKeyDown(Keyboard.KEY_UP)){
                //ballPosition.y -= 20.0f;
                velocityY += -jumpForce;
                playerPosition.y += velocityY * deltaTime;
                Main.rlj.audio.PlaySound(jump);
            }
        }
    }

    //player collision, check between each side points
    private void playerCollision(ArrayList<Block> blocks, float deltaTime)
    {
        groundPointX = (int)(playerPosition.getX()) + (int)(playerSize.getX()/2);
        groundPointY = (int)(playerPosition.getY()) + (int)(playerSize.getY());
        leftPointX = (int)(playerPosition.getX());
        leftPointY = (int)(playerPosition.getY()) + (int)(playerSize.getY()/2);
        rightPointX = (int)(playerPosition.getX()) + (int)(playerSize.getX());
        rightPointY = (int)(playerPosition.getY()) + (int)(playerSize.getY()/2);
        topPointX = (int)(playerPosition.getX()) + (int)(playerSize.getX());
        topPointY = (int)(playerPosition.getY());

        if (!onGround)
        {
            //ballPosition.setY(ballPosition.getY() + 4.0f);
            velocityY += gravity;
            playerPosition.y += velocityY * deltaTime;
        }

        for(int i = 0; i < blocks.size(); i++)
        {
            if (i == 0)
            {
                onBlocks.clear();
            }
            //System.out.println(blocks.get(i).position.getX());
            //System.out.println(blocks.get(i).position.getY());
            //System.out.println(blocks.get(i).position.getX() + blocks.get(i).size.getX());
            //System.out.println("gpx " + groundPointX);
            //System.out.println("gpy " + groundPointY);
            if (!blocks.get(i).type.equals("coin") && !blocks.get(i).type.equals("flag") && !blocks.get(i).type.equals("spike") && !blocks.get(i).type.equals("a1") && !blocks.get(i).type.equals("a2"))
            {
                if (groundPointX >= blocks.get(i).position.getX() && groundPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && groundPointY >= blocks.get(i).position.getY() && groundPointY <= blocks.get(i).position.getY() + 20)
                {
                    //onGround = true;
                    playerPosition.y -= 0.5;
                    //onBlocks.add(Main.rlj.shapes.CheckCollisionCircleRec(playerPosition, 30, blocks.get(i).box));
                    onBlocks.add(Main.rlj.shapes.CheckCollisionRecs(new Rectangle(playerPosition, 45, 45), blocks.get(i).box));
                }
                if(leftPointX >= blocks.get(i).position.getX() && leftPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && leftPointY + (playerSize.getY()/2) >= blocks.get(i).position.getY() && leftPointY + (playerSize.getY()/2) <= blocks.get(i).position.getY() + 20)
                {
                    playerPosition.y -= 0.5;
                    onBlocks.add(true);
                }
                if (rightPointX >= blocks.get(i).position.getX() && rightPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && rightPointY + (playerSize.getY()/2) >= blocks.get(i).position.getY() && rightPointY + (playerSize.getY()/2) <= blocks.get(i).position.getY() + 20)
                {
                    playerPosition.y -= 0.5;
                    onBlocks.add(true);
                }
                if (Main.rlj.shapes.CheckCollisionRecs(new Rectangle(playerPosition, 45, 45), blocks.get(i).box) == false)
                {
                    //onGround = false;
                    onBlocks.add(Main.rlj.shapes.CheckCollisionRecs(new Rectangle(playerPosition, 45, 45), blocks.get(i).box));
                }

                if(onBlocks.contains(true) == true)
                {
                    onGround = true;
                    canJump = true;
                    velocityY = 0;
                }
                else if (onBlocks.contains(true) == false)
                {
                    onGround = false;
                    canJump = false;
                }

                /*
                if(groundPointY >= blocks.get(i).position.getY() && groundPointY <= blocks.get(i).position.getY() + 20)
                {
                    ballPosition.y = blocks.get(i).position.getY() - 30;
                }
                 */
                /*
                if (ballPosition.getY() >= blocks.get(i).position.getY() && ballPosition.getY() <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && ballPosition.getX() > blocks.get(i).position.getX() && ballPosition.getX() < blocks.get(i).position.getX() + blocks.get(i).size.getY())
                {
                    if (rCore.IsKeyDown(Keyboard.KEY_RIGHT)){
                        ballPosition.x -= 2.0f;
                    }
                    if (rCore.IsKeyDown(Keyboard.KEY_LEFT)){
                        ballPosition.x += 2.0f;
                    }
                    if (rCore.IsKeyDown(Keyboard.KEY_UP)){
                        ballPosition.y += 4.0f;
                    }
                }
                 */


                /*
                if (topPointY >= blocks.get(i).position.getY() && topPointY <= blocks.get(i).position.getY() + blocks.get(i).size.getY() && ((leftPointX >= blocks.get(i).position.getX() && leftPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && leftPointY - 30 >= blocks.get(i).position.getY() && leftPointY - 30 <= blocks.get(i).position.getY() + blocks.get(i).size.getY()) || (rightPointX >= blocks.get(i).position.getX() && rightPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && rightPointY - 30 >= blocks.get(i).position.getY() && rightPointY - 30 <= blocks.get(i).position.getY() + blocks.get(i).size.getY())))
                {
                    //System.out.println("Touch Bottom Part of Block!");
                    ballPosition.y += 3.0f;
                }
                 */

                if (rightPointY >= blocks.get(i).position.getY() && rightPointY <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && rightPointX >= blocks.get(i).position.getX() && rightPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX())
                {
                    if (rCore.IsKeyDown(Keyboard.KEY_RIGHT)){
                        playerPosition.x -= speed* deltaTime;
                        canJump = false;
                    }
                }
                if (rightPointY + (playerSize.getY()/2) >= blocks.get(i).position.getY() && rightPointY + (playerSize.getY()/2) <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && rightPointX >= blocks.get(i).position.getX() && rightPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX())
                {
                    if (rCore.IsKeyDown(Keyboard.KEY_RIGHT)){
                        playerPosition.x -= speed* deltaTime;
                        canJump = false;
                    }
                }
                if (rightPointY - (playerSize.getY()/2) >= blocks.get(i).position.getY() && rightPointY - (playerSize.getY()/2) <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && rightPointX >= blocks.get(i).position.getX() && rightPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX())
                {
                    if (rCore.IsKeyDown(Keyboard.KEY_RIGHT)){
                        playerPosition.x -= speed* deltaTime;
                        canJump = false;
                    }
                }
                if (leftPointY >= blocks.get(i).position.getY() && leftPointY <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && leftPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && leftPointX >= blocks.get(i).position.getX())
                {
                    if (rCore.IsKeyDown(Keyboard.KEY_LEFT)){
                        playerPosition.x += speed* deltaTime;
                        canJump = false;
                    }
                }
                if (leftPointY + (playerSize.getY()/2) >= blocks.get(i).position.getY() && leftPointY + (playerSize.getY()/2) <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && leftPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && leftPointX >= blocks.get(i).position.getX())
                {
                    if (rCore.IsKeyDown(Keyboard.KEY_LEFT)){
                        playerPosition.x += speed* deltaTime;
                        canJump = false;
                    }
                }
                if (leftPointY - (playerSize.getY()/2) >= blocks.get(i).position.getY() && leftPointY - (playerSize.getY()/2) <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && leftPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && leftPointX >= blocks.get(i).position.getX())
                {
                    if (rCore.IsKeyDown(Keyboard.KEY_LEFT)){
                        playerPosition.x += speed* deltaTime;
                        canJump = false;
                    }
                }
            }

            //System.out.println(onGround);
            //System.out.println(i + ": " + blocks.get(i).box.getX());
        }
    }

    //plays and render the player animation
    private void playerAnimation()
    {
        int flip = -1;

        if(rCore.IsKeyDown(Keyboard.KEY_RIGHT) || rCore.IsKeyDown(Keyboard.KEY_LEFT))
        {
            framesCounter++;

            if(framesCounter >= framesSpeed)
            {
                framesCounter = 0;
                currentFrame++;

                if(currentFrame >= 2)
                {
                    currentFrame = 0;
                }
            }

            if(rCore.IsKeyDown(Keyboard.KEY_RIGHT))
            {
                flip = -1;
            }
            else if(rCore.IsKeyDown(Keyboard.KEY_LEFT))
            {
                flip = 1;
            }

        }

        rTextures.DrawTexturePro(
                spriteSheet,
                new Rectangle(currentFrame*frameWidth,0,flip*frameWidth,frameHeight),
                new Rectangle(playerPosition.x-2,playerPosition.y-1,frameWidth*2.0f,frameHeight*2.0f),
                new Vector2(0,0),
                0,
                Color.WHITE
        );
    }

    //checks enemy state
    private void playerVSEnemy(ArrayList<Enemy> enemys)
    {
        for (int i = 0; i < enemys.size(); i++)
        {
            if (Main.rlj.shapes.CheckCollisionRecs(new Rectangle(playerPosition, 45, 45), new Rectangle(new Vector2(enemys.get(i).enemyPosition.x + 5, enemys.get(i).enemyPosition.y - 5), enemys.get(i).enemySize.getX() - 25, enemys.get(i).enemySize.getY() - 25)))
            {
                System.out.println("Stomped on");
                Main.rlj.audio.PlaySound(enemyDeath);
                enemys.get(i).visible = false;
            }
            if (Main.rlj.shapes.CheckCollisionRecs(new Rectangle(playerPosition, 45, 45), new Rectangle(enemys.get(i).enemyPosition, enemys.get(i).enemySize.getX(), enemys.get(i).enemySize.getY())) && ((groundPointX >= enemys.get(i).enemyPosition.getX() && groundPointX <= enemys.get(i).enemyPosition.getX() + enemys.get(i).enemySize.getX() && groundPointY >= enemys.get(i).enemyPosition.getY() && groundPointY <= enemys.get(i).enemyPosition.getY() + 10) == false))
            {
                System.out.println("dead");
                Editor.edit = true;
                Main.gameState = 2;
            }
        }
    }

    //checks spike state
    private void playerSpike(ArrayList<Block> blocks)
    {
        for(int i = 0; i < blocks.size(); i++)
        {
            if (Main.rlj.shapes.CheckCollisionRecs(new Rectangle(playerPosition, 45, 45), blocks.get(i).box) && blocks.get(i).type.equals("spike"))
            {
                System.out.println("dead");
                Editor.edit = true;
                Main.gameState = 2;
            }
        }
    }
    
    //checks player win state
    private void playerFlag(ArrayList<Block> blocks)
    {
        for(int i = 0; i < blocks.size(); i++)
        {
            if (Main.rlj.shapes.CheckCollisionRecs(new Rectangle(playerPosition, 45, 45), blocks.get(i).box) && blocks.get(i).type.equals("flag"))
            {
                System.out.println("Win");
                Editor.edit = true;
                Main.gameState = 3;
            }
        }
    }

    //checks player coin collection state
    private void playerCoin(ArrayList<Block> blocks)
    {
        for(int i = 0; i < blocks.size(); i++)
        {
            if (Main.rlj.shapes.CheckCollisionRecs(new Rectangle(playerPosition, 45, 45), blocks.get(i).box) && blocks.get(i).type.equals("coin"))
            {
                System.out.println("Coin");
                coins += 1;
                Main.rlj.audio.PlaySound(coinSound);
                blocks.get(i).visible = false;
            }
        }
    }

    //calls all update realted methods in one
    public void playerUpdate(ArrayList<Block> blocks, ArrayList<Enemy> enemys , boolean editMode)
    {
        if (editMode == false)
        {
            float deltaTime = rCore.GetFrameTime();
            playerMovement(deltaTime);
            playerCollision(blocks, deltaTime);
            playerSpike(blocks);
            playerFlag(blocks);
            playerCoin(blocks);
            playerVSEnemy(enemys);
        }

    }

    //calls all render realted methods in one
    public void playerRender()
    {
        //Main.rlj.shapes.DrawCircleV(playerPosition, 30, Color.MAROON);

        //Main.rlj.shapes.DrawRectangleV(playerPosition, new Vector2(45.0f, 45.0f), Color.VIOLET);

        playerAnimation();

        //Main.rlj.shapes.DrawRectangle(groundPointX, groundPointY, 1, 1, Color.MAGENTA);
        //Main.rlj.shapes.DrawRectangle(leftPointX, leftPointY, 1, 1, Color.MAGENTA);
        //Main.rlj.shapes.DrawRectangle(rightPointX, rightPointY, 1, 1, Color.MAGENTA);
        //Main.rlj.shapes.DrawRectangle(leftPointX, leftPointY + 23, 1, 1, Color.BLUE);
        //Main.rlj.shapes.DrawRectangle(rightPointX, rightPointY + 23, 1, 1, Color.BLUE);
        //Main.rlj.shapes.DrawRectangle(topPointX, topPointY, 1, 1, Color.MAGENTA);

    }

}
