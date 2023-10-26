import fri.shapesge.Obdlznik;

public class Mapa {
    private Hra hra;
    private Obdlznik[][] mapa;
    private int dlzka;
    private int vyska;
    private int medzera;
    private boolean[][] zivoty;
    private int[][] blokLHX;
    private int[][] blokLHY;

    /**
     * Konštruktor vytvorí mapu s m riadkov a n stĺpcov, definuje sa aj dĺžka a
     * výška obdĺžnika ako aj medzera medzi nimi.
     * 
     * @param hra     Inštancia hry
     * @param m       Počet riadkov obdĺžnikov
     * @param n       Počet stĺpcov obdĺžnikov
     * @param dlzka   Dĺžka strany X obdĺžnika
     * @param vyska   Dĺžka strany Y obdĺžnika
     * @param medzera Medzera medzi obdĺžnikmi
     */
    public Mapa(Hra hra, int m, int n, int dlzka, int vyska, int medzera) {
        this.hra = hra;
        this.dlzka = dlzka;
        this.vyska = vyska;
        this.medzera = medzera;
        this.mapa = new Obdlznik[m][];
        this.zivoty = new boolean[m][];
        this.blokLHX = new int[m][];
        this.blokLHY = new int[m][];
        for (int i = 0; i < this.mapa.length; i++) {
            this.mapa[i] = new Obdlznik[n];
            this.zivoty[i] = new boolean[n];
            this.blokLHX[i] = new int[n];
            this.blokLHY[i] = new int[n];
            for (int j = 0; j < this.mapa[i].length; j++) {
                this.zivoty[i][j] = true;
                this.blokLHX[i][j] = 5 + (j * (this.dlzka + this.medzera));
                this.blokLHY[i][j] = 5 + (i * (this.vyska + this.medzera));
                this.mapa[i][j] = new Obdlznik(this.blokLHX[i][j], this.blokLHY[i][j]);
                this.mapa[i][j].zmenStrany(this.dlzka, this.vyska);
                this.mapa[i][j].zmenFarbu("yellow");
                this.mapa[i][j].zobraz();
            }
        }
    }

    /**
     * Prejde pole obdĺžnikov a ArrayList Lôpt a spúšťa metódu na zistenie či každá
     * lopta je mimo každého zobrazeného obdĺžnika
     */
    public void kontrola() {
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[i].length; j++) {
                if (this.hra.getListLopt().size() <= 0) {
                    continue;
                }
                for (int k = 0; k < this.hra.getListLopt().size(); k++) {
                    Lopta lopty;
                    lopty = this.hra.getListLopt().get(k);
                    if (this.jeMimo(i, j, lopty.getLoptaLHX(), lopty.getLoptaLHY(), lopty.getLoptaDlzka(), lopty)) {
                        this.mapa[i][j].zmenFarbu("blue");
                        this.mapa[i][j].skry();
                        this.hra.zvysSkore();
                        this.hra.getPowerUp().znicenyBlok(this.blokLHX[i][j] + (this.dlzka / 2), this.blokLHY[i][j]);
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    /**
     * Spúšťa 2 metódy, ktoré zisťujú či je lopta mimo obdĺžika, ak nie je, tak
     * obdĺžnik skryje
     * 
     * @param index1     Umiestnenie obdĺžnika v riadku
     * @param index2     Umiestnenie obdĺžnika v stĺpci
     * @param loptaLHX   Pozícia lopty na osi X
     * @param loptaLHY   Pozícia lopty na osi Y
     * @param loptaDlzka Dĺžka lopty
     * @param lopta      Lopta, ktorá zmení pohyb za určitých podmienok
     * @return vráti true, ak je lopta mimo obdĺžnika
     */
    private boolean jeMimo(int index1, int index2, int loptaLHX, int loptaLHY, int loptaDlzka, Lopta lopta) {
        if (!this.mimoOsX(index1, index2, loptaLHX, loptaDlzka, lopta)
                && !this.mimoOsY(index1, index2, loptaLHY, loptaDlzka, lopta)
                && this.zivoty[index1][index2]) {
            this.zivoty[index1][index2] = false;
            return true;
        }
        return false;
    }

    /**
     * Zisťuje či je lopta mimo obdĺžnika na osi X ak nieje, tak zmení pohyb lopty
     * po osi Y
     * 
     * @param index1       Umiestnenie obdĺžnika v riadku
     * @param index2       Umiestnenie obdĺžnika v stĺpci
     * @param loptaPozicia Pozícia lopty na osi Y
     * @param loptaDlzka   Dĺžka lopty
     * @param lopta        Lopta, ktorá zmení pohyb za určitých podmienok
     * @return Vráti true, ak je lopta mimo os Y
     */
    private boolean mimoOsY(int index1, int index2, int loptaPozicia, int loptaDlzka, Lopta lopta) {
        if (this.blokLHY[index1][index2] + this.vyska < loptaPozicia
                || this.blokLHY[index1][index2] > loptaPozicia + loptaDlzka && this.zivoty[index1][index2]) {
            return true;
        } else if (this.zivoty[index1][index2]) {
            lopta.zmenPadanie();
        }
        return false;
    }

    /**
     * Zisťuje či je lopta mimo obdĺžnika na osi X ak nieje, tak zmení pohyb lopty
     * po osi X
     * 
     * @param index1       Umiestnenie obdĺžnika v riadku
     * @param index2       Umiestnenie obdĺžnika v stĺpci
     * @param loptaPozicia Pozícia lopty na osi X
     * @param loptaDlzka   Dĺžka lopty
     * @param lopta        Lopta, ktorá zmení pohyb za určitých podmienok
     * @return Vráti true, ak je lopta mimo os X
     */
    private boolean mimoOsX(int index1, int index2, int loptaPozicia, int loptaDlzka, Lopta lopta) {
        if (loptaPozicia + loptaDlzka < this.blokLHX[index1][index2]
                || this.blokLHX[index1][index2] + this.dlzka < loptaPozicia && this.zivoty[index1][index2]) {
            return true;
        } else if (this.zivoty[index1][index2]) {
            lopta.zmenDoprava();
        }
        return false;
    }

    /**
     * Zisťuje či existuje aspoň 1 zobrazený obdĺžnik
     * 
     * @return vracia true ak existuje aspoň 1 zobrazený obdĺžnik
     */
    public boolean existuje() {
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[i].length; j++) {
                if (this.zivoty[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Skryje všetky obdĺžniky
     */
    public void skryVsetko() {
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[i].length; j++) {
                if (this.mapa[i][j] != null) {
                    this.mapa[i][j].skry();
                }
            }
        }
    }
}