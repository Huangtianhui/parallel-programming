package edu.coursera.parallel;

import java.security.KeyStore.Entry;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple wrapper class for various analytics methods.
 */
public final class StudentAnalytics {
    /**
     * Sequentially computes the average age of all actively enrolled students
     * using loops.
     *
     * @param studentArray Student data for the class.
     * @return Average age of enrolled students
     */
    public double averageAgeOfEnrolledStudentsImperative(
            final Student[] studentArray) {
        List<Student> activeStudents = new ArrayList<Student>();

        for (Student s : studentArray) {
            if (s.checkIsCurrent()) {
                activeStudents.add(s);
            }
        }

        double ageSum = 0.0;
        for (Student s : activeStudents) {
            ageSum += s.getAge();
        }

        return ageSum / (double) activeStudents.size();
    }

    /**
     * TODO compute the average age of all actively enrolled students using
     * parallel streams. This should mirror the functionality of
     * averageAgeOfEnrolledStudentsImperative. This method should not use any
     * loops.
     *
     * @param studentArray Student data for the class.
     * @return Average age of enrolled students
     */
    public double averageAgeOfEnrolledStudentsParallelStream(
            final Student[] studentArray) {
    	try{
    		double ageSum=Stream.of(studentArray)
        			.parallel()
        			.filter(s->s.checkIsCurrent())
        			.mapToDouble(a->a.getAge())
        			.average()
        			.getAsDouble();
    		return ageSum;
    	}
    	catch(Exception e){
    		throw new UnsupportedOperationException();
    	}
        
    }

    /**
     * Sequentially computes the most common first name out of all students that
     * are no longer active in the class using loops.
     *
     * @param studentArray Student data for the class.
     * @return Most common first name of inactive students
     */
    public String mostCommonFirstNameOfInactiveStudentsImperative(
            final Student[] studentArray) {
        List<Student> inactiveStudents = new ArrayList<Student>();

        for (Student s : studentArray) {
            if (!s.checkIsCurrent()) {
                inactiveStudents.add(s);
            }
        }

        Map<String, Integer> nameCounts = new HashMap<String, Integer>();

        for (Student s : inactiveStudents) {
            if (nameCounts.containsKey(s.getFirstName())) {
                nameCounts.put(s.getFirstName(),
                        new Integer(nameCounts.get(s.getFirstName()) + 1));
            } else {
                nameCounts.put(s.getFirstName(), 1);
            }
        }

        String mostCommon = null;
        int mostCommonCount = -1;
        for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
            if (mostCommon == null || entry.getValue() > mostCommonCount) {
                mostCommon = entry.getKey();
                mostCommonCount = entry.getValue();
            }
        }

        return mostCommon;
    }

    /**
     * TODO compute the most common first name out of all students that are no
     * longer active in the class using parallel streams. This should mirror the
     * functionality of mostCommonFirstNameOfInactiveStudentsImperative. This
     * method should not use any loops.
     *
     * @param studentArray Student data for the class.
     * @return Most common first name of inactive students
     */
    public String mostCommonFirstNameOfInactiveStudentsParallelStream(
            final Student[] studentArray) {
//    	try{
//    		String mostCommon=Stream.of(studentArray)
//    				.collect(Collectors.groupingBy(w -> w, Collectors.counting()))
//    				.entrySet()
//    				.stream()
//    				.max(Comparator.comparing(Entry::getValue))
//    				.get()
//    				.getKey()
//      	          //.collect(Collectors.groupingBy(w -> w, Collectors.counting()))
//      	          //.entrySet()
//      	          //.stream()
//      	          //.max(Comparator.comparing(Entry::getValue))
//      	          //.get()
//      	          //.getKey();
//    		return mostCommon;
//    	}
//    	catch(Exception e){
//    		throw new UnsupportedOperationException();
//    	}
//    	
//    	String mostCommon=Stream.of(studentArray)
//    			.parallel()
//    			.filter(student -> !student.checkIsCurrent())
//    			.map(//to firstname)
//    			.collect(Collectors.groupingByConcurrent(s -> s, Collectors.counting()))
//    			.entrySet()
//   				.stream()
//				.max(// comparator).//unwrap optional
//    							
//    							
//    			.collect(Collectors.toMap(s -> s.getFirstName(), s -> 1, (s , a)-> s+a)).entrySet().stream().parallel()
//   				.max()
    	try{
    		String mostCommon=Stream.of(studentArray)
					.parallel()
					.filter(s->!s.checkIsCurrent())
					.collect(Collectors.groupingByConcurrent(Student::getFirstName,
                            Collectors.summingInt(s -> 1)))
                     .entrySet().stream()
                     .max(Map.Entry.comparingByValue())
                     .map(Map.Entry::getKey)
                     .orElse(null);
    		return mostCommon;
    	}
    	catch(Exception e){
    		throw new UnsupportedOperationException();
    	}
    						
        
    }

    /**
     * Sequentially computes the number of students who have failed the course
     * who are also older than 20 years old. A failing grade is anything below a
     * 65. A student has only failed the course if they have a failing grade and
     * they are not currently active.
     *
     * @param studentArray Student data for the class.
     * @return Number of failed grades from students older than 20 years old.
     */
    public int countNumberOfFailedStudentsOlderThan20Imperative(
            final Student[] studentArray) {
        int count = 0;
        for (Student s : studentArray) {
            if (!s.checkIsCurrent() && s.getAge() > 20 && s.getGrade() < 65) {
                count++;
            }
        }
        return count;
    }

    /**
     * TODO compute the number of students who have failed the course who are
     * also older than 20 years old. A failing grade is anything below a 65. A
     * student has only failed the course if they have a failing grade and they
     * are not currently active. This should mirror the functionality of
     * countNumberOfFailedStudentsOlderThan20Imperative. This method should not
     * use any loops.
     *
     * @param studentArray Student data for the class.
     * @return Number of failed grades from students older than 20 years old.
     */
    public int countNumberOfFailedStudentsOlderThan20ParallelStream(
            final Student[] studentArray) {
//    	int numStudent=Stream.of(studentArray)
//    			.parallel()
//    			.filter(s->s.isCurrent)
//    			.filter(a -> a < 65).collect(Collectors.toList())
//    			.filter(a-> a>20).collect(Collectors.toList())
//    			.filter(a -> a < 65)
//    			.filter(a -> a > 20)
//    			.getAsInt();
    	try{
    		int numStudent=(int) Stream.of(studentArray)
					.parallel()
					.filter(s -> !s.checkIsCurrent() && s.getAge()>20 && s.getGrade()<65)
					.count();
					//.collect(Collectors.toList())
					//.size();
					
    		return numStudent;
    	}
    	catch(Exception e){
    		throw new UnsupportedOperationException();
    	}
        
    }
}
