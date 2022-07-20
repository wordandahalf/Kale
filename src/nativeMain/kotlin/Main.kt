import kotlinx.cinterop.*
import llvm.*
import antlr3c.*
import platform.posix.exit

inline fun TOKENSOURCE(lexer: pKaleLexer) =
    lexer.pointed.pLexer!!.pointed.rec!!.pointed.state!!.pointed.tokSource

fun main(args: Array<String>) {
    memScoped {
        // Create the input stream. The file name needs to be cast to an ANTLR3 type.
        val fileName = (args.getOrNull(0) ?: "script.k")
        val input = antlr3FileStreamNew(fileName.cstr.ptr.reinterpret(), ANTLR3_ENC_UTF8)

        if (input == null) {
            println("Unable to open file $fileName due to malloc() failure!")
            exit(ANTLR3_ERR_NOMEM)
        }

        // Create the lexer
        val lexer = KaleLexerNew(input)

        if (lexer == null) {
            println("Unable to create the lexer due to malloc() failure!")
            exit(ANTLR3_ERR_NOMEM)
        }

        // Create the token stream
        val tokenStream = antlr3CommonTokenStreamSourceNew(ANTLR3_SIZE_HINT, TOKENSOURCE(lexer!!))

        if (tokenStream == null) {
            println("Out of memory trying to allocate token stream!")
            exit(ANTLR3_ERR_NOMEM)
        }

        // Create the parser
        val parser = KaleParserNew(tokenStream)

        if (parser == null) {
            println("Out of memory tring to allocate parser!")
            exit(ANTLR3_ERR_NOMEM)
        }

        // Interacting with pointers with Kotlin/Native is extremely disgusting.
        // Invoke the `parse` rule (see Kale.g) on the input and get the resulting AST.
        val ast = parser!!.pointed.parse!!.invoke(parser).ptr.reinterpret<KaleParser_parse_return_struct>().pointed
        val tree = ast.tree

        val childrenCount = tree!!.pointed.getChildCount!!.invoke(tree)

        println("Tree has $childrenCount children.")

        var child: pANTLR3_BASE_TREE
        for(i in 0 until childrenCount.toInt()) {
            child = tree.pointed.getChild!!.invoke(tree, i.convert())!!.reinterpret()

            val type = child.pointed.getType!!.invoke(child).toInt()
            val text = child.pointed.getText!!.invoke(child)!!.pointed.chars!!.reinterpret<ByteVar>()
                .toKString()
                .replace("\n", "\\n")
                .replace("\r", "\\r")

            println("[${type.toString().padStart(2, ' ')}]\t'${text}'")
        }
    }
}