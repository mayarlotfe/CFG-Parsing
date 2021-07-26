import java.util.ArrayList;
import java.util.Stack;

public class Parsing
{
	static ArrayList<ArrayList<String>> grammerRules = new ArrayList<ArrayList<String>> ();
	static ArrayList<String> variables = new ArrayList<String> ();
	static ArrayList<String> terminals = new ArrayList<String> ();
	static ArrayList<ArrayList<String>> first = new ArrayList<ArrayList<String>> ();
	static ArrayList<ArrayList<String>> follow = new ArrayList<ArrayList<String>> ();
	static ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>> ();
	static ArrayList<String> finalOutput = new ArrayList<String> ();
	static Stack<String> statesStack = new Stack<>();
	
	
public Parsing(String rules)
{
	String[] tempGrammer = rules.split("\\#");
	String [] tempRules = tempGrammer[0].split("\\;");
	String [] tempFirst = tempGrammer[1].split("\\;");
	String [] tempFollow = tempGrammer[2].split("\\;");
	for (int i = 0 ; i < tempRules.length ; i++)
	{
		ArrayList<String> temp = new ArrayList<String> ();
	    String [] temp2 = tempRules[i].split("\\,");
	    for ( int j = 0 ; j < temp2.length ; j++)
	    {
	    	temp.add(temp2[j]);
	    }
		grammerRules.add(temp);
		variables.add(temp.get(0));
	}

	for (int i = 0 ; i < tempFirst.length ; i++)
	{
		ArrayList<String> temp = new ArrayList<String> ();
	    String [] temp2 = tempFirst[i].split("\\,");
	    for ( int j = 0 ; j < temp2.length ; j++)
	    {
	    	temp.add(temp2[j]);
	    }
	    for (int k = 1 ; k < temp.size(); k++) 
	    {
	    	if (temp.get(k).length()>1)
	    	{
	    		for (int n = 0 ; n < temp.get(k).length() ; n++)
	    		{
	    			String y = "" + temp.get(k).charAt(n);
	    			if (!y.equals("e")) {
	    			if (!terminals.contains(y)) {
	    				String x = "" + temp.get(k).charAt(n);
	    				terminals.add(x);
	    			}
	    			}
	    		}
	    	}
	    	else 
	    	{
	    		if (!terminals.contains(temp.get(k))) 
	    		{
	    			if (!temp.get(k).equals("e")) 
    				terminals.add(temp.get(k));
    			}
	    	}
	    }
	    first.add(temp);
	}
	for (int i = 0 ; i < tempFollow.length ; i++)
	{
		ArrayList<String> temp = new ArrayList<String> ();
	    String [] temp2 = tempFollow[i].split("\\,");
	    for ( int j = 0 ; j < temp2.length ; j++)
	    {
	    	temp.add(temp2[j]);
	    }
	    for (int k = 1 ; k < temp.size(); k++) 
	    {
	    	if (temp.get(k).length()>1)
	    	{
	    		for (int n = 0 ; n < temp.get(k).length() ; n++)
	    		{
	    			String y = "" + temp.get(k).charAt(n);
	    			if (!y.equals("e")) {
	    			if (!terminals.contains(y)) {
	    				String x = "" + temp.get(k).charAt(n);
	    				terminals.add(x);
	    			}
	    			}
	    		}
	    	}
	    	else 
	    	{
	    		if (!terminals.contains(temp.get(k))) 
	    		{
	    			if (!temp.get(k).equals("e")) 
    				terminals.add(temp.get(k));
    			}
	    	}
	    }
	    follow.add(temp);
	}
	parsingTable();
	
}


public static void parsingTable() {
	for (int i = 0 ;  i < variables.size() ; i++) {
	ArrayList<String> tempTable = new ArrayList<String>();
	tempTable.add(variables.get(i));
	for (int s = 0 ; s< terminals.size() ;s++) {
		tempTable.add(s+1,"#");
	}
	ArrayList<String> tempFirst= first.get(i);
	ArrayList<String> tempFollow= follow.get(i);
	ArrayList<String> tempRules= grammerRules.get(i);
	for (int j = 1 ; j < tempFirst.size() ; j++) 
	{
		if (tempFirst.get(j).length()>1) {
			String [] temp = new String[tempFirst.get(j).length()];
			for (int n = 0 ; n <tempFirst.get(j).length() ; n++) {
				String x =""+ tempFirst.get(j).charAt(n);
				temp[n] = x;
			}
			for (int z = 0 ; z <temp.length ; z++) {
				if (!temp[z].equals("e"))
				{
				int index = terminals.indexOf(temp[z]);
				tempTable.remove(index+1);
				tempTable.add(index+1, tempRules.get(j));
				}
			}
		}
		else {
		if (!tempFirst.get(j).equals("e"))
		{
		int index = terminals.indexOf(tempFirst.get(j));
		tempTable.remove(index+1);
		tempTable.add(index+1, tempRules.get(j));
		}
		}
		
	}
	if (tempFirst.contains("e")) 
	{
		for (int e = 1 ; e < tempFollow.size();e++) {
			if (tempFollow.get(e).length()>1) 
			{
				String [] temp = new String[tempFollow.get(e).length()];
				for (int n = 0 ; n <tempFollow.get(e).length() ; n++) {
					String x =""+ tempFollow.get(e).charAt(n);
					temp[n] = x;
				}
				for (int z = 0 ; z <temp.length ; z++) {
					if (!temp[z].equals("e"))
					{
					int index = terminals.indexOf(temp[z]);
					tempTable.remove(index+1);
					tempTable.add(index+1, "e");
					}
				}
				
			}
			else {
				if (!tempFollow.get(e).equals("e"))
				{
				int index = terminals.indexOf(tempFollow.get(e));
				tempTable.remove(index+1);
				tempTable.add(index+1, "e");
				}
				}
		}
	}
	table.add(tempTable);	
	}
	
}

public static void LL1(String parse) {
	statesStack.push("$");
	statesStack.push("S");
	int index = 0;
	boolean errorFound= false;
	String up = "";
	while (!statesStack.peek().equals("$")&& !errorFound && index != parse.length()) 
	{
		up = "";
	
		if (variables.contains(statesStack.peek())) 
		{
			int indexTerminal = terminals.indexOf(""+parse.charAt(index));
			int indexVariable = variables.indexOf(statesStack.peek());
			if (indexTerminal != -1 ) 
			{
			
			String output = table.get(indexVariable).get(indexTerminal+1);
			if (output.equals("#"))
			{
				
				statesStack.pop();
				finalOutput.add("ERROR");
				errorFound = true;
			}
			else if (output.equals("e")) 
			{	
				
				for(int j = statesStack.size()-1 ; j >=1 ; j--) {
					up = up + statesStack.elementAt(j);
				}
				if(!finalOutput.contains(parse.substring(0,index)+up))
					finalOutput.add(parse.substring(0,index)+up);
				statesStack.pop();
			}
			else 
			{
				
				for(int j = statesStack.size()-1 ; j >=1 ; j--) {
					up = up + statesStack.elementAt(j);
				}
				if(!finalOutput.contains(parse.substring(0,index)+up))
					finalOutput.add(parse.substring(0,index)+up);
				
				statesStack.pop();
				for(int i = output.length()-1 ; i >= 0 ; i--)
				{
					statesStack.add(""+output.charAt(i));
				}
			}
			}
			else {
				finalOutput.add("ERROR");
				errorFound = true;
			}
		}
		else if (terminals.contains(statesStack.peek())) 
		{
			if (statesStack.peek().equals(""+parse.charAt(index))) 
			{
				for(int j = statesStack.size()-1 ; j >=1 ; j--) 
				{
					up = up + statesStack.elementAt(j);
				}
				if(!finalOutput.contains(parse.substring(0,index)+up))
					finalOutput.add(parse.substring(0,index)+up);
				statesStack.pop();
				index++;
			}
			else 
			{
				finalOutput.add("ERROR");
				errorFound = true;
			}
		}	
		else 
		{
		if (parse.charAt(index)==statesStack.peek().charAt(0)) 
		{
			index++;
			statesStack.pop();
		}
		}
	}
	//System.out.println(finalOutput);
	String s= finalOutput.get(finalOutput.size()-1);
	boolean error=false;
	while(!error) 
	{
		if(s.equals(parse))
			break;
		if(s.equals("ERROR"))
			break;
		else 
		{
			for(int ii=0;ii<s.length();ii++) {
			char c = s.charAt(ii);
			if(!(c>='a'&&c<='z')) 
			{
				int id =variables.indexOf(c+"");
				
				ArrayList<String> k= first.get(id);
				if(k.contains("e")) 
				{
				
						String first=s.substring(0,ii);
						String last=s.substring(ii+1,s.length());
						finalOutput.add(first+last);
						s=first+last;
						error=false;
						break;
					}
					else 
						error=true;
				
			}
			else {
				error =true;
				
			}	
		}
	}
	}
	if(error)
		finalOutput.add("ERROR");
	
	String x = "";
	for (int i = 0 ; i < finalOutput.size() ; i++) 
	{
	if (i != finalOutput.size()-1 )
		x = x + finalOutput.get(i) + ",";
	else
		x = x + finalOutput.get(i);
	}
	System.out.println(x);
}
public static void main(String[] args) {

	Parsing lre5 = new Parsing("S,iD;D,VaE,nS,e;V,e;E,iV,n,e#S,i;D,a,n,e;V,e;E,i,n,e#S,$;D,$;V,a$;E,$");
	LL1("inian");
	finalOutput = new ArrayList<String> ();
	statesStack = new Stack<>();
	LL1("inia");
	finalOutput = new ArrayList<String> ();
	statesStack = new Stack<>();
	LL1("ininiai");
	finalOutput = new ArrayList<String> ();
	statesStack = new Stack<>();
	LL1("inininia");
	finalOutput = new ArrayList<String> ();
	statesStack = new Stack<>();
	LL1("inina");
	finalOutput = new ArrayList<String> ();
	statesStack = new Stack<>();
//	
//	Parsing lre5 = new Parsing("S,Mm,e;M,QR,e;R,xMy,pQmc;Q,o,e#S,mopx,e;M,opx,e;R,x,p;Q,o,e#S,$;M,my;R,my;Q,mpx");
//	LL1("xym");
//	finalOutput = new ArrayList<String> ();
//	statesStack = new Stack<>();
//	LL1("opmcm");
//	finalOutput = new ArrayList<String> ();
//	statesStack = new Stack<>();
//	LL1("xopomcym");
//	finalOutput = new ArrayList<String> ();
//	statesStack = new Stack<>();
//	LL1("xxpomcyym");
//	finalOutput = new ArrayList<String> ();
//	statesStack = new Stack<>();
//	LL1("xcycx");
//	finalOutput = new ArrayList<String> ();
//	statesStack = new Stack<>();
//	

	
	
	
}


	
}