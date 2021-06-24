### Table of Contents
* [Project Description](#description)
* [Rules](#rules)
* [Donald Knuth Algorithm](#knuth)

<a name="description"></a>
# Bulls-and-Cows

### Project Description
**Bulls and Cows** is a guessing number game. One player has to come up with a 4 digit number, the other has to guess it with the least number of guesses possible. 

<a name = "rules"></a>
### Rules
Originally, the game allows any number between 0-9999; However, our assignment specification limits only numbers within 1000-9999.

After having come up with a secret number, the opponent shall try to guess that number. For each turn, there will be a hint based on the guessed number with the following info:
* **Hits:** are digits in the guesses that are in wrong positions
* **Strikes** are digits in the guesses that are in correct positions

Example 1:
* **Input:** secret = 1234, guess = 1122
* **Output:** 1 hit 1 strike

Example 2:
* **Input:** secret = 3295, guess = 1122
* **Output:** 1 hit 0 strike

<a name = "knuth"></a>
### Donald Knuth Algorithm
Our algorithm is based on Donald Knuth's five guess algorithm in 1977 for solving the game Mastermind (a similar game with less set of input). The algorithm works as follow:

**1.** Create a set S of all 9000 possible numbers (1000-9999)
**2.** Play with 1122 as initial guess. (Donald Knuth gave example of different intial numbers can result in higher number of guesses)
**3.** Play the guess to get the hint
**4.** If the hint returns 4 strikes (correct guess) -> program terminates. Otherwise, remove all numbers  that would not return the same response from S if the current guess number were to be the secret number

Example: 
* **Input:** secret = 1234, guess = 1122
* **Output:** 1 hit 1 strike
    Now we treat 1122 like the secret number and loop through the list of possible numbers S and eliminate any number that would not return 1 hit 1 strike. E.g., Secret = 1122, guess = 1000 -> 1 strike, which is different from 1 hit 1 strike -> eliminate

**5.** Apply minimax technique. Calculate how many possibilities in S would be eliminated for
each possible guess. The number of eliminations will be the score. Then create a set of guesses with the smallest max score and select the lowest one as the next guess. 
