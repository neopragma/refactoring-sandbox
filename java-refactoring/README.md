# Sample refactoring: Replace conditional with polymorphism 

Package testmodule contains the original code. The test directory tree has some unit test cases added to provide a modest amount of safety for refactoring the code. 

Package testmodule2 shows a first pass at refactoring to make the code easier to understand. 

Package testmodule3 shows another small step toward more-understandable code. 

Package testmodule4 shows some preliminary work to loosen dependencies in preparation to break out polymorphic classes. 

Package testmodule5 shows one possible polymorphic implementation of the original conditional logic. This implementation is based on Java's ResourceBundle facility. Other approaches are also possible.
