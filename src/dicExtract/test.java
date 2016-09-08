package dicExtract;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.*;
import java.util.*;


public class test {
	public static void main(String[] args) throws IOException{
		String test = "abcdefg";
		String tmp = "星湖僿說";
		
		System.out.println(test);
		System.out.println(test.length());
		System.out.println(tmp);
		System.out.println(tmp.length());
		String srcName="test";
		
		System.out.println("read 시작");

        BufferedReader br = new BufferedReader(new FileReader("./indexDataSource/"+srcName+".txt"));
		while(true) {
			String line = br.readLine();
			if (line==null) break;
            
            System.out.println(line);
            System.out.println(line.length());
            System.out.println(line.trim());
            System.out.println(line.trim().length());
    		
		}
		br.close();

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
	
	public static String getHanScript(String s) {
		String data="";
		/*
		 * 예외처리할 CodePoint 
		 * 32 : ' '
		 * 9 : 특수문자(	) //여기서 표현이 안됨	
		 * 40 : '('

		*/
		ArrayList exceptCodePoint= new ArrayList();
		exceptCodePoint.add(32);
		exceptCodePoint.add(9);
		exceptCodePoint.add(40);

		//한자어 or ExceptCode 확인
		for (int i = 0; i < s.length(); ) {
	    	int codepoint = s.codePointAt(i);
	   
	    	if(exceptCodePoint.contains(codepoint)){
	    		System.out.println("tete : "+codepoint);
	    		data+=" ";
	    	}else if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
	            data+=Character.toString(s.charAt(i));
	        }
	        i += Character.charCount(codepoint);
	    	
	    }
	    data.trim();
	    return data;
	}
	
	public static String getHanScript2(String s) {
		String data=" ";

		//한자어 or ExceptCode 확인
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
}
