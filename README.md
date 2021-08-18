# Project Euler __ Kotlin
___

A Kotlin solution set for problems corresponding to the 
[Project Euler](https://projecteuler.net/archives) challenges.

Some problems have been altered to either generalise their goal or implement constraints, as 
influenced by the problem outlines found on [HackerRank](https://www.hackerrank.com/contests/projecteuler/challenges).

Problem content on the Project Euler site is licensed under [CC BY-NC-SA 4.0](https://projecteuler.net/copyright).

### Structure Guideline

---
Problems are separated into packages (*batch#*) with 10 problem classes per package. Each problem has a 
corresponding test class that can be found in the matching test package.

If a function gets reused in a later solution, it is elevated out of its original problem class and
placed as a top-level function in the **util folder**. Any custom/helper classes can also be found there.

### Sibling Repositories

---
While this is the original solution set, 2 other project repositories have been created to 
practise the following languages:
- [Project Euler __ Python](https://github.com/bog-walk/project-euler-python)
- [Project Euler __ C++]()
