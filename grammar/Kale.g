grammar Kale;

options {
    language = C;
    output = AST;
}

parse
  : method_declaration+ EOF
  ;

method_declaration
  : KEYWORD_METHOD IDENTIFIER LPAREN identifier_list* RPAREN (block | OP_ASSIGN NL* expression)
  ;

identifier_list
  : IDENTIFIER (COMMA IDENTIFIER)*
  ;

block
  : LBRACE NL* statements NL* RBRACE;

statements
  : (statement (STATEMENT_DELIMITER statement)*)? STATEMENT_DELIMITER?
  ;

statement
  : assignment
  | function_call
  ;

assignment
  : IDENTIFIER OP_ASSIGN expression
  ;

function_call
  : IDENTIFIER LPAREN expression (COMMA expression)* RPAREN
  ;

expression
  : or_expression
  ;

or_expression
  : and_expression (OP_REL_OR and_expression)*
  ;

and_expression
  : equ_expression (OP_REL_AND equ_expression)*
  ;

equ_expression
  : add_expression ((OP_REL_EQ | OP_REL_NEQ) add_expression)*
  ;

add_expression
  : mul_expression ((OP_ADD | OP_SUBTRACT) mul_expression)*
  ;

mul_expression
  : unary_expression ((OP_MULTIPLY | OP_DIVIDE) unary_expression)*
  ;

unary_expression
  : '-' atom
  | OP_BOOL_INVERSE atom
  | atom
  ;

atom
  : NUMBER
  | BOOLEAN
  | STRING
  | CHAR
  | IDENTIFIER
  | function_call
  ;

LBRACE: '{';
RBRACE: '}';
LPAREN: '(';
RPAREN: ')';
SEMI:   ';';
COMMA:  ',';
NL:     '\n' | '\r' '\n'?;

STATEMENT_DELIMITER
  : (SEMI | NL)+
  ;

OP_ASSIGN:   '=';
OP_ADD:      '+';
OP_SUBTRACT: '-';
OP_MULTIPLY: '*';
OP_DIVIDE:   '/';

OP_BOOL_INVERSE: '!';

OP_REL_OR:   '||';
OP_REL_AND:  '&&';
OP_REL_EQ:   '==';
OP_REL_NEQ:  '!=';

KEYWORD_METHOD: 'fn';
KEYWORD_IF:             'if';
KEYWORD_ELSE:       'else';

NUMBER
  : ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
  | '.' ('0'..'9')+ EXPONENT?
  | ('0'..'9')+ EXPONENT?
  ;
EXPONENT: ('e'|'E') ('+'|'-')? ('0'..'9')+ ;
STRING
  : '"' ~('\\'|'"')* '"'
  ;
CHAR
  : '\'' ~('\''|'\\') '\''
  ;
BOOLEAN: ('true'|'false');
IDENTIFIER: ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
WS: (' ' | '\n')+ {$channel = HIDDEN;};