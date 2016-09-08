package indexDataExtract;
import java.util.regex.*;
import java.io.IOException;
import java.util.*;
public class parsing {
	public String srcName;
	public String[] synonymCheck={""};
	
	public parsing(String name){
		this.srcName=name;
	}
	
	public String delKor(String data){
		data = data.replaceAll("[가-힣ㄱ-ㅎㅏ-ㅣ]" , "").trim();
    	return data;
	}
	
	public static boolean containsHanScript(String s) {
	    for (int i = 0; i < s.length(); ) {
	        int codepoint = s.codePointAt(i);
	        i += Character.charCount(codepoint);
	        if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static boolean containsHanGulScript(String s) {
	    for (int i = 0; i < s.length(); ) {
	        int codepoint = s.codePointAt(i);
	        i += Character.charCount(codepoint);
	        if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HANGUL) {
	            return true;
	        }
	    }
	    return false;
	}	
	
	/*	문장에 첫번째 한자어를 반환한다.
	 * */
	public static String getHanScript(String s) {
		String data=" ";

		//한자어  확인
		for (int i = 0; i < s.length(); ) {
	    	int codepoint = s.codePointAt(i);
	   
	    	if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
	            data+=Character.toString(s.charAt(i));
	        }else if(data.charAt(data.length()-1)!=' '){
	        	data+=" ";
	        }
	        i += Character.charCount(codepoint);
	    }
	    return data.trim();
	}
	
	/*
	 * 	parsing Case1
	 * 	각 문장의 첫번째 한자만 가져온다.
	 * */
	public void parsingCase1(String info, ArrayList<dicData> dataList){
		//문장에 한자어가 없는 경우 종료
		if(!containsHanScript(info))
			return;
		
		
		//변수선언
		dicData data = new dicData();
		data.org=info;
		String han = getHanScript(info);
		
			
		if(han.indexOf(" ")!=-1)
			data.form = han.substring(0,han.indexOf(" "));
		else 
			data.form=han;

		dataList.add(data);
	}
	
	/*
	 * 	parsing Case2
	 * 	문장에 특정 delimiter가 있는 문장에서만 한자어만 가져온다.
	 * */
	public void parsingCase2(String info, String delimiter, ArrayList<dicData> dataList){
		//문장에 한자어가 없는 경우 종료
		if(!containsHanScript(info))
			return;
		
		//문장에 특정 delimiter가 없는 경우 종료
		if(!info.contains(delimiter))
			return;
		
		//변수선언
		dicData data = new dicData();
		data.org=info;
		String han = getHanScript(info);
		
			
		if(han.indexOf(" ")!=-1)
			data.form = han.substring(0,han.indexOf(" "));
		else 
			data.form=han;

		dataList.add(data);
	}
	
	/*
	 * 	parsing Case3
	 * 	어휘총람 맞춤 case
	 * 	문장에 특정 delimiter사이의 문장에서 띄어쓰기를 없앤 뒤 한자어를 가져온다.
	 * */
	public void parsingCase3(String info, String delimiter1, String delimiter2, ArrayList<dicData> dataList){
		//문장에 한글이 없는 경우 종료
		if(!containsHanGulScript(info))
			return;
		
		//문장에 한자어가 없는 경우 종료
		if(!containsHanScript(info))
			return;
		
		//문장에 특정 delimiter가 없는 경우 종료
		if(!info.contains(delimiter1) || !info.contains(delimiter2))
			return;
		
		//변수선언
		dicData data = new dicData();
		data.org=info;

		String tmp = info;	

		try{	
			tmp = info.substring(info.indexOf(delimiter1),info.indexOf(delimiter2));			
		}catch(StringIndexOutOfBoundsException e){
		}
		tmp = tmp.replaceAll(" ", "");
		
		String han = getHanScript(tmp);
		
			
		if(han.indexOf(" ")!=-1)
			data.form = han.substring(0,han.indexOf(" "));
		else 
			data.form=han;

		dataList.add(data);
	}
	
	
	/*
	 * 	parsing Case11
	 * 	동의어가 있는경우 해당 문장에 있는 모든 한자어를 가져와서 동의어 처리해준다.
	 * */
	public void parsingCase11(String info, ArrayList<dicData> dataList) throws IOException{
		//인자에 한자어가 없는 경우 종료
		if(!containsHanScript(info))
			return;
		
		
		//변수선언
		String han = getHanScript(info).trim();
		String[] arr = han.split(" ");
		for(int i=0;i<arr.length;i++){
			dicData data = new dicData();
			data.form = arr[i];
			dataList.add(data);
		}
		
		//동의어 처리해주기 위해 변수에 arr[] 저장
		this.synonymCheck=arr;
	}
	
	/*
	 * 	parsing Case12
	 * 	동의어가 있는경우 
	 * 	한국인명_자호목록의 경우(특이 Case)
	 * */
	public void parsingCase12(String info, ArrayList<dicData> dataList) throws IOException{
		//인자에 한자어가 없는 경우 종료
		if(!containsHanScript(info))
			return;
		
		
		//한국인명_자호목록의 경우 "자는", "호는" 을 기준으로 문장을 split한 뒤 첫번째 한자어만 가져오는 형식을 갖는다.
		String[] splitedInfo;
		if(info.contains("자는") && info.contains("호는")){
			splitedInfo = info.split("자는|호는");
		}else if(info.contains("자는") && !info.contains("호는")){
			splitedInfo = info.split("자는");
		}else if(!info.contains("자는") && info.contains("호는")){
			splitedInfo = info.split("호는");
		}else{
			splitedInfo=info.split("");
		}
		
		//나눠진 문장에서 첫번째 한자어를 가져온다.
		String han = "";
		for(int i=0;i<splitedInfo.length;i++){
			if(!containsHanScript(splitedInfo[i]))	//인자에 한자어가 없으면 다음으로 넘어간다.
				continue;
			else{
				han += getHanScript(splitedInfo[i]).trim()+" ";
			}
		}
		han=han.trim();
		
		String[] arr = han.split(" ");
		for(int i=0;i<arr.length;i++){
			dicData data = new dicData();
			data.form = arr[i];
			dataList.add(data);
		}
		
		//동의어 처리해주기 위해 변수에 arr[] 저장
		this.synonymCheck=arr;

	}
	
	
}