# Refactoring the Hard Way, Part 3

In <a href="">Part 1</a>, we wrote a quick-and-dirty _rename_ refactoring script in Bash, and a corresponding _undo_ script. The goal was to get a feel for what's involved in performing a _rename_ operation. 

In <a href="">Part 2</a>, we implemented the _rename_ functionality as a VSCode extension. The goal was to understand approximately how difficult it would be to implement a VSCode extension to support _rename_ refactorings, as well as to gain appreciation for the work others have done to write editor and IDE extensions and plugins to support refactoring. 

At the end of Part 2, we decided (well, I guess I decided for us) our next step would be to write an _extract constant_ extension for VSCode. Let's get to it!

## Extract Constant refactoring for VSCode 

We need the user to select a literal - either a string literal or a numeric literal - and then choose _extract constant_ (whatever "choose" means; probably a key mapping). What's interesting about that? Maybe these points, at least: 

- an opportunity to explore a different part of the VSCode extension API;
- an opportunity to do text insertion rather than text replacement (something a little different, anyway); and  
- an opportunity to handle a language-specific refactoring; constants look different in different programming languages.

## Steps in the _extract constant_ process 

To perform an _extract constant_ refactoring: 

1. User selects a literal in the editor pane.
2. User invokes the _extract constant_ command.
3. Extension prompts the user to enter a name for the new constant. 
4. User provides name.
5. Extension inserts text in the current document declaring the constant. 
6. Extension replaces all occurrences of the literal in the document with the name of the constant.

We'll start by generating a boilerplate extension just as we did for _rename_. We can dispense with showing those steps again, and with showing the generated boilerplate code. 






### Next steps

Vim is another popular lightweight editor for programming. It's a different type of editor from VSCode. Vim is a bimodal editor that comes from the pre-GUI days.In Part 4, we propose to write Vim plugins to perform _rename_ and _extract constant_ refactorings. Our goal is to see what more we can learn by working with a different text editor, with a very different design from VSCode. 
