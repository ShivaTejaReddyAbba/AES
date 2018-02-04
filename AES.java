import java.io.*;
import java.util.*;

public class AES 
{
	private static Scanner br;
	static String k,p,c;
      
    public static ArrayList<String> str = new ArrayList<String>();
	public static ArrayList<Integer> m = new ArrayList<Integer>();
	
	
	
	public static void main(String[] args) throws IOException
	{
		AES obj = new AES();
		try
		{
			br = new Scanner(new File("input.txt"));
			while(br.hasNext())
    		{
    			str.add(br.next());	
    	    }
    	for(int i=0;i<str.size();i++)
    	{
    		if(i<=8)
    		{
    			m.add(0,Integer.valueOf(str.get(i)));
    		}
    		if(i==9)
    		{
    			 k=str.get(i);
    			
    		}
    		if(i==10)
    		{
    			 p=str.get(i);
    		
    			
    		}
    		if(i==11)
    		{
    		     c=str.get(i);
    		    
    		}
    	}
    	String[][] key =new String[44][4];
    	key=obj.keyexpansion(k);
    	String[][] pt = decrypt(p,key);    	
    	String[][] ct = encrypt(c,key);
    	 FileWriter outputFile = new FileWriter(new File("Output.txt"));
    	 String result1=cas(pt);
    	 outputFile.write(result1+" ");
    	 String result2=cas(ct);
    	 outputFile.write(System.getProperty("line.separator"));
    	 outputFile.write(result2+" ");
    	
    	
    	
    	   
    	  System.lineSeparator();
     	  
    	   outputFile.close(); 
    	
		}
		catch(FileNotFoundException e) 
		{
    		System.out.println("couldn't find the file");	
    	}
	
	}
	
