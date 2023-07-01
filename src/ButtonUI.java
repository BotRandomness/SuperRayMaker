import com.raylib.java.gui.elements.Button;
import com.raylib.java.core.input.Mouse;
import com.raylib.java.core.rCore;

public class ButtonUI
{
    Button button;

    public ButtonUI(float x, float y, float width, float height, String text)
    {
        button = new Button(x, y, width, height, text);
    }

    public boolean click()
    {
        boolean clicked;

        if(Main.rlj.shapes.CheckCollisionPointRec(rCore.GetMousePosition(), button.bounds) && Main.rlj.core.IsMouseButtonPressed(Mouse.MouseButton.MOUSE_BUTTON_LEFT))
        {
            clicked = true;
        }
        else
        {
            clicked = false;
        }

        return clicked;
    }

    public void buttonRender()
    {
        Main.rgui.GuiButton(button);
    }
}
