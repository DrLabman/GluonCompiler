package gluoncompiler.syntax;

import gluoncompiler.GluonLabels;
import gluoncompiler.GluonOutput;
import gluoncompiler.Keyword;
import gluoncompiler.Token;

/**
 * Break Statement := 'BREAK'
 */
class BreakStatement extends Statement {
	
	public BreakStatement(Token next, ScopeObject parentScope) {
		super(next, parentScope);
	}

	@Override
	public Token parse() {
		assert(first.isKeyword());
		assert(Keyword.BREAK.equals(first.getKeyword()));
		Token next = first.getNext();
		assert(next.isNewline());
		return next;
	}

	@Override
	public void emitCode(GluonOutput code) {
		String label = GluonLabels.getEndLabel();
		
		code.comment("Break");
		code.code("JMP " + label);
	}
	
	@Override
	public void print(int level) {
		printClass(level);
	}
}
