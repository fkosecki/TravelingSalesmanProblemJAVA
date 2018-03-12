# TravelingSalesmanProblemJAVA
A tool for solving TSP problem.

Right now some things are implented, GUI looks bad, many needs to be changed and even more needs to be added, but as of now you can:
- solve TSP problem using genetic algorithm
- easily add mutation and crossing methods to genetic algorithm 
- solve TSP problem using Help-Karp algorithm (optimal solution, solves problems for up to 12 locations)
- freely load/save locations datasets from files (TXT and XML format) and database (SQL)

Even though GUI shows option for saving/loading options, it doesn't work. I'm sorry.

Solving TSP with Genetic Algorithm:
- load dataset from file or database(script for setting database included)
- population size - amount of randomly generated answers that will be evolving into almost optimal answer
- number of iterations - in each iteration population is being replaced with new upgraded subjects
- elite count - amount of populations subject that will be compared to each other and the two most suitable will be crossed and mutated
- crossing and mutation probability - how likely it would be for mutation or crossing process to take place
- Mutator - choose mutation options that could take a place and odds of using selected method compared to the odds of other selected options
- Crosser - choose crossing options that could take a place and odds of using selected method compared to the odds of other selected options
- EXECUTE

If you want to add your own mutation method, simply:
- write your own method in tspAlgorithms->GeneticAlgorithm->Mutator class
- it needs to be public and static
- it needs to return Subject class object 

If you want to add your own crossing method, simply:
- write your own method in tspAlgorithms->GeneticAlgorithm->Crosser class
- it needs to be public and static
- it needs to return Subject[] array class object 

After adding your own mutation/crossing method (if the object they return is correct). Thanks to reflection, it will be automatically added as an option in GUI.

SQL database:
- you can use SQL database to store, edit and load locations datasets
- connect to database using JDBC connection URL. By default it connects to local database(TSPlocations)
- script for setting up local database in MS SQL Server is included 
