import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {
    private static int dlzkaOkna;
    private static int vyskaOkna;
    private static String menoHraca;
    private static final ArrayList<String> SBGE = new ArrayList<String>();
    private static final File SUBOR = new File("sbge.ini");
    private static final String[] DEFAULT_NASTAVENIA = {"[Window]", "Width = 800", "Height = 600",
                                                        "Title = BlockBreaker, Hrac: Neznamy", "Background = #5F5F5F", "FPS = 100", "ShowInfo = false",
                                                        "Fullscreen = false", "ExitOnClose = true", "[Timers]", "tik500 = 500", "tikMain = 10",
                                                        "tik20 = 20" };
    /**
     * Súkromný konštruktor, aby sa nedal vytvoriť prádny objekt
     */
    private Main() {
    }
    /**
     * Metóda sa spúšťa hneď, pričom nastaví atribúty dlzkaOkna a vyskaOkna podľa
     * súboru sbge.ini a zároveň upraví súbor sbge.ini, tak aby sa upravilo
     * meno hráča, ktoré sa bude zapisovať po dohratí hry do súboru skore.txt. Počká
     * sekundu a spustí metódu na spustenie hry. Metóda aj kontroluje či sa existuje
     * súbor sbge.ini, ktorý je potrebný pre hranie hry a či má dostatočný počet
     * riadkov, ak neexistuje, tak sa spustí metóda, ktorá sa ho pokúsi vytvoriť
     * Kód na počkanie vykonávania metódy prevzatý z internetu. Autor: Anju Aravind.
     * Zdroj:
     * https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
     * 
     * @param args bez parametrov
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        menoHraca = JOptionPane.showInputDialog(null, "Zadajte meno");
        if (menoHraca == null) {
            return;
        }
        // Zobrazenie okna na vyberanie som použil z internetu.
        // Autor: Hovercraft Full Of Eels
        // Zdroj:
        // https://stackoverflow.com/questions/21957696/how-to-use-joptionpane-with-many-options-java
        int option = JOptionPane.showOptionDialog(null, "Vyberte obtiaznost", "BlockBreaker",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                ObtiaznostHry.values(), ObtiaznostHry.values()[0]);
        ObtiaznostHry[] moznosti = ObtiaznostHry.values();
        if (option < 0 || option >= moznosti.length) {
            return;
        }
        if (menoHraca.equals("")) {
            menoHraca = "Neznamy";
        }
        // kontrola či meno obsahuje číslo, pre to aby sa zamedzilo možným chybám pri
        // zápise skore do súboru
        for (int i = 0; i < menoHraca.length(); i++) {
            if (Character.isDigit(menoHraca.charAt(i))) {
                menoHraca = "Neznamy";
                break;
            }
        }
        if (!SUBOR.exists() || !SUBOR.canRead()) {
            if (!opravaSuboru()) {
                return;
            }
        }
        Scanner citac = new Scanner(SUBOR);
        while (citac.hasNextLine()) {
            SBGE.add(citac.nextLine());
        }
        citac.close();
        if (SBGE.size() < DEFAULT_NASTAVENIA.length) {
            if (!opravaSuboru()) {
                return;
            }
        }
        SBGE.clear();
        Scanner citacDruhy = new Scanner(SUBOR);
        while (citacDruhy.hasNextLine()) {
            SBGE.add(citacDruhy.nextLine());
        }
        citacDruhy.close();
        String width = "Width = ";
        for (int i = 0; i < SBGE.size(); i++) {
            String dlzka = SBGE.get(i);
            if (dlzka.length() >= width.length()) {
                dlzka = dlzka.substring(0, width.length());
                if (dlzka.equals(width)) {
                    String dlzkaKod = SBGE.get(i).substring(width.length()).trim();
                    for (int j = 0; j < dlzkaKod.length(); j++) {
                        if (!Character.isDigit(dlzkaKod.charAt(j))) {
                            if (opravaSuboru()) {
                                JOptionPane.showMessageDialog(null,
                                        "Súbor sbge.ini bol prepísaný reštartujte hru",
                                        "Potrebný reštart",
                                        1);
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                    dlzkaOkna = Integer.parseInt(dlzkaKod);
                }
            }
        }
        String height = "Height = ";
        for (int i = 0; i < SBGE.size(); i++) {
            String vyska = SBGE.get(i);
            if (vyska.length() >= height.length()) {
                vyska = vyska.substring(0, height.length());
                if (vyska.equals(height)) {
                    String vyskaKod = SBGE.get(i).substring(height.length()).trim();
                    for (int j = 0; j < vyskaKod.length(); j++) {
                        if (!Character.isDigit(vyskaKod.charAt(j))) {
                            if (opravaSuboru()) {
                                JOptionPane.showMessageDialog(null,
                                        "Súbor sbge.ini bol prepísaný reštartujte hru",
                                        "Potrebný reštart",
                                        1);
                                return;
                            } else {
                                return;
                            }
                        }
                    }
                    vyskaOkna = Integer.parseInt(vyskaKod);
                }
            }
        }
        boolean zapisaneMenoTitle = false;
        String player = "Title = BlockBreaker, Hrac: ";
        for (int i = 0; i < SBGE.size(); i++) {
            String hrac = SBGE.get(i);
            if (hrac.length() >= player.length()) {
                hrac = hrac.substring(0, player.length());
                if (hrac.equals(player)) {
                    SBGE.add(i, hrac + menoHraca);
                    SBGE.remove(i + 1);
                    zapisaneMenoTitle = true;
                }
            }
        }
        if (!zapisaneMenoTitle) {
            SBGE.add(3, player + menoHraca);
        }
        PrintWriter zapisovac = new PrintWriter(SUBOR);
        for (String s : SBGE) {
            zapisovac.println(s);
        }
        zapisovac.close();
        // Nasledujúci riadok kódu prevzatý z internetu, Autor: Anju Aravind. Zdroj:
        // https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
        Thread.sleep(1000);
        Hra.getInstanciaHra(dlzkaOkna, vyskaOkna, menoHraca, moznosti[option]);
    }

    /**
     * Vyhodí okno s chybou, nie je možné spustiť hru, skontrolujte sbge.ini súbor
     */
    private static void nemoznoSpustit() {
        JOptionPane.showMessageDialog(null, "Nie je možné spustiť hru, skontrolujte sbge.ini súbor",
                "Nemožno spustiť hru",
                0);
    }

    /**
     * Opýta sa užívateľa či sa má pokúsiť opraviť súbor, ak áno tak sa pokúsi o
     * opravu súboru sbge.ini a na zápis základných nastavení do neho. Ak súbor
     * existuje, tak ho najskôr odstráni a zapíše do neho základné
     * nastavenia
     * 
     * @return Vráti true ak sa subor podatilo vytvoriť a zapísať do ňho pôvodné
     *         nastavenia, inak vráti false
     */
    public static boolean opravaSuboru() throws IOException {
        int moznost = JOptionPane.showConfirmDialog(null,
                "Súbor 'sbge.ini' neexistuje, nedá sa čítať alebo sa v ňom našli chyby. Chcete skúsiť vymazať a znovu vytvoriť súbor s pôvodným nastavením?");
        if (moznost == 0) {
            if (SUBOR.exists()) {
                SUBOR.delete();
            }
            SUBOR.createNewFile();
            PrintWriter zapisovac = new PrintWriter(SUBOR);
            for (int i = 0; i < DEFAULT_NASTAVENIA.length; i++) {
                zapisovac.println(DEFAULT_NASTAVENIA[i]);
            }
            zapisovac.close();
            return true;
        }
        nemoznoSpustit();
        return false;
    }
}