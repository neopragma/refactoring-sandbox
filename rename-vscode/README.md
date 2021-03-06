# Refactoring the Hard Way, Part 2 

This is a continuation of an exercise to learn how difficult it is to add refactoring support to a text editor. There's no intent to produce a fully-featured and robust solution, but just in case it proves to be useful I want to focus on a couple of tools that don't already have satisfactory refactoring support for widely-used legacy languages.

To be clear: This isn't a "lesson". I'm not teaching you something I already know how to do. I'm writing down what happens as I try to teach myself how to do something that's new to me. Others already know how to write refactoring logic and how to write extensions and plugins for editors and IDEs. Fair warning, in case that sort of thing doesn't interest you. 

I write "we" instead of "I", as if you and I are going on this exploration together. But anything that's wrong is just on me, and not you. 

We'll start with Microsoft VSCode, a modern text editor that has gained a lot of popularity in recent years. It has very limited support for refactoring compared with its "older sibling", VisualStudio. 

## Rename refactoring for VSCode

In [the first part of our exploration](https://neopragma.com/2020/05/refactoring-the-hard-way-part-1/), we implemented some basic _raname_ functionality in Bash. It isn't a complete implementation, but it can operate on multiple files and provides a crude sort of _undo_ functionality by copying the original files before making changes. Let's get a sense of how difficult it would be to do something similar as a VSCode extension.

### Generate a boilerplate extension 

VSCode extensions are generally written in TypeScript or JavaScript. We'll use TypeScript here. To work on VSCode extensions we're in the Node.js and npm world, so if you want to play with this you should configure a development environment accordingly. 

Details of how to write a VSCode extension are covered in the tutorial at [https://code.visualstudio.com/api/get-started/your-first-extension](https://code.visualstudio.com/api/get-started/your-first-extension) and in the VSCode documentation. That's not our focus here, so we'll just assume all that. 

We'll start with a boilerplate VSCode extension generated by Yeoman: 

<pre>
yo code 
</pre>

It asks a few questions before generating a boilerplate "Hello, World!" extension. You get something like this:

<pre>
~ $ mkdir rename-vscode
~ $ cd rename-vscode
rename-vscode $ yo code

     _-----_     ╭──────────────────────────╮
    |       |    │   Welcome to the Visual  │
    |--(o)--|    │   Studio Code Extension  │
   `---------´   │        generator!        │
    ( _´U`_ )    ╰──────────────────────────╯
    /___A___\   /
     |  ~  |     
   __'.___.'__   
 ´   `  |° ´ Y ` 

? What type of extension do you want to create? New Extension (TypeScript)
? What's the name of your extension? Rename
? What's the identifier of your extension? rename
? What's the description of your extension? Proof-of-concept Rename refactoring for VSCode
? Initialize a git repository? Yes
? Which package manager to use? npm
   create rename/.vscode/extensions.json
   create rename/.vscode/launch.json
   create rename/.vscode/settings.json
   create rename/.vscode/tasks.json
   create rename/src/test/runTest.ts
   create rename/src/test/suite/extension.test.ts
   create rename/src/test/suite/index.ts
   create rename/.vscodeignore
   create rename/.gitignore
   create rename/README.md
   create rename/CHANGELOG.md
   create rename/vsc-extension-quickstart.md
   create rename/tsconfig.json
   create rename/src/extension.ts
   create rename/package.json
   create rename/.eslintrc.json
</pre>

Among other things, that generates a _package.json_ file containing some configuration settings for the extension and an _extension.ts_ file containing the source code for the extension. We'll do most of our coding in those two files. 

### Steps in the _rename_ process 

Basically we need code that does the following: 

1. Pick up the highlighted text in the currently-active editor pane. 
2. Ask the user to enter the replacement text for the _rename_ refactoring. 
3. Ask the user to confirm that they want to replace text _a_ with text _b_.
4. Make a backup of each file that will be affected by the change. 
5. Make the change in all relevant files. 

The _undo_ feature is a separate command. It needs to do the following: 

1. Copy the backup files over the modified ones. Note that ultimately I did not write the _undo_ feature for _rename_, in the interest of time. 

### The bones of the extension 

So far, we have enough code to show the general structure of a VSCode extension that could support a _rename_ operation. It's crude and incomplete, but here's where we are just now. This is file _src/extension.ts_.

<pre>
import * as vscode from 'vscode';

export function activate(context: vscode.ExtensionContext) {
	let disposable = vscode.commands.registerCommand('rename.refactor', getTextToRefactor);
	context.subscriptions.push(disposable);
}

export function deactivate() {}

async function getTextToRefactor() {
    const originalText = getSelectedText() || "";
	const replacementText = await solicitReplacementText(originalText) || "";
	replaceTextIfConfirmed(originalText, replacementText);
}

function getSelectedText() {
	const window = vscode.window;
	const editor = window.activeTextEditor;  
	if (editor) {  
		const selection = editor.selection;  
		if (selection) {  
			const originalText = editor.document.getText(selection); 
			if (originalText.length > 0) {
				return originalText;
			}
		}
	}
	return undefined;		
}

async function replaceTextIfConfirmed(originalText: string, replacementText: string) {
	const userConfirmation = await solicitUserConfirmation(originalText, replacementText);
	if (userConfirmation) {
		replaceText(originalText, replacementText);
	}	
}

function solicitReplacementText(originalText: string) {
	return vscode.window.showInputBox({
		placeHolder: "Enter replacement text",
		prompt: "Selected text: <" + originalText + ">"
	});
}

function solicitUserConfirmation(originalText: string|undefined, replacementText: string|undefined) {
	if (replacementText) {
		return vscode.window.showInputBox({
			value: 'Do you want to replace <' + originalText + '> by <' + replacementText + '>?'
		});	
	}
}

function noTextSelected() {
	vscode.window.showInformationMessage('No text selected');
}

function replaceText(originalText: string, replacementText: string) {
    vscode.window.showInformationMessage("...and then a miracle occurs.");
}
</pre>

That's hardly a complete implementation. As you can see, when the time finally comes to perform the actual text replacement, the extension just displays the message, "...and then a miracle occurs." 

But this is a reasonable point to pause for an explanation. The code demonstrates it's feasible to build an extension to handle refactoring. I think it's advisable to proceed a bit further for proof-of-concept purposes. What I've learned from this is how to _get started_ writing a new VSCode extension. There's a whole lot more to learn. 

First, I want to acknowledge the obvious: VSCode already has refactoring extensions, for instance for Python and (unsurprisingly) TypeScript. So there's no doubt it's feasible to do this. We needn't prove that. 

There is a lack of refactoring support in this tool for Java, C#, and COBOL, which are the most widely-used languages for existing business applications, and therefore the languages most in need of refactoring support. Refactoring for Java is well supported in JetBrains IntelliJ IDEA, and for C# in Microsoft VisualStudio. I'm not aware of any development tool that includes good support for refactoring in COBOL.

If you're interested in using lighter-weight development tools than the big-footprint, feature-rich IDEs, you may appreciate improved refactoring support for those languages in tools like VSCode, Vim, Sublime Text, Atom, etc. So if we want to play with this idea, it makes sense to keep the main legacy languages in mind. That said, this is not an attempt to fill that gap in a robust way; it's only a learning exercise. What value may come from it is yet to be seen.

Most of the sample code you find online for VSCode extensions has all the logic contained in the _activate_ function in the _extension.ts_ or _extension.js_ file, glommed together into a big if/else structure. It's my habit to break things out into smaller methods. That's the reason there are several methods in place already, even though we haven't even begun to rename anything. 

If you prefer to see all the logic in one place, you can structure your file in that way. It won't make any difference at execution time; it's a readability consideration. Most extensions probably won't have a lot of code, so the benefit of factoring out small methods may be small. But I'm old, and I have my habits. 



### Find and replace text in one file

Before we try to process the project directory tree, let's see if we can do a _rename_ in one file. Many examples we can find online deal with the document that is open in the currently-active editor. As we are interested in processing files that are not open in any editor, let's begin by processing a single file without assuming it's open in an editor. 

In principle we can use the _fs_ module to read and write files. In the context of what the user is doing, that might not really be the best option. The user is a programmer who is in the middle of editing source code and configuration files. They might make a number of changes, including refactorings, before saving a file. 

According to the VSCode extension API documentation, under _workspace_ there's a method called _openTextDocument_ that will return the contents of the file as it currently stands within the tool, including unsaved changes. The method returns a _TextDocument_ instance that includes an indicator named _isDirty_ that is set to _true_ if the document contains unsaved changes. For our purposes, this would be better than a standard filesystem "read" operation, because we want to see what the user sees at the moment they invoke the _rename_ operation. 

Unfortunately, this is not straightforward. After hours of trial and error, I found a sequence of calls that would open a file as a _TextDocument_ based on a Uri, but it didn't seem to work for relative paths. Then I learned this is by design, to avoid potential security issues because there's no way to guarantee the extension "should" have access to a given relative path. See [this bug report and response](https://github.com/microsoft/vscode/issues/34449). 

There _is_ a way to get the "root" directory of the VSCode project. That suggests that when we extend the functionality to search recursively through the project directory tree, the code will work as expected. For this particular step we want to see that the code can process a single file. I created a test file for the purpose and placed it in a particular location under the project directory tree, but that is a hard-coded, temporary solution. 

I will admit to you that I had a lot of difficulty getting this far. I read a lot of documentation, examined a lot of sample code and existing extensions on Github, and spent hours doing trial and error. What I ended up with is a truly horrific and stupid implementation. It does the _rename_ of all occurrences of the selected string within the single test file, but it does so by replacing the entire contents of the text document object. I just couldn't discover a better way in the time I was willing to spend on this exercise. 

On a positive note, I am certainly achieving my goal of gaining appreciation for the work others have done to write refactoring plugins and extensions for editors and IDEs! 

One thing that became clear is the extension API is rich, and if we take the time to learn it well, there's a lot we can do with it. But not today.

I am embarrassed to say the following code is where we've ended up so far: 

<pre>
import * as vscode from 'vscode';

export function activate(context: vscode.ExtensionContext) {
	let disposable = vscode.commands.registerCommand('rename.refactor', renameRefactoring);
	context.subscriptions.push(disposable);
}

export function deactivate() {}

async function renameRefactoring() {
    const originalText = getSelectedText() || "";
	const replacementText = await solicitReplacementText(originalText) || "";
	replaceTextIfConfirmed(originalText, replacementText);
}

function getSelectedText() {
	const window = vscode.window;
	const editor = window.activeTextEditor;  
	if (editor) {  
		const selection = editor.selection;  
		if (selection) {  
			const originalText = editor.document.getText(selection); 
			if (originalText.length > 0) {
				return originalText;
			}
		}
	}
	return undefined;		
}

async function replaceTextIfConfirmed(originalText: string, replacementText: string) {
	const userConfirmation = await solicitUserConfirmation(originalText, replacementText);
	if (userConfirmation) {
		processDocument(vscode.Uri.file("./test/testFile.ts"), originalText, replacementText);
	}	
}

function solicitReplacementText(originalText: string) {
	return vscode.window.showInputBox({
		placeHolder: "Enter replacement text",
		prompt: "Selected text: <" + originalText + ">"
	});
}

function solicitUserConfirmation(originalText: string|undefined, replacementText: string|undefined) {
	if (replacementText) {
		return vscode.window.showInputBox({
			value: 'Do you want to replace <' + originalText + '> by <' + replacementText + '>?'
		});	
	}
}

function processDocument(uri: vscode.Uri, originalText: string, replacementText: string) {
	var documentUri: vscode.Uri = vscode.Uri.parse(vscode.workspace.rootPath + "/rename/src/test/testFile.ts");
    vscode.workspace.openTextDocument(documentUri).then((document: vscode.TextDocument) => {
		let modifiedText = document.getText().replace(new RegExp(originalText, 'g'), replacementText);
		var firstLine = document.lineAt(0);
		var lastLine = document.lineAt(document.lineCount - 1);
		var replacementRange = new vscode.Range(firstLine.range.start, lastLine.range.end);				
        vscode.window.showTextDocument(document, 1, false).then(e => { 
			 e.edit(edit => {
		 		edit.replace(replacementRange, modifiedText);
		 		vscode.workspace.applyEdit;
             });
	     });
    });
}

</pre>

This clunky implementation is not pretty to behold. User interaction is via input boxes that pop up in the VSCode window. Other refactoring extensions I've seen on Github provide a much more natural and visually-pleasing user interface. There are many options in the extensions API, and given time to learn them we could improve this implementation. 

### Find and replace text in all files

In our shell implementation we passed a filename glob to _sed_. In a language-specific implementation, we can be smarter than that and choose appropriate files based on file type. The VSCode extension API provides a way to register specific file types for particular extension commands, so this can be pretty seamless. 

For now, let's assume the _rename_ will apply to all files in the project directory hierarchy. Our next step is to wrap the _processDocument_ method in logic that walks the project directory tree. 

The method _vscode.workspace.findFiles_ returns a list of the absolute paths of all the files in the project workspace. This is just what we want. We'll apply the _rename_ operation to the contents of each file in this list. A brute-force implementation, yes, but this is only an exploratory learning exercise and not a polished solution. 

Method _findFiles_ takes a filename glob for files to include and optionally another one for files to exclude, so it will be possible to refine this behavior later either by including language-specific "smarts" in the extension or by asking the user to enter a filename glob. 

In _src/extension.ts_, the methods _processFiles_ and _processDocument_ were affected by this change. 

<pre>
function processFiles(originalText: string, replacementText: string) {
	vscode.workspace.findFiles('rename/src/**/*.ts').then((files) => {
		files.forEach((filePath) => {
			processDocument(filePath, originalText, replacementText);
		});
	});
}

function processDocument(uri: vscode.Uri, originalText: string, replacementText: string) {
    vscode.workspace.openTextDocument(uri).then((document: vscode.TextDocument) => {
		if(document.getText().search(originalText) > -0) {
			let modifiedText = document.getText().replace(new RegExp(originalText, 'g'), replacementText);	
			var firstLine = document.lineAt(0);
			var lastLine = document.lineAt(document.lineCount - 1);
			var replacementRange = new vscode.Range(firstLine.range.start, lastLine.range.end);				
			vscode.window.showTextDocument(document, 1, false).then(e => { 
				 e.edit(edit => {
			 		edit.replace(replacementRange, modifiedText);
			 		vscode.workspace.applyEdit;
			     });
			 });
		}
	});
}
</pre>

The filename glob passed to _findFiles_ is still hardcoded, but it's a little bit less hard. Next step will be to prompt the user with a default glob and allow them to enter something different if they wish. 

### User controls the filename glob for the _rename_ 

This proved to be pretty easy, especially after all the struggles to figure out how to get anything at all to work. A new method, _solicitFilenameGlob_, presents the user with a default glob of '\*\*/\*.\*' that means "all files from the project root directory down", and the user can enter a different pattern if they wish. That method is called before the final user confirmation of the refactoring. 

<pre>
import * as vscode from 'vscode';

export function activate(context: vscode.ExtensionContext) {
	let disposable = vscode.commands.registerCommand('rename.refactor', renameRefactoring);
	context.subscriptions.push(disposable);
}

export function deactivate() {}

async function renameRefactoring() {
    const originalText = getSelectedText() || "";
	const replacementText = await solicitReplacementText(originalText) || "";
	const filenameGlob = await solicitFilenameGlob() || '**/*.*';
	replaceTextIfConfirmed(filenameGlob, originalText, replacementText);
}

function getSelectedText() {
	const window = vscode.window;
	const editor = window.activeTextEditor;  
	if (editor) {  
		const selection = editor.selection;  
		if (selection) {  
			const originalText = editor.document.getText(selection); 
			if (originalText.length > 0) {
				return originalText;
			}
		}
	}
	return undefined;		
}

async function replaceTextIfConfirmed(filenameGlob: string, originalText: string, replacementText: string) {
	const userConfirmation = await solicitUserConfirmation(originalText, replacementText);
	if (userConfirmation) {
		processFiles(filenameGlob, originalText, replacementText);
	}	
}

function solicitReplacementText(originalText: string) {
	return vscode.window.showInputBox({
		placeHolder: "Enter replacement text",
		prompt: "Selected text: <" + originalText + ">"
	});
}

function solicitFilenameGlob() {
	return vscode.window.showInputBox({
		placeHolder: '**/*.*'
	})
}

function solicitUserConfirmation(originalText: string|undefined, replacementText: string|undefined) {
	if (replacementText) {
		return vscode.window.showInputBox({
			value: 'Do you want to replace <' + originalText + '> by <' + replacementText + '>?'
		});	
	}
}

function processFiles(filenameGlob: string, originalText: string, replacementText: string) {
	vscode.workspace.findFiles(filenameGlob).then((files) => {
		files.forEach((filePath) => {
			processDocument(filePath, originalText, replacementText);
		});
	});
}

function processDocument(uri: vscode.Uri, originalText: string, replacementText: string) {
    vscode.workspace.openTextDocument(uri).then((document: vscode.TextDocument) => {
		if(document.getText().search(originalText) > -0) {
			let modifiedText = document.getText().replace(new RegExp(originalText, 'g'), replacementText);	
			var firstLine = document.lineAt(0);
			var lastLine = document.lineAt(document.lineCount - 1);
			var replacementRange = new vscode.Range(firstLine.range.start, lastLine.range.end);				
			vscode.window.showTextDocument(document, 1, false).then(e => { 
				 e.edit(edit => {
			 		edit.replace(replacementRange, modifiedText);
			 		vscode.workspace.applyEdit;
			     });
			 });
		}
	});
}
</pre>

### A weak implementation 

This implementation is hardly sufficient for real work. All it really does is string replacement. I'm satisfied that I've learned a bit about the general structure of a VSCode extension and the general level of difficulty of writing one. But a "real" _rename_ refactoring tool has to be smarter than this. 

### Weakness: When search string is part of a longer string

The search string (what I call _originalText_ in the code) might occur as part of a longer string, and the user might not intend to modify the longer string. Consider an application that has these lines of code somewhere in the code base, not necessarily in the same source artifact: 

<pre>
    protected Money getAccountBalance() {...}

    private void calculateNewAccountBalance() {...}

    private void startAccountFraudMonitor() {...}

    public class Account {...}

    public class CheckingAccount extends Account {...}
</pre>

Let's say the programmer wants to change the name of the Account class to "ConsumerAccount". Our implementation would change the string "Account" in all the places shown above. But the chances are good that would not be what is intended. 

### Weakness: Handling surrounding text 

A corollary of the first weakness is that our implementation doesn't consider text that might occur immediately before or after the search string. A poor solution to the substring problem would be not to select occurrences of the search string that had anything other than whitespace before or after. That implementation would overlook valid "hits" in the search.

Depending on the syntax of the language in question, various characters other than spaces, newlines, and tabs may occur before or after the search string. Thinking of our "big three" legacy languages, the syntax of both Java and C# make it possible for a name to be preceded by "(", ",", "{", "[", "=" and so forth, or to be followed by "(", ",", "{", "[", ")", ";", "}", "]", "=" and so forth.

 In COBOL source, a variable name in the Procedure Division might be preceded by "(" or followed by "(", ".", or a few other characters. The search string (in COBOL) might be part of a long line that is broken up by a continuation line, with the first part of the string on one line and the second part on the next line. It's an edge case for sure, but it does occur in existing code bases. The tool has to be aware of the possibilities for each supported language. 

### Weakness: Languages that have classes

That sample code is Java-esque. So, let's pretend it _is_ Java and the programmer wants to rename the Account class. In Java, public classes must live in files whose names match the class names. Our implementation doesn't consider that. We would end up with 'public class ConsumerAccount' in a file named 'Account.java'. 

The same consideration applies to Ruby, as well. A class named 'ConsumerAccount' must live in a file named 'consumer\_account.rb'. So, our implementation of _rename_ would break the code. 

Our implementation fails for any language that has that sort of characteristic.

Some languages have classes but don't require filenames to match the class names. Kotlin comes to mind, as it's a popular language these days. In these cases, the tool might need to ask the user what they want to do about the filenames, if anything. If we want to avoid interrupting the programmer's train of thought with prompts, we might just do nothing and let the programmer decide to deal with file names separately from the refactoring operation. In any case, it's a deliberate design decision that requires some thought.

### Weakness: Languages that have unusual characteristics

Refactoring is very important for working with existing code bases. One of the most common languages for existing code bases is COBOL, and there is a lack of tooling to support refactoring for mainframe COBOL. If we wanted to fill that gap, our _rename_ functionality would have to consider some of the realities of legacy mainframe COBOL code bases. 

COBOL is case-agnostic. All the following declarations refer to the same thing: 

<pre>
    05  Account-Number     Pic x(19).

    05  account-number     pic x(19).

    05  ACCOUNT-NUMBER     PIC X(19). 

    05  acCOunt-nuMbEr     pIC x(19).
</pre>

Our implementation would not recognize all those declarations as equivalent. It would match exactly on what the user had selected in the editor pane, and it would overlook other occurrences the programmer had intended to change. Of course, no one would design code that way on purpose. But remember, we're talking about legacy code that has been around (possibly) for decades, and that has been modified thousands of times by dozens of people, most likely using relatively crude development tools. You'll find anything and everything in those code bases. That means if you write a tool to modify source code, it has to handle anything and everything.

Modern COBOL is free form, but the vast majority of legacy COBOL code is not written that way. It conforms with the old standard that each line of source code represents an 80-column Hollerith card. The first 6 columns are reserved, and were often used for a sequence number. Column 7 is reserved for an indicator byte, such as a '*' to denote a comment line or a '-' to denote a continuation line. Columns 73-80 are reserved, and may contain any value for any purpose; sometimes this was used by another tool to perform some kind of automated change management, and our tool would have to avoid breaking that. Source code fits between columns 8 and 72 inclusive, with columns 8-11 reserved (in the Procedure Division) for paragraph headers, and other code limited to columns 12 through 72. 

For purposes of a _rename_ refactoring, that means if the replacement text makes a line of code extend beyond column 72, the tool has to break that line up somehow. There's more than one way to accomplish this, and the choice is context-dependent. So a "simple" _rename_ operation isn't so simple. 

Another characteristic of COBOL source code: Programs usually include a large number of COPY statements that bring in snippets of code from other files, called _copybooks_. At editing time, those COPY statements are not resolved. All you see in the editor pane are the COPY statements themselves, and not the full code that results from importing the contents of the copybooks. Our _rename_ tool would have to plow through the copybooks recursively to find all relevant occurrences of the search string.  

There's an option called COPY REPLACING, in which different programs provide substitution values at compile time for copied code. This has implications for a _rename_ operation, as well. In this case, we want to _avoid_ changing the substitution marker text in the copybooks (if it happens to match the search string), as other programs besides the one we're working with are dependent on those values. 

### Weakness: Poor user experience for programmers

Another problem with our implementation is that it doesn't provide a particularly good user experience for programmers. The VSCode extensions API gives us better ways to interact with the user; I just didn't take the time to learn them for this exercise. 

We also ask the user to enter a filename glob to control which files will be affected by the refactoring. This is a poor way to handle it. It would be preferable to have a configuration file somewhere that "knows" about particular programming languages and the project directory structure, and that can make reasonable default decisions about which files to include in the _rename_ operation.

There are some general defaults that are fairly obvious, too. We almost certainly want to exclude files such as:

- .gitignore, .hgignore, and similar
- LICENSE or LICENSE.txt or LICENSE.md and similar
- CODE\_OF\_CONDUCT.md 

However, some files that aren't source artifacts might need the _rename_ in order to stay consistent with the source code. In these cases, we can't accurately guess what the programmer intends to do. They must decide on a case-by-case basis. These are files such as:

- README.md 
- *.fsproj, *.csproj (.NET project files) 
- *.ini (tool-related configuration or installation files) 
- package-info.java (Java javadoc files)
- *.properties, *.yaml, *.json, *.xml, *.txt, *.conf (application configuration files) 
- Any sort of documentation file 

A significant user experience factor is that our implementation opens every file that contains matching text and leaves the files open in editor panes. This is a real annoyance. 

### Weakness: No convenient _undo_ support

Finally, we never got around to implementing an _undo_ feature. 


### Next steps 

I feel as if I've learned a bit about what's involved in writing a VSCode extension to support basic refactorings, and I've gained a greater appreciation for the work of others in this area. 

My feeling is the amount of effort needed to finish a language-specific implementation of _rename_ would be about the same as the effort it took me to write the current implementation; about three evenings of work, or roughly 9-10 hours. That's with no previous experience writing VSCode extensions, or using TypeScript. So, a team working together, with members familiar with the tools, could probably write pretty solid support for _rename_ for any single programming language in about the same amount of time, with the possible exception of COBOL because of the longer list of idiosyncracies to be supported. 

Doubling the time to ensure we have time for testing and polishing, we're looking at, maybe, 40 hours or so to come up with a reasonable first release for _rename_ to support one programming language. 

Some refactorings would be easier to write than _rename_. I'm thinking of _move line up_, for example. (I'm not sure that really qualifies as a "refactoring" as it seems likely to change behavior, but it's often included under that heading in development tools.) 

As usual for software development, once you've got one of them done, the rest of them will be a little quicker to implement because you'll have the structure in place, and probably some reusable code, as well. Searching for a string is probably a pretty common function, for instance. 

What's the next logical step in learning about how to implement refactoring support for text editors? I'm interested in trying to write a plugin for a different _type_ of editor...possibly Vim. 

But I'm not sure we've learned quite enough from VSCode just yet. The _rename_ operation is a little unusual as refactorings go, because it affects multiple documents, including files that are not open. Most refactorings only concern the document currently open in the editor pane where the programmer has focus. It might be informative to write something like that for VSCode; not something as trivial as _move line up_, but not something terribly complicated, either. 

Another learning consideration is that we've only attempted to write one VSCode extension; typically, that's not sufficient experience to get started with new skills. The first thing you try is full of errors and stumbling around. So it's good to try a second thing, at least. 

I propose we try to implement _extract constant_ next time. 

