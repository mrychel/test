package mini_max;
import gra.Pole;
import gra.Stale;

import java.util.Arrays;

public class Heurystyka {
	
	static int wyliczWartoscPlanszy(String kto, Lisc drzewo, String ruch)
	{
		int suma = 0;
		Pole[][] plansza = drzewo.dajPlansze();		
		String pionek = "";
		
		for (int k=1; k<=Stale.ROZMIAR_PLANSZY; k++)
    		for (int w=1; w<=Stale.ROZMIAR_PLANSZY; w++) {
	        	pionek = (plansza[w][k]!=null)?plansza[w][k].dajPionek():"";		            
	        	if (plansza[w][k]!=null && !Stale.POLE_PUSTE.equals(pionek))
	            	if(Arrays.asList(Stale.BIALE).contains(pionek))		            		            		
	            		suma += plansza[w][k].dajWartosc()*1;		            	
	        		if(Arrays.asList(Stale.CZARNE).contains(pionek))
	            		suma += (9-plansza[w][k].dajWartosc())*(-1);
            }
		if (Stale.BICIE.equals(ruch))
			suma += 10*(Stale.GRACZ.equals(kto)?(-1):1);
		// ilo�� damek
		suma += (-50)*plansza[0][1].dajWartosc()+50*plansza[0][2].dajWartosc();
			
		return suma;
	}
}