import java.util.ArrayList;
import javax.swing.JOptionPane;
import fri.shapesge.Manazer;
import fri.shapesge.Text;
import fri.shapesge.FontStyle;
import fri.shapesge.Obdlznik;

public class Hra {
    private String meno;
    private int dlzkaOkna;
    private int vyskaOkna;
    private ArrayList<Lopta> listLopt;
    private Manazer manazer;
    private int dlzkaPosuvnika;
    private Posuvnik posuvnik;
    private Mapa mapa;
    private PowerUp powerUp;
    private Text vypisSkore;
    private int skore;
    private Obdlznik[] pauzaObdlznik;
    private double cas;
    private static Hra instanciaHry;
    private StavHry stavHry;
    private ObtiaznostHry obtiaznost;

    /**
     * Konštruktor, ktorý vytvára hru, ako vstupné parametre sú dĺžka, výška okna a
     * meno hráča, pošlú sa potrebné správy na vytvorenie inštancií, stav hry sa
     * nastaví na HRA_SA a pošle sa zároveň správa na pozastavenie hry.
     * 
     * @param dlzkaOkna  Dĺžka okna obrazovky
     * @param vyskaOkna  Výška okna obrazovky
     * @param meno       Meno hráča
     * @param obtiaznost Nastavená obtiažnosť hry
     */
    private Hra(int dlzkaOkna, int vyskaOkna, String meno, ObtiaznostHry obtiaznost) {
        this.meno = meno;
        this.dlzkaOkna = dlzkaOkna;
        this.vyskaOkna = vyskaOkna;
        this.obtiaznost = obtiaznost;
        this.listLopt = new ArrayList<Lopta>();
        this.manazer = new Manazer();
        this.dlzkaPosuvnika = this.dlzkaOkna / 2 - 100;
        this.posuvnik = new Posuvnik(this.vyskaOkna - 50, (this.dlzkaOkna - this.dlzkaPosuvnika) / 2,
                this.dlzkaPosuvnika, this.dlzkaOkna);
        int dlzkaObdlznika = dlzkaOkna / 10;
        if (dlzkaObdlznika > 100) {
            dlzkaObdlznika = 100;
        }
        int vyskaObdlznika = vyskaOkna / 10 - 10;
        if (vyskaObdlznika > 75) {
            vyskaObdlznika = 75;
        }
        int medzeraObdlznika = dlzkaObdlznika / 15;
        if (medzeraObdlznika < 5) {
            medzeraObdlznika = 5;
        }
        if (medzeraObdlznika > 10) {
            medzeraObdlznika = 10;
        }
        int pocetRiadkov = 0;
        int pomocnyVypocet = this.vyskaOkna - 320;
        do {
            pomocnyVypocet -= (vyskaObdlznika + medzeraObdlznika);
            pocetRiadkov++;
        } while (pomocnyVypocet > 0);
        if (pomocnyVypocet < 0) {
            pocetRiadkov--;
        }
        if (pocetRiadkov > 7) {
            pocetRiadkov = 7;
        }
        int pocetStlpcov = 0;
        pomocnyVypocet = this.dlzkaOkna;
        do {
            pomocnyVypocet -= (dlzkaObdlznika + medzeraObdlznika);
            pocetStlpcov++;
        } while (pomocnyVypocet > 0);
        if (pomocnyVypocet < -30) {
            pocetStlpcov--;
        }
        this.mapa = new Mapa(this, pocetRiadkov, pocetStlpcov, dlzkaObdlznika, vyskaObdlznika, medzeraObdlznika);
        for (int i = 0; i < this.obtiaznost.getPocetStartovacichLopt(); i++) {
            this.pridajLoptu();
        }
        this.manazer.spravujObjekt(this.posuvnik);
        this.manazer.spravujObjekt(this);
        this.powerUp = new PowerUp(this, 20, this.obtiaznost);
        this.manazer.spravujObjekt(this.powerUp);
        this.vypisSkore = new Text("Skore: ", 5, 25);
        this.skore = 0;
        this.pauzaObdlznik = new Obdlznik[2];
        this.cas = 0;
        this.stavHry = StavHry.HRA_SA;
        this.zrus();
    }

