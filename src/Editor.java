import com.raylib.java.core.Color;
import com.raylib.java.core.rcamera.Camera2D;
import com.raylib.java.core.input.Keyboard;
import com.raylib.java.core.input.Mouse;
import com.raylib.java.core.rCore;
import com.raylib.java.raymath.Vector2;
import com.raylib.java.textures.rTextures;
import com.raylib.java.raymath.Raymath;
import java.util.*;
import java.io.*;

public class Editor
{
    Player player;
    public int screenWidth;
    public int screenHeight;
    public static boolean edit;

    Vector2 mousePos = new Vector2(Main.rlj.core.GetMouseX(), Main.rlj.core.GetMouseY());
    Camera2D camera = new Camera2D();
    Vector2 stw = Main.rlj.core.GetScreenToWorld2D(mousePos, camera);

    ArrayList<Block> blocks = new ArrayList<>();
    ArrayList<Enemy> enemys = new ArrayList<>();

    //Button objects to render to screen
    ButtonUI buttonImport = new ButtonUI(550, 3, 100, 35, "import");
    ButtonUI buttonExport = new ButtonUI(655, 3, 100, 35, "Export");
    ButtonUI play = new ButtonUI(360, 3, 85, 30, "Play");
    ButtonUI editMode = new ButtonUI(360, 3, 85, 30, "Edit Mode");
    InputUI inputFile = new InputUI(600, 40, 100, 35, "  level.csv");
    ButtonUI mouse = new ButtonUI(3, 8, 15,15, "M");
    ButtonUI block = new ButtonUI(20, 8, 15,15, "B");
    ButtonUI plat = new ButtonUI(38, 8, 15,15, "P");
    ButtonUI eme = new ButtonUI(55, 8, 15,15, "E");
    ButtonUI beme = new ButtonUI(72, 8, 15,15, "BE");
    ButtonUI spike = new ButtonUI(89, 8, 15,15, "S");
    ButtonUI flag = new ButtonUI(107, 8, 15,15, "F");
    ButtonUI coin = new ButtonUI(124, 8, 15,15, "C");
    ButtonUI g1 = new ButtonUI(3, 26, 15,15, "G1");
    ButtonUI g2 = new ButtonUI(3, 44, 15,15, "G2");
    ButtonUI g3 = new ButtonUI(3, 62, 15,15, "G3");
    ButtonUI f1 = new ButtonUI(3, 80, 15,15, "F1");
    ButtonUI f2 = new ButtonUI(3, 98, 15,15, "F2");
    ButtonUI f3 = new ButtonUI(3, 116, 15,15, "F3");
    ButtonUI c1 = new ButtonUI(3, 134, 15,15, "C1");
    ButtonUI c2 = new ButtonUI(3, 152, 15,15, "C2");
    ButtonUI c3 = new ButtonUI(3, 170, 15,15, "C3");
    ButtonUI a1 = new ButtonUI(3, 188, 15,15, "A1");
    ButtonUI a2 = new ButtonUI(3, 206, 15,15, "A2");
    ButtonUI u1 = new ButtonUI(3, 224, 15,15, "U1");
    ButtonUI u2 = new ButtonUI(3, 242, 15,15, "U2");
    ButtonUI u3 = new ButtonUI(3, 260, 15,15, "U3");

    int blockState = 0;
    int gridSize = 50;

    private boolean mouseControl;

    //Enemy enemy = new Enemy(new Vector2(400, 200), new Vector2(30, 30), true, true, "nor");

    //Editor screen require parameters to render
    public Editor(Player player, int screenWidth, int screenHeight)
    {
        this.player = player;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        edit = true;

        camera.target = new Vector2(player.playerPosition.getX(), player.playerPosition.getY());
        camera.offset = new Vector2(screenWidth / 2.0f, screenHeight / 2.0f);
        camera.rotation = 0.0f;
        camera.zoom = 1.0f;
    }

