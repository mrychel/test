package gra;

import mini_max.GeneratorRuchow;
import mini_max.Lisc;
import static gra.Bicie.*;
import static gra.Stale.*;
import java.awt.Point;
import java.util.ArrayList;

public class Ruch {
 
	// zwraca list� p�l, dla danego gracza i danej planszy, majacych ruch lub bicie
	public static ArrayList<Point> dajRuchy(Plansza plansza) {
		ArrayList<Point> polaMajaceRuch = new ArrayList<Point>();
		
		if (!dajBicia(plansza).isEmpty()) 
            return dajBicia(plansza);
		
		for (int x = 0; x < ROZMIAR_PLANSZY; x++) 
            for (int y = 0; y < ROZMIAR_PLANSZY; y++)            	
		    	if (plansza.czyjRuch() == plansza.dajPlansze()[x][y].dajPionek()) {
		    		Point m = new Point(x, y);	
		    		if (!dajRuchy(plansza, m).isEmpty()) polaMajaceRuch.add(m);						
		    	}
		    
		return polaMajaceRuch;
	}
	
	// zwraca list� p�l, dla danego gracza i planszy, na kt�re mo�e by� wykonany ruch lub bicie
	public static ArrayList<Point> dajRuchy(Plansza plansza, Point p) {
		if (!dajBicia(plansza, p).isEmpty())
			return dajBicia(plansza, p);
		
		ArrayList<Point> polaGdzieMoznaRuszyc = new ArrayList<Point>();
		polaGdzieMoznaRuszyc.add(dajRuchPrawo(plansza, p));
		polaGdzieMoznaRuszyc.add(dajRuchLewo(plansza, p));
		while (polaGdzieMoznaRuszyc.remove(null));	
		return polaGdzieMoznaRuszyc;
	}
	
	private static Point dajRuchPrawo(Plansza plansza, Point p) {
	
		Point docelowePole = GRACZ.equals(plansza.czyjRuch()) ? new Point(p.x+1, p.y-1) : new Point (p.x-1, p.y+1);
		
		return (czyNaPlanszy(docelowePole)
				&& plansza.dajPlansze()[docelowePole.x][docelowePole.y].dajPionek() == PUSTE_POLE) ? 
				new Point(docelowePole) : null;		
	}
	
	private static Point dajRuchLewo(Plansza plansza, Point p) {	
		
		Point docelowePole = GRACZ.equals(plansza.czyjRuch()) ? new Point(p.x-1, p.y-1) : new Point (p.x+1, p.y+1);
		
		return (czyNaPlanszy(docelowePole)
				&& plansza.dajPlansze()[docelowePole.x][docelowePole.y].dajPionek() == PUSTE_POLE) ? 
				new Point(docelowePole) : null;	
	}
	
	public static void wykonajRuch(Plansza plansza, Point skad, Point dokad) {
	
		plansza.dajPlansze()[dokad.x][dokad.y].ustawPionek(
			plansza.dajPlansze()[skad.x][skad.y].dajPionek());
		plansza.dajPlansze()[skad.x][skad.y].ustawPionek(PUSTE_POLE);
		// bicie
		if (Math.abs(skad.x - dokad.x) > 1) {
			int x = (skad.x - dokad.x) > 0 ? skad.x - 1 : skad.x + 1;
		    int y = (skad.y - dokad.y) > 0 ? skad.y - 1 : skad.y + 1;		    
		    plansza.dajPlansze()[x][y].ustawPionek(PUSTE_POLE);
		    plansza.ustawBicie(true);
		} else 
			plansza.ustawBicie(false);
		// koniec bicia
		plansza.czyjRuch(!plansza.czyjRuch());
		String komunikat = GRACZ.equals(!plansza.czyjRuch()) ? "GRACZ" : "KOMPUTER"
						   +" wykona� "+ (plansza.dajBicie() ? "bicie: " : "ruch: ")
						   +dajNazweRuchu(skad)+" na "+dajNazweRuchu(dokad);
		plansza.ustawKomuniakt(komunikat);
	}		
	
	public static Plansza ruchKomputera(Plansza plansza) {
			
		Lisc drzewo = new Lisc(plansza);
		GeneratorRuchow.budujPodDrzewo(drzewo, 0);
		if (drzewo.dajNajRuch() == null) return null;
		plansza.ustawPlansze(drzewo.dajNajRuch());	
				
		return plansza;		
	}
	
	
/*
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
	public static boolean czyNaPlanszy(Point p) {
		
		return p.x < ROZMIAR_PLANSZY  && p.y < ROZMIAR_PLANSZY 
				&& p.x >= 0 && p.y >= 0;
	}
	
	private static String dajNazweRuchu(Point p) {	 
		
	    return COLS.substring(p.x, p.x + 1) + (p.y+1);	    
	}
}