	private static String cas(String[][] pt) {
		String res="";
		String res1="";
		
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				res=pt[i][j];
				res1=res1.concat(res);
			}
		}
		
		return res1;
	}

	private static String[][] decrypt(String c2, String[][] key2) {
		String[][] in=new String[4][4];
		int t=0;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{	
					String sub = c2.substring(t,t+2);
					in[i][j]=sub;
					t=t+2;					
			}
		}
		int i=0;
		String[][] temp=addroundkey(in,key2,i);
		for(i=0;i<9;i++)
		{
			temp=subbyte(temp);
			temp=shiftrow(temp);
			temp=mixcol(temp);
			temp=addroundkey(temp,key2,i+1);
		}
		temp=subbyte(temp);
		temp=shiftrow(temp);
		temp=addroundkey(temp,key2,10);
		return temp;
	}




	private static String[][] mixcol(String[][] temp) {
		String[][] mul = {{"02","03","01","01"},{"01","02","03","01"},{"01","01","02","03"},{"03","01","01","02"}};
	    int[] temp2=new int[8];
	    int[] temp3=new int[8];
	    int[] res=new int [15];
	    int[] res2=new int[15];
	    String[][] fres=new String[4][4];
	    for(int i=0;i<4;i++)
	    {
	    	for(int j=0;j<4;j++)
	    	{
	    		for(int l=0;l<4;l++)
	    		{
	    			temp2=csb(mul[j][l]);
	    			temp3=csb(temp[i][l]);
	    			res=bmul(temp2,temp3);
	    			for(int m=0;m<res.length-1;m++)
	    			{
	    				res2[m]=(res2[m]+res[m])%2;
	    			}
	    		}
	    		temp3=check(res2);
	    		fres[i][j]=CBH(temp3);
	    		Arrays.fill(res2, 0);
	    	}
	    }
		return fres;
	}

	private static int[] check(int[] res2) {
		    ArrayList<Integer> al = new ArrayList<Integer>();
	        ArrayList<Integer> bl = new ArrayList<Integer>();
            for(int i=0;i<=res2.length-1;i++)
            {
           	 al.add(res2[i]);
            } 

            for(int i=0;i<=m.size()-1;i++)
            {
           	 bl.add(m.get(i));
            }
   		    while(al.size()>1&&al.get(al.size()-1)==0)
   	        {
   	       	 al.subList(al.size()-1,al.size()).clear(); 
   	        } 
            
	        if(al.size()>=bl.size())                                                      
	        {     	 
	            while(al.get(al.size()-1)==0)
	            {
	           	 al.subList(al.size()-1,al.size()).clear(); 
	            }
	           int i,temp;
	           int  pos=0;
	           int[] q1=new int[15];
	       	 while(al.size()>=bl.size())
	       	 {
	     		   temp = 0;
	     		   i=0;
	               Arrays.fill(q1, 0);
	     		   while(temp==0)
	     		    {
	     			    int c =  al.get(al.size()-1);
	         		    int d = bl.get(bl.size()-1);
	     		    	i++;
	     		    	d=d*i;
	     		    	int sub = c-d;
	     		    	if(sub%2==0)
	     		    	{
	     		    		temp=1;
	     		    		
	     		    	}
	     		    	    	    	    		    	  		    	
	     		    }
	     		   
	     		    for(int t=0; t<bl.size();t++)
	     		    {
	     		    	pos= al.size() - bl.size();
	     		    	pos=pos+t;
	     		    	int temp2;
	     		    	temp2=i*bl.get(t);
	     		     q1[pos]=temp2;
	     		    }
	     		    
	     		    for(int t=0;t<al.size();t++)
	     		    {
	     		    	al.set(t,al.get(t)-q1[t]);
	     		    }
	     		  al.subList(al.size()-1,al.size()).clear(); 
	     		 
	       	 }
	        }
	       	 int[] result = new int[8];
	       	for(int et=0;et<al.size();et++)
	       	{
	       		al.set(et, al.get(et)%2);
	       	}
	       	  
	       	 for(int et=0;et<al.size();et++)
	       	 {
	       		 if(al.get(et)>=0)
	       		 {result[et]=(al.get(et)%2); }
	       		 else
	       		 {result[et]=(al.get(et)%2)+2;}
	       		 
	       	 }
	        
		return result;
	}

	private static int[] bmul(int[] temp2, int[] temp3) {
          int[] res = new int[15];
          for(int i =0;i<8;i++)
          {
        	  for(int j=0;j<8;j++)
        	  {
        		  res[i+j]+=temp2[i]*temp3[j];
        	  }
          }

		return res;
	}

	private static String[][] shiftrow(String[][] temp) {
		String[][] temp1=temp;
		int i=0,count;
		String s1="";
		for(int j=1;j<=3;j++)
		{
			count=j;
			while(count>0)
			{
				i=0;
				s1=temp1[i][j];
				while(i<3)
				{
					temp1[i][j]=temp1[i+1][j];
				    i++;
				}
				temp1[3][j]=s1;
				count--;
			}
		}
		
		return temp1;
	}

	private static String[][] subbyte(String[][] temp) {
		String[][] temp1 = temp;
		int[] temp2 = new int[8];
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				String s=temp1[i][j];
				temp2=csb(s);
				temp2=EEA(temp2);
				temp2=t2(temp2);
				temp[i][j]=CBH(temp2);
			}
		}		
		return temp;
	}

	private static String[][] addroundkey(String[][] in, String[][] key2, int i2) {
		int[] res1 = new int[8];
		int[] res2 = new int[8];
		int i3 = i2*4;
		for(int i=i3,count=0;count<4;i++,count++)
		{
			for(int j=0;j<4;j++)
			{
			 res1 = csb(in[count][j]);	
			 res2 = csb(key2[i][j]);
			 for(int a=0;a<8;a++)
			 {
				 res1[a]=res1[a]^res2[a];
			 }
			 in[count][j]=CBH(res1);
			}
		}
		
		return in;
	}

	private static String[][] encrypt(String p2, String[][] key2) {
		String[][] in=new String[4][4];
		int t=0;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{	
					String sub = p2.substring(t,t+2);
					in[i][j]=sub;
					t=t+2;					
			}
		}
		int i=0;
		String[][] temp=addroundkey(in,key2,10);
		for(i=8;i>=0;i--)
		{
			temp=inshiftrow(temp);
			temp=insubbyte(temp);
			temp=addroundkey(temp,key2,i+1);
			temp=inmixcol(temp);
			
		}
		temp=inshiftrow(temp);
		temp=insubbyte(temp);
		temp=addroundkey(temp,key2,0);
		
		return temp;
	}




	private static String[][] inmixcol(String[][] temp) {
		String[][] mul = {{"0e","0b","0d","09"},{"09","0e","0b","0d"},{"0d","09","0e","0b"},{"0b","0d","09","0e"}};
	    int[] temp2=new int[8];
	    int[] temp3=new int[8];
	    int[] res=new int [15];
	    int[] res2=new int[15];
	    String[][] fres=new String[4][4];
	    for(int i=0;i<4;i++)
	    {
	    	for(int j=0;j<4;j++)
	    	{
	    		for(int l=0;l<4;l++)
	    		{
	    			temp2=csb(mul[j][l]);
	    			temp3=csb(temp[i][l]);
	    			res=bmul(temp2,temp3);
	    			for(int m=0;m<res.length-1;m++)
	    			{
	    				res2[m]=(res2[m]+res[m])%2;
	    			}
	    		}
	    		temp3=check(res2);
	    		fres[i][j]=CBH(temp3);
	    		Arrays.fill(res2, 0);
	    	}
	    }
		
		return fres;
	}

	private static String[][] insubbyte(String[][] temp) {
		String[][] temp1 = temp;
		int[] temp2 = new int[8];
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				String s=temp1[i][j];
				temp2=csb(s);
				temp2=int2(temp2);
				temp2=EEA(temp2);
				temp[i][j]=CBH(temp2);
			}
		}		
		return temp;
	}

	private static int[] int2(int[] temp2) {
		int[][] mul= {{0,0,1,0,0,1,0,1},{1,0,0,1,0,0,1,0},{0,1,0,0,1,0,0,1},{1,0,1,0,0,1,0,0},
				                {0,1,0,1,0,0,1,0},{0,0,1,0,1,0,0,1},{1,0,0,1,0,1,0,0},{0,1,0,0,1,0,1,0}};
		int[] add = {1,0,1,0,0,0,0,0};
		int[] res = new int[8];
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
			res[i]+=mul[i][j]*temp2[j];	
			}
		}
		for(int i=0;i<8;i++){
			res[i]+=add[i];
			res[i]=res[i]%2;
		}
		return res;
	}

	private static String[][] inshiftrow(String[][] temp) {
		String[][] temp1=temp;
		int i=0,count;
		String s1="";
		for(int j=1;j<=3;j++)
		{
			count=j;
			while(count>0)
			{
				i=3;
				s1=temp1[3][j];
				while(i>0)
				{
					temp1[i][j]=temp1[i-1][j];
				    i--;
				}
				temp1[0][j]=s1;
				count--;
			}
		}	
		return temp1;
	}

	private String[][] keyexpansion(String k2)
	{
		String rc[]=rcgen();
		String[][] key1 = new String[44][4];
		int k=0;
		for(int i = 0;i<4;i++)
		{
			for(int j =0;j<4;j++)
			{
				
			String sub = k2.substring(k,k+2);
			key1[i][j]=sub;
			k=k+2;
			}
		}
		for(int i=4;i<44;i++)
		{
			String[] temp = new String[4];
			for(int j=0;j<4;j++)
			{
			temp[j]=key1[i-1][j];
			}
			if(i%4==0)
			{
					temp = rotword(temp);
					temp= subword(temp,i,rc);	
			}
			for(int j=0;j<4;j++)
			{
				int[] p1 = new int[8];
				String s1 = key1[i-4][j];
				String s2 = temp[j];
				int[] temp2 =new int[8];
				int[] temp3 = new int[8];
				temp2=csb(s1);
				temp3=csb(s2);
				for(int k1=0;k1<=7;k1++)
				{
				p1[k1]=temp2[k1]^temp3[k1];
				}
				key1[i][j]=CBH(p1);
				
			}
		}
		return key1;
	}

	private String[] rcgen() {
		String[] rc = new String[11];
		rc[0]="00";
		rc[1]="01";
		for(int i=2;i<=10;i++)
		{
			int[] temp=csb(rc[i-1]);
			int[] temp2 = {0,1,0,0,0,0,0,0};
			int[] temp3=bmul(temp,temp2);
			temp3=check(temp3);
			rc[i]=CBH(temp3);
		}
		
		return rc;
	}

	private static String CBH(int[] p1) {
		String s="",s1="";
		for(int i=3;i>=0;i--)
			s += ""+p1[i];
		String result = Long.toHexString(Long.parseLong(s,2));
		for(int i=7;i>=4;i--)
			s1+=""+p1[i];
		String result1 =Long.toHexString(Long.parseLong(s1,2));
		
		result1=result1.concat(result);
		
		return result1;
	}

    private String[] rotword(String[] temp) {
		String[] sub=new String[4];
		int j;
		for(int i=3;i>-1;i--)
		{
			j=i-1;
			if(j==-1)
				j=3;
			sub[j]=temp[i];				
		}
		return sub;
	}

	private String[] subword(String[] temp, int i2, String[] rc2) 
	{
		String s;
		String[] temp2 = new String[8];
		int[] temp3 = new int[8];
		int[] temp4 = new int[8];
		for(int i=0;i<4;i++)
		{
			temp3=csb(temp[i]);
			temp3 = EEA(temp3);
			temp3 = t2(temp3);
			if(i==0)
			{
                s=rc2[i2/4];
               temp4=csb(s);
               for(int j=7;j>=0;j--)
               {
            	   temp3[j]=temp3[j]^temp4[j];
               }
			}
	//	String s1 = Arrays.toString(temp3);
		temp2[i]=CBH(temp3);
		}
		return temp2;
	}




	private static int[] csb(String string) {
		int[] temp3 = new int[8];
		String s=string;
		String s1=s.substring(1);
		s=s.substring(0,1);
		int l = Integer.valueOf(s,16);
		int l1 = Integer.valueOf(s1,16);
		s=Integer.toBinaryString(l);
		s1=Integer.toBinaryString(l1);
		String kl="0";
		while(s.length()<4)
			{s=kl.concat(s);}
			
		while(s1.length()<4)
		{s1=kl.concat(s1);}
		s=s.concat(s1);
		int et=-1;
		for(int j=s.length()-1;j>-1;j--)
		{
			et++;
			String str = Character.toString(s.charAt(j));
			temp3[et]=Integer.parseInt(str);
		}
		return temp3;
	}




	private static int[] t2(int[] temp3) {
		int[][] mul = {{1,0,0,0,1,1,1,1},{1,1,0,0,0,1,1,1},{1,1,1,0,0,0,1,1},{1,1,1,1,0,0,0,1},
				{1,1,1,1,1,0,0,0},{0,1,1,1,1,1,0,0},{0,0,1,1,1,1,1,0},{0,0,0,1,1,1,1,1}};
		int[] add = {1,1,0,0,0,1,1,0};
		int[] res = new int[8];
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
			res[i]+=mul[i][j]*temp3[j];	
			}
		}
		for(int i=0;i<8;i++){
			res[i]+=add[i];
			res[i]=res[i]%2;
		}
		return res;
	}




	private static int[] EEA(int[] temp3) {
        int[][] q = new int[9][9];
       int[] lo1=new int[9];
       ArrayList<Integer> al = new ArrayList<Integer>();
       ArrayList<Integer> bl = new ArrayList<Integer>();
       ArrayList<Integer> lo = new ArrayList<Integer>();
       int[] q1=new int[9];
       int[] r1=new int[50];
        for(int i=0; i<m.size();i++)
        {
       	 al.add(m.get(i));
        }
        for(int i=0;i<temp3.length;i++)
        {
       	 bl.add(temp3[i]);
        }
        while(bl.get(bl.size()-1)==0&&bl.size()>1)
        {
       	 bl.subList(bl.size()-1,bl.size()).clear(); 
        }
        Arrays.fill(q1, 0);
      if(!(bl.size()==1&&bl.get(bl.size()-1)==0) )
      {
       q[0][0]=0;
      int r= 0;
      int temp1 = 0;
      int temp;
      int i;
	while(r==0)                                                                           
      {     
   	  temp1++;
   	   while(al.size()>=bl.size())
   	   {
   		   int pos= al.size() - bl.size();
   		   temp = 0;
   		   i=0;
             
   		   while(temp==0)
   		    {
       		    int c =  al.get(al.size()-1);
       		    int d = bl.get(bl.size()-1);
   		    	i++;
   		    	d=d*i;
   		    	int sub = c-d;
   		    	if(sub%2==0)
   		    	{
   		    		temp=1;
   		    		q[temp1][pos]=i;
   		    	}
   		    	    	    	    		    	  		    	
   		    }   		   
   		    for(int t=0; t<bl.size();t++)
   		    {   		    	
   		    	int temp2=0;
   		    	temp2=i*bl.get(t);
   		     lo1[pos+t]=temp2;
   		    }  		    
   		    for(int t=0;t<al.size();t++)
   		    {
   		    	al.set(t,(al.get(t)-lo1[t])%2);
   		    }
   		    al.subList(al.size()-1,al.size()).clear();  
   		    
   		    while(al.size()>1&&al.get(al.size()-1)==0)
   	        {
   	       	 al.subList(al.size()-1,al.size()).clear(); 
   	        }                            
               Arrays.fill(lo1, 0);
               }
   	   
   	   if(al.size()==0 )
   	   {
   		   r=1;
   	   }
   	   else
   	   {
   	   lo.clear();
   	   lo.addAll(al);
   	   al.clear();
   	   al.addAll(bl);
   	   bl.clear();
   	   bl.addAll(lo);
   	   lo.clear();
   	   }   
      }
    int b1 = bl.get(0);
    if(b1>2)
    {
   	 b1=b1%2;
    }
    if(b1<0)
    {
   	b1=(b1%2)+2;
    }
	if(b1>0)	
		{
		q1[0]=b1/2;
		r1[0]=b1%2;
		 temp=0;
		 i=0;
		int l1 = 2;
		int m1 = r1[0];
		while(temp==0)
		{
			i++;
		  q1[i]=l1/m1;
		  r1[i]=l1%m1;
		  if(r1[i]==0)
			  temp=1;
		  l1=r1[i-1];
		  m1=r1[i];  
		}
		int r3=0;
		int r2=r1[i-1];
		int r4;
		while(i>=0)
		{
		  r4=r2;
		  r2=r3;
		  r3=(r4-(q1[i])*(r2));
		  i--;	  
		}
		int div1;	
		div1=r2*1;   	
      Arrays.fill(r1, 0);
       r1[0]=0;
       Arrays.fill(lo1, 0);
       lo1[0]=div1;
       Arrays.fill(q1, 0);
       }  
      
    int[] div = new int[20];
    
      for(temp=temp1; temp>=0;temp--)
      {
   	  
         System.arraycopy(r1,0,q1,0,lo1.length);
		   
   	   for( i=0; i<8;i++)
   	   {
   		   for(int j = 0;j<8;j++)
   		   {
               div[i+j]+=q[temp][i]*r1[j];                                      
   		   }
   	   }
   	   
   	   System.arraycopy(div,0,r1,0,div.length);
   	   Arrays.fill(div, 0);
   	   
   	   for(i=0; i<lo1.length;i++)
   	   {
   		   r1[i]=lo1[i]-r1[i];
   		   
   	   }
   		   System.arraycopy(q1,0,lo1,0,lo1.length);
      }
      
      
      } for(int t=7;t>=0;t--)
          {q1[t]=q1[t]%2;}
      for(int t=7;t>=0;t--)
		    {
		    	if(q1[t]>=0)
		    	{q1[t]=q1[t]%2;}
		    	if(q1[t]<0)
		    		{q1[t]=(q1[t]%2)+2;}
		    }
		return q1;
	}
}