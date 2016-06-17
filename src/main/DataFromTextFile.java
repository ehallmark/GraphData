package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringJoiner;

public class DataFromTextFile {

	public static void main(String[] args) throws IOException {
		File f;
		try {
			if(args.length==0) throw new Exception("Must include a filename");
			f = new File(args[0]);
			// make sure file exists
			if(!f.exists() || !f.isFile()) {
				throw new Exception("File does not exist");
			}
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}

		// have a valid file		
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String line;
		StringJoiner data = new StringJoiner(",","[","]");

		/*
				data: [
				       [0, 4], 
				       [1, 3], 
				       [2, 10]
				   ]
		*/		   
		String headers = "<head><script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js\"></script><script src=\"https://code.highcharts.com/stock/highstock.js\"></script><script src=\"https://code.highcharts.com/stock/modules/exporting.js\"></script></head><div id=\"container\" style=\"height: 400px; min-width: 310px\"></div>";
		String begin = "<script>$(function () {$('#container').highcharts('StockChart', { rangeSelector : {selected : 1 },title : { text : 'Time On' }, series : [{ name : 'Time On', data :";
		String end = ",tooltip: {valueDecimals: 2 }}]});});</script>";
		
		while((line=br.readLine())!=null) {
			StringJoiner inner = new StringJoiner(",","[","]");
			// split into spaces
			String[] words = line.replaceAll(",", "").split("\\s+");
			int timeOn = Integer.valueOf(words[9]);
            //first convert string to java.util.Date object using SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd yyyy hh:mma");
            java.util.Date date;
            try {
				date = sdf.parse(String.join(" ", words[1], words[2], words[3], words[5]));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
            inner.add(String.valueOf(date.getTime())).add(String.valueOf(timeOn));
            data.add(inner.toString());
		}
		
				
		br.close();
		
		// New file
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("output.html")));
		bw.write(headers);
		bw.write(begin);
		bw.write(data.toString());
		bw.write(end);
		bw.flush();
		bw.close();		
	}
}
