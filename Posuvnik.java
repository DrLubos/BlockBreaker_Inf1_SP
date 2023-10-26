import fri.shapesge.Obdlznik;

public class Posuvnik {
    private Obdlznik posuvnik;
    private int posuvnikLHX;
    private int dlzka;
    private int osY;
    private int vyskaPosuvnika;
    private int velkostPosunu;
    private int dlzkaOkna;
    private boolean prebiehaZmena;
    private int novaDlzka;

    /**
     * Vytvára posuvník na určenom mieste
     * 
     * @param osY            Vzdialenosť od začiatku okna, po začiatok posuvníka
     * @param posuvnikLHX    Ľavá horná X súradnica posuvníka
     * @param dlzkaPosuvnika Dĺžka posuvníka
     * @param dlzkaOkna      Dĺžka okna
     */
    public Posuvnik(int osY, int posuvnikLHX, int dlzkaPosuvnika, int dlzkaOkna) {
        this.posuvnikLHX = posuvnikLHX;
        this.dlzka = dlzkaPosuvnika;
        this.osY = osY;
        this.vyskaPosuvnika = 15;
        this.velkostPosunu = this.dlzka / 3;
        this.posuvnik = new Obdlznik(this.posuvnikLHX, this.osY);
        this.posuvnik.zmenStrany(this.dlzka, this.vyskaPosuvnika);
        this.posuvnik.zmenFarbu("black");
        this.posuvnik.zobraz();
        this.dlzkaOkna = dlzkaOkna;
        this.prebiehaZmena = false;
        this.novaDlzka = 0;
    }

    /**
     * Zistí či je dĺžka menšia alebo väčšia ako je aktuálna, a potom zavolá
     * príslišné metódy na zmenenie dĺžky a posunutie posuvníka
     * 
     * @param zadanaDlzka Dĺžka na ktorú sa má posuvník zmeniť
     */
    public void zmenDlzku(int zadanaDlzka) {
        if (this.dlzka < zadanaDlzka) {
            int dlzkaPosunu = (zadanaDlzka - this.dlzka) / 2;
            if (this.posuvnikLHX - dlzkaPosunu >= 0
                    && this.posuvnikLHX + this.dlzka + dlzkaPosunu <= this.dlzkaOkna) {
                this.posuvnik.posunVodorovne(-dlzkaPosunu);
                this.posuvnikLHX -= dlzkaPosunu;
            } else if (this.posuvnikLHX + this.dlzka + dlzkaPosunu <= this.dlzkaOkna) {
                this.posunVlavo(dlzkaPosunu);
            } else {
                int posunutieO = 0;
                int pomocnyVypocet = this.posuvnikLHX + this.dlzka + dlzkaPosunu;
                while (pomocnyVypocet != this.dlzkaOkna) {
                    posunutieO++;
                    pomocnyVypocet--;
                }
                this.posunVlavo(posunutieO + dlzkaPosunu);
            }
            this.posuvnik.zmenStrany(zadanaDlzka, this.vyskaPosuvnika);
            this.dlzka = zadanaDlzka;
            this.prebiehaZmena = false;
        }
        if (this.dlzka > zadanaDlzka) {
            this.prebiehaZmena = true;
            this.novaDlzka = zadanaDlzka;
        }
    }

    /**
     * Použité pre plynulé zmenšenie posuvníka, pričom ho posúva aj doprava, aby to
     * vyzeralo, že sa skracuje z oboch strán
     */
    public void tik20() {
        if (this.novaDlzka < this.dlzka && prebiehaZmena) {
            this.dlzka -= 4;
            this.posuvnik.zmenStrany(this.dlzka, this.vyskaPosuvnika);
            this.posunVpravo(1);
            if (this.novaDlzka >= this.dlzka) {
                this.prebiehaZmena = false;
            }
        }
    }

    /**
     * Posunie posuvnik doprava o veľkosť premennej 'this.velkostPosunu', pričom
     * neprejde mimo okno
     */
    public void posunVpravo() {
        for (int i = 0; i <= this.velkostPosunu; i++) {
            if (this.posuvnikLHX + this.dlzka < this.dlzkaOkna) {
                this.posuvnikLHX++;
                this.posuvnik.posunVodorovne(1);
            } else {
                return;
            }
        }
    }

    /**
     * Posunie posuvnik doľava o veľkosť premennej 'this.velkostPosunu', pričom
     * neprejde mimo okno
     */
    public void posunVlavo() {
        for (int i = 0; i <= this.velkostPosunu; i++) {
            if (this.posuvnikLHX > 0) {
                this.posuvnikLHX--;
                this.posuvnik.posunVodorovne(-1);
            } else {
                return;
            }
        }
    }

    /**
     * Posúva posuvník vpravo o zadanú dĺžku doprava, pričom neprejde za veľkosť
     * okna, použité pri zväčšovaní posuvníka
     * 
     * @param velkost Veľkosť posunu doľava
     */
    private void posunVlavo(int velkost) {
        for (int i = 0; i <= velkost; i++) {
            if (this.posuvnikLHX > 0) {
                this.posuvnikLHX--;
                this.posuvnik.posunVodorovne(-1);
            } else {
                return;
            }
        }
    }

    /**
     * Posúva posuvník vpravo o zadanú dĺžku doprava, pričom neprejde za veľkosť
     * okna
     * 
     * @param velkost Veľkosť posunu doprava
     */
    private void posunVpravo(int velkost) {
        for (int i = 0; i <= velkost; i++) {
            if (this.posuvnikLHX + this.dlzka < this.dlzkaOkna) {
                this.posuvnikLHX++;
                this.posuvnik.posunVodorovne(1);
            } else {
                return;
            }
        }
    }

    /**
     * Vracia X súradnicu posuvníka
     * 
     * @return X súradnica posuvníka
     */
    public int getPoziciaL() {
        return this.posuvnikLHX;
    }

    /**
     * Vracia aktuálnu dĺžku posuvníka
     * 
     * @return Aktuálna dĺžku posuvníka
     */
    public int getDlzka() {
        return this.dlzka;
    }

    /**
     * Vracia Y súradnicu ľavého horného rohu
     * 
     * @return Y súradnica ľavého horného rohu
     */
    public int getOsY() {
        return this.osY;
    }

    /**
     * Skryje posuvník
     */
    public void skryVsetko() {
        this.posuvnik.skry();
    }
}