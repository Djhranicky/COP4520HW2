# BirthdayParty

## Running BirthdayParty.java
To run the BirthdayParty.java file, navigate to the directory in a terminal shell.
Compile with "javac BirthdayParty.java" and run with "java BirthdayParty". 
Alternatively, use the batch script by typing "build bp". Remove the class files with "build rm".

## Proof of Correctness
The algorithm implemented requires the leader to eat the first cupcake and they count that as the first cupcake eaten.
After that, each other member will only replace the cupcake one time, and another member will only place another 
cupcake once that one has been eaten. That one can only be eaten by the leader, and when they do, they will increment 
their count by one, thus incrementing their count by one for each and every participating member. Once the leader's 
count reaches the number of guests, they then know for sure that each participating guest, including themselves, has
accounted for one and only one cupcake.

## Experimental Evaluation
The previous version of this code had all of the threads spin waiting for their turn, which caused it to slow down dramatically 
past 11 guests (12 threads including the main one). Thinking back on it now this is probably the hardware limit for my computer, 
and after that it incurred massive overhead from context switching for all of the threads. The current implementation has each 
thread wait to be notified by the main thread. This notification process allows for random selection of the guests and also only 
one guest will make it through at a time as an AtomicBoolean is being used as a pseudo-semaphore with one lock. After the leader 
has declared everyone has entered, the Minotaur (main thread) verifies that they did indeed all go through the maze. This does 
not violate the communication restriction between guests as they only ever tell the Minotaur if they have entered, not each 
other.

# CrystalVase

## Running CrystalVase.java
To run the CrystalVase.java file, navigate to the directory in a terminal shell.
Compile with "javac CrystalVase.java" and run with "java CrystalVase". 
Alternatively, use the batch script by typing "build cv". Remove the class files with "build rm".

## Choice of Strategy
The first strategy mentions this in the problem description, but it could easily lead to starvation of some threads as they 
are forced to compete and may never get access to the showroom before the night ends. The second strategy can also very easily 
lead to this as many guests could wait for the sign to no longer be busy. The third strategy solves this issue as no matter what 
order the guests show up in, that order is recorded and as the line progresses, all of the guests will eventually see the vase. 

## Exit Condition
Since none of the strategies mention how the night ends (an exit condition), the night ends when no one is found to be queueing for the vase, at which point everyone is sent home (and the threads can stop running).

## Proof of Correctness
A proof of correctness for this will only go as far as prove that the implementation satisfies the requirements of the third 
strategy. The only requirements are that they use a queue, they notify each other when it's someone's turn, and that each
guest can enter the queue multiple times. In this implementation, a ConurrentLinkedQueue is used to implement a thread-safe
queue, when a guest is done viewing the vase they notify the next guest, and each guest has a decreasing chance of entering the 
queue each time they enter.

