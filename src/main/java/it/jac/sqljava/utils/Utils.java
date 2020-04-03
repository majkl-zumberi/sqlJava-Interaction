package it.jac.sqljava.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.jac.sqljava.entity.Citta;

public class Utils {

	public static void stampaListaCitta(List<Citta> list) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		StringBuilder sb = new StringBuilder();
		
		sb.append("-----------------------------------------------------------\n");
		for(Citta bean : list) {

			sb
			.append("|")
			.append(StringUtils.rightPad(String.valueOf(bean.getId()), 10))
			.append("|")
			.append(StringUtils.rightPad(bean.getNome(), 30))
			.append("|")
			.append(StringUtils.rightPad(bean.getCap(), 5))
			.append("|")
			.append(StringUtils.rightPad(sdf.format(bean.getCreationTime()), 15))
			.append("|")
			.append(StringUtils.rightPad(bean.getCreationUser(), 15))
			.append("|")
			.append(StringUtils.rightPad(bean.getUpdateTime() == null ? "" : sdf.format(bean.getUpdateTime()), 15))
			.append("|")
			.append(StringUtils.rightPad(bean.getUpdateUser(), 15));
			sb.append("\n");
		}
		sb.append("-----------------------------------------------------------");	
		System.out.println(sb.toString());

	}
	
	public static List<Citta> readFromFile(String filePath) {
		
		List<Citta> result = new ArrayList<>();
		
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				
				String[] tokens = line.split(";");
				
				Citta citta = new Citta();
				
				citta.setId(Integer.parseInt(tokens[0]));
				citta.setNome(tokens[1]);
				citta.setCap(tokens[2]);
				citta.setCreationTime(new Date());
				citta.setCreationUser("auto");
				
				result.add(citta);
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}		
		
		return result;
	}
}
