# Kale
Kale is a hobby programming language built upon Kotlin/Native, LLVM, and ANTLR3.

Its goal is to illustrate the (oft convoluted) process of programming language development and how to leverage modern tools to make it more accessible.

The choice of Kotlin/Native was less about any objective metric and more about "Can it be done?"

### Current Progress

I've gotten as far as writing the language grammar, creating the static library, and interfacing with it using Kotlin.
When running the project, it will look for a `script.k` file and parse it, printing its AST.

### Architecture

Kale is broken roughly into three parts.
ANTLR3 (see `grammar/`) is used to generate a static library containing parsing and lexing logic.
This is used to generate an AST, which is further processed by the Kotlin/Native project and turned into LLVM IR.
With IR, the language can be JIT-ed and compiled to native object files.

### Resources
[Seo Sanghyeon's ANTLR3 C sample](https://gist.github.com/sanxiyn/845066)

[ANTLR3's C Runtime API and Usage Guide](https://www.antlr3.org/api/C/)

[Creating your own programming language with ANTLR.](http://web.archive.org/web/20140519034030/http://bkiers.blogspot.nl/2011/03/creating-your-own-programming-language.html)

[LLVM's My First Language Frontend Tutorial](https://llvm.org/docs/tutorial/MyFirstLanguageFrontend/LangImpl01.html)
> Note: Kale uses LLVM's C API (due to K/N interop limitations), so some adaptation is required.

[LLVM-C's Documentation](https://llvm.org/doxygen/group__LLVMC.html)

[llvm-hello](https://github.com/MWGuy/llvm-hello/blob/master/main.cpp)