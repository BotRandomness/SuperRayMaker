import com.raylib.java.gui.elements.TextBox;
import com.raylib.java.core.rCore;
import com.raylib.java.core.input.Mouse;
import com.raylib.java.core.input.Keyboard;

public class InputUI
{
    TextBox textBox;

    private String textConstant = " ";

    public InputUI(float x, float y, float width, float height, String text, boolean editMode)
    {
        textBox = new TextBox(x, y, width, height, text, editMode, 20);
    }

    public InputUI(float x, float y, float width, float height, String text)
    {
        textBox = new TextBox(x, y, width, height, text, false, 20);
    }

    public void checkActive()
    {
        if(Main.rlj.shapes.CheckCollisionPointRec(rCore.GetMousePosition(), textBox.bounds) && Main.rlj.core.IsMouseButtonPressed(Mouse.MouseButton.MOUSE_BUTTON_LEFT))
        {
            textBox.editMode = true;
        }
        else if (!Main.rlj.shapes.CheckCollisionPointRec(rCore.GetMousePosition(), textBox.bounds) && Main.rlj.core.IsMouseButtonPressed(Mouse.MouseButton.MOUSE_BUTTON_LEFT))
        {
            textBox.editMode = false;
        }
    }

    public String getText()
    {
        return textBox.text;
    }

    public void inputRender()
    {
        Main.rgui.GuiTextBox(textBox);

        if ((textBox.text.equals(textConstant)) && rCore.IsKeyDown(Keyboard.KEY_BACKSPACE))
        {
            textBox.text = "  ";
        }
    }
}
