package gra;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

import gra.Plansza.Pole;
import mini_max.GeneratorRuchow;
import mini_max.Lisc;
import static gra.Stale.*;

import java.awt.Point;
import java.util.ArrayList;

public class Ruch {
 
	// zwraca list� p�l, dla danego gracza i danej planszy, majacych ruch
	public static ArrayList<Point> dajRuchy(Boolean kto, Pole[][] plansza) {
		ArrayList<Point> polaMajaceRuch = new ArrayList<Point>();
		ArrayList<Point> p = new ArrayList<Point>();		
		
		// TO DO ----- if dajBicia(kto, plansza)
		for (int x = 0; x < ROZMIAR_PLANSZY; x++) 
            for (int y = 0; y < ROZMIAR_PLANSZY; y++) { 
            	Boolean ff = plansza[x][y].dajPionek();
		    	if (kto.equals(plansza[x][y].dajPionek())) {
		    		Point m = new Point(x, y);	
		    		p=dajRuchy(kto, plansza, m);		    			    		
		    		if (!p.isEmpty()) polaMajaceRuch.add(m);						
		    	}
		    }		
		return polaMajaceRuch;
	}
	
	// zwraca list� p�l, dla danego gracza i planszy oraz pola, na kt�re mo�e by� wykonane bicie (max. dwa pola, w lewo lub w prawo)
	public static ArrayList<Point> dajRuchy(Boolean kto, Pole[][] plansza, Point p) {
		ArrayList<Point> polaGdzieMoznaRuszyc = new ArrayList<Point>();
		polaGdzieMoznaRuszyc.add(dajRuchPrawo(kto, plansza, p));
		polaGdzieMoznaRuszyc.add(dajRuchLewo(kto, plansza, p));
		while (polaGdzieMoznaRuszyc.remove(null));	
		return polaGdzieMoznaRuszyc;
	}
	
	public static Point dajRuchPrawo(Boolean kto, Pole[][] plansza, Point p) {
	
		int docelowaKolumna = GRACZ.equals(kto) ? p.x+1 : p.x-1;
		int docelowyWiersz = GRACZ.equals(kto) ? p.y-1 : p.y+1; 
		
		return (czyNaPlanszy(docelowaKolumna, docelowyWiersz)
				&& plansza[docelowaKolumna][docelowyWiersz].dajPionek() == null) ? 
				new Point(docelowaKolumna, docelowyWiersz) : null;		
	}
	
