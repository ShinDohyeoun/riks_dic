package dicExtract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class synonymList {
	public ArrayList<String> synonymList = new ArrayList<String>();

	/*	동의어 정보가 synonymList에 존재하는지 확인하고
	 * 	존재하면 해당 정보를 update
	 * 	존재하지 않으면 해당정보를 add 해준다.
	 * */
	public void isInSynonymList(String[] data){
		for(int i=0;i<synonymList.size();i++){
			String[] tmp = synonymList.get(i).split(" ");
			
			for(int j=0;j<data.length;j++){
				for(int k=0;k<tmp.length;k++){
					if(data[j].equals(tmp[k])){
							updateSynonymList(synonymList, i, data);
							return;
					}
				}
			}
		}
		
		addSynonymList(synonymList, data);
	}

	
	/*	synonymList에 없는 새로운 동의어정보일 때
	 * 	list에 해당 동의어 정보를 추가하는 함수
	 * */
	public void addSynonymList(ArrayList<String> synonymList, String[]data){
		String addedData="";
		for(int i=0;i<data.length;i++){
			addedData+=data[i]+" ";
		}
		synonymList.add(addedData.trim());
	}
	
	/*	synonymList에 이미 있는 단어라
	 * 	관련된 동의어 정보를 수정하는 함수
	 * */
	public void updateSynonymList(ArrayList<String> synonymList, int index, String[]data){
		//data들을 가져와서 한 array에 저장하는 부분
		String[] synonymData = synonymList.get(index).split(" ");
		
		String[] updatedData = new String[synonymData.length+data.length];
		for(int i=0;i<updatedData.length;i++){
			if(i<synonymData.length){
				updatedData[i]=synonymData[i];
			}else{
				updatedData[i]=data[i-synonymData.length];
			}
		}
		
		//중복제거를 위해 arrayLsit형식으로 바꾼뒤 중복제거를 해주는 부분
		ArrayList<String> tempList = new ArrayList<String>(Arrays.asList(updatedData));
		ArrayList<String> dataList;
		
		dataList = new ArrayList<String>(new HashSet<String>(tempList));
		Collections.sort(dataList);
		
		//정리된 data를 String형식으로 바꿔서 저장하는 부분
		String result="";
		for(int i=0;i<dataList.size();i++){
			result+=dataList.get(i)+" ";
		}
		result.trim();
		synonymList.set(index, result);
	}
	
	public void readSynonymList() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("./synonymList.txt"));
        while(true) {
            String line = br.readLine();
            if (line==null) break;
            synonymList.add(line);
            
        }
        br.close();
    }
	
	
	public void saveSynonymList() throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter("./synonymList.txt"));
        for(int i=0;i<synonymList.size();i++){
        	out.write(synonymList.get(i));
        	out.newLine();
        }
        out.close();
	}
}
