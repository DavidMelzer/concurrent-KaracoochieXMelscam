# Concurrency — Exercise 3: Locking 1

#### Problem 1: What is the double-checked locking antipattern? Why is it an antipattern?

##### What?

The Double-Checked Locking Pattern is a design pattern used to reduce the overhead of acquiring a lock during the lazy
initialization of resources in a multithreaded environment. It is called a pattern because it is a common approach to
implementing lazy initialization in a multithreaded environment.

##### How?

1. First Check (Without Lock): It first checks if the instance is null without acquiring a lock. This is to avoid the
   overhead of locking if the instance is already initialized.
2. Acquiring Lock: If the instance is found to be null in the first check, it proceeds to synchronize a block of code to
   acquire a lock. This ensures that only one thread can initialize the instance.
3. Second Check (With Lock): Once inside the synchronized block, it performs a second check to see if the instance is
   still null. This is necessary because another thread might have initialized the instance between the first check and
   acquiring the lock.
4. Initialization: If the instance is still null after the second check, it initializes the instance.
5. Release Lock: After initialization, it exits the synchronized block, releasing the lock, and returns the instance.

##### Why?

The Double-Checked Locking Pattern is used to minimize the cost of acquiring a lock during the lazy initialization of
resources in a multithreaded environment by checking if the resource has already been initialized before proceeding to
lock.

##### Why is it an antipattern?

Double-Checked Locking is viewed as an antipattern primarily due to its inherent complexity and the potential for
subtle concurrency issues. It attempts to reduce lock overhead for better performance but can lead to memory consistency
errors, especially in languages like Java without proper use of the volatile keyword. This complexity increases the risk
of bugs that are hard to detect and fix. Furthermore, the advent of modern programming constructs provides simpler and
more reliable alternatives for lazy initialization and resource management, making the use of Double-Checked Locking
unnecessary and potentially hazardous in a multithreaded environment.
