Insert Without reWriteBatchedInserts:

with index: Threads: 1 Rows: 1000000 Time: 20895 Rows/sec: 47000 MAssets/hour: 17
no index:   Threads: 1 Rows: 1000000 Time: 67833 Rows/sec: 14000 MAssets/hour: 5

reWriteBatchedInserts: +60%


Insert: Rows: 5000000 Time: 59295 Rows/sec: 84000
Copy: Rows: 5000000 Time: 59966 Rows/sec: 83000

Insert With indexes: Rows: 1000000 Time: 41921 Rows/sec: 23000
Copy With indexes: Rows: 1000000 Time: 50026 Rows/sec: 19000


With indexes:

Threads: 1 Rows: 1000000 Time: 44693 Rows/sec: 22000   8
Threads: 2 Rows: 2000000 Time: 56972 Rows/sec: 35000   12
Threads: 4 Rows: 4000000 Time: 82571 Rows/sec: 48000   17
Threads: 8 Rows: 2000000 Time: 39850 Rows/sec: 50000   18

No indexes

Threads: 1 Rows: 1000000 Time: 12142 Rows/sec: 82000   29
Threads: 2 Rows: 2000000 Time: 15452 Rows/sec: 129000  46
Threads: 4 Rows: 4000000 Time: 22860 Rows/sec: 174000  62
Threads: 8 Rows: 8000000 Time: 51520 Rows/sec: 155000  55

Row/sec = / 10 * 60 * 60 h
