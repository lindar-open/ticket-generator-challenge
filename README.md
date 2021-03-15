# Ticket Generator Challenge

A small challenge that involves building a [Bingo 90](https://en.wikipedia.org/wiki/Bingo_(United_Kingdom)) ticket generator.

**Requirements:**

* Generate a strip of 6 tickets
  - Tickets are created as strips of 6, because this allows every number from 1 to 90 to appear across all 6 tickets. If they buy a full strip of six it means that players are guaranteed to mark off a number every time a number is called.
* Each row contains five numbers and four blank spaces
* Each column contains up to three numbers, which are arranged as follows:
  - The first column contains numbers from 1 to 9,
  - The second column numbers from 10 to 20,
  - The third, 20 to 30 and so on up until the last column, which contains numbers from 81 to 90.
* Each column should contain at least 1 number (and not 3 white spaces)
* There can be **no duplicate** numbers between 1 and 90 **in the strip** (since you generate 6 tickets with 15 numbers each)

**Please make sure you add unit tests to verify the above conditions and an output to view the strips generated (command line is ok).**

Try to also think about the performance aspects of your solution. How long does it take to generate 10k strips? The recommended time is less than 1s (with a lightweight random implementation)
