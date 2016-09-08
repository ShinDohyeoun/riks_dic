package indexDataExtract;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

/*	최장일치 효율을 높이기 위한 방법
 * 	1. 가장 긴 단어의 글자수를 이용
 * 	2. 단어의 첫글자를 map을 이용해 저장해서 index값을 이용한다.
 * */
public class indexing {
	ArrayList<String> dicData = new ArrayList<String>();
	/*
	 * 검색효율을 높이기 위한 indexMap
	 * key : 단어의 첫번째 글자가 들어가 있다.
	 * value : 해당 key가 첫번째글자로 나오는 dicData 단어 중 첫번째 단어의 index값
	 */
	TreeMap<String, Integer> indexMap = new TreeMap<String, Integer>();
	
	/*	단어의 count갯수가 저장되는 hashMap
	 * */
	HashMap<String, Integer> countedWordData = new HashMap<String, Integer>();
	
	//최장일치의 효율을 높이기위해 가장 긴 글자의 수를 저장
	int longestWordSize=0;
	
	//html코드로 변환되어 저장될 string
	String result;
	
	//html코드에 넣을 font color
	String color="red";
	
	
	public void writeIndexedData(String srcName)throws IOException{
	    BufferedWriter out = new BufferedWriter(new FileWriter("./indexDataResult/"+srcName+"_count.txt"));
    	Iterator<String> iterator = countedWordData.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            out.write(key+"	"+countedWordData.get(key));
            out.newLine();
        }
        out.close();
	}
	
	public void changeCountVal(String data){
		if(countedWordData.containsKey(data))
			countedWordData.put(data, countedWordData.get(data)+1);
		else
			countedWordData.put(data, 1);
	}
	
	/*	주어진 line과 indexMap을 이용하여 비교한 뒤
	 * 	같은 단어가 나오면 true를 반환
	 * */
	public boolean indexMapCompare(String line){
		String firstChar = line.substring(0,1);

		if(indexMap.containsKey(firstChar)){
			int startIndex = indexMap.get(firstChar);
			int endIndex = indexMap.get(indexMap.higherKey(firstChar));
			for(int i=startIndex;i<endIndex;i++){
				//System.out.println("index i 에 들어있는 data : "+dicData.get(i));

				//검색결과 같은 것이 나오면 count를 change해주고 다음 검색할 parsedLine을 다시 설정
				if(line.equals(dicData.get(i))){
					changeCountVal(dicData.get(i));
					return true;
				}
			}
		}
		return false;
	}
	
	public void longestCompare(String line){
		int startIndex=0;
		int endIndex=0;
		String cuttedLine;
		this.result="";
				
		while(startIndex<line.length()){
			endIndex=startIndex+this.longestWordSize;
			if(endIndex>line.length())
				endIndex=line.length();
				
			while(startIndex<endIndex){
				cuttedLine = line.substring(startIndex,endIndex);
				
				
				if(indexMapCompare(cuttedLine)){
					startIndex+=cuttedLine.length();
					
					//result(html을 이용해 색을 입힌 결과)에 값을 넣는 부분
					this.result+="<font color="+this.color+">"+cuttedLine+"</font>";
					if(this.color.equals("red"))
						this.color="blue";
					else
						this.color="red";
					break;
				}else{
					endIndex--;
					if(startIndex==endIndex){
						this.result+=cuttedLine;
						startIndex++;
					}
				}
			}
		}
	}
	
	public void readDicData() throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader("./dicWordList.txt"));
		while(true) {
            String line = br.readLine();
            if (line==null) break;
            dicData.add(line);
            if(line.length()>this.longestWordSize)
            	this.longestWordSize=line.length();
            
           
        }
		
        br.close();
        
      //dicData 중복제거
        System.out.println("중복제거 전 단어 갯수 : "+dicData.size());
        
        dicData = new ArrayList<String>(new HashSet<String>(dicData));
        Collections.sort(dicData);
        System.out.println("중복제거 후 단어 갯수 : "+dicData.size());
        
	}
	
	public void mapping(){
		for(int i=this.dicData.size()-1;i>=0;i--){
	        if(dicData.get(i).length()!=0)
	        	indexMap.put(dicData.get(i).substring(0, 1),i);
		}
        System.out.println("indexMap의 갯수 : "+indexMap.size());
	}
	
	
	
	
}
