package it.jac.sqljava;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import it.jac.sqljava.dao.CityDao;
import it.jac.sqljava.entity.Citta;
import it.jac.sqljava.utils.Utils;

public class MainApp {

	public static void main(String[] args) {
		
		System.out.println("Menu");
		System.out.println("---");
		
		int scelta = 0;
		do {

			System.out.println("1 - Test Connessione");
			System.out.println("2 - Stampa lista citt�");
			System.out.println("3 - Inserisci citta");
			System.out.println("4 - Carica citta");
			System.out.println("5 - Elimina citta");
			System.out.println("9 - Esci");
			
			System.out.print("Scegli voce: ");
			Scanner in = new Scanner(System.in);
			String s = in.nextLine();			
			scelta = Integer.parseInt(s);
			
			switch(scelta) {
			case 1:
				
				testConnessione();
				break;
			case 2:
				
				stampaLista();
				break;
				
			case 3:
				
				inserisciCitta();
				break;
				
			case 4:
				
				caricaCitta();
				break;
			case 5:
				
				deleteCitta();
				break;
			case 9:
				
				break;
			default:
			}
		
		} while(scelta != 9);
	}

	private static void deleteCitta() {

		System.out.print("Inserisci id citta da eliminare: ");
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		try {

			int idCitta = Integer.parseInt(s);
			CityDao dao = CityDao.getInstance();
			dao.deleteCitta(idCitta);
			
		} catch(NumberFormatException e) {
			
			System.out.println("Identificato non numerico");
		}
		
	}

	private static void caricaCitta() {
		
		CityDao dao = CityDao.getInstance();
//		DataSourceCityDao dao = DataSourceCityDao.getInstance();
		// leggere un file csv e caricare tutte le citta contenute nel file
		System.out.println("Carica tabella citta");

		long startTime = System.currentTimeMillis();
		
		List<Citta> list = Utils.readFromFile("comune_cap.csv");

//		dao.createCitta(list);
		for(Citta item : list) {
			
			long itemStartTime = System.currentTimeMillis();
			dao.createCitta(item);
			System.out.println("Tempo creazione citta in " + (System.currentTimeMillis() - itemStartTime));
		}
		
		System.out.println("Tempo totale caricamento " + (System.currentTimeMillis() - startTime));
	}

	private static void inserisciCitta() {
		
		// richiesta dei campi all'utente e inserimento nella base dati
		System.out.println("Crea citta");
		
		CityDao dao = CityDao.getInstance();

		Citta citta = new Citta();
		citta.setNome("Milano");
		citta.setCap("20100");
		citta.setCreationTime(new Date());
		citta.setCreationUser("admin");

		dao.createCitta(citta);
	}

	private static void stampaLista() {
		
		// stampa a video di tutte le citta presenti nella base dati
		//| ID    | NOME CITTA      | CAP CITTA      | DATA CREZIONE     |
		//| 1     | MILANO          | 20100          | 12/03/2020 10:22  |
		
		CityDao dao = CityDao.getInstance();
		List<Citta> list = dao.findAll();
		
		Utils.stampaListaCitta(list);
	}

	private static void testConnessione() {
		
		CityDao dao = CityDao.getInstance();
		
		try {
			dao.test();
			System.out.println("ok");
			
		} catch (ClassNotFoundException e) {
			
			System.out.println("ko 1");
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			System.out.println("ko 2");
			e.printStackTrace();
		}
	}
}
