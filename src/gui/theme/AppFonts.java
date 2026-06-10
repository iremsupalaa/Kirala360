package gui.theme;

import java.awt.Font;
import java.io.InputStream;

public class AppFonts {

    private static Font REGULAR;
    private static Font MEDIUM;
    private static Font BOLD;

    static {
        try {

            REGULAR = Font.createFont(
                    Font.TRUETYPE_FONT,
                    AppFonts.class.getResourceAsStream(
                            "/assets/fonts/Montserrat/static/Montserrat-Regular.ttf"
                    )
            );

            MEDIUM = Font.createFont(
                    Font.TRUETYPE_FONT,
                    AppFonts.class.getResourceAsStream(
                            "/assets/fonts/Montserrat/static/Montserrat-Medium.ttf"
                    )
            );

            BOLD = Font.createFont(
                    Font.TRUETYPE_FONT,
                    AppFonts.class.getResourceAsStream(
                            "/assets/fonts/Montserrat/static/Montserrat-Bold.ttf"
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();

            REGULAR = new Font("SansSerif", Font.PLAIN, 14);
            MEDIUM  = new Font("SansSerif", Font.BOLD, 14);
            BOLD    = new Font("SansSerif", Font.BOLD, 14);
        }
    }

    // Başlıklar
    public static final Font TITLE =
            BOLD.deriveFont(26f);

    public static final Font SUBTITLE =
            BOLD.deriveFont(18f);

    public static final Font SECTION =
            BOLD.deriveFont(15f);

    // İçerik
    public static final Font BODY =
            REGULAR.deriveFont(13f);

    public static final Font BODY_MEDIUM =
            MEDIUM.deriveFont(13f);

    public static final Font SMALL =
            REGULAR.deriveFont(11f);

    // Buton
    public static final Font BUTTON =
            MEDIUM.deriveFont(14f);

    // Tablo
    public static final Font TABLE_HEADER =
            BOLD.deriveFont(13f);
    public static final Font TABLE_CELL =
            REGULAR.deriveFont(13f);

    // Badge
    public static final Font BADGE =
            MEDIUM.deriveFont(12f);

    private AppFonts() {}
}