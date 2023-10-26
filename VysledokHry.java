import fri.shapesge.Text;
import fri.shapesge.FontStyle;
import java.io.IOException;

public class VysledokHry {
    private static Text vyslednyText;
    private static Text casHry;

    /**
     * Zapíše skóre s ďalšími údajmi do súboru a zobrazí sa výsledok hry na obrazovku, vstup je stav hry, čas a výška a dĺžka
     * okna, pre pozíciu na zobrazenie textu. Na naformátovanie času sa používa
     * metóda v triede ZapisSkore
     * 
     * @param stavHry   Stav hry, v metóde sú použité stavy buď VYHRA alebo PREHRA
     * @param cas       Dĺžka trvania hry v sekundách
     * @param dlzkaOkna Dĺžka okna obrazovky
     * @param vyskaOkna Výška okna obrazovky
     */
    public static void zobrazVysledok(String stavHry, double cas, int dlzkaOkna, int vyskaOkna, String meno, int dosiahnuteSkore, String obtiaznost) {
        String vyhrana = "NIE     ";
        if (stavHry.equals("VYHRA")) {
            vyhrana = "ANO     ";
        }
        // Nasledujúcich pár riadkov bolo urobených cez 'Quick Fix Surround with
        // try/catch' funkciu vo VS Code, aby som nemusel používať 'throws
        // IOExepction'
        try {
            ZapisSkore.zapisSkore(meno, dosiahnuteSkore, cas, vyhrana, obtiaznost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vyslednyText = new Text(stavHry, dlzkaOkna / 3, vyskaOkna / 3);
        vyslednyText.zmenFont(stavHry, FontStyle.BOLD, 60);
        casHry = new Text("Cas: " + ZapisSkore.naformatujCasMinuty(cas), dlzkaOkna / 3 + 50,
                vyskaOkna / 3 + 50);
        casHry.zmenFont("Cas: " + ZapisSkore.naformatujCasMinuty(cas), FontStyle.PLAIN, 20);
        vyslednyText.zobraz();
        casHry.zobraz();
    }

    /**
     * Skryje sa výsledok z obrazovky, v prípade že sa začína nová hra
     */
    public static void skryVysledok() {
        if (vyslednyText != null && casHry != null) {
            vyslednyText.skry();
            casHry.skry();
        }
    }
}
