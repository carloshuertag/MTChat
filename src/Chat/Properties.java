package Chat;

/**
 *
 * @author huert
 */
public class Properties {

    public static final String SERVER_IP = "127.0.0.1";
    public static final int PORT = 1234;
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    public static final String SLASH= "\\"; //Windows
    public static final String HTMLHEAD = "<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" charset=\"UTF-8\">"
            + "<base href = \"file:" + System.getProperty("user.dir") + SLASH + "src"+SLASH+"Images\">"
            + "<style>body {margin: 0 auto; max-width: 800px; padding: 0 20px;font-family: Arial, Helvetica, sans-serif;font-size:large}"
            + ".container {border-width: 2px; border-style: solid; border-color: #ddd; background-color: #eee; border-radius: 5px; padding: 10px; margin: 10px 0;}"
            + ".darker {border-color: #ccc; background-color: #ddd;}"
            + ".container::after {content: \"\"; clear: both; display: table;}"
            + ".container img {float: left; max-width: 60px; width: 100%; margin-right: 20px; border-radius: 50%;}"
            + ".container img.right {float: right; margin-left: 20px; margin-right:0;}</style></head>";
    public static final String HTMLMSG1START = "<div class=\"container darker\"><p><img class=\"right\" width=24 height=24 src=\""
            + "https://vk.com/images/emoji/2709_2x.png\"/>&nbsp;&nbsp;";
    public static final String HTMLMSG2START = "<div class=\"container\"><p><img width=24 height=24 src=\""
            + "https://vk.com/images/emoji/2709_2x.png\"/>&nbsp;&nbsp;";
    public static final String HTMLMSGEND = "</p></div>";
    public static final String HTMLIMGSTART = "<img width=25 height=25 alt=\"emoji\" src=\"";
    public static final String HTMLIMGEND = "\" />";
    public static final String[] EMOJIURLS = {
        "https://vk.com/images/emoji/D83DDE00_2x.png",
        "https://vk.com/images/emoji/D83DDE02_2x.png",
        "https://vk.com/images/emoji/D83DDE04_2x.png",
        "https://vk.com/images/emoji/D83DDE09_2x.png",
        "https://vk.com/images/emoji/D83DDE0A_2x.png",
        "https://vk.com/images/emoji/D83DDE0D_2x.png",
        "https://vk.com/images/emoji/D83DDE18_2x.png",
        "https://vk.com/images/emoji/D83DDE31_2x.png",
        "https://vk.com/images/emoji/D83DDE2A_2x.png"
    }, EMOJINAMES = {
        "Happy",
        "Lmao",
        "Lol",
        "Wink",
        "Smile",
        "Love",
        "Kiss",
        "Surprised",
        "Sad"
    };
    public static final String AUDIOICON = "https://vk.com/images/emoji/D83DDCE3_2x.png";
}