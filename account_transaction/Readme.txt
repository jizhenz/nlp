1. This is a Maven/Eclipse project
2. To test using command line maven:
   2.1 Open a cmd window on Windows OS
   2.2 Go to the folder that contains the project
   2.3 run: mvn install 
       This is to compile/test the project and build jar file
   2.4 run: mvn clean
       This is to clean the build
3. To test in in Eclipse:
   3.1 Right click the project
   3.2 Click Run As -> Maven install
   3.2 or Click Run As -> Maven clean
   
4. The design:
   4.1 The class AccountServiceImpl has a map at class level that maintain a lock 
       for each of the Accounts, using the account ID as key, and ReentrantLock 
       as value. On first time an account being called, a lock created and put 
       into the map. All the following calls will use the same lock. This 
       ensures the thread safety.
   4.2 In the test class AccountServiceImplTest, there are two methods for testing 
       transfer: one is sequential, one is parallel using threads.