# graviton
## How to build and run:

### Create the jar with dependencies:
Run these commands inside usage-transaction-service.

```mvn clean``` <br/>
```mvn package``` <br/>

It will create a jar with this name in the target: ```usage-transaction-service-1.0-SNAPSHOT-jar-with-dependencies.jar```

### Run the jar:

Either using sample inputs: <br/>

``` java -jar usage-transaction-service/target/usage-transaction-service-1.0-SNAPSHOT-jar-with-dependencies.jar  samples/pricingInfo.json samples/purchase.csv samples/usageInfo.csv``` <br/>

Or, provide file path for your custom inputs: <br/>

```java -jar usage-transaction-service/target/usage-transaction-service-1.0-SNAPSHOT-jar-with-dependencies.jar  <pricingInfo-filepath> <purchase-filepath> <usage-filepath> <output-filepath>``` <br/>

Note: ```<output-filepath>``` is optional. By default, an output file will be created in the same directory from where command is run. 

### Testing
This project tested with Java 11. Also, there exists some basic unit tests.

## Thought process

### Assumptions about inputs:
1. PricingInfo has details of services and packages as json and a sample can be found here: ```samples/pricingInfo.json```
2. Purchase data is a CSV (with comma as delimeter). A sample can be found here: ```samples/purchase.csv```
3. Usage data is similar to usage log lines. Essentially, there exists one entry for each use of a service at a time. A sample can be found here: ```samples/usageInfo.csv```
4. The data files are small enough to be read and loaded in in-memory data structures; otherwise we can do the processing in chunks.
5. Assuming that individual data lines for purchase and usage has timestamp of the transaction and its in the increasing sorted order (otherwise it can be sorted based on timestamp before processing)
6. Output file is a json file with transaction history for each customer.
7. As it is a command line tool which reads input from static files and output is to a single file, it will run in a single threaded environment.
   
### Solution Approach:
The problem largely translates as merging the two types of transactions: _credit_ (purchase of package) and _debit_ (usage of a service) and making a consolidated statement for the data so far. 

Assuming the input data is already sorted by timestamp:
1. We start with two pointers - one for purchase log lines and other for usage log lines and maintain a variable to hold the balance. 
2. Initialise balance with 0.
3. Iterate through purchase and usage input lines and based on which has earlier timestamp (purchase or usage), we do the following processing:
 - if purchase was done before the usage, we just update the credit based on the package and quanity and increment the counter for purchase lines; 
 - else we check if we have sufficient balance for the usage of service. If yes, allow and decrement the balance, else, decline the request and leave balance as is. Increment the counter for usage lines thereafter.

To store the data in memory: 
1. I have used 2 maps, one for storing purchase data and other for storing usage data for each user.
2. Map is of type  map<string, List<logLines>> where key is userId and LogLines indicate lines read from the input file. loglines here used are just an indicator, actual objects are different for purchase and usage.
3. The  List<logLines> are assumed to be sorted based on the timestamp.

_Note_: Currently usage input contains one line for each service used along with timestamp. If multiple services are at the same timestamp, we can preprocess these logLines and club the data of the same time. Additionally, then we will have to handle the partial allowed and denied scenario separately.

In the implementation there are braodaly 3 layers:

1. Main takes care of construction of beans and calling them to solve the problem.
2. Service and its implementation layer. It has the main logic to populate data in to the in-memory data structures.
3. Data layer for reading files (Writer part can also be refactored, but currenlty it is being made part of the service layer itself).

## What more can be done:
There are a lots of scope of improvements, eg. proper error and exception handling, increasing test covergae, proper logging, use of Spring, Java docs etc..