    //editorControl handles all player input during editing
    private void editorControls() throws FileNotFoundException
    {
        if(edit)
        {
            buttonStates();

            mouseControl = Main.rlj.core.IsMouseButtonPressed(Mouse.MouseButton.MOUSE_BUTTON_LEFT) || (rCore.IsMouseButtonDown(Mouse.MouseButton.MOUSE_BUTTON_LEFT) && (rCore.IsKeyDown(Keyboard.KEY_LEFT_CONTROL) || rCore.IsKeyDown(Keyboard.KEY_RIGHT_CONTROL)));

            // Translate based on mouse right click
            if (rCore.IsMouseButtonDown(Mouse.MouseButton.MOUSE_BUTTON_MIDDLE))
            {
                Vector2 delta = Main.rlj.core.GetMouseDelta();
                delta = Raymath.Vector2Scale(delta, -1.0f/camera.zoom);

                camera.target = Raymath.Vector2Add(camera.target, delta);
            }

            // Zoom based on mouse wheel
            float wheel = rCore.GetMouseWheelMove();
            if (wheel != 0)
            {
                // Get the world point that is under the mouse
                Vector2 mouseWorldPos = Main.rlj.core.GetScreenToWorld2D(rCore.GetMousePosition(), camera);

                // Set the offset to where the mouse is
                camera.offset = rCore.GetMousePosition();

                // Set the target to match, so that the camera maps the world space point
                // under the cursor to the screen space point under the cursor at any zoom
                camera.target = mouseWorldPos;

                // Zoom increment
                final float zoomIncrement = 0.125f;

                camera.zoom += (wheel*zoomIncrement);

                if (camera.zoom < zoomIncrement)
                {
                    camera.zoom = zoomIncrement;
                }
            }

            mousePos.setY((float)(Main.rlj.core.GetMouseY()));
            mousePos.setX((float)(Main.rlj.core.GetMouseX()));

            stw = Main.rlj.core.GetScreenToWorld2D(mousePos, camera);

            if (Main.rlj.core.IsKeyPressed(Keyboard.KEY_LEFT_SHIFT) || Main.rlj.core.IsKeyPressed(Keyboard.KEY_RIGHT_SHIFT))
            {
                int snappedX = (int)(mousePos.getX()/gridSize) * gridSize;
                int snappedY = (int)(mousePos.getY()/gridSize) * gridSize;

                Main.rlj.core.SetMousePosition(snappedX, snappedY);
            }

            //depending on button click place that block
            if (mouseControl && blockState == 1)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "box", rTextures.LoadTexture("resources/tile_0000.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 2)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "plat", rTextures.LoadTexture("resources/tile_0009.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 5)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "g1", rTextures.LoadTexture("resources/tile_0001.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 6)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "g2", rTextures.LoadTexture("resources/tile_0002.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 7)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "g3", rTextures.LoadTexture("resources/tile_0003.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 8)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,25f), true, edit, "spike", rTextures.LoadTexture("resources/tile_0068.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 9)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,90f), true, edit, "flag", rTextures.LoadTexture("resources/flagstrip.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 10)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(30f,30f), true, edit, "coin", rTextures.LoadTexture("resources/coinstrip.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 11)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "f1", rTextures.LoadTexture("resources/tile_0021.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 12)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "f2", rTextures.LoadTexture("resources/tile_0022.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 13)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "f3", rTextures.LoadTexture("resources/tile_0023.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 14)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "c1", rTextures.LoadTexture("resources/tile_0153.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 15)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "c2", rTextures.LoadTexture("resources/tile_0154.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 16)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "c3", rTextures.LoadTexture("resources/tile_0155.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 17)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "a1", rTextures.LoadTexture("resources/tile_0087.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 18)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "a2", rTextures.LoadTexture("resources/tile_0088.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 19)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "u1", rTextures.LoadTexture("resources/tile_0121.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 20)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "u2", rTextures.LoadTexture("resources/tile_0122.png")));
                System.out.println("Blocks: " + blocks.size());
            }
            if (mouseControl && blockState == 21)
            {
                blocks.add(new Block(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50f,50f), true, edit, "u3", rTextures.LoadTexture("resources/tile_0123.png")));
                System.out.println("Blocks: " + blocks.size());
            }

