/*
 * Siddhant Iyer
 * Net ID: siyer7
 * Project2
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
public class URCalculator {
	static Stack<Double> numbers= new Stack<Double>();
	static Stack<Character> operators= new Stack<Character>();
	static HashMap<String,Double> variables= new HashMap<String,Double>();
	static Scanner scan= new Scanner(System.in);
	static boolean varexists=false;
	//checks if the map stores any variables
	public static void main(String[] args) {
		boolean on= true;//becomes false once user exits
		while(on) {
			boolean redo=false;//if an error occurs on user's behalf, calculator restarts
			//removing any variables from the map that may have been added but the previous expression was not well formed or had some other kind of error
			for(Iterator<Map.Entry<String, Double>> it = variables.entrySet().iterator(); it.hasNext(); ) {
		      Map.Entry<String, Double> entry = it.next();
		      if(variables.get(entry.getKey())==null) {
		        it.remove();
		      }
		    }
			//emptying number and operator stacks for new operation
			while(!numbers.empty())
				numbers.pop();
			while(!operators.empty())
				operators.pop();
			
			boolean assignment=false;//when an assignment is made, it becomes true, and the output isnt displayed
			String s= scan.nextLine();
			String tempstr=s.trim();

			if(tempstr.equalsIgnoreCase("clear all")) {
				int z=0;
				for(Iterator<Map.Entry<String, Double>> it = variables.entrySet().iterator(); it.hasNext(); ) {
				      Map.Entry<String, Double> entry = it.next();
				      z++;
				      it.remove();
				}
				if(z==0)
					System.out.println("variables don't exist anyway!");
				continue;
			}
			else if(tempstr.length()>"clear".length() && tempstr.substring(0,5).equalsIgnoreCase("clear")) {
				int z=0;
				String var= tempstr.substring(6, tempstr.length());
				for(Iterator<Map.Entry<String, Double>> it = variables.entrySet().iterator(); it.hasNext(); ) {
				      Map.Entry<String, Double> entry = it.next();
				      if(entry.getKey().equals(var)) {
				      	z++;
				      	it.remove();
				      }
				}
				if(z==0)
					System.out.println("variables don't exist anyway!");
				continue;
			}
			else if(tempstr.equalsIgnoreCase("show all")) {
				int z=0;
				for(String key: variables.keySet()) {
					z++;
					System.out.println(key+"= "+variables.get(key));
				}
				if(z==0)
					System.out.println("No variables stored yet");
				continue;
			}
			else if(tempstr.equalsIgnoreCase("exit"))
				break;
			
			s="";
			//removing all spaces, unless it isnt a well formed expression
			for(int j=0; j<tempstr.length(); j++) {
				char chtemp= tempstr.charAt(j);
				if(chtemp==' ') {
					char ch1= tempstr.charAt(j-1);
					for(int i=j+1; i<tempstr.length(); i++) {
						char ch2=tempstr.charAt(i);
						if(ch2!=' ') {
			//if, before and after the space, are 2 digits or variables, it means that the expression isnt well formed. So I'm taking care of that.
							if((Character.isDigit(ch1) || (65<=ch1 && ch1<=90) || (97<=ch1 && ch1<=122)) && (Character.isDigit(ch2) || (65<=ch2 && ch2<=90) || (97<=ch2 && ch2<=122))) {
								System.out.println("Not a well formed expression!");
								redo=true;
								j=tempstr.length()+10;
							}
							break;
						}
					}
				}
				if(redo)
					break;
				if(chtemp!=' ')
					s+=chtemp;//making a new string with all the spaces removed
			}
			if(redo)
				continue;
			String str="";
			boolean start=false;
			int counter=0;

			//making sure first the number of each kind of opening and closed brackets are the same
			int para1=0,parb1=0,parc1=0,para2=0,parb2=0,parc2=0;
			for(int i0=0; i0<s.length(); i0++) {
				char c1= s.charAt(i0);
				if(c1=='(')
					para1++;
				if(c1=='{')
					parb1++;
				if(c1=='[')
					parc1++;
				if(c1==')')
					para2++;
				if(c1=='}')
					parb2++;
				if(c1==']')
					parc2++;
			}
			if(para1!=para2 || parb1!=parb2 || parc1!=parc2) {
				System.out.println("Not well formed expression (parenthesis mismatch)");
				redo=true;
				continue;
			}
				//main program begins, by considering one character at a time
				for(int i=0; i<s.length(); i++) {
					char c= s.charAt(i);
					if(c=='+') {
					//need to count the total number of +s or -s that occur together.
					//this is what the counter variable is used for- to compute the overall sign, whether + or -
						int tempi=i-1;
						if(numbers.empty())
							start=true;
						counter=0;
						while(++i<s.length()) {
							if(s.charAt(i)=='-')
								counter++;
						//invalid if any other sign comes in between the + and -, or if it occurs at the end of the expression
							else if(s.charAt(i)=='/' || s.charAt(i)=='*') {
								System.out.println("Ïnvalid expression");
								i=s.length()+10;
								redo=true;
								break;
							}
						//reached the next digit/variable
							else if(Character.isDigit(s.charAt(i)) || s.charAt(i)=='.'|| s.charAt(i)=='('|| s.charAt(i)=='{'|| s.charAt(i)=='[' || ((65<=s.charAt(i) && s.charAt(i)<=90) || (97<=s.charAt(i) && s.charAt(i)<=122)))
								break;
						}
						if(i>=s.length()) {
							System.out.println("Ïnvalid expression");
							redo=true;
						}
						if(redo)
							break;
						i--;
						//handles the case in which the negative appears at the beginning of the expression
						if(start) {
							start=false;
							if(counter%2==1) {
								numbers.push(0.0);
								c='-';
							}
						}
						else {//here is where the final sign is figured out
							if(counter%2==1)
								c='-';
							else {
								if(tempi<0 || s.charAt(tempi)=='(' || s.charAt(tempi)=='{' || s.charAt(tempi)=='[')
									continue;
							}
						}
					}
					//same logic for a negative operator as for the + explained above
					else if(c=='-') {
						int tempi=i-1;
						if(numbers.empty())
							start=true;
						counter=0;
						while(++i<s.length()) {
							if(s.charAt(i)=='-')
								counter++;
							else if(s.charAt(i)=='/' || s.charAt(i)=='*') {
								System.out.println("Ïnvalid expression");
								i=s.length()+10;
								redo=true;
								break;
							}
							else if(Character.isDigit(s.charAt(i)) || s.charAt(i)=='.'|| s.charAt(i)=='('|| s.charAt(i)=='{'|| s.charAt(i)=='[' || ((65<=s.charAt(i) && s.charAt(i)<=90) || (97<=s.charAt(i) && s.charAt(i)<=122)))
								break;
						}
						if(i>=s.length()) {
							System.out.println("Ïnvalid expression");
							redo=true;
						}
						if(redo)
							break;
						i--;
						if(start) {	
							start=false;
							if(counter%2==0) {
								numbers.push(0.0);	
								c='-';
							}
						}
						else {
							if(counter%2==1)
								c='+';
							if(tempi<0 || s.charAt(tempi)=='(' || s.charAt(tempi)=='{' || s.charAt(tempi)=='[')
								continue;
						}
					}
					if(redo)
						break;
					//'.' to account for a decimal number
					if(Character.isDigit(c) || c=='.') {
						int checkcounter=0;
						if(c=='.')
							checkcounter=1;
						str+=c+"";
						//keep concatenating the digits that appear together
						while(++i<s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i)=='.')) {
							str+=s.charAt(i);
							if(s.charAt(i)=='.')
								checkcounter++;
						}
						i--;
						if(checkcounter<=1)
							numbers.push(Double.valueOf(str));
						else {
						// if more than 1 period occurs in between the digits of a number
							System.out.println("Ïnvalid expression");
							i=s.length()+10;
							redo=true;
							break;
						}
						if(redo)
							break;
						str="";
					}
					else if(c=='+' || c=='-' || c=='/' || c=='*' || c=='(' || c=='{' || c=='[') {
						//now to convert from postfix to infix, by performing the operation on the last 2 numbers in the stack
						if(!operators.empty()) {
							char temp= operators.peek();
							while((!operators.empty()) && c!='(' && c!='{' && c!='[' && ((c==temp) || (c=='/' && temp=='*') || (c=='-' && (temp=='+' || temp=='/' || temp=='*')) || (c=='+' && (temp=='-' || temp=='/' || temp=='*')) || (c=='*' && temp=='/'))) { 
								double b=numbers.pop();
								double a=numbers.pop();
								double result=0;
								char ch=operators.pop();
								if(ch=='+')
									result=a+b;
								else if(ch=='-')
									result=a-b;
								else if(ch=='*')
									result=a*b;
								else if(ch=='/') {
									if(b==0) {
										System.out.println("Can't divide by 0");
										i=s.length()+10;
										redo=true;
										break;
									}
									else
										result=a/b;
								}
								if(redo)
									break;
								numbers.push(result);
								if(!operators.empty())
									temp=operators.peek();
							}
						}
						if(redo)
							break;
						operators.push(c);
					}
					else if(c=='}') {
						char temp= operators.peek();
						if(temp=='[' || temp=='(') {
							//parenthesis mismatch
							System.out.println("Parenthesis mismatch!");
							i=s.length()+10;
							redo=true;
							break;
						}
						if(redo)
							break;
						while(temp!='{') {
						//once the bracket is encountered, I compute the value within the brackets
							double b=numbers.pop();
							double a=numbers.pop();
							double result=0;
							char ch=operators.pop();
							if(ch=='+')
								result=a+b;
							else if(ch=='-')
								result=a-b;
							else if(ch=='*')
								result=a*b;
							else if(ch=='/') {
								if(b==0) {
									System.out.println("Can't divide by 0");
									i=s.length()+10;
									redo=true;
									break;
								}
								else
									result=a/b;
							}
							if(redo)
								break;
							numbers.push(result);
							if(!operators.empty())
								temp=operators.peek();
							if(temp=='(' || temp=='[' || operators.empty()) {
								System.out.println("Parenthesis mismatch!");
								i=s.length()+10;
								redo=true;
								break;
							}
							if(redo)
								break;
						}
						if(redo)
							break;
						if(!(i>s.length()))
							operators.pop();
					}
					//same logic applies to next 2 types of brackets
					else if(c==')') {
						char temp= operators.peek();
						if(temp=='{' || temp=='[') {
							System.out.println("Parenthesis mismatch!");
							i=s.length()+10;
							redo=true;
							break;
						}
						if(redo)
							break;
						while(temp!='(') {
							double b=numbers.pop();
							double a=numbers.pop();
							double result=0;
							char ch=operators.pop();
							if(ch=='+')
								result=a+b;
							else if(ch=='-')
								result=a-b;
							else if(ch=='*')
								result=a*b;
							else if(ch=='/') {
								if(b==0){
									System.out.println("Can't divide by 0");
									i=s.length()+10;
									redo=true;
									break;
								}
								else
									result=a/b;
							}
							if(redo)
								break;
							numbers.push(result);
							if(!operators.empty())
								temp=operators.peek();
							if(temp=='{' || temp=='[' || operators.empty()) {
								System.out.println("Parenthesis mismatch!");
								redo=true;
								break;
							}
							if(redo)
								break;
						}
						if(redo)
							break;
						if(!(i>s.length()))
							operators.pop();
					}
					else if(c==']') {
						char temp= operators.peek();
						if(temp=='{' || temp=='(') {
							System.out.println("Parenthesis mismmatch!");
							i=s.length()+10;
							redo=true;
							break;
						}
						if(redo)
							break;
						while(temp!='[') {
							double b=numbers.pop();
							double a=numbers.pop();
							double result=0;
							char ch=operators.pop();
							if(ch=='+')
								result=a+b;
							else if(ch=='-')
								result=a-b;
							else if(ch=='*')
								result=a*b;
							else if(ch=='/') {
								if(b==0){
									System.out.println("Can't divide by 0");
									i=s.length()+10;
									redo=true;
									break;
								}
								else
									result=a/b;
							}
							if(redo)
								break;
							numbers.push(result);
							if(!operators.empty())
								temp=operators.peek();
							if(temp=='{' || temp=='(' || operators.empty()) {
								System.out.println("Parenthesis mismatch!");
								redo=true;
								i=s.length()+10;
								break;
							}
						}
						if(redo)
							break;
						if(!(i>s.length()))
							operators.pop();
					}
					else if((65<=c && c<=90) || (97<=c && c<=122)) {
					//using ASCII values of upper or lower case variables
						str="";
						str+=c;
					//I'll keep concatenating if consecutive letters occur, to form the whole variable name
						while(++i<s.length()) {
							if((65<=s.charAt(i) && s.charAt(i)<=90) || (97<=s.charAt(i) && s.charAt(i)<=122))
								str+=s.charAt(i);
							else if(s.charAt(i)=='=') {
							//if this appears, I will not print the output
								assignment=true;
								variables.put(str,null);
								varexists=true;
							//now, the map holds some value
								i++;
								break;
							}
							else if(s.charAt(i)=='+' || s.charAt(i)=='-' || s.charAt(i)=='/' || s.charAt(i)=='*' || s.charAt(i)==')' || s.charAt(i)=='}' || s.charAt(i)==']') {
								if(variables.get(str)==null) {
									System.out.println("Undefined variable.");
									i=s.length()+10;
									redo=true;
									break;
								}
								else
							//adding variable to map
									numbers.push(variables.get(str));
								if(redo)
									break;
								break;
							}
						}
						if(redo)
							break;
						if(!(i<s.length())) {
							if(variables.get(str)==null) {
								System.out.println("Undefined variable.");
								i=s.length()+10;
								redo=true;
							}
							else
								numbers.push(variables.get(str));
						}
						if(redo)
							break;
						i--;
						str="";
					}
					else {
						System.out.println("Inavlid expression.");
						i=s.length()+10;
						redo=true;
					}
					if(redo)
						break;
				}
				if(redo)
					continue;
				while(!operators.empty()) {
				//popping the last 2 numbers from the stack to perform the operation on them
					double b=numbers.pop();
					double a=numbers.pop();
					double result=0;
					char ch=operators.pop();
					if(ch=='+')
						result=a+b;
					else if(ch=='-')
						result=a-b;
					else if(ch=='*')
						result=a*b;
					else if(ch=='/') {
						if(b==0){
							System.out.println("Can't divide by 0");
							redo=true;
							break;
						}
						else
							result=a/b;
					}
					numbers.push(result);//pushing the new computed value back into the stack
				}
				if(redo)
					continue;
				double result= numbers.pop();
				if(!assignment)
				//printing output only if assignment hasnt occured
					System.out.println(result);
				if(varexists) {
				//to assign the value of the result computed to the variables in the map
					for(String key: variables.keySet()) {
						if(variables.get(key)==null)
							variables.put(key,result);
					}
				}
		}
	}
}