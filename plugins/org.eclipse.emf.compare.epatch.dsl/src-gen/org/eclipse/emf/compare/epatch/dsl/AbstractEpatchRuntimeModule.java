
/*
 * generated by Xtext
 */
package org.eclipse.emf.compare.epatch.dsl;

import org.eclipse.xtext.Constants;
import org.eclipse.xtext.service.DefaultRuntimeModule;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Manual modifications go to {org.eclipse.emf.compare.epatch.dsl.EpatchRuntimeModule}
 */
public abstract class AbstractEpatchRuntimeModule extends DefaultRuntimeModule {
	
	@Override
	public void configure(Binder binder) {
		super.configure(binder);
		binder.bind(String.class).annotatedWith(Names.named(Constants.LANGUAGE_NAME)).toInstance(
			"org.eclipse.emf.compare.epatch.dsl.Epatch");
	}

	public Class<? extends org.eclipse.xtext.IGrammarAccess> bindIGrammarAccess() {
		return org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.class;
	}

	public Class<? extends org.eclipse.xtext.parser.packrat.IPackratParser> bindIPackratParser() {
		return org.eclipse.emf.compare.epatch.dsl.parser.packrat.EpatchPackratParser.class;
	}

	public Class<? extends org.eclipse.xtext.parsetree.reconstr.IParseTreeConstructor> bindIParseTreeConstructor() {
		return org.eclipse.emf.compare.epatch.dsl.parseTreeConstruction.EpatchParsetreeConstructor.class;
	}

	public Class<? extends org.eclipse.xtext.parser.antlr.IAntlrParser> bindIAntlrParser() {
		return org.eclipse.emf.compare.epatch.dsl.parser.antlr.EpatchParser.class;
	}

	public Class<? extends org.eclipse.xtext.parser.ITokenToStringConverter> bindITokenToStringConverter() {
		return org.eclipse.xtext.parser.antlr.AntlrTokenToStringConverter.class;
	}

	public Class<? extends org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider> bindIAntlrTokenFileProvider() {
		return org.eclipse.emf.compare.epatch.dsl.parser.antlr.EpatchAntlrTokenFileProvider.class;
	}

	public Class<? extends org.eclipse.xtext.parser.antlr.Lexer> bindLexer() {
		return org.eclipse.emf.compare.epatch.dsl.parser.antlr.internal.InternalEpatchLexer.class;
	}

	public Class<? extends org.eclipse.xtext.parser.antlr.ITokenDefProvider> bindITokenDefProvider() {
		return org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider.class;
	}
}
