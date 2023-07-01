import com.raylib.java.core.Color;
import com.raylib.java.core.input.Mouse;
import com.raylib.java.core.rCore;
import com.raylib.java.shapes.Rectangle;
import com.raylib.java.raymath.Vector2;
import com.raylib.java.textures.*;
import com.raylib.java.textures.rTextures;

public class Block
{
    public Vector2 position = new Vector2(0, 0);
    public Vector2 size;
    public  Color color;
    public boolean visible;
    public boolean edit;
    public String type;
    public Rectangle box = new Rectangle();
    public Texture2D texture;

    int frameWidth;
    int frameHeight;
    int currentFrame = 0;
    int framesCounter = 0;
    int framesSpeed = 7;

    public Block(Vector2 position, Vector2 size, boolean visible, boolean edit, String type, Texture2D texture)
    {
        this.position = position;
        this.size = size;
        this.color = new Color(Main.rlj.core.GetRandomValue(0,255), Main.rlj.core.GetRandomValue(0,255), Main.rlj.core.GetRandomValue(0,255), 255);
        this.visible = visible;
        this.edit = edit;
        this.type = type;
        this.texture = texture;
        box.setX(position.getX());
        box.setY(position.getY());
        box.setHeight(size.getY());
        box.setWidth(size.getX());
        frameWidth = texture.width/2;
        frameHeight = texture.height;
    }

    public Block(float x, float y, Vector2 size, boolean visible, boolean edit, String type, Texture2D texture)
    {
        position.x = x;
        position.y = y;
        this.size = size;
        this.color = new Color(Main.rlj.core.GetRandomValue(0,255), Main.rlj.core.GetRandomValue(0,255), Main.rlj.core.GetRandomValue(0,255), 255);
        this.visible = visible;
        this.edit = edit;
        this.type = type;
        this.texture = texture;
        box.setX(position.getX());
        box.setY(position.getY());
        box.setHeight(size.getY());
        box.setWidth(size.getX());
    }

    public Block(Vector2 position, Vector2 size, boolean visible, boolean edit, String type)
    {
        this.position = position;
        this.size = size;
        this.color = new Color(Main.rlj.core.GetRandomValue(0,255), Main.rlj.core.GetRandomValue(0,255), Main.rlj.core.GetRandomValue(0,255), 255);
        this.visible = visible;
        this.edit = edit;
        this.type = type;
        box.setX(position.getX());
        box.setY(position.getY());
        box.setHeight(size.getY());
        box.setWidth(size.getX());
    }

    public Block()
    {
        this.color = new Color(Main.rlj.core.GetRandomValue(0,255), Main.rlj.core.GetRandomValue(0,255), Main.rlj.core.GetRandomValue(0,255), 255);
    }

    //draws a block based on x, y, width, length
    public void drawBlock(Vector2 mousePositionWorld, boolean editable)
    {
        if (editable && Main.rlj.shapes.CheckCollisionPointRec(mousePositionWorld, box) && rCore.IsMouseButtonDown(Mouse.MouseButton.MOUSE_BUTTON_RIGHT))
        {
            visible = false;
        }

        if (visible)
        {
            if (!type.equals("flag") && !type.equals("coin"))
            {
                //Main.rlj.shapes.DrawRectangleV(position, size, color);
                Main.rlj.textures.DrawTextureEx(texture, position, 0f, 2.8f, Color.WHITE);
            }
            if (type.equals("flag"))
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

                //Main.rlj.shapes.DrawRectangleV(position, size, color);
                rTextures.DrawTexturePro(
                        texture,
                        new Rectangle(currentFrame*frameWidth,0,frameWidth,frameHeight),
                        new Rectangle(position.x,position.y+17,frameWidth*2.0f,frameHeight*2.0f),
                        new Vector2(0,0),
                        0,
                        Color.WHITE
                );
            }
            if (type.equals("coin"))
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

                //Main.rlj.shapes.DrawRectangleV(position, size, color);
                rTextures.DrawTexturePro(
                        texture,
                        new Rectangle(currentFrame*frameWidth,0,frameWidth,frameHeight),
                        new Rectangle(position.x-10,position.y-11,frameWidth*2.0f,frameHeight*2.0f),
                        new Vector2(0,0),
                        0,
                        Color.WHITE
                );
            }

        }
    }

    public void drawBlock(Vector2 mousePositionWorld)
    {
        if (edit && Main.rlj.shapes.CheckCollisionPointRec(mousePositionWorld, box) && rCore.IsMouseButtonDown(Mouse.MouseButton.MOUSE_BUTTON_RIGHT))
        {
            visible = false;
        }

        if (visible)
        {
            Main.rlj.shapes.DrawRectangleV(position, size, color);
        }
    }
}
