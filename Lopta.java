import java.util.Random;
import fri.shapesge.Stvorec;

public class Lopta {
    private Stvorec lopta;
    private int loptaLHX;
    private int loptaLHY;
    private int dlzka;
    private boolean padanie;
    private boolean doprava;
    private int skok;
    private Posuvnik posuvnik;
    private Mapa mapa;
    private Random generator;
    private int dlzkaOkna;

    /**
     * Konštruktor vytvára lopty, s definovaným posuvníkom a definovanou mapou.
     * Definuje sa aj pozícia vykreslenia lopty a jej dĺžka, a aj dĺžka okna pre
     * detekciu kolízie
     * 
     * @param posuvnik   Posuvník na kontrolu kolízie
     * @param mapa       Mapa na
     * @param loptaLHX   Pozícia vygenerovania lopty na osi X
     * @param loptaLHY   Pozícia vygenerovania lopty na osi Y
     * @param dlzkaLopty Dĺžka lopty
     * @param dlzkaOkna  Dĺžka okna
     */
    public Lopta(Posuvnik posuvnik, Mapa mapa, int loptaLHX, int loptaLHY, int dlzkaLopty, int dlzkaOkna) {
        this.dlzka = dlzkaLopty;
        this.loptaLHX = loptaLHX;
        this.loptaLHY = loptaLHY;
        this.posuvnik = posuvnik;
        this.lopta = new Stvorec(this.loptaLHX, this.loptaLHY);
        this.lopta.zmenStranu(this.dlzka);
        this.lopta.zmenFarbu("magenta");
        this.lopta.zobraz();
        this.padanie = true;
        this.generator = new Random();
        if (this.generator.nextInt(2) == 1) {
            this.doprava = true;
        } else {
            this.doprava = false;
        }
        this.skok = 4;
        this.mapa = mapa;
        this.dlzkaOkna = dlzkaOkna;
    }

    /**
     * Vykonáva pohyb s loptou, využíva iné metódy na pohyb lopty a kontrolu kolízie
     */
    public void tikMain() {
        for (int i = 0; i <= this.skok; i++) {
            this.padaj();
            this.kontrolyLopta();
            this.odboc();
        }
    }

    /**
     * Zmení smer pohybu lopty po osi Y
     */
    public void zmenPadanie() {
        this.padanie = !this.padanie;
        if (this.loptaLHY <= 0) {
            this.padanie = true;
        }
    }

    /**
     * Zmení smer pohybu lopty po osi X
     */
    public void zmenDoprava() {
        this.doprava = !this.doprava;
    }

    /**
     * Metóda posúva loptu po osi Y a spušťa metódy na zistenie kolízie, vrátane
     * kolízie s mapou
     */
    private void padaj() {
        if (this.padanie) {
            this.loptaLHY += 1;
            this.lopta.posunZvisle(1);
            this.mapa.kontrola();
        } else {
            this.loptaLHY -= 1;
            this.lopta.posunZvisle(-1);
            this.mapa.kontrola();
        }
    }

    /**
     * Metóda posúva loptu po osi X a spušťa metódy na zistenie kolízie, vrátane
     * kolízie s mapou
     */
    private void odboc() {
        if (this.doprava) {
            this.loptaLHX += 1;
            this.lopta.posunVodorovne(1);
            this.mapa.kontrola();
        } else {
            this.loptaLHX -= 1;
            this.lopta.posunVodorovne(-1);
            this.mapa.kontrola();
        }
    }

    /**
     * Kontroluje kolízie lopty s veľkosťou okna a posuvníka
     */
    private void kontrolyLopta() {
        if ((this.loptaLHY + this.dlzka >= this.posuvnik.getOsY()
                && this.loptaLHY + this.dlzka <= this.posuvnik.getOsY() + 3)
                && this.loptaLHX < this.posuvnik.getPoziciaL() + this.posuvnik.getDlzka()
                && this.loptaLHX + this.dlzka > this.posuvnik.getPoziciaL()) {
            this.padanie = false;
        }
        if (this.loptaLHY <= 0) {
            this.padanie = true;
        }
        if (this.loptaLHX + this.dlzka >= dlzkaOkna) {
            this.doprava = false;
        }
        if (this.loptaLHX <= 0) {
            this.doprava = true;
        }
    }

    /**
     * Metóda skryje loptu
     */
    public void skryLoptu() {
        this.lopta.skry();
    }

    /**
     * Metóda vracia pozíciu X lopty
     * 
     * @return Vracia pozíciu X lopty
     */
    public int getLoptaLHX() {
        return this.loptaLHX;
    }

    /**
     * Metóda vracia pozíciu Y lopty
     * 
     * @return Vracia pozíciu Y lopty
     */
    public int getLoptaLHY() {
        return this.loptaLHY;
    }

    /**
     * Metóda vracia dĺžku lopty
     * 
     * @return Vracia dĺžku lopty
     */
    public int getLoptaDlzka() {
        return this.dlzka;
    }

    /**
     * Zvyšuje veľkosť skoku lopty. Vzdielenosť o ktorú sa lopta posúva
     */
    public void zvysSkok() {
        this.skok++;
    }

    /**
     * Znižuje veľkosť skoku lopty. Vzdielenosť o ktorú sa lopta posúva, minimĺna vzdialenosť je 1
     */
    public void znizSkok() {
        if (this.skok > 1) {
            this.skok--;
        }
    }
}