    /**
     * Zistí či existuje inštancia hry, ak nie tak ju vytvorí a vracia inštanciu
     * hry.
     * 
     * @param dlzkaOkna  Dĺžka okna obrazovky
     * @param vyskaOkna  Výška okna obrazovky
     * @param meno       Meno hráča
     * @param obtiaznost Nastavená obtiažnosť hry
     * @return Inštancia hry
     */
    public static Hra getInstanciaHra(int dlzkaOkna, int vyskaOkna, String meno, ObtiaznostHry obtiaznost) {
        if (Hra.instanciaHry == null) {
            Hra.instanciaHry = new Hra(dlzkaOkna, vyskaOkna, meno, obtiaznost);
        }
        return Hra.instanciaHry;
    }

    /**
     * Ak sa hrá, tak počíta čas hry, kontrolujú sa lopty a vypisuje skóre, zároveň
     * sa kontroluje či existuje dáky nezničený obdĺžnik v Mape, ak nie tak sa
     * kontroluje, či nepadá užitočná schopnosť (nastavené iba na schopnosť, ktorá
     * pridáva skóre). Ak schopnosť nepadá, tak je stavhry VYHRA a na obrazovke sa
     * zobrazí výsledok hry, čo zabezpečuje trieda VysledokHry, prestanú sa
     * spravovať objekty a pošle sa správa na zápis skóre. Ak je inštancia null, tak
     * sa pošle správa na skrytie všetých objektov.
     */
    public void tikMain() {
        if (this.stavHry == StavHry.HRA_SA) {
            this.cas += 0.01;
            this.kontrolaLopt();
            this.vypisujSkore();
            if (!this.mapa.existuje()) {
                if (!this.powerUp.padaUzitocne()) {
                    this.prestanSpravovat();
                    this.stavHry = StavHry.VYHRA;
                    VysledokHry.zobrazVysledok(this.stavHry.toString(), this.cas, this.dlzkaOkna, this.vyskaOkna, this.meno, this.skore, this.obtiaznost.toString());
                }
            }
        }
        if (Hra.instanciaHry == null) {
            this.skryVsetko();
        }
    }

    /**
     * Kontroluje či nejaká lopta nie je na osi Y vzdialená ďalej ako je posuvník,
     * ak áno, tak ju skryje a vymaže s ArrayListu. Ak je list prázdny, tak sa
     * kontroluje, či nepadá dáka schopnosť z triedy PowerUp, ak nie tak je hra
     * prehratá a posiela sa správa na prestanie spravovania objektov a zápis skóre.
     * Ak je obtiažnosť NEKONECNO, tak namiesto zmeny satvu hry na PREHRA sa
     * vygeneruje automaticky lopta, ak je ArrayList listlopt prázdny
     */
    private void kontrolaLopt() {
        if (this.listLopt.size() > 0) {
            for (Lopta loptaFor : this.listLopt) {
                if (loptaFor.getLoptaLHY() > this.posuvnik.getOsY()) {
                    loptaFor.skryLoptu();
                    this.listLopt.remove(loptaFor);
                    break;
                }
            }
        } else {
            if (this.powerUp.getGenerovanie() && this.mapa.existuje() && this.obtiaznost != ObtiaznostHry.NEKONECNA) {
                this.prestanSpravovat();
                this.stavHry = StavHry.PREHRA;
                VysledokHry.zobrazVysledok(this.stavHry.toString(), this.cas, this.dlzkaOkna, this.vyskaOkna, this.meno, this.skore, this.obtiaznost.toString());
            }
            if (this.obtiaznost == ObtiaznostHry.NEKONECNA && this.listLopt.isEmpty()) {
                this.pridajLoptu();
            }
        }
    }

    /**
     * Vypisuje skóre na obrazovku počas hry
     */
    private void vypisujSkore() {
        this.vypisSkore.changeText("Skore: " + this.skore);
        this.vypisSkore.zmenFont("Skore: " + this.skore, FontStyle.PLAIN, 20);
        this.vypisSkore.zobraz();
    }

