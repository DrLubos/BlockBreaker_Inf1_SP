import java.util.Random;
import fri.shapesge.Stvorec;

public class PowerUp {
    private Random generator;
    private Hra hra;
    private Stvorec stvorec;
    private int posun;
    private int strana;
    private boolean generovanie;
    private int schopnost;
    private int x;
    private int y;
    private int cas;
    private boolean casovanie;
    private int dobaPredlzeniaPosuvnika;
    private ObtiaznostHry obtiaznost;

    /**
     * Konštruktor s dĺžkov strany štvorca, inštanciou hry a nastavenou obtiažnosťou
     * 
     * @param hra        Inštancia hry
     * @param strana     dĺžka strany štvorca, ktorý zobrazuje schopnosti
     * @param obtiaznost Zvolená obtiažnosť hry
     */
    public PowerUp(Hra hra, int strana, ObtiaznostHry obtiaznost) {
        this.dobaPredlzeniaPosuvnika = obtiaznost.getDobaPredlzeniaPosuvnika();
        this.generator = new Random();
        this.schopnost = -1;
        this.stvorec = null;
        this.hra = hra;
        this.posun = 15;
        this.strana = strana;
        this.generovanie = true;
        this.x = -50;
        this.y = -50;
        this.casovanie = false;
        this.cas = 0;
        this.obtiaznost = obtiaznost;
    }

    /**
     * Vygeneruje nahodné číslo a momentálne nepadá žiadna schopnosť, tak spúšťa
     * metódu 'vygeneruj'
     * 
     * @param x X pozicia vygenerovania štvorca
     * @param y Y pozicia vygenerovania štvorca
     */
    public void znicenyBlok(int x, int y) {
        if (this.generovanie) {
            this.generovanie = false;
            this.vygeneruj(x, y);
        }
    }

    /**
     * Vygemeruje číslo schopnosti a vykreslí štvorec na určenej pozícii
     * 
     * @param x X pozicia vygenerovania štvorca
     * @param y Y pozicia vygenerovania štvorca
     */
    private void vygeneruj(int x, int y) {
        this.stvorec = new Stvorec(x, y);
        this.stvorec.zmenStranu(this.strana);
        this.x = x - this.strana;
        this.y = y;
        this.schopnost = this.generator.nextInt(7);
        switch (this.schopnost) {
            case 1:
                this.stvorec.zmenFarbu("green");
                break;
            case 2:
                this.stvorec.zmenFarbu("blue");
                break;
            case 3:
                this.stvorec.zmenFarbu("red");
                break;
            case 4:
                if (this.obtiaznost == ObtiaznostHry.STREDNA || this.obtiaznost == ObtiaznostHry.TAZKA) {
                    this.stvorec.zmenFarbu("yellow");
                    break;
                }
                if (this.obtiaznost == ObtiaznostHry.LAHKA) {
                    this.stvorec.zmenFarbu("red");
                    break;
                }
                this.stvorec.zmenFarbu("black");
                break;
            default:
                if (this.obtiaznost == ObtiaznostHry.LAHKA) {
                    this.stvorec.zmenFarbu("red");
                    break;
                }
                this.stvorec.zmenFarbu("black");
                break;
        }
        this.stvorec.zobraz();
    }

    /**
     * Po uplinutí tiku posunie štvorec zo schopnosťou dolu
     */
    public void tik20() {
        if (this.stvorec != null) {
            for (int i = 0; i <= this.posun; i++) {
                this.kontrola();
                this.y++;
                this.stvorec.posunZvisle(1);
            }
        }
    }

    /**
     * Kontroluje či štvorec, ktorý reprezentuje schopnosť, bol zachytený posuvníkom
     */
    private void kontrola() {
        if (this.stvorec == null) {
            return;
        }
        if (this.y + this.strana == this.hra.getPosuvnik().getOsY()
                && this.x + this.strana < this.hra.getPosuvnik().getPoziciaL() + this.hra.getPosuvnik().getDlzka()
                && this.x > this.hra.getPosuvnik().getPoziciaL()) {
            this.aplikujSchopnost();
        }
        if (this.y > this.hra.getPosuvnik().getOsY() + 20) {
            this.stvorec.skry();
            this.schopnost = -1;
            this.generovanie = true;
        }
    }

    /**
     * Posiela triede Hra správu aby aplikovala určitú schopnosť
     */
    private void aplikujSchopnost() {
        switch (this.schopnost) {
            case 1:
                this.hra.zmenDlzkuPosuvnika();
                this.casovanie = true;
                this.cas = 0;
                break;
            case 2:
                for (int i = 0; i < 4; i++) {
                    this.hra.zvysSkore();
                }
                break;
            case 3:
                this.hra.pridajLoptu();
                break;
            case 4:
                if (this.obtiaznost == ObtiaznostHry.STREDNA || this.obtiaznost == ObtiaznostHry.TAZKA) {
                    this.hra.znizSkokLopt();
                    break;
                }
                if (this.obtiaznost == ObtiaznostHry.LAHKA) {
                    this.hra.pridajLoptu();
                    break;
                }
                this.hra.zvysSkokLopt();
                break;
            default:
                if (this.obtiaznost == ObtiaznostHry.LAHKA) {
                    this.hra.pridajLoptu();
                }
                this.hra.zvysSkokLopt();         
                break;
        }
        if (this.stvorec != null) {
            this.stvorec.skry();
        }
        this.generovanie = true;
    }

    /**
     * Vykonáva tik každých 500ms a po uplinutí určitej doby (podľa obtiažnosti hry)
     * pošle správu na
     * zresetovanie posuvníka
     */
    public void tik500() {
        if (!this.casovanie) {
            return;
        }
        this.cas++;
        if (this.cas % this.dobaPredlzeniaPosuvnika == 0) {
            this.hra.zresetujPosuvnik();
            this.casovanie = false;
            this.cas = 0;
        }
    }

    /**
     * Metóda vracia, či momentálne padá hocijaká schopnosť
     * 
     * @return Vracia false, keď padá schopnosť, inak true (môže sa vygenerovať
     *         schopnosť)
     */
    public boolean getGenerovanie() {
        return this.generovanie;
    }

    /**
     * Metóda vracia, či padá užitočná schopnosť
     * 
     * @return Vracia, či padá užitočná schopnosť
     */
    public boolean padaUzitocne() {
        if (this.schopnost == 2) {
            return true;
        }
        return false;
    }

    /**
     * Metóda slúži na skrytie štvorca
     */
    public void skryVsetko() {
        if (this.stvorec != null) {
            this.stvorec.skry();
        }
    }
}