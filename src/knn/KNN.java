package knn;

import java.io.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class KNN {
	
	
	double dmax=0.00;
	ArrayList<Patient>patient;
	int noOfClasses;
	int k;

	public KNN (int k){
		patient=new ArrayList<Patient>();
		noOfClasses=16;
		this.k=k;
	}
	
	public void trainWithInput()
	{
		try{
			FileReader fr=new FileReader("E:/My 5th/knn-weighted-method/src/knn/inputTumor.txt");    
	        BufferedReader br=new BufferedReader(fr);
			String str;
			while((str=br.readLine())!=null){
				String []att=str.split(",");
				Patient f=new Patient(Integer.parseInt(att[att.length-2]),Integer.parseInt(att[att.length-1]));
				for(int i=0;i<noOfClasses;i++){
					if(att[i].equals("?")){
						att[i]="1000";
						f.attribute[i]=Double.valueOf(att[i]);
					}
					else f.attribute[i]=Double.valueOf(att[i]);
					
				}
				patient.add(f);

			}
			br.close();
		}catch(Exception e){

			e.printStackTrace();
		}
		
		System.out.println("Input size : "+ patient.size());
	}
	
	public int calculateDecision1(){
		int positive=0;
		int negative=0;
		double w2=0;
		double w1=0;
		for(int i=0;i<k;i++){
			int className=patient.get(i).decision1;
			
			if(className==2){
				negative++;
				double weight=dmax-patient.get(i).distance;
				w2+=weight;
			}
			else if(className==1){
				positive++;
				double weight=dmax-patient.get(i).distance;
				w1+=weight;
			}
		}
		
		if(w1>w2){
			return 1;
		}
		else if(w2>w1){
			return 2;
		}
		return 0;
	}
	
	public int calculateDecision2(){
		int positive=0;
		int negative=0;
		double w2=0;
		double w1=0;
		for(int i=0;i<k;i++){
			int className=patient.get(i).decision2;
			
			if(className==2){
				negative++;
				double weight=dmax-patient.get(i).distance;
				w2+=weight;
			}
			else if(className==1){
				positive++;
				double weight=dmax-patient.get(i).distance;
				w1+=weight;
			}
		}
		
		if(w1>w2){
			return 1;
		}
		else if(w2>w1){
			return 2;
		}
		return 0;
	}
	
	
	public void calculateMaxDistance(){
		double tempDistance=0;
		for(int i=0;i<patient.size();i++){
			for(int j=0;j<patient.size();j++){
				tempDistance=calculateDistance(patient.get(i).attribute,patient.get(j).attribute);
				if(tempDistance > patient.get(i).internalDistance){
					patient.get(i).internalDistance=tempDistance;
				}
			}
			
		}
		
		
		for(int m=0;m<patient.size();m++){
			if(patient.get(m).internalDistance > dmax){
				dmax = patient.get(m).internalDistance;
			}
		}
		
		System.out.println("Maximum internal distance is : "+dmax);
	}
	
	public double calculateDistance(double []d1,double[]d2)
	{
		double total=0;
		for(int i=0;i<noOfClasses;i++){
			
			if(d1[i] != 1000 || d1[i] != 1000){
				total+=(d1[i]-d2[i])*(d1[i]-d2[i]);
			}
			
		}
		return (Math.sqrt(total));
	}
	
	
	public void knnAlgorithm(){
		
		Patient p;
		int count=0;
		int total=0;
		
		trainWithInput();
		calculateMaxDistance();
		
		try{
			FileReader fr=new FileReader("E:/My 5th/knn-weighted-method/src/knn/testTumor.txt");    
	        BufferedReader br=new BufferedReader(fr);
			String str;
			while((str=br.readLine())!=null){
				String []parsedString=str.split(",");
				p=new Patient(Integer.parseInt(parsedString[parsedString.length-2]),Integer.parseInt(parsedString[parsedString.length-1]));
				System.out.println("Expected decision1: "+parsedString[parsedString.length-2]);
				System.out.println("Expected decision2: "+parsedString[parsedString.length-1]);
				
				for(int i=0;i<noOfClasses;i++){
					
					if(parsedString[i].equals("?")){
						parsedString[i]="1000";
						p.attribute[i]=Double.valueOf(parsedString[i]);
					}
					else p.attribute[i]=Double.valueOf(parsedString[i]);
				}
				for(int j=0;j<patient.size();j++){
					
					patient.get(j).distance=calculateDistance(p.attribute,patient.get(j).attribute);
					
				}
				Collections.sort(patient, new Comparator<Patient>(){
					public int compare(Patient f1, Patient f2) {
						double dif=f1.distance-f2.distance;
						if(dif>0)
							return 1;
						else if(dif<0)
							return -1;
						else{
							return 0;
						}
					}
				});
				
				
				int decision1=calculateDecision1();
				if(decision1==p.decision1){
					count++;
				}
				System.out.println("Calculated decision1: "+decision1);
				total++;
				int decision2=calculateDecision2();
				if(decision2==p.decision2){
					count++;
				}
				System.out.println("Calculated decision2: "+decision2);
				total++;
				System.out.println();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Accuracy rate: "+ ((count*100)/total)+"%");
	}


}