    /**
     * Ak je hra v stave VYHRA alebo PREHRA, tak sa atribút Hra.instanciaHry nastavý
     * na null, pošle správa na vymazanie objektov a pošle sa aj správa na spustenie
     * novej hry. Ak je obtiažnosť nastavená na NEKONECNA, tak sa po stalačení Enter
     * alebo Medzerník vytvorí nová lopta. Ak je stav hry PAUZA, tak sa zobrazí okno
     * na výber možností obtiažnosti a ak užívateľ vyberie obtiažnosť vytvorí sa
     * nová hra
     */
    public void aktivuj() {
        if (this.stavHry == StavHry.HRA_SA && this.obtiaznost == ObtiaznostHry.NEKONECNA && this.listLopt.size() < 25) {
            this.pridajLoptu();
            return;
        }
        if (this.stavHry == StavHry.PREHRA || this.stavHry == StavHry.VYHRA) {
            Hra.instanciaHry = null;
            this.skryVsetko();
            getInstanciaHra(this.dlzkaOkna, this.vyskaOkna, this.meno, this.obtiaznost);
        }
        if (this.stavHry == StavHry.PAUZA) {
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
            Hra.instanciaHry = null;
            this.skryVsetko();
            getInstanciaHra(this.dlzkaOkna, this.vyskaOkna, this.meno, moznosti[option]);
        }
    }

    /**
     * Skryje všetky objekty na obrazovke a pripravý plátno na novú hru
     */
    private void skryVsetko() {
        if (Hra.instanciaHry == null) {
            this.manazer.prestanSpravovatObjekt(this);
            this.manazer.prestanSpravovatObjekt(this.powerUp);
            this.manazer.prestanSpravovatObjekt(this.posuvnik);
            if (this.listLopt.size() > 0) {
                for (Lopta loptaFor : this.listLopt) {
                    this.manazer.prestanSpravovatObjekt(loptaFor);
                    loptaFor.skryLoptu();
                }
            }
            this.skryPauza();
            this.mapa.skryVsetko();
            this.powerUp.skryVsetko();
            this.posuvnik.skryVsetko();
            this.vypisSkore.skry();
            VysledokHry.skryVysledok();
        }
    }

    /**
     * Po stlačení Esc sa spustia metódy, ktoré pozastavia hru a zobrazia
     * signalizáciu na obrazovku, v prípade že je hra pozastavená, tak sa spustia
     * metódy na pokračovanie v hre. Ak jsa hra nehrá, tak sa ukončí program.
     */
    public void zrus() {
        if (this.stavHry == StavHry.HRA_SA) {
            this.prestanSpravovat();
            this.zobrazPauza();
            this.stavHry = StavHry.PAUZA;
            return;
        }
        if (this.stavHry == StavHry.PAUZA) {
            this.skryPauza();
            this.zacniSpravovat();
            this.stavHry = StavHry.HRA_SA;
            return;
        }
        System.exit(0);
    }

    /**
     * Prestaňe spravovať objekty, tým pádom pozastaví hru
     */
    private void prestanSpravovat() {
        this.manazer.prestanSpravovatObjekt(this.posuvnik);
        this.manazer.prestanSpravovatObjekt(this.powerUp);
        for (Lopta loptaFor : this.listLopt) {
            this.manazer.prestanSpravovatObjekt(loptaFor);
        }
    }

    /**
     * Začne spravovať všetky objekty, tým pádom sa môže pokračovať v hre
     */
    private void zacniSpravovat() {
        this.manazer.spravujObjekt(this.posuvnik);
        this.manazer.spravujObjekt(this.powerUp);
        for (Lopta loptaFor : this.listLopt) {
            this.manazer.spravujObjekt(loptaFor);
        }
    }

