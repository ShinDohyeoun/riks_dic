package dicDivision;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class main {
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
	
}
