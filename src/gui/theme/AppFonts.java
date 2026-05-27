package gui.theme;

import java.awt.Font;

/**
 * Uygulama genel font sistemi.
 */
public class AppFonts {

    // Başlıklar
    public static final Font TITLE =
            new Font("SansSerif", Font.BOLD, 26);

    public static final Font SUBTITLE =
            new Font("SansSerif", Font.BOLD, 18);

    public static final Font SECTION =
            new Font("SansSerif", Font.BOLD, 15);

    // İçerik
    public static final Font BODY =
            new Font("SansSerif", Font.PLAIN, 13);

    public static final Font BODY_MEDIUM =
            new Font("SansSerif", Font.BOLD, 13);

    public static final Font SMALL =
            new Font("SansSerif", Font.PLAIN, 11);

    // Buton
    public static final Font BUTTON =
            new Font("SansSerif", Font.BOLD, 14);

    // Tablo
    public static final Font TABLE_HEADER =
            new Font("SansSerif", Font.BOLD, 13);

    public static final Font TABLE_CELL =
            new Font("SansSerif", Font.PLAIN, 13);

    // Badge
    public static final Font BADGE =
            new Font("SansSerif", Font.BOLD, 12);

    private AppFonts() {}
}