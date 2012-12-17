package infa.automation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import jxl.*;
import jxl.read.biff.*;



public class TestClass1 {
	// instance variables
	
	// methods
	
	// static method for capitalizing the first letter of each input word 	
		public static String capitalize(String inWord)
		 {
			 if (inWord == null || inWord.trim().length() == 0) 
			 	{
				 return "";
				 }
			 if (inWord.trim().length() == 1) 
			 	{return inWord.toUpperCase();
			 	}
			 
		     return Character.toUpperCase(inWord.charAt(0)) + inWord.substring(1);
		 }
	
	public static void main (String[] args)	throws Exception{
		String inputFilename ="C:/Users/c152783/Desktop/_dummyb2bgenerator/RandP_Transactions-RandPTrans_example.xls";
		String [] FacetName;
		
		File newDir = new File("C:/Users/c152783/Desktop/MappingFolder");
		System.out.println("Output file directory: "+newDir.getAbsolutePath());
		newDir.mkdir();
		String in_line ="";
		String udo_temp = ""; 
		String exp_backslash_temp = "";
		String connector_temp="";
		
		File inputWorkbook = new File(inputFilename);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet S = w.getSheet("NFieldsInfo");
			for (int i = 1; i < S.getRows(); i++) {
				if(S.getCell(2, i).getContents().trim().length()>0) {
				String FacetNameTemp="";
				FacetName=S.getCell(2, i).getContents().trim().split("\\.");
				for (int j = 0; j < FacetName.length; j++) {
					FacetNameTemp = FacetNameTemp+capitalize(FacetName[j]);
				}
				String udo_constant="<TRANSFORMFIELD DATATYPE =\"string\" DEFAULTVALUE =\"\" DESCRIPTION =\"\" GROUP =\"UDTOutput\" NAME =\"@@CHANGE_UDO@@\" OUTPUTGROUP =\"UDTOutput\" PICTURETEXT =\"\" PORTTYPE =\"OUTPUT\" PRECISION =\"65535\" SCALE =\"0\"><TRANSFORMFIELDATTR NAME =\"OutputFilename\" VALUE =\"4\"/></TRANSFORMFIELD>\n";
				String exp_bslash_constant="\n<TRANSFORMFIELD DATATYPE =\"string\" DEFAULTVALUE =\"\" DESCRIPTION =\"\" NAME =\"io_@@CHANGE_EXPBSS@@\" PICTURETEXT =\"\" PORTTYPE =\"INPUT\" PRECISION =\"65535\" SCALE =\"0\"/>\n<TRANSFORMFIELD DATATYPE =\"string\" DEFAULTVALUE =\"ERROR(&apos;transformation error&apos;)\" DESCRIPTION =\"\" EXPRESSION =\"REPLACESTR(1,REPLACESTR(1,io_@@CHANGE_EXPBSS@@,&apos;&#x5c;&apos;,&apos;&#x5c;&#x5c;&apos;),&apos;&quot;&apos;,&apos;-mdoc.dquotes-&apos;)\" EXPRESSIONTYPE =\"GENERAL\" NAME =\"vio_@@CHANGE_EXPBSS@@\" PICTURETEXT =\"\" PORTTYPE =\"OUTPUT\" PRECISION =\"65535\" SCALE =\"0\"/>";
				String connector_constant="<CONNECTOR FROMFIELD =\"out_@@CHANGE_PORT@@\" FROMINSTANCE =\"UDO_Stream_Documents\" FROMINSTANCETYPE =\"Custom Transformation\" TOFIELD =\"io_@@CHANGE_PORT@@\" TOINSTANCE =\"exp_BackslashandDoubleQuotes_SS\" TOINSTANCETYPE =\"Expression\"/>\n";
//				System.out.println("--------");
				udo_temp=udo_temp +udo_constant.replace("@@CHANGE_UDO@@", "out_"+FacetNameTemp);
				exp_backslash_temp=exp_backslash_temp+exp_bslash_constant.replace("@@CHANGE_EXPBSS@@", FacetNameTemp);
				connector_temp=connector_temp+connector_constant.replace("@@CHANGE_PORT@@", FacetNameTemp);

			}
		}
		
		}catch (BiffException e) {
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\c152783\\git\\InfaAutomation\\InformaticaAutomation\\mapping_sample.txt"));
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(newDir.getPath()+"\\"+"m_sample.xml")));
			while ((in_line = reader.readLine()) != null ){
				in_line = in_line.replaceAll("@@REPLACE_FOR_UDO_STREAM@@", udo_temp); // inputs the udo ports into the steamer
				in_line = in_line.replaceAll("@@REPLACE_FOR_EXP_BACKSLASH@@", exp_backslash_temp); // inputs expression backslash values					
				in_line = in_line.replaceAll("@@REPLACE_CONNECTOR@@", connector_temp); // joins udo_stream and expression backslash
				writer.println(in_line);
				
			}
			
			reader.close();
			writer.close();
			System.out.println("Thanks for using the tool..your Mapping has been generated!");
		} catch (Exception e){
			
		}
		
		
	}
	

}
