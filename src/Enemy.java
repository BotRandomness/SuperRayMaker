import com.raylib.java.core.Color;
import com.raylib.java.core.input.Mouse;
import com.raylib.java.core.rCore;
import com.raylib.java.raymath.Vector2;
import com.raylib.java.shapes.Rectangle;
import com.raylib.java.textures.rTextures;
import com.raylib.java.textures.*;
import java.util.*;

public class Enemy
{
    public Vector2 enemyPosition;
    public Vector2 enemySize;
    public float speed = 50.0f;
    public float gravity = 1.2f;
    public float velocityY = 0f;
    public boolean onGround = false;
    Rectangle enemyHitbox;

    public boolean visible;
    public boolean edit;
    public String type;

    public Texture2D sprite;
    int frameWidth;
    int frameHeight;
    int currentFrame = 0;
    int framesCounter = 0;
    int framesSpeed = 6;

    public int groundPointX;
    int groundPointY;
    int leftPointX;
    int leftPointY;
    int rightPointX;
    int rightPointY;
    int topPointX;
    int topPointY;

    ArrayList<Boolean> onBlocks = new ArrayList<>();

    public Enemy(Vector2 enemyPosition, Vector2 enemySize, boolean visible, boolean edit, String type, Texture2D sprite)
    {
        this.enemyPosition = enemyPosition;
        this.enemySize = enemySize;
        this.visible = visible;
        this.edit = edit;
        this.type = type;
        this.sprite = sprite;
        frameWidth = sprite.width/2;
        frameHeight = sprite.height;
    }

    //handles enemys movements, changes direction is collides with side of blocks, or the air
    public void enemyMovement(ArrayList<Block> blocks)
    {
        groundPointX = (int)(enemyPosition.getX()) + (int) (enemySize.getX() / 2);
        groundPointY = (int)(enemyPosition.getY()) + (int) (enemySize.getY());
        leftPointX = (int)(enemyPosition.getX());
        leftPointY = (int)(enemyPosition.getY()) + (int) (enemySize.getY() / 2);
        rightPointX = (int)(enemyPosition.getX()) + (int) (enemySize.getX());
        rightPointY = (int)(enemyPosition.getY()) + (int) (enemySize.getY() / 2);
        topPointX = (int)(enemyPosition.getX()) + (int) (enemySize.getX());
        topPointY = (int)(enemyPosition.getY());

        enemyHitbox = new Rectangle(enemyPosition, enemySize.getX(), enemySize.getY());

        for(int i = 0; i < blocks.size(); i++)
        {
            if (i == 0)
            {
                onBlocks.clear();
            }

            if(i == blocks.size() - 1)
            {
                if(onBlocks.contains(true) == false)
                {
                    speed *= -1;
                    System.out.println("Reverse");
                }
            }

            //onBlocks.add(Main.rlj.shapes.CheckCollisionRecs(enemyHitbox, blocks.get(i).box));

            if (rightPointY >= blocks.get(i).position.getY() && rightPointY <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && rightPointX >= blocks.get(i).position.getX() && rightPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX())
            {
                speed *= -1;
            }

            if ((rightPointY + ((enemySize.getY()/2)) >= blocks.get(i).position.getY() && rightPointY - ((enemySize.getY()/2) + 20) <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && rightPointX >= blocks.get(i).position.getX() && rightPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX()) == true)
            {
                //speed *= -1;
                //System.out.println("Right NOT ON");
                onBlocks.add(true);
            }

            if (leftPointY >= blocks.get(i).position.getY() && leftPointY <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && leftPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && leftPointX >= blocks.get(i).position.getX())
            {
                speed *= -1;
            }

            if ((leftPointY + ((enemySize.getY()/2)) >= blocks.get(i).position.getY() && leftPointY - ((enemySize.getY()/2) + 20) <= blocks.get(i).position.getY() + blocks.get(i).size.getX() && leftPointX <= blocks.get(i).position.getX() + blocks.get(i).size.getX() && leftPointX >= blocks.get(i).position.getX()) == false)
            {
                //speed *= -1;
                //System.out.println("Left NOT ON");
                onBlocks.contains(true);
            }
        }
    }

