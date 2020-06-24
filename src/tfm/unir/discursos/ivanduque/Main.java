package tfm.unir.discursos.ivanduque;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Rafael Guillermo Blanco Banquez <rafaelg.blancob@gmail.com>
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        File dirBase = new File("C:\\Users\\rafar\\OneDrive\\Documentos\\DocRafael\\Master\\trabajo grado\\2020");

        File dirDest = new File(dirBase.getAbsolutePath() + "\\discursos");

        if (dirDest.isDirectory() && dirDest.exists()) {
            File[] files = dirDest.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
        List<String> urls = new ArrayList<>();

        Scanner scannerIN = new Scanner(new File(dirBase.getAbsoluteFile() + "\\discursos.txt"));
        String line, url;
        while (scannerIN.hasNextLine()) {
            line = scannerIN.nextLine();
            int posHttp = line.indexOf("https://");
            url = line.substring(posHttp, line.length());
            urls.add(url);
        }
        scannerIN.close();

        for (String u : urls) {

            Document doc = Jsoup.connect(u).get();
            StringBuilder sb = new StringBuilder();
            Element content = doc.getElementById("ctl00_PlaceHolderMain_ctl06__ControlWrapper_RichHtmlField");
            for (Element element : content.children()) {
                if (element.tagName().equals("p")) {
                    //System.out.println( element.text());
                    sb.append(element.text());
                    sb.append("\n");
                }
            }
            Files.write(Paths.get(dirBase.getAbsolutePath() +"\\discursos\\"+ getNameFromURL(u)), sb.toString().getBytes());
        }

    }

    public static String getNameFromURL(String url) {
        String splitURL[] = url.split("/");
        String splitURLContext[] = splitURL[splitURL.length-1].split("-");
        StringBuilder sb = new StringBuilder();
        for (String p : splitURLContext) {
            if (p.length() > 3) {
                sb.append(p);
            }
        }
        System.out.println(url);
        String name = sb.toString();
        int indexDot = name.indexOf(".");
        if( indexDot > 0){
            name = name.replaceAll(name.substring(indexDot,name.length()),"");
        }
        name = name+".txt";
        System.out.println(name);

        return name;
    }

}
