# Packer

Description
-----------
The Package Challenge library provides a solution for optimizing package selection based on weight and cost constraints. It solves the problem of determining which items to include in a package, given a weight limit, to maximize the total cost. The library accepts input from a file containing test cases and returns the solution as a string.

Installation
-------------
To use this library in your Java project, follow these steps:

Download the library JAR file from this link https://github.com/sbsathish999/Packer/blob/master/lib/com.mobiquity.packer-1.0-SNAPSHOT.jar

Add the JAR file to your project's classpath.

Import the library classes and start using them in your code.

Usage
------
To use the Package Challenge library in your Java project, follow these steps:

Add the library JAR file to your project's classpath.

Import the necessary classes from the library into your Java file.

Call the pack method from the com.mobiquity.packer.Packer class, passing the absolute file path as a parameter.

Handle the returned solution string according to your application's requirements.

Input Format
------------
The input file should contain multiple lines, with each line representing a test case. Each test case consists of the weight limit followed by a colon (:) and a list of items enclosed in parentheses. Each item is represented by its index number, weight, and cost, separated by commas.

Example:
<pre>
81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
8 : (1,15.3,€34)
75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)
</pre>
Output Format
-------------
The library returns a solution string for each test case. Each solution is represented by a new row in the output string. The indexes of the selected items are separated by commas. If no items can be selected, a hyphen (-) is used.

Example:
<pre>
4

\-

2,7

8,9
</pre>

Exceptions
----------
If any constraints are not met or there is an error in the input file, the library throws an APIException. Handle this exception in your code and provide appropriate error handling and messaging.