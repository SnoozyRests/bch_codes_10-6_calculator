# Java BCH-Code(10, 6) Calculator
Developed for the University of the West of England Cryptography Module (UFCFT4-15-3) coursework.
Developed in Intellij.

##Tests 
 
**Passed All Tests 21/09/2018**

In: The input  
Out: Corrected Output (n/a if more than two errors)  
Result: Result of correction / calculation, split by error positions.  
Debug: If not getting the desired output, check these internal values for mathematical errors.
  
In: 3745195876  
Out: 3745195876  
Result: No Error

In: 3945195876  
Out: 3745195876  
Result: Single Error, Position 2, Magnitude 2.  
Debug: (i = 2, a=2), syn(2,4,8,5) 

In: 3715195076  
Out: 3745195876  
Result: Double Error, 1st (Position 8, Magnitude 3), 2nd (Position 3, Magnitude 8)  
Debug: (i=8, a=3, j=3, b=8), syn (0,4,0,3), pqr(5,0,10)

In: 0743195876  
Out: 3745195876  
Result: Double Error, 1st (Position 4, Magnitude 9), 2nd (Position 1, Magnitude 8)  
Debug: (i=4, a=9, j=1, b=8), syn(6,0,9,1), pqr(1,6,4)

In: 3745195840  
Out: 3745195876  
Result: Double Error, 1st (Position 10, Magnitude 5), 2nd (Position 9, Magnitude 8)  
Debug: (i=10, a=5, j=9, b=8), syn(2,1,4,8), pqr(4,1,8)

In: 2745795878  
Out: n/a  
Result: More than two errors.  
Debug: syn(7,5,8,10), pqr(2,8,3)

In: 8745105876  
Out: 3745195876  
Result: Double Error, 1st (Position 6, Magnitude 2), 2nd (Position 1, Magnitude 5)  
Debug: (i=6, a=2, j=1, b=5), syn(7,6,0,8), pqr(3,1,7)

In: 3745102876  
Out: 3745195876  
Result: Double Error, 1st (Position 6, Magnitude 2), 2nd (Position 7, Magnitude 8)  
Debug: (i=6, a=2, j=7, b=8), syn(10,2,2,8), pqr(6,10,10)

In: 3742102896  
Out: n/a  
Result: More than two errors.  
Debug: syn(9,8,6,9), pqr(10,0,8)

In: 1145195876  
Out: 3745195876  
Result: Double Error, 1st (Position 1, Magnitude 9), 2nd (Position 2, Magnitude 5)  
Debug: (i=1, a=9, j=2, b=5), syn(3,8,7,5), pqr(10,3,9)

In: 1115195876  
Out: n/a  
Result: More than two errors  
Debug: syn(0,10,2,1), pqr(1,2,5)

In: 3121195876  
Out: n/a  
Result: More than two errors.  
Debug: syn(10,10,4,5), pqr(5,10,10)

In: 3745191976  
Out: 3745195876  
Result: Double Error, 1st (Position 8, Magnitude 1), 2nd (Position 7, Magnitude 7)  
Debug: (i=8, a=1, j=7, b=7), syn(8,2,0,9), pqr(4,6,4)

In: 3745190872  
Out: 3745195876  
Result: Double Error, 1st (Position 7, Magnitude 6), 2nd (Position 10, Magnitude 7)  
Debug: (i=7, a=6, j=10, b=7), syn(2,2,4,5), pqr(7,2,6)
