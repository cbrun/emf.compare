package org.eclipse.emf.compare.logical.workspace.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.workspace.Dependency;
import org.eclipse.emf.compare.logical.workspace.Model;
import org.eclipse.emf.compare.logical.workspace.WorkspaceFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

/**
 * @author cedric
 * @since 1.3
 */
public class XMIHrefLookAheadResourceImpl extends ResourceImpl {

	public XMIHrefLookAheadResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream is, Map<?, ?> options) throws IOException {
		Set<String> foundDependencies = collectDependencies(is);

		Model root = WorkspaceFactory.eINSTANCE.createModel();
		getContents().add(root);

		for (String uri : foundDependencies) {
			Dependency newDep = WorkspaceFactory.eINSTANCE.createDependency();
			newDep.setTarget(forgeProxy(uri));
			root.getDependencies().add(newDep);
		}

	}

	private Set<String> collectDependencies(InputStream is) throws UnsupportedEncodingException, IOException {
		Set<String> foundDependencies = new LinkedHashSet<String>();

		// a depedency looks like this : href="SimpleMM1I%20/my/othernstance2.xmi#/

		final char[] buffer = new char[0x10000];
		StringBuilder out = new StringBuilder();
		// TODO find the encoding...
		Reader in = new InputStreamReader(is, "UTF-8"); //$NON-NLS-1$
		int read;
		do {
			read = in.read(buffer, 0, buffer.length);
			if (read > 0) {
				out.append(buffer, 0, read);
			}
		} while (read >= 0);
		Pattern p = Pattern.compile("([a-zA-Z/\\.:]+)#"); //$NON-NLS-1$

		Matcher m = p.matcher(out.toString());
		while (m.find()) {
			String depURI = m.group(1);
			foundDependencies.add(depURI);
		}
		return foundDependencies;
	}

	private Model forgeProxy(String resourceURI) {

		URI uri = URI.createURI(resourceURI + "#/").resolve(getURI()); //$NON-NLS-1$

		Model targetedModel = WorkspaceFactory.eINSTANCE.createModel();
		((InternalEObject)targetedModel).eSetProxyURI(uri);
		return targetedModel;
	}
}
