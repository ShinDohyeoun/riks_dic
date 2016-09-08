package dicExtract;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/*	**사전목록**
 * 	고법전용어집 불광대사전 선교사목록
 * 	어휘총람 중국역대인명
 * 	지명일람_관청이칭 지명일람_중국지명 지명일람_한국고지명
 * 	한국인명_방백목록 한국인명_자호목록
 * 	한국한자어사전
 * */
public class app {

	public static void main(String[] args) throws IOException{
		//사전 src의 정보를 가지고 있는 파일에서 data를 읽어와서 이용
		BufferedReader br = new BufferedReader(new FileReader("./dicSrcInfo.txt"));
		while(true) {
            String line = br.readLine();
            if (line==null) break;
            if(line.contains("//"))
            	continue;
            extractFile(line.split("	")[0]);
        }
		
        br.close();
	}
	
	public static void extractFile(String srcName) throws IOException{
		String srcInfo=getDicSrcInfo(srcName);
		/*
		 * 	srcInfo정보
		 * 1. srcName
		 * 2. 어떤 parsing case를 사용할지
		 * 3. parsing case에 따른 추가 delimiter
		 * 
		 * */
		
		if(srcInfo.equals("error")){
	    	System.out.println("해당 src에 대한 정보는 srcInfo에 없습니다.");
	    	return;
		}

		//data정보를 저장
		ArrayList<dicData> dataList=new ArrayList<dicData>();
		parsing p = new parsing(srcName);
		BufferedReader br = new BufferedReader(new FileReader("./dicSource/"+srcName+".txt"));
        
		//동의어 처리해주는 부분
		synonymList synList = new synonymList();
		synList.readSynonymList();
		
		while(true) {
            String line = br.readLine();
            if (line==null) break;
            switch(srcInfo.split("	")[1]){
	            case "1" : 
	            	p.parsingCase1(line,dataList);
	            	break;
	    		case "2" : 
	    			p.parsingCase2(line,srcInfo.split("	")[2],dataList);
	    			break;
	    		case "3" : 
	    			p.parsingCase3(line,srcInfo.split("	")[2],srcInfo.split("	")[3],dataList);
	    			break;
	    		case "11" : 
	    			p.parsingCase11(line,dataList);
	    			if(p.synonymCheck.length>1)
	    				synList.isInSynonymList(p.synonymCheck);
	    			break;
	    		case "12" : 
	    			p.parsingCase12(line,dataList);
	    			if(p.synonymCheck.length>1)
		    			synList.isInSynonymList(p.synonymCheck);
	    			break;
            }
        }
		//동의어 정보 저장
		synList.saveSynonymList();
        System.out.println(srcName + " synonymList저장 끝");

        br.close();
        
        BufferedWriter out = new BufferedWriter(new FileWriter("./dicSourceExtracted/"+srcName+".txt"));
        BufferedWriter out2 = new BufferedWriter(new FileWriter("./dicWordList.txt",true));
        BufferedWriter out3 = new BufferedWriter(new FileWriter("./dicWordList(srcName).txt",true));
        for(int i=0;i<dataList.size();i++){
        	String s=dataList.get(i).form;
            out.write(s); 
            out.newLine();
            out2.write(s);
            out2.newLine();
            out3.write(s+"	"+srcName);
            out3.newLine();
        }

        out.close();
        out2.close();
        out3.close();
        
        
        
    	System.out.println(srcName+"Extract finished");
	}
	
	
	public static String getDicSrcInfo(String srcName) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("./dicSrcInfo.txt"));


		while(true) {
            String line = br.readLine();
            if (line==null) break;
            if(line.contains(srcName))
            	return line;
            
        }
		
        br.close();
        return "error";
	}
}
