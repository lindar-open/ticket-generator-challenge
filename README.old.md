# Ticket Generator Challenge - Python Solution

Python3 required.  
To see the output, clone repository and run:
```bash
python bingo.py
```
Author: **Przemysław Baj**

A small challenge that involves building a [Bingo 90](https://en.wikipedia.org/wiki/Bingo_(United_Kingdom)) ticket generator.

**Requirements:**

* Generate a strip of 6 tickets
  - Tickets are created as strips of 6, because this allows every number from 1 to 90 to appear across all 6 tickets. If they buy a full strip of six it means that players are guaranteed to mark off a number every time a number is called.
* Each row contains five numbers and four blank spaces
* Each column contains up to three numbers, which are arranged as follows:
  - The first column contains numbers from 1 to 9 (or 10),
  - The second column numbers from 10 (or 11) to 20,
  - The third, 20 (or 21) to 30 and so on up until the last column, which contains numbers from 81 to 90.
* Each column should contain at least 1 number (and not 3 white spaces)
* There can be **no duplicate** numbers between 1 and 90 **in the strip** (since you generate 6 tickets with 15 numbers each)
