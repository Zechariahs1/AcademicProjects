/*****************************
Query the University Database
*****************************/
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.CallableStatement;
import java.util.Date;
import java.util.Scanner;
import java.lang.String;

public class MyQuery {

    private Connection conn = null;
	 private Statement statement = null;
	 private ResultSet resultSet = null;
    
    public MyQuery(Connection c)throws SQLException
    {
        conn = c;
        // Statements allow to issue SQL queries to the database
        statement = conn.createStatement();
    }
    
    public void findFall2009Students() throws SQLException
    {
        String query  = "select distinct name from student natural join takes" + 
        " where semester = \'Fall\' and year = 2009;";

        resultSet = statement.executeQuery(query);
    }
    
    public void printFall2009Students() throws IOException, SQLException
    {
	      System.out.println("******** Query 0 ********");
         System.out.println("name");
         while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number which starts at 1
			String name = resultSet.getString(1);
         System.out.println(name);
   		}        
    }

    public void findGPAInfo() throws SQLException{
      String tempTable = "CREATE TEMPORARY TABLE IF NOT EXISTS temp1(Select ID,course_id, sec_id,"+
      " semester, year, grade,CASE"+
      " when grade ='A' then 4.0 when grade ='A-' then 3.67 when grade ='B+' then 3.33"+
      " when grade = 'B' then 3.0 when grade = 'B-' then 2.67 when grade ='C+'then 2.33"+
      " when grade = 'C' then 2.0 when grade = 'C-' then 1.67 when grade = 'D+' then 1.33"+
      " when grade = 'D' then 1 when grade = 'D-' then 0.67 else 0.0 END AS num_grade"+
      " from takes);";
      String query  = "SELECT distinct ID, name, (SUM(num_grade* credits)/sum(credits))"+
      " from temp1 natural join student natural join course where grade is NOT NULL"+
      " group by ID;"; 
      statement.executeUpdate(tempTable);
      resultSet = statement.executeQuery(query);
    }
    
    public void printGPAInfo() throws IOException, SQLException
    {
		   System.out.println("******** Query 1 ********");
           System.out.println("ID    |    name    |  GPA");
         while (resultSet.next()) {
			String id = resultSet.getString(1);
         String name = resultSet.getString(2);
         String gpa = resultSet.getString(3);
         System.out.printf("%5s | ", id); 
         System.out.printf("%10s | ", name);
         System.out.printf("%7s | \n", gpa);
   		}        

    }

    public void findMorningCourses() throws SQLException
    {
      String query = "SELECT course_id, title, section.sec_id, section.semester, section.year,"+
         " instructor.name, (Count(distinct takes.ID)) AS Enrollment"+
         " from section natural join course join time_slot using(time_slot_id) natural join takes"+
         " join teaches using(course_id) join instructor on teaches.ID = instructor.ID"+ 
         " where time_slot.start_hr <= 12"+
         " group by course_id, sec_id;";
     resultSet = statement.executeQuery(query);
    }

    public void printMorningCourses() throws IOException, SQLException
    {
	   	System.out.println("******** Query 2 ********");
         System.out.println("Course   |          Title             |   Section  |  Semester  |     Year   | Instructor | Enrollment |");
         while (resultSet.next()) {
         String cid = resultSet.getString(1);
         String title = resultSet.getString(2);
         String secid = resultSet.getString(3);
         String sem = resultSet.getString(4);
         String year = resultSet.getString(5);
         String name = resultSet.getString(6);
         String enroll = resultSet.getString(7);
         System.out.printf("%8s | %26s | %10s | %10s | %10s | %10s | %10s |\n",cid,title,secid,sem,year,name,enroll);
         }//end
    }

    public void findBusyInstructor() throws SQLException
    {
    String query = "SELECT name FROM instructor i1 natural join teaches t1 group by name having"+ 
                   " count(t1.course_id) >= all(SELECT count(t2.course_id)"+ 
                   "FROM instructor i2 natural join teaches t2 group by name );";
    resultSet = statement.executeQuery(query);
 
    }

    public void printBusyInstructor() throws IOException, SQLException
    {
		   System.out.println("******** Query 3 ********");
         System.out.println("|     Name     |");
         while (resultSet.next()) {
			String name = resultSet.getString(1);
         System.out.printf("| %12s |\n", name);
   		}        

    }

    public void findPrereq() throws SQLException
    {
      String query = "SELECT DISTINCT c1.title,"+
      " (CASE when c2.title is NULL then ' ' else c2.title END)"+
      " FROM course c1 NATURAL LEFT JOIN prereq p LEFT JOIN course c2 on c2.course_id = p.prereq_id;";
      resultSet = statement.executeQuery(query);
    }

    public void printPrereq() throws IOException, SQLException
    {
		   System.out.println("\n******** Query 4 ********\n");
         System.out.println("|            Course          |         Prereq            |");
         while (resultSet.next()) {
			String course = resultSet.getString(1);
         String prereq = resultSet.getString(2);
         System.out.printf("| %26s | %26s|\n", course, prereq);
   		}        

    }

    public void updateTable() throws SQLException
    {
      String studentCopy = "CREATE TEMPORARY TABLE IF NOT EXISTS studentCopy(SELECT * FROM student);";
      statement.executeUpdate(studentCopy);
      
      String update = "update studentCopy set tot_cred = (SELECT Case"+ 
                    " when sum(course.credits) is NULL then 0"+ 
                    " else sum(course.credits) end"+ 
              " FROM takes join course using(course_id)"+
              " WHERE takes.grade <> 'F' AND takes.grade is not NULL AND studentCopy.ID = takes.ID);";
      statement.executeUpdate(update);
       
      String query = " SELECT * From studentCopy;";
      resultSet = statement.executeQuery(query); 
      
    }

    public void printUpdatedTable() throws IOException, SQLException
    {
		   System.out.println("******** Query 5 ********");
         System.out.println("|  ID   |    Name    |  Dept_name |TotalCreds|");
         while (resultSet.next()) {                                  
         String id = resultSet.getString(1);
         String name =resultSet.getString(2);
         String dept = resultSet.getString(3);
         String totCred = resultSet.getString(4);
         System.out.printf("| %5s | %10s | %10s | %8s |\n",id,name,dept,totCred);
         }
    }

    public void findFirstLastSemester() throws SQLException
    {
     statement.executeUpdate("CREATE TEMPORARY TABLE IF NOT EXISTS firstSem(Select *,"+ 
     " case when semester = 'Spring' then 0.1 + year when semester ='Summer' then 0.2 + year"+ 
     " when semester ='Fall' then 0.3 + year else 0.4 + year end as quarter from student natural join takes)");
     statement.executeUpdate("CREATE TEMPORARY TABLE IF NOT EXISTS cpSem1(Select * from firstSem);");
     statement.executeUpdate("CREATE TEMPORARY TABLE IF NOT EXISTS cpSem2(Select * from firstSem);");
     statement.executeUpdate("CREATE TEMPORARY TABLE IF NOT EXISTS lastSem(Select * from firstSem);");
     
     String query = "Select fs.ID, fs.name, concat(fs.semester, ' ', fs.year), concat(ls.semester, ' ', ls.year)"+
     " from firstSem fs join lastSem ls on fs.ID = ls.ID"+
     " WHERE (fs.quarter) in (SELECT min(quarter) from cpSem1 cpF where fs.ID = cpF.ID group by ID)"+
     " AND ls.quarter in (SELECT MAX(quarter) from cpSem2 cpL where ls.ID = cpL.ID group by ID)"+
     " group by fs.ID";
      
      resultSet = statement.executeQuery(query);  
    }

    public void printFirstLastSemester() throws IOException, SQLException
    {
        System.out.println("******** Query 6 ********");
        System.out.println("|  ID   |    Name    |  First Semester |  Last Semester  |");
         while (resultSet.next()) {                                  
         String id = resultSet.getString(1);
         String name =resultSet.getString(2);
         String first = resultSet.getString(3);
         String last = resultSet.getString(4);
         System.out.printf("| %5s | %10s | %15s | %15s |\n",id,name,first,last);
         }

    }
	
	public void findHeadCounts() throws SQLException
	{
		   System.out.println("******** Query 7 ********");
         System.out.println("Enter Department Name: "); 
         Scanner in = new Scanner(System.in);
         String dept = in.nextLine();	
         CallableStatement callStatement = conn.prepareCall("{call getNumbers(?,?,?)}");
         callStatement.registerOutParameter(2,Types.INTEGER);
         callStatement.registerOutParameter(3,Types.INTEGER);
         callStatement.setString(1,dept);
         callStatement.execute();
         System.out.println(dept +" has "+ callStatement.getInt(2)+ " instructors");
         System.out.println(dept +" has "+ callStatement.getInt(3)+ " students");
	}
}
