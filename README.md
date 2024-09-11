# graviton
How to build and run:

mvn clean
mvn package
It will create the jar with dpendecies: usage-transaction-service-1.0-SNAPSHOT-jar-with-dependencies.jar
You can use the following command to run the jar with sample inputs(or you can create your inputs in similar structure and pass it on command line).

java -jar usage-transaction-service/target/usage-transaction-service-1.0-SNAPSHOT-jar-with-dependencies.jar  samples/pricingInfo.json samples/purchase.csv samples/usageInfo.csv

It will create an output in the same dirctory by default.
If you want output file to be written in a particular location, you can pass that as 4th input.
 java -jar usage-transaction-service/target/usage-transaction-service-1.0-SNAPSHOT-jar-with-dependencies.jar  samples/pricingInfo.json samples/purchase.csv samples/usageInfo.csv outputfilePath

This project tested with java 11.

Thought process:

Assumptions about inputs:
1. pricingInfo has details of services and packages as json and a sample can be found here: samples/pricingInfo.json
2. Purchase data is a CSV(comma as delimeter) sample can be found here: samples/purchase.csv
3. Usage data is similar to usage log lines, basically one entry for each use of a service at a time, sample can be found here: samples/usageInfo.csv
4. The data fies are small enoughs to be read and loaded in in-memory data structures; otherwise we could have done the processing in chunks
5. Assuming that individual data lines for purchase and usage has timestamp of the transaction and its in the increasing sorted order(other wise we can sort it before processing)

If we see this problem, it translates as merging the two types of transactions credit(purchase of package) and debit(use of service) and making a statement for the data so far. We start two pointers for each of the purchase and transaction log lines, maintain a balance(initialized to 0).
In case of purchase was done before the usage we just update the credit based on the package and quanity and increment the counter for purchase lines; else we check if we have sufficient balance for the usage of service, if so we allow and decremnt the balance, then increment the counter for usage lines.

We can preprocess these loglines and club the data of the same time, but we will have to handle the partial allowed and denied scenario seprately.


