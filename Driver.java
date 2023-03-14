package google_test1;

import java.io.*;
import java.util.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public class Driver {
//this function gets the word itself removing punctiation.
	public static String getKeyWord(String word)
	{
		if(word==null)
			return null;
		word = word.toLowerCase();
		String alpha = "abcdefghijklmnopqrstuvwxyz";
		
		while(true)
		{
			int w = word.length()-1;
			if ((int)word.charAt(w)<97||(int)word.charAt(w)>122)
			{
				if (w != 0)
					word = word.substring(0,w);
				else
					return null;
			}
			else
				break;
			}
		int i=0;
		for (int w = 0 ; w < word.length();w++)
		{
			for (i = 0 ; i < alpha.length() ; i++)
			{
				if (word.charAt(w) == alpha.charAt(i))
					break;
				else if((int)word.charAt(w)<97||(int)word.charAt(w)>122)
				{
					word=word.substring(0,w)+word.substring(w+1,word.length());
					break;
				}
			}
				if (i == alpha.length())
				{
					return null;
				}
					
				i=0;
		}
		
		return word;
	}


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//setting JFrame defaults.
		JFrame frame= new JFrame();
		frame.setSize(600,400);
		//JPanel panel= new JPanel();
		JLabel l0=new JLabel();
		l0.setBounds(50,20,300,20);
		JLabel l1=new JLabel();
		l1.setBounds(50,50,200,30);
		JTextField t1= new JTextField();
		t1.setBounds(50,90,200,30);
		JTextField t2= new JTextField();
		t2.setBounds(50,200,500,50);
		l1.setText("Enter your query: ");
		l0.setText("WELCOME ASK.OE: YOUR LITTLE SEARCH ENGINE");
		JButton btn= new JButton("search");
		btn.setBounds(50,150,200,30);

		FileReader fileReader = new FileReader("docs.txt");
		String line;
	    String doc;
        System.out.println("This can take a while, please wait a few seconds...");
		BufferedReader br = new BufferedReader(fileReader);
		HashedDictionary<String, ArrayList<Occurence>> hashedTable = new HashedDictionary<>(2500);
		HashedDictionary<String, ArrayList<Occurence>> table = new HashedDictionary<>(2500);
		
		while ((line = br.readLine()) != null)
		{
            line=line.toLowerCase(Locale.ENGLISH);
            doc=line;
           
 	       Scanner sc = null;
 		try {
 			sc = new Scanner(new File(doc));
 		} catch (FileNotFoundException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} 
            while(sc.hasNext())
            {
             String word = sc.next();
             int docnum=Integer.parseInt(doc.substring(0,3));
             int i=docnum-1;
             word=getKeyWord(word);
             if(word!=null)
             { 
                //creating the arraylist of word and increase the frequency everytime the word occurs.
                 if(!hashedTable.contains(word).contain)
                 {
                	 ArrayList<Occurence> list= new ArrayList<Occurence>();
                	 for(int j=0;j<100;j++)
                	 {
                		 String file;
                		 if(j<9)
                		 {
                			 file="00"+String.valueOf(j+1)+".txt";
                		 }
                		 else if(j>98)
                		 {
                			 file=String.valueOf(j+1)+".txt";
                		 }
                		 else
                		 {
                			 file="0"+String.valueOf(j+1)+".txt";
                		 }
                		 Occurence occur=new Occurence(word,file,0);
                		 list.add(j,occur);
                	 }
                	 list.get(i).frequency++;
                	 hashedTable.add(word, list);
                 }
                 
                 else if(hashedTable.contains(word).contain)
                 {
                	 ArrayList<Occurence> temp=hashedTable.getValue(word);
                	 temp.get(i).frequency++;
                	 hashedTable.add(word, temp);
                 } 
             }

 
       
            }
    		

     
		}
		//String input = null;
		btn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// taking query from user and splitting it word by word
				    String input=t1.getText();
					String[] queryWords=input.split(" ");
					String a,b,c;
					a=queryWords[0];
					b=queryWords[1];
					c=queryWords[2];
					//Arraylists for each word's document-frequency arraylist in hashtable.
					ArrayList<Occurence> find_1 = null;
					ArrayList<Occurence> find_2 = null;
					ArrayList<Occurence> find_3 = null;
					if(hashedTable.contains(a).contain)
					{
						find_1=hashedTable.getValue(a);
					}
					if(hashedTable.contains(b).contain)
					{
						 find_2=hashedTable.getValue(b);
					}
					if(hashedTable.contains(c).contain)
					{
				  		 find_3=hashedTable.getValue(c);
					}
			  	    //count_word here calculates the number of documents contains that word.
					int count_a=0;
					int count_b=0;
					int count_c=0;
			        for(int i=0;i<100;i++)
			        {
			     	   if(hashedTable.contains(a).contain&&find_1.get(i).frequency!=0)
			     	   {
			     		   count_a++;
			     	   }
			     	   if(hashedTable.contains(b).contain&&find_2.get(i).frequency!=0)
			     	   {
			     		   count_b++;
			     	   }
			     	   if(hashedTable.contains(c).contain&&find_3.get(i).frequency!=0)
			     	   {
			     		   count_c++;
			     	   }
			        }
					
					FileReader fileReader_2 = null;
					try {
						fileReader_2 = new FileReader("docs.txt");
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					String line_2;
					String doc_2;
					int totalNum;
					int docNum=0;
					//NTF calculates normalized term frequency.
					double NTF_a,NTF_b,NTF_c;
					//IDF calculates inversed document frequency.
					double IDF_a = 0;
					double IDF_b=0;
					double IDF_c=0;
					//matrix contains TF*IDF values for each word in query and each document
					double[][] matrix = new double[3][100];
					//M's represent TF*IDF values for query words
					double M1=0;
					double M2=0;
					double M3=0;
					double dotProduct;
					double vecQuery;
					double vecDocument;
					double cosineSimilarity;
					double min_sim=2;
					// x_word represens word's frequency.
					double x_a;
					double x_b;
					double x_c;
					String output="Sorry, we can not find what you are looking for. :(";
					//end of declaration table
					BufferedReader br_2 = new BufferedReader(fileReader_2);
					try {
						while ((line_2 = br_2.readLine()) != null)
						{
						    line_2=line_2.toLowerCase(Locale.ENGLISH);
						    doc_2=line_2;
						    totalNum=0;
						    
						   Scanner sc_2 = null;
						try {
							sc_2 = new Scanner(new File(doc_2));
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						    while(sc_2.hasNext())
						    {
						        String str = sc_2.next();
						    	totalNum++;
						    }
						    if(!hashedTable.contains(a).contain)
						    {
						    	x_a=0;
						    }
						    else
						    	x_a=find_1.get(docNum).frequency;
						    if(!hashedTable.contains(b).contain)
						    {
						    	x_b=0;
						    }
						    else
						    	x_b=find_2.get(docNum).frequency;
						    if(!hashedTable.contains(c).contain)
						    {
						    	x_c=0;
						    }
						    else
						    	x_c=find_3.get(docNum).frequency;
						    
      
      NTF_a=x_a/totalNum;
      NTF_b=x_b/totalNum;
      NTF_c=x_c/totalNum;

      if(!hashedTable.contains(a).contain)
						   IDF_a=1;
      else if(hashedTable.contains(a).contain)
						   IDF_a=1+Math.log(100/count_a);
      if(!hashedTable.contains(b).contain)
						   IDF_b=1;
      else if(hashedTable.contains(b).contain)
						   IDF_b=1+Math.log(100/count_b);
      if(!hashedTable.contains(c).contain)
						   IDF_c=1;
      else if(hashedTable.contains(c).contain)
						   IDF_c=1+Math.log(100/count_c);

      matrix[0][docNum]=NTF_a*IDF_a;
      matrix[1][docNum]=NTF_b*IDF_b;
      matrix[2][docNum]=NTF_c*IDF_c;
       M1=0.3*IDF_a;
	   M2=0.3*IDF_b;
	   M3=0.3*IDF_c;
	   docNum++;

						    
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					vecQuery=Math.sqrt((Math.pow(M1,2)+Math.pow(M2,2)+Math.pow(M3,2)));
					for(int i=0;i<100;i++)
					{
						dotProduct=(M1*matrix[0][i])+(M2*matrix[1][i])+(M3*matrix[2][i]);	
						vecDocument=Math.sqrt((Math.pow(matrix[0][i],2)+Math.pow(matrix[1][i],2)+Math.pow(matrix[2][i],2)));
						cosineSimilarity=dotProduct/(vecQuery*vecDocument);
						cosineSimilarity=Math.abs(1-cosineSimilarity);
						// the more the cosine similarity is closer to 1 the more relevant the document.
						if(cosineSimilarity<min_sim)
						{
							min_sim=cosineSimilarity;
							 if(i<9)
			        		 {
			        			 output="00"+String.valueOf(i+1)+".txt";
			        		 }
			        		 else if(i>98)
			        		 {
			        			 output=String.valueOf(i+1)+".txt";
			        		 }
			        		 else
			        		 {
			        			 output="0"+String.valueOf(i+1)+".txt";
			        		 }
						}
					}
					
					t2.setText("this may be what you looking for: \n"+output);
			}
			
		});
		frame.add(l0);
		frame.add(l1);
		frame.add(t1);;	
		frame.add(btn);	
		frame.add(t2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
        //end of frame settings



	}

}
	