            if (Main.rlj.core.IsMouseButtonPressed(Mouse.MouseButton.MOUSE_BUTTON_LEFT) && blockState == 3)
            {
                enemys.add(new Enemy(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(30, 30), true, edit, "nor", rTextures.LoadTexture("resources/norstrip.png")));
                System.out.println("Enemys: " + enemys.size());
            }
            if (Main.rlj.core.IsMouseButtonPressed(Mouse.MouseButton.MOUSE_BUTTON_LEFT) && blockState == 4)
            {
                enemys.add(new Enemy(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(45, 45), true, edit, "bnor", rTextures.LoadTexture("resources/bnorstrip.png")));
                System.out.println("Enemys: " + enemys.size());
            }

            if(play.click())
            {
                inputFile.textBox.text = "  temp.csv";
                exportLevel();
                edit = false;
                player.coins = 0;
            }

        }
        else if (edit == false)
        {
            camera.target = new Vector2(player.playerPosition.getX(), player.playerPosition.getY() - 0.001f);
            camera.zoom = 0.8f;
            camera.offset = new Vector2(screenWidth / 2.0f, screenHeight / 2.0f);
            camera.rotation = 0.0f;

            if (editMode.click())
            {
                inputFile.textBox.text = "  temp.csv";
                importLevel();
                edit = true;
            }
        }

        //player.playerUpdate(blocks, enemys , edit);
    }

    //change block place states
    private void buttonStates()
    {
        if(mouse.click())
        {
            blockState = 0;
        }
        else if(block.click())
        {
            blockState = 1;
        }
        else if (plat.click())
        {
            blockState = 2;
        }
        else if (eme.click())
        {
            blockState = 3;
        }
        else if (beme.click())
        {
            blockState = 4;
        }
        else if (g1.click())
        {
            blockState = 5;
        }
        else if (g2.click())
        {
            blockState = 6;
        }
        else if (g3.click())
        {
            blockState = 7;
        }
        else if (spike.click())
        {
            blockState = 8;
        }
        else if (flag.click())
        {
            blockState = 9;
        }
        else if (coin.click())
        {
            blockState = 10;
        }
        else if (f1.click())
        {
            blockState = 11;
        }
        else if (f2.click())
        {
            blockState = 12;
        }
        else if (f3.click())
        {
            blockState = 13;
        }
        else if (c1.click())
        {
            blockState = 14;
        }
        else if (c2.click())
        {
            blockState = 15;
        }
        else if (c3.click())
        {
            blockState = 16;
        }
        else if (a1.click())
        {
            blockState = 17;
        }
        else if (a2.click())
        {
            blockState = 18;
        }
        else if (u1.click())
        {
            blockState = 19;
        }
        else if (u2.click())
        {
            blockState = 20;
        }
        else if (u3.click())
        {
            blockState = 21;
        }
    }

    private void blockHighlights()
    {
        if (blockState == 0)
        {

        }
        else if (blockState == 3 || blockState == 10)
        {
            Main.rlj.shapes.DrawRectangleV(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(30, 30), new Color(0, 255, 0, 60));
        }
        else if (blockState == 4)
        {
            Main.rlj.shapes.DrawRectangleV(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(45, 45), new Color(0, 255, 0, 60));
        }
        else if (blockState == 8)
        {
            Main.rlj.shapes.DrawRectangleV(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50, 25), new Color(0, 255, 0, 60));
        }
        else if (blockState == 9)
        {
            Main.rlj.shapes.DrawRectangleV(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50, 90), new Color(0, 255, 0, 60));
        }
        else
        {
            Main.rlj.shapes.DrawRectangleV(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), new Vector2(50, 50), new Color(0, 255, 0, 60));
        }
    }

    private void importTypeBlocks(String type, float x, float y, float l, float w)
    {
        if (type.equals("nor"))
        {
            Enemy enemy = new Enemy(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/norstrip.png"));
            enemys.add(enemy);
        }
        if (type.equals("bnor"))
        {
            Enemy enemy = new Enemy(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/bnorstrip.png"));
            enemys.add(enemy);
        }
        if (type.equals("box"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0000.png"));
            blocks.add(block);
        }
        if (type.equals("plat"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0009.png"));
            blocks.add(block);
        }
        if (type.equals("g1"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0001.png"));
            blocks.add(block);
        }
        if (type.equals("g2"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0002.png"));
            blocks.add(block);
        }
        if (type.equals("g3"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0003.png"));
            blocks.add(block);
        }
        if (type.equals("spike"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0068.png"));
            blocks.add(block);
        }
        if (type.equals("flag"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/flagstrip.png"));
            blocks.add(block);
        }
        if (type.equals("coin"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/coinstrip.png"));
            blocks.add(block);
        }
        if (type.equals("f1"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0021.png"));
            blocks.add(block);
        }
        if (type.equals("f2"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0022.png"));
            blocks.add(block);
        }
        if (type.equals("f3"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0023.png"));
            blocks.add(block);
        }
        if (type.equals("c1"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0153.png"));
            blocks.add(block);
        }
        if (type.equals("c2"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0154.png"));
            blocks.add(block);
        }
        if (type.equals("c3"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0155.png"));
            blocks.add(block);
        }
        if (type.equals("a1"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0087.png"));
            blocks.add(block);
        }
        if (type.equals("a2"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0088.png"));
            blocks.add(block);
        }
        if (type.equals("u1"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0121.png"));
            blocks.add(block);
        }
        if (type.equals("u2"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0122.png"));
            blocks.add(block);
        }
        if (type.equals("u3"))
        {
            Block block = new Block(new Vector2(x,y), new Vector2(l, w), true, edit, type, rTextures.LoadTexture("resources/tile_0123.png"));
            blocks.add(block);
        }
    }

    //draws the blocks on screen
    public void editorDrawBlocks()
    {
        if(edit)
        {
            Main.rlj.core.ClearBackground(Color.RAYWHITE);
        }
        else if (edit == false)
        {
            Main.rlj.core.ClearBackground(Color.SKYBLUE);
        }

        Main.rlj.core.BeginMode2D(camera);

        for (int i = 0; i < blocks.size(); i++)
        {
            blocks.get(i).drawBlock(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), edit);

            if (blocks.get(i).visible == false)
            {
                blocks.remove(i);
            }
        }

        for (int i = 0; i < enemys.size(); i++)
        {
            enemys.get(i).enemyRender(Main.rlj.core.GetScreenToWorld2D(mousePos, camera), edit);

            if (enemys.get(i).visible == false)
            {
                enemys.remove(i);
            }
        }

        player.playerRender();
        //enemy.enemyRender(Main.rlj.core.GetScreenToWorld2D(mousePos, camera));

        if(edit)
        {
            grid();
            blockHighlights();
        }

        Main.rlj.core.EndMode2D();
    }

    private void grid()
    {
        for(int i = -screenWidth; i <= screenWidth * 15; i += gridSize)
        {
            Main.rlj.shapes.DrawLine(i, -screenHeight, i, screenHeight * 15, Color.GREEN);
        }

        for(int i = -screenHeight; i <= screenHeight * 15; i += gridSize)
        {
            Main.rlj.shapes.DrawLine(-screenWidth, i, screenWidth * 15, i, Color.GREEN);
        }
    }

    public void exportLevel() throws FileNotFoundException
    {
        Scanner input = new Scanner(System.in);
        String fileNameRaw = inputFile.getText();
        String fileName = "";

        for(int i = 0; i < fileNameRaw.length(); i++)
        {
            if(Character.isLetter(fileNameRaw.charAt(i)) || fileNameRaw.charAt(i) == '.')
            {
                fileName += fileNameRaw.charAt(i);
            }
        }

        //printer write object called "export" writes data to a file
        PrintWriter export = new PrintWriter(fileName);

        //for every block in level...
        for (int i = 0; i < blocks.size(); i++)
        {
            //...for every piece of information in the block...
            for (int j = 0; j < 6; j++)
            {
                //...write a piece of date to file sperated by a comma
                if (j == 6 - 1)
                {

                    if (j == 0)
                    {
                        export.print(blocks.get(i).type);
                    }
                    else if (j == 1)
                    {
                        export.print(blocks.get(i).position.getX());
                    }
                    else if (j == 2)
                    {
                        export.print(blocks.get(i).position.getY());
                    }
                    else if (j == 3)
                    {
                        export.print(blocks.get(i).size.getX());
                    }
                    else if (j == 4)
                    {
                        export.print(blocks.get(i).size.getY());
                    }
                }
                else
                {

                    if (j == 0)
                    {
                        export.print(blocks.get(i).type + ",");
                    }
                    else if (j == 1)
                    {
                        export.print(blocks.get(i).position.getX() + ",");
                    }
                    else if (j == 2)
                    {
                        export.print(blocks.get(i).position.getY() + ",");
                    }
                    else if (j == 3)
                    {
                        export.print(blocks.get(i).size.getX() + ",");
                    }
                    else if (j == 4)
                    {
                        export.print(blocks.get(i).size.getY() + ",");
                    }
                }
            }

            //after one block writen, start new line to write next block
            export.println();

        }

        for (int i = 0; i < enemys.size(); i++)
        {
            //...for every piece of information in the block...
            for (int j = 0; j < 5; j++)
            {
                //...write a piece of date to file sperated by a comma
                if (j == 5 - 1)
                {
                    if (j == 0)
                    {
                        export.print(enemys.get(i).type);
                    }
                    else if (j == 1)
                    {
                        export.print(enemys.get(i).enemyPosition.getX());
                    }
                    else if (j == 2)
                    {
                        export.print(enemys.get(i).enemyPosition.getY());
                    }
                    else if (j == 3)
                    {
                        export.print(enemys.get(i).enemySize.getX());
                    }
                    else if (j == 4)
                    {
                        export.print(enemys.get(i).enemySize.getY());
                    }
                }
                else
                {
                    if (j == 0)
                    {
                        export.print(enemys.get(i).type + ",");
                    }
                    else if (j == 1)
                    {
                        export.print(enemys.get(i).enemyPosition.getX() + ",");
                    }
                    else if (j == 2)
                    {
                        export.print(enemys.get(i).enemyPosition.getY() + ",");
                    }
                    else if (j == 3)
                    {
                        export.print(enemys.get(i).enemySize.getX() + ",");
                    }
                    else if (j == 4)
                    {
                        export.print(enemys.get(i).enemySize.getY() + ",");
                    }
                }
            }

            export.println();

        }

        //saves file that has been written to
        export.close();
        System.out.println("Exported to: " + fileName);

    }

    public void importLevel() throws FileNotFoundException
    {
        String fileNameRaw = inputFile.getText();
        String fileName = "";

        for(int i = 0; i < fileNameRaw.length(); i++)
        {
            if(Character.isLetter(fileNameRaw.charAt(i)) || fileNameRaw.charAt(i) == '.')
            {
                fileName += fileNameRaw.charAt(i);
            }
        }

        //file object to store information from the file
        File toRead = new File(fileName);

        int i = 0; //index varible to keep track of number of blocks

        //if the file exist
        if (toRead.exists())
        {
            //scanner object to read a single line from the file
            Scanner read = new Scanner(new File(fileName));

            for (int c = 0; c < blocks.size(); c++)
            {
                //blocks.get(c).drawBlock(Main.rlj.core.GetScreenToWorld2D(mousePos, camera));

                if (blocks.get(c).visible == false)
                {
                    blocks.remove(c);
                }
            }

            for (int e = 0; e < enemys.size(); e++)
            {
                //enemys.get(e).enemyRender(Main.rlj.core.GetScreenToWorld2D(mousePos, camera));

                if (enemys.get(e).visible == false)
                {
                    enemys.remove(e);
                }
            }

            enemys.clear();
            blocks.clear();

            //while the file has lines to read...
            while (read.hasNextLine())
            {
                //stores a line in line varible
                String line = read.nextLine();
                //scanner object to read each word in a line
                Scanner item = new Scanner(line);
                //comma delimiter to show the program that each word is divied by commas
                item.useDelimiter(",");

                ArrayList<String> blockInfo = new ArrayList<>();
                blockInfo.clear();

                float x = 0;
                float y = 0;
                float l = 0;
                float w = 0;
                String type = "";

                //while there more words to read in a line...
                while(item.hasNext())
                {
                    //create a block object...
                    //Block block = new Block();
                    //...add that block object to the level list
                    //blocks.add(block);

                    //...for every piece of information needed in a block...
                    for (int j = 0; j < 5; j++)
                    {
                        //write that piece of information to corresponding index
                        //phoneBook.get(i).setterSet(j, item.next());
                        blockInfo.add(item.next());
                    }

                    for (int j = 0; j < 5; j++)
                    {
                        if (j == 0)
                        {
                            //export.print(blocks.get(i).type + ",");
                            type = blockInfo.get(j);
                        }
                        else if (j == 1)
                        {
                            //export.print(blocks.get(i).position.getX() + ",");
                            x = Float.parseFloat(blockInfo.get(j));
                        }
                        else if (j == 2)
                        {
                            //export.print(blocks.get(i).position.getY() + ",");
                            y = Float.parseFloat(blockInfo.get(j));
                        }
                        else if (j == 3)
                        {
                            //export.print(blocks.get(i).size.getX() + ",");
                           l = Float.parseFloat(blockInfo.get(j));
                        }
                        else if (j == 4)
                        {
                            //export.print(blocks.get(i).size.getY() + ",");
                            w = Float.parseFloat(blockInfo.get(j));
                        }
                    }
                }

                importTypeBlocks(type, x, y, l, w);

                i++; //add number of contacts index by 1
            }

            System.out.println("imported from: " + fileName);
        }
        //...else file does not exist...
        else
        {
            //...print message that file does not exist
            System.out.println(fileName + " does not exists");
        }
    }

    //calls the rest of methods in one method
    public void editorUpdate() throws FileNotFoundException
    {
        editorControls();
        player.playerUpdate(blocks, enemys, edit);

        if (edit)
        {
            if(buttonExport.click())
            {
                exportLevel();
            }
            if(buttonImport.click())
            {
                importLevel();
            }

            player.playerPosition = new Vector2((float) screenWidth / 2, (float) screenHeight / 2);
        }

        //enemy.enemyUpdate(blocks);
        for (int i = 0; i < enemys.size(); i++)
        {
            enemys.get(i).enemyUpdate(blocks, edit);
        }

        inputFile.checkActive();
    }

    //calls all rendering methods in one
    public void editorRender()
    {
        editorDrawBlocks();

        if (edit)
        {
            buttonImport.buttonRender();
            buttonExport.buttonRender();
            inputFile.inputRender();
            mouse.buttonRender();
            block.buttonRender();
            plat.buttonRender();
            eme.buttonRender();
            beme.buttonRender();
            g1.buttonRender();
            g2.buttonRender();
            g3.buttonRender();
            f1.buttonRender();
            f2.buttonRender();
            f3.buttonRender();
            c1.buttonRender();
            c2.buttonRender();
            c3.buttonRender();
            a1.buttonRender();
            a2.buttonRender();
            u1.buttonRender();
            u2.buttonRender();
            u3.buttonRender();
            spike.buttonRender();
            flag.buttonRender();
            coin.buttonRender();
            play.buttonRender();
        }
        else if (edit == false)
        {
            editMode.buttonRender();
            Main.rlj.text.DrawText("Coins: " + Integer.toString(player.coins), 50, 15, 20, Color.GOLD);
        }

    }
}