    //handles enemy animation and render
    public void enemyAnimation()
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

        if(type.equals("nor"))
        {
            rTextures.DrawTexturePro(
                    sprite,
                    new Rectangle(currentFrame*frameWidth,0,frameWidth,frameHeight),
                    new Rectangle(enemyPosition.x-10,enemyPosition.y-18,frameWidth*2.0f,frameHeight*2.0f),
                    new Vector2(0,0),
                    0,
                    Color.WHITE
            );
        }

        if(type.equals("bnor"))
        {
            rTextures.DrawTexturePro(
                    sprite,
                    new Rectangle(currentFrame*frameWidth,0,frameWidth,frameHeight),
                    new Rectangle(enemyPosition.x-1,enemyPosition.y-3,frameWidth*2.0f,frameHeight*2.0f),
                    new Vector2(0,0),
                    0,
                    Color.WHITE
            );
        }

    }

    //moves the enemy
    public void enemyUpdate(ArrayList<Block> blocks, boolean editable)
    {
        if (editable == false)
        {
            float deltaTime = rCore.GetFrameTime();

            enemyPosition.x += (speed*deltaTime);
            enemyMovement(blocks);
        }
    }

    //renders the enemy
    public void enemyRender(Vector2 mousePositionWorld, boolean editable)
    {
        if (editable && Main.rlj.shapes.CheckCollisionPointRec(mousePositionWorld, new Rectangle(enemyPosition, enemySize.getX(), enemySize.getY())) && rCore.IsMouseButtonDown(Mouse.MouseButton.MOUSE_BUTTON_RIGHT))
        {
            visible = false;
        }

        if (visible)
        {
            //Main.rlj.shapes.DrawRectangleV(enemyPosition, enemySize, Color.RED);
            enemyAnimation();

            //Main.rlj.shapes.DrawRectangle(groundPointX, groundPointY, 1, 1, Color.MAGENTA);
            //Main.rlj.shapes.DrawRectangle(leftPointX, leftPointY, 1, 1, Color.MAGENTA);
            //Main.rlj.shapes.DrawRectangle(rightPointX, rightPointY, 1, 1, Color.MAGENTA);
            //Main.rlj.shapes.DrawRectangle(leftPointX, leftPointY + 15, 1, 1, Color.BLUE);
            //Main.rlj.shapes.DrawRectangle(rightPointX, rightPointY + 15, 1, 1, Color.BLUE);
            //Main.rlj.shapes.DrawRectangle(topPointX, topPointY, 1, 1, Color.MAGENTA);

            //Main.rlj.shapes.DrawRectangle(leftPointX, (int)(leftPointY + ((enemySize.getY()/2) - 5)), 1, 1, Color.BLACK);
            //Main.rlj.shapes.DrawRectangle(rightPointX, (int)(rightPointY + ((enemySize.getY()/2) - 5)), 1, 1, Color.BLACK);


        }
    }

    public void enemyRender(Vector2 mousePositionWorld)
    {
        if (edit && Main.rlj.shapes.CheckCollisionPointRec(mousePositionWorld, new Rectangle(enemyPosition, enemySize.getX(), enemySize.getY())) && rCore.IsMouseButtonDown(Mouse.MouseButton.MOUSE_BUTTON_RIGHT))
        {
            visible = false;
        }

        if (visible)
        {
            Main.rlj.shapes.DrawRectangleV(enemyPosition, enemySize, Color.RED);
            Main.rlj.shapes.DrawRectangle(groundPointX, groundPointY, 1, 1, Color.MAGENTA);
            Main.rlj.shapes.DrawRectangle(leftPointX, leftPointY, 1, 1, Color.MAGENTA);
            Main.rlj.shapes.DrawRectangle(rightPointX, rightPointY, 1, 1, Color.MAGENTA);
            Main.rlj.shapes.DrawRectangle(leftPointX, leftPointY + 15, 1, 1, Color.BLUE);
            Main.rlj.shapes.DrawRectangle(rightPointX, rightPointY + 15, 1, 1, Color.BLUE);
            Main.rlj.shapes.DrawRectangle(topPointX, topPointY, 1, 1, Color.MAGENTA);
        }
    }
}