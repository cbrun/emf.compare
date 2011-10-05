package org.eclipse.emf.compare.logical.workspace;

import java.util.Iterator;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.google.common.collect.Iterators;

/**
 * @author cedric
 * @since 1.3
 */
public class GraphVizGenerator {

	private ResourceSet set;

	public GraphVizGenerator(ResourceSet set) {
		this.set = set;
	}

	public String buildGraph() {
		StringBuffer graph = new StringBuffer();
		graph.append("digraph dependencies {\n"); //$NON-NLS-1$
		for (Resource res : set.getResources()) {
			graph.append(escapeNodeName(res.getURI().toString()) + ";\n "); //$NON-NLS-1$
		}
		Iterator<Model> it = Iterators.filter(set.getAllContents(), Model.class);
		while (it.hasNext()) {
			Model cur = it.next();
			for (Dependency other : cur.getDependencies()) {
				if (other.getTarget().eResource() != null) {
					graph.append(escapeNodeName(cur.eResource().getURI().toString()) + " -> " //$NON-NLS-1$
							+ escapeNodeName(other.getTarget().eResource().getURI().toString()) + ";\n"); //$NON-NLS-1$
				} // else its non resolvable
			}
		}
		graph.append("}"); //$NON-NLS-1$
		return graph.toString();
	}

	private String escapeNodeName(String uri) {
		return uri.replace('.', '_').replace('/', '_').replace(':', '_').replace('-', '_').replace('%', '_');

	}

}
