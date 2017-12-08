package model;

import javafx.scene.control.Button;

import java.awt.*;
import java.net.URI;

public class Item
{
    private String itemLink = "";
    private String itemDescription = "";
    private String img = "";

    public Item(String itemLink, String itemDescription, String img)
    {

        this.itemLink = itemLink;
        this.itemDescription = itemDescription;
        this.img = img;
    }

    /** Method used via reflection, do not remove**/
    public Button getItemLink()
    {
        Button button = new Button(itemLink);
        button.setOnAction(e -> {
            Desktop d = Desktop.getDesktop();
            try
            {
                d.browse(new URI(itemLink));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        return button;
    }

    /** Method used via reflection, do not remove**/
    public String getItemDescription()
    {
        return itemDescription;
    }

    public String getImg()
    {
        return img;
    }

}

