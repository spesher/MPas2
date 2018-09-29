\ENCODING=ISO-8859-1
\Problem name: ilog.cplex

Minimize
 obj: x35 + x34 + x33 + x32 + x31 + x30 + x29 + x28 + x27 + x26 + x25 + x24
      + x23 + x22 + x21 + x20 + x19 + x18 + x17 + x16 + x15 + x14 + x13 + x12
      + x11 + x10 + x9 + x8 + x7 + x6 + x5 + x4 + x3 + x2 + x1
Subject To
 cover1:  x35 + x34 + x33 + x32 + x31 + x30 + x29 + x28 + x27 + x26 + x25  = 1
 cover2:  x35 + x34 + x24 + x23 + x22 + x21 + x20 + x19 + x18 + x17  = 1
 cover3:  x35 + x33 + x24 + x16 + x15 + x14 + x13 + x12 + x11  = 1
 cover4:  x32 + x23 + x16 + x10 + x9 + x8  = 1
 cover5:  x31 + x22 + x15 + x10 + x7  = 1
 cover6:  x30 + x21 + x14 + x9 + x6  = 1
 cover7:  x29 + x20 + x13 + x5  = 1
 cover8:  x28 + x19 + x12 + x4  = 1
 cover9:  x27 + x18 + x3  = 1
 cover10: x26 + x2  = 1
Bounds
 0 <= x35 <= 1
 0 <= x34 <= 1
 0 <= x33 <= 1
 0 <= x32 <= 1
 0 <= x31 <= 1
 0 <= x30 <= 1
 0 <= x29 <= 1
 0 <= x28 <= 1
 0 <= x27 <= 1
 0 <= x26 <= 1
 0 <= x25 <= 1
 0 <= x24 <= 1
 0 <= x23 <= 1
 0 <= x22 <= 1
 0 <= x21 <= 1
 0 <= x20 <= 1
 0 <= x19 <= 1
 0 <= x18 <= 1
 0 <= x17 <= 1
 0 <= x16 <= 1
 0 <= x15 <= 1
 0 <= x14 <= 1
 0 <= x13 <= 1
 0 <= x12 <= 1
 0 <= x11 <= 1
 0 <= x10 <= 1
 0 <= x9 <= 1
 0 <= x8 <= 1
 0 <= x7 <= 1
 0 <= x6 <= 1
 0 <= x5 <= 1
 0 <= x4 <= 1
 0 <= x3 <= 1
 0 <= x2 <= 1
 0 <= x1 <= 1
Generals
 x35  x34  x33  x32  x31  x30  x29  x28  x27  x26  x25  x24  x23  x22  x21 
 x20  x19  x18  x17  x16  x15  x14  x13  x12  x11  x10  x9  x8  x7  x6  x5  x4 
 x3  x2  x1 
End
