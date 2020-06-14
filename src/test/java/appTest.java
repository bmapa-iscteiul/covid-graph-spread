package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.Before;
import org.junit.Test;

public class appTest {

	
	private main.java.app app;
	@Before
	public void setUp() throws Exception {
		app.cloneRepository();
	}

	@Test
	public void testAddRowToHTMLTable() {
		app.createHTMLTable();
		String[] tableData = {"1", "2", "3", "4", "5"};
		int tableIndex = 0;
		int sizeBefore = app.doc.select("table").select("tr").size();
		app.addRowToHTMLTable(tableIndex, tableData);
		assertEquals("Checking number of rows of table 0", sizeBefore + 1, app.doc.select("table").select("tr").size());
	}

	@Test
	public void testGetCommitTimestamp() {
		app.cloneRepository();
		String timestamp = null;
		try {
			//gets the list of all tags
			List<Ref> call = app.git.tagList().call();
			RevWalk walk = new RevWalk(app.git.getRepository());
			RevCommit commit;
			try {
				commit = (RevCommit)walk.parseAny(call.get(0).getObjectId());
				timestamp = app.getCommitTimestamp(commit);
			} catch (MissingObjectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull("A string é um timestamp inválido", timestamp);
	}

	@Test
	public void testGetHyperlinkOfFileFromCommit() {
		app.cloneRepository();
		String hyperlink = null;
		try {
			//gets the list of all tags
			List<Ref> call = app.git.tagList().call();
			RevWalk walk = new RevWalk(app.git.getRepository());
			RevCommit commit;
			try {
				commit = (RevCommit)walk.parseAny(call.get(0).getObjectId());
				hyperlink = app.getHyperlinkOfFileFromCommit(commit);
			} catch (MissingObjectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull("Hyperlink inv�lido", hyperlink);
	}

	@Test
	public void testCreateHTMLTable() {
		app.createHTMLTable();
		assertEquals("Didnt create table", 1, app.doc.select("table").size());
	}
	
	@Test
	public void testGetCommitFromObjectId() {
		Repository repository = app.git.getRepository();
		RevCommit commit = null;
		try {
			//gets the list of all tags
			List<Ref> call = app.git.tagList().call();
			
			for (Ref ref : call) {
				RevWalk walk = new RevWalk(repository);
				try {
					RevObject object = walk.parseAny(ref.getObjectId());
					//gets the commit associated to the tag
					if (object instanceof RevCommit) {
						//retrieves file from commit using the commit ObjectId
						commit = app.getCommitFromObjectId(ref.getObjectId());
					}
				} catch (Exception E) {
					
				}
			}
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull("O commit é inválido.", commit);
	}
}