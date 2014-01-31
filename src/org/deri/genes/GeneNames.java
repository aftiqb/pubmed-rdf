package org.deri.genes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.deri.pubmed.Publish;
import org.deri.pubmed.Search;

import banner.eval.LinkedTcgaBanner;

public class GeneNames {

	LinkedTcgaBanner diseaseBanner;
	LinkedTcgaBanner geneBanner;
	// 7800 genes have 0 results
	// total genes are: 
	// is there a gene database from where we can verify if all genes we have in the list are actual genes and is not
	// noise data.
	// which URI we use between pubmed and disease ontology? Which URI we use for gene? Do we need to use URI for gene
	// LIMES - 
	// for gene URI --> http://download.bio2rdf.org/current/homologene/homologene.html
	// table lookup - hashtable - who will do it
	// fox results - send some results around
	// homology, 
	
	public void readFile(String filePath, String outFilePath) {
		BufferedReader br = null;
		diseaseBanner  = new LinkedTcgaBanner("config/banner_NCBIDisease_DEV.xml");
		//geneBanner = new LinkedTcgaBanner("config/banner_UIMA_TEST.xml");
		
		String[] str;
		Search obj = new Search();
		obj.createFile("data/stats.csv");
		
		try {
 			String line;
 			
 			br = new BufferedReader(new FileReader(filePath));
 			
 			while ((line = br.readLine()) != null) {
 				if(!line.equals("")) {
 					str = line.split("\t");
 					System.out.println("processing: " + str[str.length-2]);
 					obj.searchTerm(str[str.length-2], "", outFilePath);
 				}
 			}
 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		GeneNames obj = new GeneNames();
		//obj.readFile("data/genes_list.txt", "data/Gene-related/");
		String input = "A 36-year-old woman with benign phyllodes tumor of the left breast had undergone lumpectomy 1 year ago and was admitted to our hospital because of a left breast mass on the operation scar. Ultrasonography showed a 35 mm low-echoic, elliptical mass with a high depth to width( D/W) ratio in the C area and a 10 mm low-echoic, polygonal mass with a high D/W ratio in the E area. Histological examination of an ultrasonography-guided vacuum-assisted biopsy specimen indicated recurrent phyllodes tumor. Since both tumors were assumed to be recurrent phyllodes tumors, quadrantectomy was performed. Finally, the mass in the C area was diagnosed as a recurrent phyllodes tumor and the mass in the E area was diagnosed as a fibroadenoma. A non-invasive ductal carcinoma was incidentally detected between the 2 tumors, and the surgical margin was negative. Radiotherapy was performed on the remnant breast tissue.";
		Publish obj1 = new Publish();
		//obj1.getAnnotations_Disease(input, new LinkedTcgaBanner(""));
	}
}
