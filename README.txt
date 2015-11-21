This assignment follows specification of HW3, examples & discussions on D2L.
Please Note - 
1. 
O/P is Either 'TRUE' or 'FALSE' (all letters in upper-case)

2. Infinite loop is not Handled as specified in D2L.
ex: 
Better(A,B)
3
Better(A,x)&Better(x,B)=>Better(A,B)
Better(A,C)
Better(C,B)
I/Ps like these will cause MemoryOutofBoundException due to infinite loop.

2. Variable x can take multiple values in multiple sentences. But only a unique value would be substituted in all x for a given sentence.
eg:
A(A,B)
4
B(x,ABC)&C(x,CDE)=>A(A,B)
D(x,ABC)=>B(DEF,ABC)
D(DE,ABC)
C(DEF,CDE)
This will evaluate to TRUE. In sentence 1, x will be substituted with DEF. In Sentence 2, x will be substituted with DE.

3. Incorrect I/P is not handled. So the solution expects the I/P file to exactly follow the specification.

4. I/P O/P file should be placed inside the folder 'Mohit_Raman_HW3' as per the grading_perl.pl script provided. I have successfully executed the assignment with the grading script in Aludra.  

Please contact me in case of any unforeseen errors.

Happy Grading!