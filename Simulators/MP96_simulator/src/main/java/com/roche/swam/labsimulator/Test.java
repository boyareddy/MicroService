package com.roche.swam.labsimulator;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
List<Student> stuList = new ArrayList<>();
		
Student s1 = new Student();
s1.setId("1");
s1.setMarks(99);
s1.setName("Nan");
stuList.add(s1);
Student s2 = new Student();
s2.setId("1");
s2.setMarks(98);
s2.setName("sar");
stuList.add(s2);

List<Student> finalList= new ArrayList<>();

finalList= filterStudent(stuList,"E");	

System.out.println("Student with Even marks : ");
for(Student s:finalList) {
	System.out.println("\n"+s.getName());
}
		
	}

	private static List<Student> filterStudent(List<Student> stuList, String string) {
		List<Student> filterList= new ArrayList<>();
		for(Student s:stuList) {
			if("E".equalsIgnoreCase(string) && s.getMarks()%2==0) {
				filterList.add(s);	
			}
			if("O".equalsIgnoreCase(string) && s.getMarks()%2!=0) {
				filterList.add(s);	
			}
			
		}
		
		return filterList;
		// TODO Auto-generated method stub
		
	}

}
