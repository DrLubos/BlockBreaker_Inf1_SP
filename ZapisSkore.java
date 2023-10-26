import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ZapisSkore {
    // Maximálna povolená dĺžka mena
    private static final int DLZKA_MENA = 25;

    /**
     * Metóda otvorí súbor a načíta ho do ArrayListu, začne prechádzať ArrayList a
     * pridá naformátované údaje (ktoré sú získané z metódy 'formatovanie') na
     * pozíciu, tak aby výsledky boli zoradené podľa dosiahnutého skóre. Potom celý
     * ArrayList prepíše do súboru a vyčistý ArrayList.
     * 
     * @param meno            Meno hráča
     * @param dosiahnuteSkore dosiahnuté skóre
     * @param cas             Dĺžka hry
     * @param vyhra           Príjma String, ktorý je už naformátovaný a pripravený
     *                        na zápis, informuje o tom či hra je VYHRA alebo nie
     * @param obtiaznbost
     */
    public static void zapisSkore(String meno, int dosiahnuteSkore, double cas, String vyhra, String obtiaznost)
            throws IOException {
        ArrayList<String> tabulka = new ArrayList<String>();
        File subor = new File("skore.txt");
        subor.createNewFile();
        Scanner citac = new Scanner(subor);
        String riadokCitac = "";
        String prvyRiadok = "Meno";
        for (int i = 0; i <= DLZKA_MENA - 4; i++) {
            prvyRiadok += " ";
        }
        tabulka.add(prvyRiadok + "Skore   Cas       Vyhra   Obtiaznost");
        String druhyRiadok = "";
        for (int i = 0; i <= DLZKA_MENA; i++) {
            druhyRiadok += "=";
        }
        tabulka.add(druhyRiadok + "=====================================");
        if (citac.hasNextLine()) {
            citac.nextLine();
        }
        if (citac.hasNextLine()) {
            citac.nextLine();
        }
        while (citac.hasNextLine()) {
            riadokCitac = citac.nextLine();
            String prvaCast = "";
            String druhaCast = "";
            for (int i = 0; i < riadokCitac.length(); i++) {
                char znak = riadokCitac.charAt(i);
                if (Character.isSpaceChar(znak) || Character.isWhitespace(znak)) {
                    prvaCast = riadokCitac.substring(0, i);
                    continue;
                }
                if (Character.isDigit(znak)) {
                    druhaCast = riadokCitac.substring(i);
                    break;
                }
            }
            if (prvaCast.length() > DLZKA_MENA) {
                prvaCast = prvaCast.substring(0, DLZKA_MENA);
            }
            do {
                prvaCast += " ";
            } while (prvaCast.length() != DLZKA_MENA + 1);
            tabulka.add(prvaCast + druhaCast);
        }
        citac.close();
        boolean prebehloPridanie = false;
        A: for (int i = 2; i < tabulka.size(); i++) {
            String riadok = tabulka.get(i);
            for (int j = 0; j < riadok.length(); j++) {
                if (Character.isDigit(riadok.charAt(j))) {
                    String odZaciatkuCisel = riadok.substring(j).stripLeading();
                    for (int k = 0; k < odZaciatkuCisel.length(); k++) {
                        if (!Character.isDigit(odZaciatkuCisel.charAt(k))) {
                            String skoreString = odZaciatkuCisel.substring(0, k).trim();
                            int skoreRiadok = Integer.parseInt(skoreString);
                            if (skoreRiadok < dosiahnuteSkore) {
                                String text = formatovanieRiadkuNaZapis(meno, dosiahnuteSkore, cas, vyhra, obtiaznost);
                                tabulka.add(i, text);
                                prebehloPridanie = true;
                                break A;
                            } else {
                                continue A;
                            }
                        }
                    }
                }
            }
        }
        if (!prebehloPridanie) {
            String text = formatovanieRiadkuNaZapis(meno, dosiahnuteSkore, cas, vyhra, obtiaznost);
            tabulka.add(text);
        }
        PrintWriter zapisovac = new PrintWriter(subor);
        for (String s : tabulka) {
            zapisovac.println(s);
        }
        zapisovac.close();
        tabulka.clear();
    }

    /**
     * Trieda naformátuje meno hráča, dosiahnuté skóre a čas, tak aby sa zapísal vo
     * vhodnom formáte do súboru. Metóda vacia String pripravený na zápis do súboru.
     * 
     * @param meno       Meno hráča
     * @param skore      Dosiahnuté skóre
     * @param cas        Čas ako dlho trvala hra
     * @param vyhra      Informácia o tom či sa hru podarilo vyhrať, alebo nie
     * @param obtiaznost Nastavená obtiažnosť hry
     * @return Naformátovaný string, pripravený na zápis do súboru
     */
    private static String formatovanieRiadkuNaZapis(String meno, int skore, double cas, String vyhra,
            String obtiaznost) {
        if (meno.length() > DLZKA_MENA) {
            meno = meno.substring(0, DLZKA_MENA);
        }
        do {
            meno += " ";
        } while (meno.length() != DLZKA_MENA + 1);
        String skoreString;
        if (skore > 9999999) {
            skoreString = "9999999";
        } else {
            skoreString = Integer.toString(skore);
        }
        do {
            skoreString += " ";
        } while (skoreString.length() != 8);
        return meno + skoreString + naformatujCasMinuty(cas) + "  " + vyhra + obtiaznost;
    }

    /**
     * Naformatuje cislo, ktore reprezentuje sekundy do formátu
     * minúty:sekundy,zaokrúhlené na 2 desatinné miesta
     * 
     * @param sekundy Číslo na naformátovanie
     * @return Naformátovaný reťazec
     */
    public static String naformatujCasMinuty(double sekundy) {
        int minuty = 0;
        while (sekundy > 60) {
            sekundy -= 60;
            minuty++;
        }
        if (minuty > 99) {
            minuty = 99;
        }
        String sekundyString;
        if (sekundy < 10) {
            sekundyString = "0" + String.format("%.2f", sekundy);
        } else {
            sekundyString = String.format("%.2f", sekundy);
        }
        if (minuty > 9) {
            return minuty + ":" + sekundyString;
        } else if (minuty < 1) {
            return "00:" + sekundyString;
        }
        return "0" + minuty + ":" + sekundyString;
    }
}