// Written by Mengxin Huang, Student ID z5013846
package net.datastructures;

import java.io.*;
import java.util.*;
import java.io.File;
import java.util.regex.Pattern;

public class TaskScheduler {
	// First, create 3 arrays to store 
	// the task name, release time and deadline respectively
	static String task[] = new String[512];
	static int release[] = new int[512];
	static int deadline[] = new int[512];
	// Number of cores
	public static int mcores = 0;
	// Number of tasks
	public static int number = 0;
	
	public TaskScheduler() {};
	
	// Create 3 patterns to check the format of the input
	// checkV: a string of letters and numbers
	public static boolean checkV (String str) {
		Pattern pattern = Pattern.compile("^[a-zA-Z]+[0-9]+$");
		return pattern.matcher(str).matches(); 
	}
	// checkR: a string of only digit 0-9
	public static boolean checkR (String str) {
		Pattern pattern = Pattern.compile("^[0-9]+$");
		return pattern.matcher(str).matches(); 
	}
	// checkD: a string of only digit 0
	public static boolean checkD (String str) {
		Pattern pattern = Pattern.compile("^[0]+$");
		return pattern.matcher(str).matches(); 
	}
	
	public static void scheduler(String file1, String file2, int m) throws IOException {
		// Check if file1 exists
		/*
		try {
			File fip = new File(file1);
			Scanner s = new Scanner(fip);
		} catch (FileNotFoundException e) {
			System.out.println("file1 does not exist.");
			return;
		}*/
		File f1 = new File(file1);
		if(!f1.exists())
			System.out.println("file1 does not exist.");
		// Check if file2 exists
		File f2 = new File(file2);
		if(f2.exists())
			System.out.println("file2 already exists.");
		
		File fip = new File(file1);
		Scanner s = new Scanner(fip);
		String v[] = new String[500];
		
		mcores = m; 
		int n = 0;
		int a = 0;
		int b = 0;
		int c = 0;

		while(s.hasNext()) {
			v[n] = s.next();
			// Checking the task name 
			if(n % 3 == 0) {
				if(checkV(v[n])) {
					task[a] = v[n];
					a++;
				}
				else
					System.out.println("The task attributes "
							+ "(task name, release time and deadline) "
							+ "of file1 do not follow the format as shown next.");
			}
			// Checking the release time
			// it is a non-negative integer
			if(n % 3 == 1) {
				if(checkR(v[n])) {
					release[b] = Integer.parseInt(v[n]);	
					b++;
				}
				else
					System.out.println("The task attributes "
							+ "(task name, release time and deadline) "
							+ "of file1 do not follow the format as shown next.");
			}
			// checking the deadline
			// it is a natural number not including zero
			if(n % 3 == 2) {
				if(checkR(v[n]) && !checkD(v[n])) {
					deadline[c] = Integer.parseInt(v[n]);
					c++;
				}
				else
					System.out.println("The task attributes "
							+ "(task name, release time and deadline) "
							+ "of file1 do not follow the format as shown next.");
			}
			n++;	
		}
		number = (n + 1) / 3;	

		// Using quick sort to sort the tasks instead of using heaps
		// First, sort the tasks by release time
		// and if two tasks have the same release tiem
		// compare them by deadline 
		// basically, the idea is the same as using heaps
		// and the time complexity of quick sort is O(nlogn)
		// for an array with n elements
		// each time we choose a element to be the midpoint of the array
		// then, putting all the elements greater than it to its right side
		// others to its left side
		// then, iterate the same process with logn times
		// therefore, the time complexity is O(n * logn)
		quickSortT(task, release, deadline, 0, number-1);
		
		//for(int j = 0;j < number; j++)
			//System.out.println(task[j]);
		//for(int j = 0;j < number; j++)
			//System.out.println(release[j]);
		//for(int j = 0;j < number; j++)
			//System.out.println(deadline[j]);
		
		// Writing into file2
		File f = new File("file2");
		f.createNewFile();
		FileWriter writer = new FileWriter(f);
		for(int j = 0; j < number; j++) {
			writer.write(task[j]);
			writer.write(" ");
			String str = String.valueOf(release[j]);
			writer.write(str);
			writer.write(" ");
		}
		writer.close();
		s.close();
	}
	
	// Using quick sort to sort the tasks
	// Sorting the release time 
	// If tasks have the same release time
	// Then sort them by deadline
	// The time complexity of quick sort is O(nlogn)
	// Therefore, the time complexity of quickSortT is O(nlogn)
	public static void quickSortT(String a1[], int a2[], int a3[], int low, int high) {
		int count = 1;
		int l = low;
		int h = high;
		int povit = a2[low];
		while(l < h) {
			while(l < h && a2[h] >= povit) {
				// If release time are the same
				if(a2[h] == povit) {
					// If the number of tasks that have the same release time
					// and same deadline is greater than m
					// then some tasks must be missed out
					if(count > mcores) {
						System.out.println("No feasible schedule exists.");
						System.exit(-1);
					}
					if(a3[l] == a3[h])
						count++;
					if(a3[l] > a3[h]) {
						int temp3 = a3[h];
						a3[h] = a3[l];
						a3[l] = temp3;
						String temp1 = a1[h];
						a1[h] = a1[l];
						a1[l] = temp1;
					}
				}
				h--;
			}
			// sort by release time
			if(l < h) {
					int temp2 = a2[h];
					a2[h] = a2[l];
					a2[l] = temp2;
					int temp3 = a3[h];
					a3[h] = a3[l];
					a3[l] = temp3;
					String temp1 = a1[h];
					a1[h] = a1[l];
					a1[l] = temp1; 
				l++;
			}
			while(l < h && a2[l] <= povit) {
				if(a2[h] == povit) {
					if(a3[l] > a3[h]) {
						int temp3 = a3[h];
						a3[h] = a3[l];
						a3[l] = temp3;
						String temp1 = a1[h];
						a1[h] = a1[l];
						a1[l] = temp1;
					}
				}
				l++;
			}
			if(l < h) {
					int temp2 = a2[h];
					a2[h] = a2[l];
					a2[l] = temp2;
					int temp3 = a3[h];
					a3[h] = a3[l];
					a3[l] = temp3;
					String temp1 = a1[h];
					a1[h] = a1[l];
					a1[l] = temp1;
				h--;
			}
			if(l > low)
				quickSortT(a1, a2, a3, low, l - 1);
			if(h < high)
				quickSortT(a1, a2, a3, l + 1, high);
		}
	}
	
	//the main function
	/*public static void main(String[] args) throws Exception{
	    //TaskScheduler.scheduler("samplefile1.txt", "feasibleschedule1", 4);
	   /** There is a feasible schedule on 4 cores */      
	    //TaskScheduler.scheduler("samplefile2.txt", "feasibleschedule2", 3);
	   /** There is no feasible schedule on 3 cores */
	    //TaskScheduler.scheduler("samplefile3.txt", "feasibleschedule3", 5);
	   /** There is a feasible scheduler on 5 cores */ 
	    //TaskScheduler.scheduler("samplefile4.txt", "feasibleschedule4", 4);
	   /** There is no feasible schedule on 4 cores */
	   /** The sample task sets are sorted. You can shuffle the tasks and test your program again   
	  }*/
}