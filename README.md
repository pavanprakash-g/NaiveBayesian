# NaiveBayesian


Language used:
Used Java for development.

Running:
1. Go to the src directory
2. Run the following command to compile the code:
	javac *.java
3. Run the following command to run:
	java Main arg0 arg1 
   where the arg0 is Absolute path of data set. 
	     arg1 is Absolute path of test set. 
 


Sample Output:

Directory Name:sci.space
Directory Name:talk.politics.mideast
Directory Name:sci.crypt
Directory Name:sci.med
Directory Name:misc.forsale
Time taken for Training = 25 seconds
Time taken for Testing = 90 seconds
Testing Accuracy:95.38934

Process finished with exit code 0


#second run

Directory Name:alt.atheism
Directory Name:comp.sys.mac.hardware
Directory Name:comp.os.ms-windows.misc
Directory Name:comp.graphics
Directory Name:comp.sys.ibm.pc.hardware
Time taken for Training = 24 seconds
Time taken for Testing = 77 seconds
Testing Accuracy:69.50505


#observation

If the folders are similar to each other the prediction is less accurate compared to completely independent folders.
