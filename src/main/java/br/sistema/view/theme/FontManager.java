package br.sistema.view.theme;

import java.awt.*;

public class FontManager {

    public static Font loadFont(
            String path,
            float size,
            int style
    ) {

        try {

            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    FontManager.class.getResourceAsStream(path)
            );

            return font.deriveFont(style, size);

        } catch (Exception e) {

            return new Font(
                    "SansSerif",
                    style,
                    (int) size
            );
        }
    }

    private FontManager() {
    }
}