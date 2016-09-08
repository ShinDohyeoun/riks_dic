package indexDataExtract;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/*	rightForm 정의
 * 	원문_성호사설 : [난-???] 형태는 제외
 * 	원문_오주연문장전산고_1 : 
 * */
public class app {
	public static void main(String[] args) throws IOException{
		//진행정도를 보기위한 int값
		int count = 0;

		//사전list에서 data 읽어오기
        indexing indexing = new indexing();
        indexing.readDicData();
        
        //최장일치 효율성을 높이기 위한 mapping
        indexing.mapping();
        
        //비교할 index 파일 가져오기
        String srcName="원문_성호사설";
        BufferedReader br = new BufferedReader(new FileReader("./indexDataSource/"+srcName+".txt"));
		while(true) {
            String line = br.readLine();
            if (line==null) break;
            line=line.trim();
            if(isRightForm(line)){
	            indexing.longestCompare(line);
	            writeResultString(indexing.result,srcName);
	        }else{
            	writeResultString(line,srcName);
	        }
            count++;
            if(count%100==0)
            	System.out.println("현재 "+count+"개의 문장 indexing 완료");
        }
		br.close();
		
		System.out.println("최장일치 검색 완료");
		
		//indexing에 count된 값들 출력
		indexing.writeIndexedData(srcName);

		System.out.println("최장일치 출력 완료");

	}
	public static boolean isRightForm(String line){
		if(line.contains("[난-"))
			return false;
		else
			return true;
	}
	public static void writeResultString(String result, String srcName) throws IOException{
	    BufferedWriter out = new BufferedWriter(new FileWriter("./indexDataResult/"+srcName+".txt",true));
        out.write(result);
        out.write("</br>");
        out.newLine();
        out.close();
	}
	
	
}