    /**
     * Zobrazí 2 obdĺžniki na obrazovke ako signalizáciu že je pozastavená hra
     */
    private void zobrazPauza() {
        int sirka = this.dlzkaOkna / 10;
        int medzera = 50;
        this.pauzaObdlznik[0] = new Obdlznik(this.dlzkaOkna / 2 - medzera - sirka / 2, 50);
        this.pauzaObdlznik[1] = new Obdlznik(this.dlzkaOkna / 2 + sirka / 2, 50);
        this.pauzaObdlznik[0].zmenStrany(sirka, this.vyskaOkna - 200);
        this.pauzaObdlznik[1].zmenStrany(sirka, this.vyskaOkna - 200);
        this.pauzaObdlznik[0].zmenFarbu("black");
        this.pauzaObdlznik[1].zmenFarbu("black");
        this.pauzaObdlznik[0].zobraz();
        this.pauzaObdlznik[1].zobraz();
    }

    /**
     * Skryje signalizáciu pauzy v podobe dvoch obdĺžnikov
     */
    private void skryPauza() {
        if (this.pauzaObdlznik[0] != null && this.pauzaObdlznik[1] != null) {
            this.pauzaObdlznik[0].skry();
            this.pauzaObdlznik[1].skry();
        }
    }

    /**
     * Ak je nastavená obtiažnosť na 'NEKONECNA' umožnuje užívateľovy zvýšiť skok
     * lôpt šípkou hore
     */
    public void posunHore() {
        if (this.obtiaznost == ObtiaznostHry.NEKONECNA) {
            this.zvysSkokLopt();
        }
    }

    /**
     * Ak je nastavená obtiažnosť na 'NEKONECNA' umožnuje užívateľovy znížiť skok
     * lôpt šípkou dole
     */
    public void posunDole() {
        if (this.obtiaznost == ObtiaznostHry.NEKONECNA) {
            this.znizSkokLopt();
        }
    }

    // PowerUP

    /**
     * Predlžuje posuvník o určitú hodnotu
     */
    public void zmenDlzkuPosuvnika() {
        this.posuvnik.zmenDlzku(this.dlzkaPosuvnika + 150);
    }

    /**
     * Resetuje dĺžku posuvníka na defaultnú hodnotu
     */
    public void zresetujPosuvnik() {
        this.posuvnik.zmenDlzku(this.dlzkaPosuvnika);
    }

    /**
     * Zvyšuje skóre o 5
     */
    public void zvysSkore() {
        this.skore += 5;
    }

    /**
     * Zvyšuje rýchlosť existujúcih lôpt
     */
    public void zvysSkokLopt() {
        if (this.listLopt.isEmpty()) {
            return;
        }
        for (Lopta l : this.listLopt) {
            l.zvysSkok();
        }
    }

    /**
     * Znižuje rýchlosť existujúcih lôpt
     */
    public void znizSkokLopt() {
        if (this.listLopt.isEmpty()) {
            return;
        }
        for (Lopta l : this.listLopt) {
            l.znizSkok();
        }
    }

    /**
     * Vytvára loptu, začne ju spravovať manažér a pridáva do ArrayListu listLopt,
     * podľa obtiažnosti sa upravuje aj jej rýchlosť
     */
    public void pridajLoptu() {
        Lopta novaLopta;
        novaLopta = new Lopta(this.posuvnik, this.mapa,
                (this.posuvnik.getPoziciaL() + this.posuvnik.getPoziciaL() + this.posuvnik.getDlzka()) / 2,
                this.posuvnik.getOsY() - 100, 20, this.dlzkaOkna);
        for (int i = 0; i < this.obtiaznost.getPocetZrychleni(); i++) {
            novaLopta.zvysSkok();
        }
        this.manazer.spravujObjekt(novaLopta);
        this.listLopt.add(novaLopta);
    }

    // Gettere

    /**
     * Vracia inštanciu triedy Posuvnik
     * 
     * @return inštanciu triedy Posuvnik
     */
    public Posuvnik getPosuvnik() {
        return this.posuvnik;
    }

    /**
     * Vracia inštanciu triedy PowerUp
     * 
     * @return inštanciu triedy PowerUp
     */
    public PowerUp getPowerUp() {
        return this.powerUp;
    }

    /**
     * Vracia ArrayList lôpt
     * 
     * @return list lôpt
     */
    public ArrayList<Lopta> getListLopt() {
        return this.listLopt;
    }
}