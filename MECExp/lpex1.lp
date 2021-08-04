\ENCODING=ISO-8859-1
\Problem name: ilog.cplex

Minimize
 obj1: 400 x1 + 400 x2 + 1.522 x3 + x4 + 25.22
Subject To
 c1: x1 + x2 >= 1
 c2: x5 + x6  = 1
 c3: x3 + x6 >= 1
 c4: x3 + x4  = 1
 c5: x4 + x5 >= 1
Bounds
 0 <= x1 <= 1
 0 <= x2 <= 1
 0 <= x3 <= 1
 0 <= x4 <= 1
 0 <= x5 <= 1
 0 <= x6 <= 1
Binaries
 x1  x2  x3  x4  x5  x6 
End
