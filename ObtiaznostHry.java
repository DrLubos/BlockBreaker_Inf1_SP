public enum ObtiaznostHry {
    LAHKA(3, 0, 9), // Ľahká obtiažnosť, 3 štartovcie lopty, ktoré nebudú zrýchlené a posuvník bude
                    // predĺžený 9 tikov
    STREDNA(2, 1, 6), // Stredná obtiažnosť, 2 štartovacie lopty, budú raz zrýchlené po vytvorení a
                      // posuvník bude predĺžený 6 tikov
    TAZKA(2, 2, 4), // Ťažká obtiažnosť, 2 štartovacie lopty, 2 krát zrýchlené a posuvník bude
                    // predĺžený na 4 tiky
    NEKONECNA(2, 0, 6); // Nekonečná obtiažnosť, 2 nezrýchlené lopty na začiatok a posuvník predĺžený na
                        // 6 tikov

    private int pocetStartovacihLopt;
    private int pocetZrychleni;
    private int dobaPredlzeneniaPosuvnika;

    /**
     * Konštruktor enumu
     * 
     * @param pocetStartovacihLopt      Počet koľko lôpt sa vytvorí pri spustení hry
     * @param pocetZrychleni            Počet koľko krát sa lopty zrýchlia pri ich
     *                                  vytvorení
     * @param dobaPredlzeneniaPosuvnika Koľko tikov bude platiť schopnosť na
     *                                  predĺženie posuvníka
     */
    ObtiaznostHry(int pocetStartovacihLopt, int pocetZrychleni, int dobaPredlzeneniaPosuvnika) {
        this.pocetStartovacihLopt = pocetStartovacihLopt;
        this.pocetZrychleni = pocetZrychleni;
        this.dobaPredlzeneniaPosuvnika = dobaPredlzeneniaPosuvnika;
    }

    /**
     * Vracia koľko lôpt sa má vytvoriť pri spustení hry
     * 
     * @return Koľko lôpt sa má vytvoriť pri spustení hry
     */
    public int getPocetStartovacichLopt() {
        return this.pocetStartovacihLopt;
    }

    /**
     * Vracia koľko krát sa má poslať správa na zrýchlení lopty po jej vytvorení
     * 
     * @return Počet zrýchlení lopty po jej vytvorení
     */
    public int getPocetZrychleni() {
        return this.pocetZrychleni;
    }

    /**
     * Vracia dobu predĺženia posuvníka, koľko krát má prebehnúť tik
     * 
     * @return Dobu predĺženia posuvníka, koľko krát má prebehnúť tik
     */
    public int getDobaPredlzeniaPosuvnika() {
        return this.dobaPredlzeneniaPosuvnika;
    }
}