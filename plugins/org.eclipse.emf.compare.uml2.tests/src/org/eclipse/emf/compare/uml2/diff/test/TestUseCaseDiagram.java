package org.eclipse.emf.compare.uml2.diff.test;

import java.io.IOException;

import org.junit.Test;

public class TestUseCaseDiagram extends AbstractUMLCompareTest {

	private static final String DIAGRAM_KIND_PATH = "/diagrams/usecase/";
	
	@Test
	public void extend_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("extend/changeLeftTarget");
	}
	
	@Test
	public void extend_changeRightTarget() throws IOException, InterruptedException {
		testCompare("extend/changeRightTarget");
	}
	
	@Test
	public void extend_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("extend/changeLeftTarget");
	}
	
	@Test
	public void extend_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("extend/changeRightTarget");
	}
	
	@Test
	public void include_changeLeftTarget() throws IOException, InterruptedException {
		testCompare("include/changeLeftTarget");
	}
	
	@Test
	public void include_changeRightTarget() throws IOException, InterruptedException {
		testCompare("include/changeRightTarget");
	}
	
	@Test
	public void include_changeLeftTarget_merge() throws IOException, InterruptedException {
		testMerge("include/changeLeftTarget");
	}
	
	@Test
	public void include_changeRightTarget_merge() throws IOException, InterruptedException {
		testMerge("include/changeRightTarget");
	}
	
	@Override
	String getDiagramKindPath() {
		return DIAGRAM_KIND_PATH;
	}

}
