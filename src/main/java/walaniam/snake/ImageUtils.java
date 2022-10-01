package walaniam.snake;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtils {

    public static Image loadImage(ImageResource image) {
        var icon = new ImageIcon(image.getUrl());
        return icon.getImage();
    }
}