	public static Point dajRuchLewo(Boolean kto, Pole[][] plansza, Point p) {	
		
		int docelowaKolumna = GRACZ.equals(kto) ? p.x-1 : p.x+1;
		int docelowyWiersz = GRACZ.equals(kto) ? p.y-1 : p.y+1; 
		
		return (czyNaPlanszy(docelowaKolumna, docelowyWiersz)
				&& plansza[docelowaKolumna][docelowyWiersz].dajPionek() == null) ? 
				new Point(docelowaKolumna, docelowyWiersz) : null;	
	}
	/*
	public static Pole[][]  move(String ruch, String kto, Pole[][] plansza, int pionekWiersz, int pionekKolumna) {
	
		if (Stale.RUCH_LEWO.equals(ruch))
		{
			System.out.println("Ruch w lewo!");
			return ruchLewo(kto, plansza, pionekWiersz, pionekKolumna);	
		}
		else
		{
			System.out.println("Ruch w prawo!");
			return ruchPrawo(kto, plansza, pionekWiersz, pionekKolumna);
		}
	}
	
	public static Pole[][] ruchLewo(String kto, Pole[][] plansza, int pionekWiersz, int pionekKolumna) {
		
		int skokWiersz;	
		int skokKolumna = -1; 
			
		if (Stale.GRACZ.equals(kto)) 
			skokWiersz = -1; 
		else
			skokWiersz = 1;
		
		plansza[pionekWiersz+skokWiersz][pionekKolumna+skokKolumna].ustawPionek(
				plansza[pionekWiersz][pionekKolumna].dajPionek());
		plansza[pionekWiersz][pionekKolumna].ustawPionek(Stale.POLE_PUSTE);
		
		if (pionekWiersz+skokWiersz == (Stale.GRACZ.equals(kto)?1:Stale.ROZMIAR_PLANSZY)) {
			plansza[pionekWiersz+skokWiersz][pionekKolumna+skokKolumna].ustawPionek(Stale.POLE_PUSTE);
			plansza[0][Stale.GRACZ.equals(kto)?1:2].ustawWartosc(
					plansza[0][Stale.GRACZ.equals(kto)?1:2].dajWartosc()+1); 
		} 
		
		return plansza;
	}

	public static Pole[][] ruchPrawo(String kto, Pole[][] plansza, int pionekWiersz, int pionekKolumna) {
		
		int skokWiersz;			
		int skokKolumna = 1;
		
		if (Stale.GRACZ.equals(kto)) 
			skokWiersz = -1; 
		else
			skokWiersz = 1;
		
		plansza[pionekWiersz+skokWiersz][pionekKolumna+skokKolumna].ustawPionek(
				plansza[pionekWiersz][pionekKolumna].dajPionek());
		plansza[pionekWiersz][pionekKolumna].ustawPionek(Stale.POLE_PUSTE);
		
		if (pionekWiersz+skokWiersz == (Stale.GRACZ.equals(kto)?1:Stale.ROZMIAR_PLANSZY)) {
			plansza[pionekWiersz+skokWiersz][pionekKolumna+skokKolumna].ustawPionek(Stale.POLE_PUSTE);
			plansza[0][Stale.GRACZ.equals(kto)?1:2].ustawWartosc(
					plansza[0][Stale.GRACZ.equals(kto)?1:2].dajWartosc()+1); 
		} 
			
		return plansza;
	}
	
	public static Plansza ruchKomputera(Plansza aPlansza) {
		
		System.out.println("\n\n Ruch komputera:" );
		Lisc drzewo = new Lisc(aPlansza.dajPlansze());
		GeneratorRuchow.dajRuchy(Stale.KOMPUTER, aPlansza.dajPlansze(), drzewo, 0);
		if (drzewo.dajNajRuch(true) == null) return null;
		aPlansza.ustawPlansze(drzewo.dajNajRuch(true).dajPlansze());
		aPlansza.ustawBicie(drzewo.dajNajRuch(true).jestBicie());
				
		return aPlansza;		
	}

	public static Plansza ruchGracza(Plansza aPlansza) throws IOException {
				
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		while (true) {
		System.out.println("Podaj ruch w nast�puj�cy spos�b:  'b1 lewo' ");
		System.out.println("Poprawne ruch: prawo, lewo, bicie_prawo, bicie_lewo, exit, pokaz");
			
		String command = br.readLine();
		StringTokenizer commandTokens = new StringTokenizer(command);				
		
		String pole = commandTokens.nextToken().toUpperCase();
		
		if (pole.length() < 2) {
			System.out.println("Podaj poprawne pole planszy.");
			continue;
		} else if (Stale.POKAZ.equals(pole)) {
			aPlansza.pokazPlansze();	
			continue;
		} else if (Stale.WYJSCIE.equals(pole)) {
			System.out.println("Koniec.");
			System.exit(0);
		} else if ("12345678".indexOf(pole.substring(1, 2)) == -1
				   || "ABCDEFGH".indexOf(pole.substring(0, 1)) == -1
				   || aPlansza.dajPlansze()["12345678".indexOf(pole.substring(1, 2))+1]["ABCDEFGH".indexOf(pole.substring(0, 1))+1] == null
				   || Arrays.asList(Stale.BIALE).contains(
						   aPlansza.dajPlansze()["12345678".indexOf(pole.substring(1, 2))+1]["ABCDEFGH".indexOf(pole.substring(0, 1))+1].dajPionek())
				   ) {
			System.out.println("Mo�esz porusza� tylko pionkami x.");
			continue;
		} else if(commandTokens.countTokens() != 1) {
			System.out.println("Prosz� podaj pionka i komend�.");
			continue;
		}

		String pionek = aPlansza.dajPlansze()["12345678".indexOf(pole.substring(1, 2))+1]["ABCDEFGH".indexOf(pole.substring(0, 1))+1].dajPionek();
		String ruch = commandTokens.nextToken();
				
		if (Stale.RUCH_PRAWO.equals(ruch) || Stale.RUCH_LEWO.equals(ruch)) {
			
			for (int k=1; k<=Stale.ROZMIAR_PLANSZY; k++)
	    		for (int w=1; w<=Stale.ROZMIAR_PLANSZY; w++) 
	        	    if (aPlansza.dajPlansze()[w][k]!=null && 
	        		    aPlansza.dajPlansze()[w][k].dajPionek().equals(pionek)) {
	        		 	        		 
	        		    if (Ruch.mogeRuszyc(ruch, Stale.GRACZ, aPlansza.dajPlansze(), w, k, true)) {		        			
		        			aPlansza.ustawPlansze(Ruch.move(ruch, Stale.GRACZ, aPlansza.dajPlansze(), w, k));
		        			return aPlansza;	        			 
	        		    }
		            }	        	 
	    } else if (Stale.BICIE_PRAWO.equals(ruch) || Stale.BICIE_LEWO.equals(ruch)) {
			
	    	for (int k=1; k<=Stale.ROZMIAR_PLANSZY; k++)
	    		for (int w=1; w<=Stale.ROZMIAR_PLANSZY; w++) 
	        	    if (aPlansza.dajPlansze()[w][k]!=null && 
	        		    aPlansza.dajPlansze()[w][k].dajPionek().equals(pionek)) {
	        		 	        		 
		        		if (Bicie.mogeBic(ruch, Stale.GRACZ, aPlansza.dajPlansze(), w, k, true)) {
		        			
		        			 Bicie.bicie(ruch, Stale.GRACZ, aPlansza.dajPlansze(), w, k, true);		        			
		        			 aPlansza.ustawBicie(true);
		        			 return aPlansza;
		        		 } else 
		        			 System.out.println("Nie mo�esz bi�.");
	        	    }		
		} else 
			System.out.println("Nierozpoznana komenda.");	
		}			
	}
*/
	public static boolean czyNaPlanszy(int kolumna, int wiersz) {
		
		return wiersz < ROZMIAR_PLANSZY  && kolumna < ROZMIAR_PLANSZY 
				&& wiersz >= 0 && kolumna >= 0;
	}
}
