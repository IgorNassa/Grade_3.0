package br.sistema.view.theme;

import java.awt.*;

public class ThemeFonts {

    private static final String FONT_PATH =
            "/Fonts/JetBrainsMono-Regular.ttf";

    public static final Font TITLE =
            FontManager.loadFont(
                    FONT_PATH,
                    24f,
                    Font.BOLD
            );

    public static final Font SUBTITLE =
            FontManager.loadFont(
                    FONT_PATH,
                    16f,
                    Font.PLAIN
            );

    public static final Font MENU =
            FontManager.loadFont(
                    FONT_PATH,
                    14f,
                    Font.PLAIN
            );

    public static final Font BODY =
            FontManager.loadFont(
                    FONT_PATH,
                    13f,
                    Font.PLAIN
            );

    public static final Font SMALL =
            FontManager.loadFont(
                    FONT_PATH,
                    11f,
                    Font.PLAIN
            );

    private ThemeFonts() {
    }
}