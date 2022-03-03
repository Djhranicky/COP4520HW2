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
For experimental evaluation, the main thread keeps track of the number of times each guest has participated in the 
labyrinth. Once the game has completed, the main thread verifies everyone has entered and prints as such and also prints the 
number of times each guest entered. Apparently there were concerns that the Minotaur should not know how many times each guest 
has entered the labyrinth. While those seem unfounded because the Minotaur's knowledge does not impact the functioning of the 
game, this implementation does assume that the Minotaur can know this. This would be an easy fix as all of the Minotaur's 
functionality can be abstracted out into a separate class and the information tracking would be purely from an unbiased 3rd party.

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

