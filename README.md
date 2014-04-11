SimpleOutOfCore
===============

Just a simple, buggy and not optimized out-of-core challendge I did for an interview.

The important aspect of the application was to work with very light JVM and Heap configuration.

Altought not optimized it can be executed with the following JVM configuration:
-Xmx2m -Xss2m

It's composed of four main elements:
  - A random file creator, were a JSON file with preset size is filled with random data.
  - An Iterator to pull the information from the file.
  - JSON Parser to interpret the information
  - Simple MongoDB Connector to update the information
  
Again, it's still very buggy and not optimized but it is a fun challenge nonetheless.
