
## System Requirements
- Java 17, gradle
- Docker

## Build & Run
Use the Makefile to execute build & run commands.

To build with gradle and docker:
```shell
make build
```

To run the docker container:
```shell
make run-docker
```
If the container started, the ticket generator endpoint is available at:

### [http://localhost:8080/bingo/newStrips](http://localhost:8080/bingo/newStrips)

To run tests or performance tests:
```shell
make run-tests
make run-perf-tests
```

## Implementation Details
- `-1` means `Blank` space in the ticket
- the core part of the algorithm is in `AbstractTicketGenerator`
  - it iterates through the spaces of the tickets row by row and based on a set of rules it places the blanks and numbers
- time complexity of the algorithm is `O(n)` with some amortized time, where `n` is the total number of spaces in the 6 tickets
  - the algorithm is hard to scale though. it was specifically implemented to generate 3x9 tickets in batches of 6
- array lists are initialized in the `ArrayBasedTicketGenerator` to keep track of the state of the ticket generator
  - these array lists are maybe not optimal: removing an element has `O(n)` time complexity
  - it is possible to create another subclass for `AbstractTicketGenerator` which uses another approach
    - e.g. instead of array lists, shuffled linked lists. removing from LL: `O(1)`, shuffling during init: `O(nlogn)`
  - altough these are very small array lists: `n` is usually smaller than 20
- an error checking is part of the algorithm, because in very rare cases an invalid ticket is generated. 
As soon as it's detected, the algorithm drops it and starts creating a new one. 
  - luckily this does not have a performance impact because it's so rare

## Performance
On my machine the performance test executed with these results:

`Generated 10.000 strips in 487 ms.`

And with 16 threads:

`Generated 100.000 strips in parallel in 1602 ms.`

------
Original content:
# Ticket Generator Challenge

A small challenge that involves building a [Bingo 90](https://en.wikipedia.org/wiki/Bingo_(United_Kingdom)) ticket generator.

**Requirements:**

* Generate a strip of 6 tickets
  - Tickets are created as strips of 6, because this allows every number from 1 to 90 to appear across all 6 tickets. If they buy a full strip of six it means that players are guaranteed to mark off a number every time a number is called.
* A bingo ticket consists of 9 columns and 3 rows.
* Each ticket row contains five numbers and four blank spaces
* Each ticket column consists of one, two or three numbers and never three blanks.
  - The first column contains numbers from 1 to 9 (only nine),
  - The second column numbers from 10 to 19 (ten), the third, 20 to 29 and so on up until
  - The last column, which contains numbers from 80 to 90 (eleven).
* Numbers in the ticket columns are ordered from top to bottom (ASC).
* There can be **no duplicate** numbers between 1 and 90 **in the strip** (since you generate 6 tickets with 15 numbers each)

**Please make sure you add unit tests to verify the above conditions and an output to view the strips generated (command line is ok).**

Try to also think about the performance aspects of your solution. How long does it take to generate 10k strips? 
The recommended time is less than 1s (with a lightweight random implementation)

